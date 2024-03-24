package com.RDS.skilltree;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import utils.RestAPIHelper;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SecurityContextIntegrationTest extends TestContainerManager {

    @Test
    public void testTokenIsNotPresent() {

        Response response = given().get("/v1/endorsement");
        response
                .then()
                .statusCode(401)
                .body(
                        "message",
                        equalTo(
                                "The access token provided is expired, revoked, malformed, or invalid for other reasons."));
    }

    @Test
    public void testInvalidToken() {
        Response response = given().cookie("rds-session-v2", "invalidtoken").get("/v1/endorsement");
        response
                .then()
                .statusCode(401)
                .body(
                        "message",
                        equalTo(
                                "The access token provided is expired, revoked, malformed, or invalid for other reasons."));
    }

    @Test
    public void test_GetSkill_WithGuestToken() {
        Response response =
                given()
                        .cookies(RestAPIHelper.getGuestUserCookie())
                        .contentType("application/json")
                        .get("/v1/skills/");
        response.then().statusCode(200);
    }

    @Test
    public void test_CreateSkill_WithGuestToken() {
        Response response =
                given()
                        .cookies(RestAPIHelper.getGuestUserCookie())
                        .contentType("application/json")
                        .post("/v1/skills/");
        response.then().statusCode(403).body("message", equalTo("Access Denied"));
    }
}
