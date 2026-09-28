# E-Commerce Selenium Automation Framework

Java 17 / Selenium 4 / TestNG framework for the [SauceDemo](https://www.saucedemo.com/) demonstration storefront. It covers authentication, product browsing, cart management, and checkout as independent UI scenarios. Sauce Labs uses this demo application in its own Selenium examples. This repository is a portfolio exercise, not an endorsement by Sauce Labs.

## Stack and design

| Component | Responsibility |
| --- | --- |
| Selenium WebDriver 4.49.0 | Browser automation |
| TestNG 7.11.0 | Lifecycle, suite, groups, DataProviders, assertions |
| Maven / Surefire 3.5.5 | Dependency resolution and `mvn clean test` |
| WebDriverManager 6.3.4 | ChromeDriver / GeckoDriver setup |
| ExtentReports 5.1.2 | HTML test results and failure evidence |
| Java 17 | Language and runtime baseline |

Pages own locators and interactions; tests own expected outcomes. Each test opens a new browser, starts at the login page, and creates its own cart state. `DriverFactory` stores the driver in a `ThreadLocal`, which isolates drivers if parallel execution is added later; this suite currently runs sequentially. Page interactions use explicit waits. A TestNG listener records result status and captures a PNG when a test fails.

```text
ecommerce-selenium/
├── .github/workflows/tests.yml       # Chrome headless CI
├── pom.xml                           # Dependencies and Surefire
├── testng.xml                        # Regression suite and listener
└── src/test/
    ├── java/dev/hardik/qa/
    │   ├── base/                    # Browser lifecycle, base test
    │   ├── config/                  # Properties + JVM overrides
    │   ├── listeners/               # Extent report, screenshots
    │   ├── pages/                   # Page objects and locators
    │   └── tests/                   # TestNG scenarios
    └── resources/config.properties   # Public, non-secret defaults
```

## Scenarios

| Area | Coverage |
| --- | --- |
| Login | Valid login; wrong password; locked account; missing username or password; logout |
| Catalog | Six-product listing; product name, description and price on detail page |
| Cart | Add from detail page; add two products; verify names and badge; remove one |
| Checkout | Confirm product and total; complete order; reject each missing required field |

The negative login and checkout scenarios use TestNG DataProviders. SauceDemo's sample credentials (`standard_user` / `secret_sauce`) are public demo data. Never place real account credentials in `config.properties` or Git.

## Run locally

Prerequisites: JDK 17+, Maven 3.8.7+, Chrome or Firefox, network access to SauceDemo, Maven Central, and browser driver downloads. WebDriverManager prepares matching drivers. On Linux CI, Chrome runs headless.

```bash
git clone <your-repository-url>
cd ecommerce-selenium
mvn clean test
mvn clean test -Dbrowser=firefox -Dheadless=true
mvn clean test -Dbrowser=chrome -Dheadless=false
mvn clean test -Dwait.seconds=20 -Dbase.url=https://www.saucedemo.com/
```

Defaults live in `src/test/resources/config.properties`. JVM properties override them. Supported browsers: `chrome`, `firefox`. `headless`, `wait.seconds`, and `page.load.seconds` can also be overridden. The regression XML runs all tests; `smoke`, `regression`, and `negative` groups label scenarios in the report.

After execution, open `target/reports/index.html` for ExtentReports. Failure screenshots live in `target/reports/screenshots/` and are linked from failed results; raw TestNG/Surefire results live in `target/surefire-reports/`. These are generated locally and ignored by Git. The GitHub Actions workflow runs headless Chrome on pushes, pull requests, and manual triggers, then uploads both report directories even on test failures.

## Extend the framework

1. Add a page class in `pages/` extending `BasePage`; keep locators private and expose meaningful user actions and page information.
2. Add an independent `*Test` class extending `BaseTest`; use TestNG assertions for outcomes and add it to `testng.xml`.
3. Run `mvn clean test`, inspect the HTML report, and investigate any failures before committing.

Possible next steps: Firefox CI matrix, careful parallel execution, accessibility checks, structured test data, and a separate API layer if a stable public API becomes available.

## Verification status

Maven and Chrome were installed in the build workspace; all 16 Java sources compiled. The UI suite could not run here because the workspace blocks a socket operation required by Chrome startup (`socket() failed: Operation not permitted`). **No passing UI test result is claimed.** Run `mvn clean test` in an ordinary browser-capable environment, investigate actual test failures, and confirm a successful CI run before presenting this as verified execution. See `VALIDATION.md` for the commands and evidence.
