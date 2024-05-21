package com.selenium_testng_java.apis.apitests;

import com.github.javafaker.Faker;
import com.selenium_testng_java.apis.payloads.Payloads;
import com.selenium_testng_java.constants.FrameworkConstants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import com.selenium_testng_java.driver.DriverManager;
import com.selenium_testng_java.pages.LoginPage;

import static io.restassured.RestAssured.given;

public class RegisterApi {

    public static LoginPage loginPage = new LoginPage(DriverManager.getDriver());
    public static Faker faker = new Faker();
    public static String accessToken = "";
    public static String email = "";

    public static void registrationViaAPI() {
        email = faker.internet().emailAddress();
        String body = Payloads.registerPayload
                (
                        email,
                        FrameworkConstants.getPassword(),
                        faker.name().firstName(),
                        faker.name().lastName()
                );

        Response response = given().body(body)
                .header("Content-Type", "application/json")
                .accept("*/*")
                .log().all()
                .when().post("https://todo.qacart.com/api/v1/users/register")
                .then().log().all().extract().response();
        accessToken = response.jsonPath().getString("access_token");

    }

    public static void loginToPage() {
        loginPage.login(email, FrameworkConstants.getPassword());
    }


    public static void createTaskViaAPI(){
        given().body(Payloads.taskPayload())
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .log().all()
                .when().post("https://todo.qacart.com/api/v1/tasks")
                .then().log().all().extract().asString();
    }

    public static String createTaskViaAPI(String accessToken){
       String response= given().body(Payloads.taskPayload())
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .log().all()
                .when().post("https://todo.qacart.com/api/v1/tasks")
                .then().log().all().extract().asString();
        JsonPath jsonPathTask = new JsonPath(response);
        return jsonPathTask.getString("_id");
    }

    public static void delete(String access_Token,String objectId){
        given()
                .header("Authorization", "Bearer " + access_Token)
                .when().delete("https://todo.qacart.com/api/v1/tasks/" + objectId)
                .then().log().all().statusCode(200).extract().asString();
    }

}
