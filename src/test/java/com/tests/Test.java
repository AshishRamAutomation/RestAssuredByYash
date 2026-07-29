package com.tests;

import static io.restassured.RestAssured.baseURI;

import java.util.HashMap;
import java.util.Map;

import com.base.BaseTest;
import com.utils.RestAssuredUtil;

import io.restassured.response.Response;

public class Test extends BaseTest  {
@org.testng.annotations.Test
	void m1() {
	    
        baseURI="https://reqres.in";
        String endPoint="/api/users/";
        Map<String, String> map=new HashMap<String, String>();
        map.put("x-api-ke", "reqres_dd4096a5decb479ab31181c6cb2c0cbc");
		Response res =RestAssuredUtil.performGet(baseURI, endPoint, map);
		System.out.println(res.asPrettyString());
	}
}
