package com.test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	public static void main(String[] args) {
		// Use Playwright to automate filling a registration form and verifying confirmation
		try (Playwright playwright = Playwright.create()) {
			BrowserType chromium = playwright.chromium();
			Browser browser = chromium.launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));

			Page page = browser.newPage();
			// 1. Open the website
			page.navigate("http://qa.way2automation.com");

			// 2. Enter test data in all visible input fields and textareas
			Locator inputs = page.locator("input:not([type=hidden])");
			int inputCount = inputs.count();
			for (int i = 0; i < inputCount; i++) {
				Locator input = inputs.nth(i);
				String type = input.getAttribute("type");
				if (type == null) type = "text";
				type = type.toLowerCase();
				try {
					switch (type) {
					case "email":
						input.fill("test.user+" + i + "@example.com");
						break;
					case "password":
						input.fill("TestPass123!");
						break;
					case "tel":
					case "number":
						input.fill("1234567890");
						break;
					case "date":
						input.fill("2000-01-01");
						break;
					case "checkbox":
						try { if (!input.isChecked()) input.check(); } catch (Exception e) { /* ignore */ }
						break;
					case "radio":
						try { input.check(); } catch (Exception e) { /* ignore */ }
						break;
					case "submit":
						// skip
						break;
					default:
						// text, search, url, etc.
						input.fill("TestValue" + i);
					}
				} catch (Exception e) {
					// Some inputs might not accept fill/check; continue
					System.out.println("Warning: could not set input #" + i + " (type=" + type + "): " + e.getMessage());
				}
			}

			// Fill textareas
			Locator textareas = page.locator("textarea");
			for (int i = 0; i < textareas.count(); i++) {
				try { textareas.nth(i).fill("This is a test message."); } catch (Exception ignored) {}
			}

			// 3. Click submit button (try common selectors)
			boolean clicked = false;
			try {
				if (page.locator("button[type='submit']").count() > 0) {
					page.locator("button[type='submit']").first().click();
					clicked = true;
				} else if (page.locator("input[type='submit']").count() > 0) {
					page.locator("input[type='submit']").first().click();
					clicked = true;
				} else if (page.locator("button:has-text(\"Submit\")").count() > 0) {
					page.locator("button:has-text(\"Submit\")").first().click();
					clicked = true;
				} else if (page.locator("button").count() > 0) {
					page.locator("button").first().click();
					clicked = true;
				}
			} catch (Exception e) {
				System.out.println("Warning: could not click submit button: " + e.getMessage());
			}

			// Give the page a moment to update after submit
			page.waitForTimeout(2000);

			// 4. Verify the confirmation text and print the same
			String body = "";
			try { body = page.textContent("body"); } catch (Exception e) { body = page.content(); }
			if (body == null) body = "";

			Pattern p = Pattern.compile("(?i)(thank you|successfully|success|registration|submitted|thank)");
			Matcher m = p.matcher(body);
			if (m.find()) {
				int start = Math.max(0, m.start() - 40);
				int end = Math.min(body.length(), m.end() + 140);
				String snippet = body.substring(start, end).replaceAll("\n+", " ");
				System.out.println("Confirmation text (snippet): " + snippet.trim());
			} else {
				System.out.println("Confirmation text not found. Page body snippet:");
				System.out.println(body.length() > 1000 ? body.substring(0, 1000) : body);
			}

			browser.close();
		} catch (Exception e) {
			System.out.println("Error during automation: " + e.getMessage());
			e.printStackTrace();
		}
	}
}