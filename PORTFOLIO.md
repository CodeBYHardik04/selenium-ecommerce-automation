# Upwork and GitHub copy

**Upwork title:** Selenium Java E-Commerce Test Automation Framework | TestNG, POM, CI

**Role:** QA Automation Engineer — framework design, UI test development, and CI configuration.

**Project description:** Built a maintainable Selenium Java automation project for SauceDemo's demonstration store. The TestNG suite covers login, catalog, cart, checkout, validation, and logout. Page objects keep browser interaction separate from assertions; configuration supports Chrome and Firefox; an ExtentReports listener captures failure screenshots; GitHub Actions is configured for headless Chrome. Local execution remains to be validated in a browser-enabled environment; do not describe it as a passing CI suite until confirmed.

**Five skills:** Selenium WebDriver; Java; TestNG; Test Automation; Page Object Model.

**Technologies:** Java 17, Selenium 4, TestNG, Maven, WebDriverManager, ExtentReports, GitHub Actions.

**Screenshots to capture after successful execution:** (1) the repo structure and README; (2) ExtentReports summary with actual passing tests; (3) a test method alongside its page object; (4) a successful GitHub Actions run and uploaded artifact; (5) a deliberately induced failure screenshot and corresponding report entry, clearly marked as a reporting demo. Do not fabricate evidence.

**Client explanation:** Every test creates a fresh browser, signs into the demo store as needed, and makes assertions about a focused user journey. Page objects encapsulate locators and waiting. TestNG drives cases and data combinations; a listener records outcomes and screenshots. Maven runs the XML suite locally; GitHub Actions runs it headlessly on repository events.

## Questions you may be asked

**Why Page Object Model?** It centralizes UI locators and actions, so a selector change usually needs one page edit instead of edits across tests.

**How are tests independent?** `@BeforeMethod` creates a fresh browser session and starts at the login page. A checkout test adds its own product; it does not reuse a cart from another test.

**Why explicit waits?** They wait for a specific element state, reducing timing failures without always delaying by a fixed amount.

**How do DataProviders work?** TestNG calls the same assertion with each row of invalid credentials or missing checkout fields, reporting each row separately.

**How do you investigate failures?** Read the assertion/exception and TestNG report, then inspect the captured screenshot and page object locator. Reproduce before changing a wait or assertion.

**What is the cross-browser claim?** Chrome and Firefox branches are implemented; each must be executed and verified before claiming demonstrated compatibility.

**How would you scale it?** Add focused page objects and independent tests first. Parallel execution would require checking report coordination, server state, and driver isolation under real runs.

**Why WebDriverManager?** It resolves the appropriate browser driver on supported machines. A browser and network access are still prerequisites.

**GitHub description:** Maintainable Java Selenium/TestNG UI regression suite for SauceDemo, with page objects, data-driven cases, ExtentReports, and headless CI.

**GitHub topics:** `selenium`, `java`, `testng`, `maven`, `page-object-model`, `qa-automation`, `webdrivermanager`, `extentreports`, `github-actions`, `saucedemo`.
