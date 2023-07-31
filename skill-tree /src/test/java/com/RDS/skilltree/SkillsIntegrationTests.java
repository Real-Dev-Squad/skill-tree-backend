package com.RDS.skilltree;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class SkillsIntegrationTests {

    @Before
    public void setup(){
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    @Disabled
    public void testAPIReturnsAllSkills(){

        Response response = given()
                .when()
                .get("/api/endpoint");
        response.then().statusCode(200)
                .body("field1",equalTo("value1"));
    }

    @Test
    @Disabled
    public void testAPIReturns404OnSkillsNotFound(){

    }

    @Test
    @Disabled
    public void testAPIReturnsSkillsGivenUser(){

    }

    @Test
    @Disabled
    public void testAPIReturns400OnIncorrectUserId(){

    }

    @Test
    @Disabled
    public void testAPIReturns404OnNoSkillsFound(){

    }

    @Test
    @Disabled
    public void testAPIReturnsSkillGivenSkillName(){

    }

    @Test
    @Disabled
    public void testAPIReturns400OnIncorrectSkillId(){

    }

    @Test
    @Disabled
    public void testAPIReturns404OnNoSkillFound(){

    }

}
