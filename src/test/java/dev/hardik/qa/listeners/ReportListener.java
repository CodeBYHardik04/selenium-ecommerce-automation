package dev.hardik.qa.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import dev.hardik.qa.base.DriverFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogType;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ReportListener implements ITestListener {
    private static final Logger LOG = Logger.getLogger(ReportListener.class.getName());
    private static final ThreadLocal<ExtentTest> CURRENT = new ThreadLocal<>();
    private ExtentReports report;
    private final Path reportDir = Path.of("target", "reports");
    @Override public synchronized void onStart(ITestContext context) {
        try { Files.createDirectories(reportDir); }
        catch (IOException e) { throw new IllegalStateException("Cannot create report directory", e); }
        report = new ExtentReports();
        report.attachReporter(new ExtentSparkReporter(reportDir.resolve("index.html").toString()));
        report.setSystemInfo("Browser", System.getProperty("browser", "chrome (config default)"));
        report.setSystemInfo("Suite", context.getSuite().getName());
    }
    @Override public void onTestStart(ITestResult result) {
        ExtentTest test;
        synchronized (this) { test = report.createTest(result.getMethod().getQualifiedName() + parameters(result)); }
        for (String group : result.getMethod().getGroups()) test.assignCategory(group);
        CURRENT.set(test);
    }
    @Override public void onTestSuccess(ITestResult result) { CURRENT.get().pass("Passed"); CURRENT.remove(); }
    @Override public void onTestSkipped(ITestResult result) {
        if (CURRENT.get() != null) { CURRENT.get().skip(result.getThrowable()); CURRENT.remove(); }
    }
    @Override public void onTestFailure(ITestResult result) {
        ExtentTest test = CURRENT.get();
        if (test != null) test.fail(result.getThrowable());
        try {
            if (test != null) test.info("URL at failure: " + DriverFactory.get().getCurrentUrl());
            try {
                DriverFactory.get().manage().logs().get(LogType.BROWSER).forEach(entry -> {
                    if (test != null) test.warning("Browser console: " + entry.getMessage());
                    LOG.warning("Browser console: " + entry.getMessage());
                });
            } catch (Exception unavailable) {
                LOG.fine("Browser console logs unavailable: " + unavailable.getMessage());
            }
            byte[] screenshot = ((TakesScreenshot) DriverFactory.get()).getScreenshotAs(OutputType.BYTES);
            Path directory = reportDir.resolve("screenshots");
            Files.createDirectories(directory);
            String filename = result.getMethod().getMethodName() + "_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS")) + ".png";
            Path file = directory.resolve(filename);
            Files.write(file, screenshot);
            if (test != null) test.addScreenCaptureFromPath("screenshots/" + filename, "Failure screenshot");
            LOG.info("Failure screenshot: " + file.toAbsolutePath());
        } catch (Exception e) { LOG.log(Level.WARNING, "Screenshot unavailable", e); }
        finally { CURRENT.remove(); }
    }
    @Override public synchronized void onFinish(ITestContext context) { if (report != null) report.flush(); }
    private static String parameters(ITestResult result) {
        if (result.getParameters().length == 0) return "";
        return " " + java.util.Arrays.toString(result.getParameters());
    }
}
