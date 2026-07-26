package com.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

public class ExtentManager {
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    /**
     * @author ashish.ram
     * @param .png file
     * Initializes and returns the global ExtentReports instance with advanced configurations.
     */
    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            
            ExtentSparkReporter spark = new ExtentSparkReporter("target/ExtentReports/API_Automation_Report.html");
            
            spark.config().setTheme(Theme.DARK);
            spark.config().setDocumentTitle("API Test Execution Report");
            spark.config().setReportName("REST Assured Test Results Summary");
            spark.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");

            // Build Base64 dynamically from the file to bypass code string character limits
            String base64Image = "";
            try {
                File logoFile = new File("src/test/resources/YashTechnologies.png");
                if (logoFile.exists()) {
                    byte[] fileContent = Files.readAllBytes(logoFile.toPath());
                    base64Image = "data:image/png;base64," + Base64.getEncoder().encodeToString(fileContent);
                }
            } catch (Exception e) {
                System.out.println("Logo file could not be read from src/test/resources/YashTechnologies.png");
            }

            // Direct CSS Injection targeting navigation header and info status tags
            if (!base64Image.isEmpty()) {
                String cleanCss = 
                    /* Custom Layout Alterations: Changes top navigation header bar to blue */
                    ".header .vheader { " +
                    "  height: 65px !important;" +
                    "  background-color: #1976D2 !important;" + // Vibrant blue header background
                    "}" +
                    
                    /* White contrast text fix for header icons and links */
                    ".header .vheader .nav-left li a, .header .vheader .nav-right li a, .header .vheader .nav-left li i { " +
                    "  color: #FFFFFF !important;" +
                    "}" +
                    
                    /* Custom Layout Alterations: Changes ALL INFO tags/badges and text indicators to blue */
                    ".badge-info, .status.info, .info-bg, .test-status.info { " +
                    "  background-color: #1E88E5 !important;" + // Premium blue fill for info tags
                    "  color: #FFFFFF !important;" +
                    "}" +
                    
                    /* Targets logging table row text icons for INFO events specifically */
                    "table.table tbody tr td .status.info i, .card-footer .stats .info i { " +
                    "  color: #1E88E5 !important;" +
                    "}" +

                    /* Scaled up the primary logo structure dimensions */
                    ".nav-logo { " +
                    "  background-image: url('" + base64Image + "') !important;" +
                    "  background-size: contain !important;" +
                    "  background-repeat: no-repeat !important;" +
                    "  background-position: left center !important;" +
                    "  width: 240px !important;" + 
                    "  height: 50px !important;" + 
                    "  margin-top: 7px !important;" + 
                    "  margin-left: 15px !important;" +
                    "  display: inline-block !important;" +
                    "  color: transparent !important;" +
                    "  font-size: 0 !important;" +
                    "}" +
                    
                    /* Turn off background images on alternative element selectors to prevent duplicate rendering */
                    ".logo, .brand-logo { " +
                    "  background-image: none !important; " +
                    "}" +
                    
                    /* CSS Trick: Enhances dark text contrast on Dark Mode themes */
                    ".nav-logo { " +
                    "  filter: brightness(1.2) contrast(1.1) !important;" + 
                    "}" +
                    
                    /* Completely hides default vector icons or text elements underneath */
                    ".logo .ico, .nav-logo i, .brand-logo i, .logo span, .brand-logo span, .nav-logo span { " +
                    "  display: none !important; " + 
                    "}";
                spark.config().setCss(cleanCss);
            }

            extent = new ExtentReports();
            extent.attachReporter(spark);
            
            extent.setSystemInfo("Environment", "QA Training");
            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Automation Framework", "REST Assured & TestNG");
            extent.setSystemInfo("Execution User", System.getProperty("user.name", "Unknown"));
        }
        return extent;
    }
    
    public static ExtentTest getTest() { 
        return extentTest.get(); 
    }
    
    public static void setTest(ExtentTest test) { 
        extentTest.set(test); 
    }
    
    /**
     * Clears the current thread's ExtentTest instance to prevent potential memory leaks.
     */
    public static void unloadTest() {
        extentTest.remove();
    }
}
