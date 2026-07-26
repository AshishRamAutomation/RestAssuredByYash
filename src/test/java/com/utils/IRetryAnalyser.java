package com.utils;

	import org.testng.IRetryAnalyzer;
	import org.testng.ITestResult;

	public class IRetryAnalyser implements IRetryAnalyzer {
	    private int count = 0;
	    private static final int MAX_LIMIT = 3;

	    @Override
	    public boolean retry(ITestResult result) {
	        // Check if the test failed due to an assertion on status code 429
	        Throwable throwable = result.getThrowable();
	        if (throwable != null && throwable.getMessage().contains("expected [200] but found [429]")) {
	            if (count < MAX_LIMIT) {
	                count++;
	                try {
	                    Thread.sleep(2000); 
	                } catch (InterruptedException e) {
	                    Thread.currentThread().interrupt();
	                }
	                return true;
	            }
	        }
	        return false;
	    }
	}

