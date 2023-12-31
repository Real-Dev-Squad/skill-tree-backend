package com.RDS.skilltree;

import com.RDS.skilltree.Skill.*;
import com.RDS.skilltree.User.*;
import com.RDS.skilltree.Endorsement.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class EndorsementsIntegrationTests extends TestContainerManager {

    private UserRepository userRepository;
    private SkillRepository skillRepository;
    private final UserService userService;
    private final SkillsService skillsService;
    private UserDTO user;
    private SkillDTO skill;
    private EndorsementRepository endorsementRepository;

    @Autowired
    public EndorsementsIntegrationTests(UserService userService, UserRepository userRepository, SkillsService skillsService, SkillRepository skillRepository, EndorsementRepository endorsementRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.skillsService = skillsService;
        this.skillRepository = skillRepository;
        this.endorsementRepository = endorsementRepository;
    }

    @BeforeEach
    private void addData() throws MalformedURLException {
        user = userService.createUser(UserDRO.builder()
                .role(UserRole.MEMBER)
                .rdsUserId("p6Bo61VEClhtVdwW0ihg")
                .lastName("Doe")
                .firstName("John")
                .imageUrl(new URL("https://res.cloudinary.com/realdevsquad/image/upload/v1666193594/profile/p6Bo61VEClhtVdwW0iGH/lezguwdq5bgzawa3.jpg"))
                .build());

        skill = skillsService.createSkill(
                SkillDRO.builder()
                        .name("Java")
                        .type(SkillType.ATOMIC)
                        .createdBy(user.getId())
                        .build());
    }

    @AfterEach
    private void cleanUp() {
        endorsementRepository.deleteAll();
        skillRepository.deleteAll();
        userRepository.deleteAll();
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
    @DisplayName("Return 200 on endorsements creation")
    public void testAPIReturns201_OnEndorsementCreation() {
        UUID userId = user.getId();
        UUID skillId = skill.getId();

        EndorsementDRO endorsementDRO = new EndorsementDRO();
        endorsementDRO.setUserId(userId);
        endorsementDRO.setSkillId(skillId);
        Response response = given()
                .contentType("application/json")
                .body(endorsementDRO)
                .post("/v1/endorsements");


        response.then()
                .statusCode(201)
                .contentType("application/json")
                .body("data.user.firstName", equalTo("John"))
                .body("data.skill.name", equalTo("Java"));
    }

    @Test
    @DisplayName("Return 400 on endorsements userid null")
    public void testAPIReturns400_OnEndorsementCreationUserIdNull() {

        UUID skillId = skill.getId();

        EndorsementDRO endorsementDRO = new EndorsementDRO();

        endorsementDRO.setSkillId(skillId);
        Response response = given()
                .contentType("application/json")
                .body(endorsementDRO)
                .post("/v1/endorsements");


        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("data", equalTo(null))
                .body("message", equalTo("user id cannot be null"));
    }

    @Test
    @DisplayName("Return 400 on endorsements skillid null")
    public void testAPIReturns400_OnEndorsementCreationSkillIdNull() {
        UUID userId = user.getId();

        EndorsementDRO endorsementDRO = new EndorsementDRO();
        endorsementDRO.setUserId(userId);

        Response response = given()
                .contentType("application/json")
                .body(endorsementDRO)
                .post("/v1/endorsements");


        response.then()
                .statusCode(400)
                .contentType("application/json")
                .body("data", equalTo(null))
                .body("message", equalTo("skill id cannot be null"));
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
