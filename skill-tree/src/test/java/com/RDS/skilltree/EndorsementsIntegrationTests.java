package com.RDS.skilltree;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class EndorsementsIntegrationTests {
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    @Disabled
    @DisplayName("Fetch all the endorsements")
    public void testAPIReturnsAllEndorsements() {
        /*
  {
  "code": 200,
  "data": [
    {
      "endorseeId": "user-1",
      "skillName": "Java",
      "status": "APPROVED",
      "endorsementType": "POSITIVE",
      "endorsersList": [
        {
          "endorserId": "user-2",
          "description": "string",
          "userType": "NORMAL_USER"
        }
      ]
    }
  ]
}        */
        Response response = given()
                .get("/endorsements");

        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("data", hasSize(1))
                .body("data[0].endorseeId", equalTo("user-1"))
                .body("data[0].skillName", equalTo("Java"))
                .body("data[0].status", anyOf(equalTo("APPROVED"), equalTo("REJECTED"), equalTo("PENDING")))
                .body("data[0].endorsementType", anyOf(equalTo("POSITIVE"), equalTo("NEGATIVE")))
                .body("data[0].endorsersList", hasSize(1))
                .body("data[0].endorserList[0].endorserId", equalTo("user-2"))
                .body("data[0].endorserList[0].description", isA(String.class))
                .body("data[0].endorserList[0].userType", anyOf(equalTo("NORMAL_USER"), equalTo("SUPER_USER"), equalTo("MAVEN")));


    }

    @Test
    @Disabled
    @DisplayName("Fetch all the endorsements given endorsement status")
    public void testAPIReturnsEndorsementsGivenStatus() {
        Response response = given()
                .queryParam("status", "PENDING")
                .get("/endorsements");

        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("data", hasSize(1))
                .body("data[0].endorseeId", equalTo("user-1"))
                .body("data[0].skillName", equalTo("Java"))
                .body("data[0].status", equalTo("PENDING"))
                .body("data[0].endorsementType", anyOf(equalTo("POSITIVE"), equalTo("NEGATIVE")))
                .body("data[0].endorsersList", hasSize(1))
                .body("data[0].endorserList[0].endorserId", equalTo("user-2"))
                .body("data[0].endorserList[0].description", isA(String.class))
                .body("data[0].endorserList[0].userType", anyOf(equalTo("NORMAL_USER"), equalTo("SUPER_USER"), equalTo("MAVEN")));

    }

    @Test
    @Disabled
    @DisplayName("Return 400 on invalid endorsement status passed")
    public void testAPIReturns400_OnInvalidStatusPassed() {
        Response response = given()
                .queryParam("status", "APPROVAL")
                .get("/endorsements");

        response.then()
                .statusCode(400)
                .body("code", equalTo(400))
                .body("message", equalTo("Invalid status passed"));
    }

    @Test
    @Disabled
    @DisplayName("Return 400 on invalid endorsementId passed")
    public void testAPIReturns400_OnInvalidParameterPassed() {
        String endorsementId = "randomId";
        Response response = given()
                .pathParam("endorsementId", endorsementId)
                .patch("/endorsements");

        response.then()
                .statusCode(400)
                .body("code", equalTo(400))
                .body("message", equalTo("Invalid endorsementId passed"));
    }

    @Test
    @Disabled
    @DisplayName("Return 200 on endorsements updation")
    public void testAPIReturns200_OnEndorsementGivenId() {
        String endorsementId = "e-1";
        Response response = given()
                .pathParam("endorsementId", endorsementId)
                .patch("/endorsements");

        /*
        {
          "endorseeId": "user-1",
          "skillName": "Java",
          "status": "APPROVED",
          "endorsementType": "POSITIVE",
          "endorsersList": [
                {
                  "endorserId": "user-2",
                  "description": "string",
                  "userType": "NORMAL_USER"
                }
              ]
        }
         */

        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("code", equalTo(200))
                .body("data.endorseeId", equalTo("user-1"))
                .body("data.skillName", equalTo("Java"))
                .body("data.status", anyOf(equalTo("APPROVED"), equalTo("PENDING"), equalTo("REJECTED")))
                .body("data.endorsementType", anyOf(equalTo("POSITIVE"), equalTo("NEGATIVE")))
                .body("data.endorsersList", hasSize(1))
                .body("data.endorsersList[0].endorserId", equalTo("user-2"))
                .body("data.endorsersList[0].description", isA(String.class))
                .body("data.endorsersList[0].userType", anyOf(equalTo("SUPER_USER"), equalTo("MAVEN"), equalTo("USER")));

    }

    @Test
    @Disabled
    @DisplayName("Return 400 on invalid endorsementId passed")
    public void testAPIReturn400_OnInvalidIdPassed() {
        String endorsementId = "randomId";
        Response response = given()
                .pathParam("endorsementId", endorsementId)
                .get("/endorsements");

        response.then()
                .statusCode(400)
                .body("code", equalTo(400))
                .body("message", equalTo("Invalid endorsementId passed"));
    }

    @Test
    @Disabled
    @DisplayName("Return 404 when endorsement not found given endorsementId")
    public void testAPIReturn404_OnEndorsementNotFound() {
        String endorsementId = "randomId";
        Response response = given()
                .pathParam("endorsementId", endorsementId)
                .get("/endorsements");

        response.then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("message", equalTo("Endorsement not found"));
    }
}
