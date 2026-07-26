package com.utils;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

public class RestAssuredUtil {

    /**
     * Builds a base RequestSpecification containing optional headers and body.
     */
    private static RequestSpecification buildBaseRequest(Map<String, String> headers, Object body) {
        RequestSpecification request = given();
        
        if (headers != null && !headers.isEmpty()) {
            request.headers(headers);
        } else {
            request.contentType("application/json"); // Default fallback
        }
        
        if (body != null) {
            request.body(body);
        }
        
        return request;
    }

    /**
     * Reusable GET request method.
     */
    public static Response performGet(String baseUri, String endpoint, Map<String, String> headers) {
        return buildBaseRequest(headers, null)
                .baseUri(baseUri)
                .when()
                .get(endpoint);
    }

    /**
     * Reusable POST request method.
     */
    public static Response performPost(String baseUri, String endpoint, Map<String, String> headers, Object body) {
        return buildBaseRequest(headers, body)
                .baseUri(baseUri)
                .when()
                .post(endpoint);
    }

    /**
     * Reusable PUT request method.
     */
    public static Response performPut(String baseUri, String endpoint, Map<String, String> headers, Object body) {
        return buildBaseRequest(headers, body)
                .baseUri(baseUri)
                .when()
                .put(endpoint);
    }

    /**
     * Reusable PATCH request method.
     */
    public static Response performPatch(String baseUri, String endpoint, Map<String, String> headers, Object body) {
        return buildBaseRequest(headers, body)
                .baseUri(baseUri)
                .when()
                .patch(endpoint);
    }

    /**
     * Reusable DELETE request method.
     */
    public static Response performDelete(String baseUri, String endpoint, Map<String, String> headers) {
        return buildBaseRequest(headers, null)
                .baseUri(baseUri)
                .when()
                .delete(endpoint);
    }
}
