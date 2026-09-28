# Validation record

Environment initially contained Java 17.0.20 only. Installed Maven 3.8.7, Chrome 154.0.8037.57 and the matching ChromeDriver for validation. Maven artifacts were fetched through a workspace proxy/mirror.

Attempted `mvn -version`: `mvn: command not found`.

An initial apt invocation failed on sandbox UID changes; a root sandbox setting and separate cache path allowed Maven installation. Chrome was installed from Google's Debian package.

`mvn clean test` first compiled all 16 Java sources, but WebDriverManager waited on a driver download. A second `mvn clean test` with an explicit local ChromeDriver reached TestNG and failed during `@BeforeMethod` before any UI assertion: `SessionNotCreatedException: Chrome instance exited`. A direct `google-chrome --headless=new --no-sandbox --dump-dom about:blank` confirmed the external blocker: `socket() failed: Operation not permitted (1)`. This workspace prevents Chrome from starting, including for `about:blank`. The resulting TestNG failures are setup failures, **not passing scenarios or verified locators**. Firefox was not installed or run. The GitHub Actions workflow is configuration only until run in a GitHub repository.

Next validation in a machine/runner with browser and Maven access:

```bash
mvn clean test
mvn clean test -Dbrowser=firefox -Dheadless=true
```

Inspect `target/surefire-reports/` and `target/reports/index.html`. Fix any actual locator or assertion failures before describing the suite as passing or publishing screenshots of test results. CI also needs a real run to verify its browser setup.

## Ubuntu run and follow-up

The user ran the original archive with Chrome 150 on Ubuntu: **14 tests, 8 failures, 0 errors, 0 skips**. Failures clustered around cart/detail/checkout navigation, immediate cart badge reading, and logout. The current SauceDemo JavaScript bundle confirms the product-card, route, cart, checkout, and login test IDs. The page objects now scope item actions to their product card, wait for the cart's visible state after adding/removing, and confirm each destination route and page landmark before returning the next page object. The report also records the URL at failure.

After these changes, `mvn -B -o -s /tmp/codex-maven-settings.xml test-compile` completed successfully for all 16 Java files in this workspace. The corrected UI suite has **not** run successfully here; the Chrome socket restriction remains. The next Ubuntu `mvn clean test` is the acceptance check. If a failure remains, use its report URL and screenshot to identify the actual page and selector before making another change.
