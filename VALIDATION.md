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
