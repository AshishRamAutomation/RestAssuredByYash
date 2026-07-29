package com.utils;

import static io.restassured.RestAssured.given;

import java.util.Map;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestAssuredUtil {

    /**
     *  RequestSpecification with headers, query params, and body.
     */
    private static RequestSpecification baseRequest(
            Map<String, String> headers, 
            Map<String, String> queryParams, 
            Object body) {
        
        RequestSpecification request = given().log().ifValidationFails();

        if (headers != null && !headers.isEmpty()) {
            request.headers(headers);
        } else {
            request.contentType(ContentType.JSON);
        }

        if (queryParams != null && !queryParams.isEmpty()) {
            request.queryParams(queryParams);
        }

        if (body != null) {
            request.body(body);
        }

        return request;
    }

    /**
     * GET request method (with Query Parameters).
     */
    public static Response performGet(String baseUri, String endpoint, Map<String, String> headers, Map<String, String> queryParams) {
        return baseRequest(headers, queryParams, null)
                .baseUri(baseUri)
                .log().uri()
                .when()
                .get(endpoint)
                .then()
                .log().ifError() 
                .extract()
                .response();
    }

    /**
     * GET request method (without Query Parameters).
     */
    public static Response performGet(String baseUri, String endpoint, Map<String, String> headers) {
        return performGet(baseUri, endpoint, headers, null);
    }

    /**
     * POST request method.
     */
    public static Response performPost(String baseUri, String endpoint, Map<String, String> headers, Object body) {
        return baseRequest(headers, null, body)
                .baseUri(baseUri)
                .when()
                .post(endpoint)
                .then()
                .log().ifError()
                .extract()
                .response();
    }

    /**
     *  PUT request method.
     */
    public static Response performPut(String baseUri, String endpoint, Map<String, String> headers, Object body) {
        return baseRequest(headers, null, body)
                .baseUri(baseUri)
                .when()
                .put(endpoint)
                .then()
                .log().ifError()
                .extract()
                .response();
    }

    /**
     *  PATCH request method.
     */
    public static Response performPatch(String baseUri, String endpoint, Map<String, String> headers, Object body) {
        return baseRequest(headers, null, body)
                .baseUri(baseUri)
                .when()
                .patch(endpoint)
                .then()
                .log().ifError()
                .extract()
                .response();
    }

    /**
     *  DELETE request method.
     */
    public static Response performDelete(String baseUri, String endpoint, Map<String, String> headers) {
        return baseRequest(headers, null, null)
                .baseUri(baseUri)
                .when()
                .delete(endpoint)
                .then()
                .log().ifError()
                .extract()
                .response();
    }
}
