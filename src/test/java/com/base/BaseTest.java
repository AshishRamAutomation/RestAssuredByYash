package com.base;

import io.restassured.RestAssured;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import com.utils.ExtentReportFilter;
import com.utils.ExtentReportListener;
@Listeners({ExtentReportListener.class})
public class BaseTest {
    @BeforeSuite

    public void setupSuite() {
        RestAssured.filters(new ExtentReportFilter());
    }
}
