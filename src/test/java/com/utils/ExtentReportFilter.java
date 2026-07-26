package com.utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExtentReportFilter implements Filter {
    
    // Initialize Log4j Logger
    private static final Logger logger = LogManager.getLogger(ExtentReportFilter.class);
    
    @Override
    public Response filter(FilterableRequestSpecification requestSpec, 
                           FilterableResponseSpecification responseSpec, 
                           FilterContext ctx) {
        
        // Log incoming request details
        logger.info("Sending Request - Method: {}, URI: {}", requestSpec.getMethod(), requestSpec.getURI());
        if (requestSpec.getBody() != null) {
            logger.debug("Request Body: {}", requestSpec.getBody().toString());
        }

        Response response = ctx.next(requestSpec, responseSpec);

        // Log outgoing response details
        logger.info("Received Response - Status Code: {}", response.getStatusCode());
        logger.debug("Response Payload: {}", response.asString());

        // Sync with Extent Reports
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().info("<b>Request URI:</b> " + requestSpec.getURI());
            ExtentManager.getTest().info("<b>HTTP Method:</b> " + requestSpec.getMethod());
            ExtentManager.getTest().info("<b>Status Code:</b> " + response.getStatusCode());
            ExtentManager.getTest().info("<b>Response Payload:</b> <pre>" + response.asPrettyString() + "</pre>");
        }

        return response;
    }
}
