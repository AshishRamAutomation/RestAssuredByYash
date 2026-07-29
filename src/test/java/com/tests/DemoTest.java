package com.tests;

import static io.restassured.RestAssured.*;


import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.base.BaseTest;
import com.utils.IRetryAnalyser;


@Test(retryAnalyzer = IRetryAnalyser.class)
public class DemoTest extends BaseTest {
    String ACCESS_TOKEN="reqres_dd4096a5decb479ab31181c6cb2c0cbc";
    public void testGetUser() {
        
       
        
        baseURI="https://reqres.in";
        String endPoint="/api/users/";
        

	    given()
	    .header("x-api-key",ACCESS_TOKEN)
            .queryParams("page", 2)
            .log().uri()
        .when()
            .get()
        .then()
            .statusCode(200)
            
            .extract().response();
    }
}
