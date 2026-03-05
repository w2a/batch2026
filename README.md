PlaywrightTesting
=================

This project contains a small Java `Main` class that uses Playwright for Java to launch Chromium and navigate to https://www.facebook.com.

Prerequisites
-------------
- Java 17 or newer installed and on PATH
- Maven installed and on PATH
- Internet access to download Maven dependencies and Playwright browsers

Quick run (Windows cmd.exe)
---------------------------
Open a command prompt in the project directory `d:\workspaces\mcpserver\PlaywrightTesting` and run:

```cmd
REM 1) Force-update dependencies and build the project (skip tests to speed up)
cd /d d:\workspaces\mcpserver\PlaywrightTesting
mvn -U -DskipTests package

REM 2) (Optional) Install Playwright browsers if you want to ensure browsers are downloaded before running
mvn com.microsoft.playwright:playwright-maven-plugin:${playwright.version}:install

REM 3) Run the Main class via the exec plugin
mvn exec:java
```

Notes
-----
- If `mvn exec:java` fails with a runtime error about missing browsers, run the install command (`mvn com.microsoft.playwright:playwright-maven-plugin:${playwright.version}:install`) and try again.
- If Maven is not installed, download it from https://maven.apache.org/download.cgi and add it to your PATH.
- If you prefer to run the packaged JAR directly, use `java -jar target/PlaywrightTesting-0.0.1-SNAPSHOT.jar` after packaging; ensure the Playwright native dependencies/browsers are available.

Troubleshooting
---------------
- Playwright requires native browsers. The Maven plugin above installs them, or Playwright will download them on first use (requires network/permissions).
- On Windows you may see firewall prompts when Playwright downloads browser binaries.
- If you get an UnsupportedClassVersionError, ensure your `java -version` is 17 or newer.

Files added
-----------
- `src/main/java/com/test/Main.java` — launches Chromium and navigates to facebook.com
- `README.md` — run instructions

If you'd like, I can run the Maven build and execute the Main class here to verify it works; tell me to proceed and I'll run the commands.