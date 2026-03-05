package com.test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class Main {
	public static void main(String[] args) {
		// Create Playwright instance and launch Chromium
		Playwright playwright = Playwright.create();
		BrowserType chromium = playwright.chromium();
		Browser browser = chromium.launch(new BrowserType.LaunchOptions().setHeadless(false) // show the browser window
				.setSlowMo(50)); // slow down actions slightly so you can see them

		Page page = browser.newPage();
		page.navigate("https://www.facebook.com");

		System.out.println("Navigated to: " + page.url());
		System.out.println("Page title: " + page.title());

		page.locator("input[name='email']").fill("trainer@way2automation.com");
		page.locator("input[name='pass']").fill("test1234");
		page.locator("button[name='login']").click();
		browser.close();
		playwright.close();
		// Keep the browser open for a short time so you can see it (adjust as needed)

	}
}
