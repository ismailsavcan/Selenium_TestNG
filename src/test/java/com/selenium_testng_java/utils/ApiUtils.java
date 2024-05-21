package com.selenium_testng_java.utils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class ApiUtils {
    public static RequestSpecification requestSpecBuilder(String baseUrl, String body) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setBody(body)
                .setContentType(ContentType.JSON)
                .build();
    }
    public static RequestSpecification requestSpecBuilder(String baseUrl, Object payload, String token) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .addHeader("Authorization",token)
                .setBody(payload)
                .setContentType(ContentType.JSON)
                .build();
    }
    public static RequestSpecification requestSpecBuilder(String baseUrl) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification responseSpecBuilder(int statusCode) {
        return new ResponseSpecBuilder().expectStatusCode(statusCode)
                .expectContentType(ContentType.JSON).build();
    }
}
