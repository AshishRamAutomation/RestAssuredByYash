package com.utils;

import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentReportListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        // Initializes the report configuration before the suite runs
        ExtentManager.getInstance();
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Automatically creates a test node using the method name
        ExtentTest test = ExtentManager.getInstance().createTest(result.getMethod().getMethodName());
        ExtentManager.setTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentManager.getTest().pass("Test Passed Successfully");
        ExtentManager.unloadTest(); // Prevents memory leaks
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentManager.getTest().fail(result.getThrowable());
        ExtentManager.unloadTest(); // Prevents memory leaks
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentManager.getTest().skip("Test Skipped");
        ExtentManager.unloadTest(); // Prevents memory leaks
    }

    @Override
    public void onFinish(ITestContext context) {
        // Flushes and writes all logs to the HTML report file
        if (ExtentManager.getInstance() != null) {
            ExtentManager.getInstance().flush();
        }
    }
}

