package com.RDS.skilltree;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.jupiter.api.Disabled;
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
    public void testAPIReturns400OnIncorrectUserId() {
        Response response = given()
                .get("/skills/{userId}"); //TODO: change this URL

        response.then()
                .statusCode(400)
                .body("code",equalTo(400))
                .body("message",equalTo("Invalid userId passed"));

    }

    @Test
    @Disabled
    public void testAPIReturns404OnNoSkillsFound() {
        Response response = given()
                .get("/skills/{userId}"); //TODO: change this URL

        response.then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("message",equalTo("No skills associated with the user"));

    }

    @Test
    @Disabled
    public void testAPIReturnsSkillGivenSkillName() {
        Response response = given()
                .get("/skills/{skillName}"); //TODO: change the URL

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
    public void testAPIReturns400OnIncorrectSkillName() {
        Response response = given()
                .get("/skills/{skillName}"); //TODO: change the URL

        response.then()
                .statusCode(400)
                .body("code",equalTo(400))
                .body("message",equalTo("Invalid skillName passed"));
    }
}
