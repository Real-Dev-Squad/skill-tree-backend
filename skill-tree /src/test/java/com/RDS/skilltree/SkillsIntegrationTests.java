package com.RDS.skilltree;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class SkillsIntegrationTests {

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    @Disabled
    @DisplayName("Return all skills")
    public void testAPIReturnsAllSkills() {

        Response response = given()
                .get("/skills");

        /*
       {
           "code": 200,
           "data":
                [
                    {
                    "id": "s1",
                    "name": "Java",
                    "type": "atomic / derived",
                    "description": "string"
                    }
                ]
            }

         */
        response.then()
                .statusCode(200)
                .body("code",equalTo(200))
                .body("data", hasSize(1))
                .body("data[0].id", equalTo("s1"))
                .body("data[0].name",equalTo("Java"))
                .body("data[0].type", anyOf(equalTo("ATOMIC"), equalTo("DERIVED")))
                .body("data[0].description",isA(String.class));
    }

    @Test
    @Disabled
    @DisplayName("Return all skills given userId")
    public void testAPIReturnsSkillsGivenUser() {
        Response response = given()
                .get("/skills/u1");

        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("code",equalTo(200))
                .body("data", hasSize(1))
                .body("data[0].id", equalTo("s1"))
                .body("data[0].name",equalTo("Java"))
                .body("data[0].type", anyOf(equalTo("ATOMIC"), equalTo("DERIVED")))
                .body("data[0].description",isA(String.class));
    }

    @Test
    @Disabled
    @DisplayName("Return 400 on incorrect userId")
    public void testAPIReturns400_OnIncorrectUserId() {
        Response response = given()
                .get("/skills/{userId}");

        response.then()
                .statusCode(400)
                .body("code",equalTo(400))
                .body("message",equalTo("Invalid userId passed"));

    }

    @Test
    @Disabled
    @DisplayName("Return 404 on, no skills found on given userId")
    public void testAPIReturns404_OnNoSkillsFound() {
        Response response = given()
                .get("/skills/{userId}");

        response.then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("message",equalTo("No skills associated with the user"));

    }

    @Test
    @Disabled
    @DisplayName("Return users having skill given skill name")
    public void testAPIReturns200_OnSkillGivenSkillName() {
        Response response = given()
                .get("/skills/{skillName}");

        /*

        [
          {
            "skillName": "Java",
            "userName": "John doe"
          }
        ]
         */

        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("code",equalTo(200))
                .body("data", hasSize(1))
                .body("data[0].skillName",equalTo("Java"))
                .body("data[0].userName",equalTo("John doe"));
    }

    @Test
    @Disabled
    @DisplayName("Return 400 on invalid skill name passed")
    public void testAPIReturns400_OnIncorrectSkillName() {
        Response response = given()
                .get("/skills/{skillName}");

        response.then()
                .statusCode(400)
                .body("code",equalTo(400))
                .body("message",equalTo("Invalid skillName passed"));
    }
}
