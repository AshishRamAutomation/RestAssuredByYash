package com.tests;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.base.BaseTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utils.ConfigReader;
import com.utils.FilepathUtils;
import com.utils.PartialupdateBookingPayloadRestFul;
import com.utils.RestAssuredUtil;

import io.restassured.response.Response;

public class RestfulEndtoEnd extends BaseTest {
	private static final Logger log = LogManager.getLogger(RestfulEndtoEnd.class);

	private String baseUrl;
	private int bookingid;   
	private String accesstoken;  
	private Map<String, String> headers;

	@BeforeClass
	public void setUp() {
		log.info("Initializing configuration and executing setup steps...");
		baseUrl = ConfigReader.getProperty("base.url");
		
		headers = new HashMap<>();
		headers.put("Content-Type", "application/json");

		HashMap<String, String> authPayload = new JSONObject();
		authPayload.put("username", "admin");
		authPayload.put("password", "password123");
		
		log.info("Executing generic POST request to auth endpoint.");
		Response response = RestAssuredUtil.performPost(baseUrl, "/auth", headers, authPayload);
		
		response.then().statusCode(200);
		accesstoken = response.path("token");
		log.info("Authentication complete. Token stored: {}", accesstoken);
	}

	@Test
	public void createBooking() {
		log.info("Starting Execution: createBooking");
		enableLoggingOfRequestAndResponseIfValidationFails();

		HashMap<Object, Object> bookingDates = new JSONObject();
		bookingDates.put("checkin", "2018-01-01");
		bookingDates.put("checkout", "2019-01-01");

		HashMap<Object, Object> bookingPayloadMap = new JSONObject();
		bookingPayloadMap.put("firstname", "Raj");
		bookingPayloadMap.put("lastname", "Rajesh");
		bookingPayloadMap.put("totalprice", 111);
		bookingPayloadMap.put("depositpaid", true);
		bookingPayloadMap.put("bookingdates", bookingDates);
		
		Response response = RestAssuredUtil.performPost(baseUrl, "/booking", headers, bookingPayloadMap);
		
		bookingid = response.then()
				.statusCode(200)
				.log().all()
				.assertThat()
				.body("booking.firstname", equalTo("Raj"))
				.body("booking.lastname", equalTo("Rajesh"))
				.body("booking.totalprice", equalTo(111))
				.extract()
				.path("bookingid"); 
				
		log.info("Booking entry verified successfully. Stored Booking ID: {}", bookingid);
	}

	@Test(dependsOnMethods = "createBooking")
	public void updateBooking() throws IOException {
		log.info("Starting Execution: updateBooking for Target ID: {}", bookingid);
		enableLoggingOfRequestAndResponseIfValidationFails();
		
		String updateBookingPayload = FileUtils.readFileToString(new File(FilepathUtils.UPDATE_PAYLOAD_RESTFUL_JSONPATH), "UTF-8");
		
		Map<String, String> authenticatedHeaders = new HashMap<>(headers);
		authenticatedHeaders.put("Cookie", "token=" + accesstoken);

		Response response = RestAssuredUtil.performPut(baseUrl, "/booking/" + bookingid, authenticatedHeaders, updateBookingPayload);
		
		response.then()
				.statusCode(200)   
				.log().all()
				.assertThat()
				.body("firstname", equalTo("Taj"))
				.body("lastname", equalTo("Tajesh"))
				.body("totalprice", equalTo(222));
				
		log.info("PUT update successfully processed and values validated.");
	}

	@Test(dependsOnMethods = "updateBooking")
	public void partialUpdateBooking() throws IOException {
		log.info("Starting Execution: partialUpdateBooking for Target ID: {}", bookingid);
		enableLoggingOfRequestAndResponseIfValidationFails();

		ObjectMapper objectMapper = new ObjectMapper();
		PartialupdateBookingPayloadRestFul partialupdate = new PartialupdateBookingPayloadRestFul();
		partialupdate.setFirstname("PartialUpTaj");
		partialupdate.setLastname("PartialUpTajesh");
		partialupdate.setTotalprice(333);
		
		String jsonPayload = objectMapper.writeValueAsString(partialupdate);
		
		Map<String, String> authenticatedHeaders = new HashMap<>(headers);
		authenticatedHeaders.put("Cookie", "token=" + accesstoken);

		Response response = RestAssuredUtil.performPatch(baseUrl, "/booking/" + bookingid, authenticatedHeaders, jsonPayload);
		
		response.then()
				.statusCode(200)
				.log().all()
				.assertThat()
				.body("firstname", equalTo("PartialUpTaj"))
				.body("lastname", equalTo("PartialUpTajesh"))
				.body("totalprice", equalTo(333));
				
		log.info("PATCH partial adjustment verified successfully.");
	}

	@Test(dependsOnMethods = "partialUpdateBooking")
	public void deleteBooking() {
		log.info("Starting Execution: deleteBooking for Target ID: {}", bookingid);
		enableLoggingOfRequestAndResponseIfValidationFails();

		Map<String, String> authenticatedHeaders = new HashMap<>(headers);
		authenticatedHeaders.put("Cookie", "token=" + accesstoken);

		Response response = RestAssuredUtil.performDelete(baseUrl, "/booking/" + bookingid, authenticatedHeaders);
		
		response.then()
				.statusCode(201)
				.log().all();
				
		log.info("DELETE request successfully completed.");
	}
}
