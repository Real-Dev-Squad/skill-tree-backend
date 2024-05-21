package com.RDS.skilltree.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import com.RDS.skilltree.Endorsement.*;
import com.RDS.skilltree.Skill.*;
import com.RDS.skilltree.User.*;
import io.restassured.response.Response;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import utils.RestAPIHelper;

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
    public EndorsementsIntegrationTests(
            UserService userService,
            UserRepository userRepository,
            SkillsService skillsService,
            SkillRepository skillRepository,
            EndorsementRepository endorsementRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.skillsService = skillsService;
        this.skillRepository = skillRepository;
        this.endorsementRepository = endorsementRepository;
    }

    @BeforeEach
    private void addData() throws MalformedURLException {
        user =
                userService.createUser(
                        UserDRO.builder()
                                .role(UserRole.MEMBER)
                                .rdsUserId("p6Bo61VEClhtVdwW0ihg")
                                .lastName("Doe")
                                .firstName("John")
                                .imageUrl(
                                        new URL(
                                                "https://res.cloudinary.com/realdevsquad/image/upload/v1666193594/profile/p6Bo61VEClhtVdwW0iGH/lezguwdq5bgzawa3.jpg"))
                                .build());

        skill =
                skillsService.createSkill(
                        SkillDRO.builder().name("Java").type(SkillType.ATOMIC).createdBy(user.getId()).build());
    }

    @AfterEach
    private void cleanUp() {
        endorsementRepository.deleteAll();
        skillRepository.deleteAll();
        userRepository.deleteAll();
    }

    private UUID createEndorsementModel(Boolean isStatusPending) {
        EndorsementStatus endorsementStatus;
        if (isStatusPending) {
            endorsementStatus = EndorsementStatus.PENDING;
        } else {
            endorsementStatus = EndorsementStatus.APPROVED;
        }
        EndorsementModel endorsementModel =
                EndorsementModel.builder().status(endorsementStatus).build();
        return endorsementRepository.save(endorsementModel).getId();
    }

    @Test
    @Disabled
    @DisplayName("Fetch all the endorsements")
    public void testAPIReturnsAllEndorsements() {
        Response response = given().cookies(RestAPIHelper.getUserCookie()).get("/endorsements");

        response
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("data", hasSize(1))
                .body("data[0].endorseeId", equalTo("user-1"))
                .body("data[0].skillName", equalTo("Java"))
                .body("data[0].status", anyOf(equalTo("APPROVED"), equalTo("REJECTED"), equalTo("PENDING")))
                .body("data[0].endorsementType", anyOf(equalTo("POSITIVE"), equalTo("NEGATIVE")))
                .body("data[0].endorsersList", hasSize(1))
                .body("data[0].endorserList[0].endorseeId", equalTo("user-2"))
                .body("data[0].endorserList[0].description", isA(String.class))
                .body(
                        "data[0].endorserList[0].userType",
                        anyOf(equalTo("NORMAL_USER"), equalTo("SUPER_USER"), equalTo("MAVEN")));
    }

    @Test
    @Disabled
    @DisplayName("Fetch all the endorsements given endorsement status")
    public void testAPIReturnsEndorsementsGivenStatus() {
        Response response =
                given()
                        .cookies(RestAPIHelper.getUserCookie())
                        .queryParam("status", "PENDING")
                        .get("/endorsements");

        response
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("data", hasSize(1))
                .body("data[0].endorseeId", equalTo("user-1"))
                .body("data[0].skillName", equalTo("Java"))
                .body("data[0].status", equalTo("PENDING"))
                .body("data[0].endorsementType", anyOf(equalTo("POSITIVE"), equalTo("NEGATIVE")))
                .body("data[0].endorsersList", hasSize(1))
                .body("data[0].endorserList[0].endorseeId", equalTo("user-2"))
                .body("data[0].endorserList[0].description", isA(String.class))
                .body(
                        "data[0].endorserList[0].userType",
                        anyOf(equalTo("NORMAL_USER"), equalTo("SUPER_USER"), equalTo("MAVEN")));
    }

    @Test
    @Disabled
    @DisplayName("Return 400 on invalid endorsement status passed")
    public void testAPIReturns400_OnInvalidStatusPassed() {
        Response response =
                given()
                        .cookies(RestAPIHelper.getUserCookie())
                        .queryParam("status", "APPROVAL")
                        .get("/endorsements");

        response
                .then()
                .statusCode(400)
                .body("code", equalTo(400))
                .body("message", equalTo("Invalid status passed"));
    }

    @Test
    @Disabled
    @DisplayName("Return 400 on invalid endorsementId passed")
    public void testAPIReturns400_OnInvalidParameterPassed() {
        String endorsementId = "randomId";
        Response response =
                given()
                        .cookies(RestAPIHelper.getUserCookie())
                        .pathParam("endorsementId", endorsementId)
                        .patch("/endorsements");

        response
                .then()
                .statusCode(400)
                .body("code", equalTo(400))
                .body("message", equalTo("Invalid endorsementId passed"));
    }

    @Test
    @DisplayName("Return 201 on endorsements creation")
    public void testAPIReturns201_OnEndorsementCreation() {
        UUID endorseeId = user.getId();
        UUID skillId = skill.getId();

        EndorsementDRO endorsementDRO = new EndorsementDRO();
        endorsementDRO.setEndorseeId(endorseeId);
        endorsementDRO.setSkillId(skillId);
        Response response =
                given()
                        .cookies(RestAPIHelper.getUserCookie())
                        .contentType("application/json")
                        .body(endorsementDRO)
                        .post("/v1/endorsements");

        response
                .then()
                .statusCode(201)
                .contentType("application/json")
                .body("data.endorseeId", equalTo(endorseeId.toString()))
                .body("data.skill.name", equalTo("Java"));
    }

    @Test
    @DisplayName("Return 400 on endorsements userid null")
    public void testAPIReturns400_OnEndorsementCreationUserIdNull() {

        UUID skillId = skill.getId();

        EndorsementDRO endorsementDRO = new EndorsementDRO();

        endorsementDRO.setSkillId(skillId);
        Response response =
                given()
                        .cookies(RestAPIHelper.getUserCookie())
                        .contentType("application/json")
                        .body(endorsementDRO)
                        .post("/v1/endorsements");

        response
                .then()
                .statusCode(400)
                .contentType("application/json")
                .body("data", equalTo(null))
                .body("message", equalTo("user id cannot be null"));
    }

    @Test
    @DisplayName("Return 400 on endorsements skillid null")
    public void testAPIReturns400_OnEndorsementCreationSkillIdNull() {
        UUID endorseeId = user.getId();

        EndorsementDRO endorsementDRO = new EndorsementDRO();
        endorsementDRO.setEndorseeId(endorseeId);

        Response response =
                given()
                        .cookies(RestAPIHelper.getUserCookie())
                        .contentType("application/json")
                        .body(endorsementDRO)
                        .post("/v1/endorsements");

        response
                .then()
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
        Response response =
                given()
                        .cookies(RestAPIHelper.getUserCookie())
                        .pathParam("endorsementId", endorsementId)
                        .patch("/endorsements");

        response
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("code", equalTo(200))
                .body("data.endorseeId", equalTo("user-1"))
                .body("data.skillName", equalTo("Java"))
                .body("data.status", anyOf(equalTo("APPROVED"), equalTo("PENDING"), equalTo("REJECTED")))
                .body("data.endorsementType", anyOf(equalTo("POSITIVE"), equalTo("NEGATIVE")))
                .body("data.endorsersList", hasSize(1))
                .body("data.endorsersList[0].endorseeId", equalTo("user-2"))
                .body("data.endorsersList[0].description", isA(String.class))
                .body(
                        "data.endorsersList[0].userType",
                        anyOf(equalTo("SUPER_USER"), equalTo("MAVEN"), equalTo("USER")));
    }

    @Test
    @Disabled
    @DisplayName("Return 400 on invalid endorsementId passed")
    public void testAPIReturn400_OnInvalidIdPassed() {
        String endorsementId = "randomId";
        Response response =
                given()
                        .cookies(RestAPIHelper.getUserCookie())
                        .pathParam("endorsementId", endorsementId)
                        .get("/endorsements");

        response
                .then()
                .statusCode(400)
                .body("code", equalTo(400))
                .body("message", equalTo("Invalid endorsementId passed"));
    }

    @Test
    @Disabled
    @DisplayName("Return 404 when endorsement not found given endorsementId")
    public void testAPIReturn404_OnEndorsementNotFound() {
        String endorsementId = "randomId";
        Response response =
                given()
                        .cookies(RestAPIHelper.getUserCookie())
                        .pathParam("endorsementId", endorsementId)
                        .get("/endorsements");

        response
                .then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("message", equalTo("Endorsement not found"));
    }

    @Test
    @DisplayName("Return 200, with the endorsements of a particular user given userID")
    public void itShouldReturn200OnEndorsementSearchByUserIDPresentInList() {
        String userID = "f13ac7a0-76ab-4215-8bfc-2dd5d9f8ebeb";

        Response response = given().get("/v1/endorsements?dummyData=true&userID=" + userID);

        response
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("content", everyItem(hasKey("user_id")))
                .body("content.user_id", everyItem(equalTo(userID)))
                .body("content.size()", equalTo(7))
                .body("totalPages", equalTo(1))
                .body("pageable.pageNumber", equalTo(0))
                .body("pageable.pageSize", equalTo(10))
                .body("totalElements", equalTo(7));
    }

    @Test
    @DisplayName("Return 200, with the endorsements of a particular skill given skillID")
    public void itShouldReturn200OnEndorsementSearchBySkillIDPresentInList() {
        String skillID = "7a6b8876-44e3-4b18-8579-79e9d4a5f0c9";

        Response response = given().get("/v1/endorsements?dummyData=true&skillID=" + skillID);

        response
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("content", everyItem(hasKey("skill_id")))
                .body("content.skill_id", everyItem(equalTo(skillID)))
                .body("content.size()", equalTo(1))
                .body("totalPages", equalTo(1))
                .body("pageable.pageNumber", equalTo(0))
                .body("pageable.pageSize", equalTo(10))
                .body("totalElements", equalTo(1));
    }

    @Test
    @DisplayName("Return 200, with 1st page all the endorsements with default pagesize")
    public void itShouldReturn200OnEndorsementSearchAllEndorsements() {
        Response response = given().get("/v1/endorsements?dummyData=true");

        response
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("totalPages", equalTo(2))
                .body("content.size()", equalTo(10))
                .body("pageable.pageNumber", equalTo(0))
                .body("pageable.pageSize", equalTo(10))
                .body("totalElements", equalTo(14));
    }

    @Test
    @DisplayName("Return 200, with 1st page all the endorsements with custom limit value")
    public void itShouldReturn200OnEndorsementSearchAllEndorsementsWithLimit() {
        Response response = given().get("/v1/endorsements?dummyData=true&limit=15");

        response
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("totalPages", equalTo(1))
                .body("content.size()", equalTo(14))
                .body("pageable.pageNumber", equalTo(0))
                .body("pageable.pageSize", equalTo(15))
                .body("totalElements", equalTo(14));
    }

    @Test
    @DisplayName("Return 200, with 1st page of all endorsements result where page size is 5")
    public void itShouldReturn200OnEndorsementSearchAllEndorsementsWithMultiplePages() {
        Response response = given().get("/v1/endorsements?dummyData=true&limit=5");

        response
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("totalPages", equalTo(3))
                .body("content.size()", equalTo(5))
                .body("pageable.pageNumber", equalTo(0))
                .body("pageable.pageSize", equalTo(5))
                .body("totalElements", equalTo(14));
    }

    @Test
    @DisplayName("Return 200, with 2nd page of all the endorsements result")
    public void itShouldReturn200With2ndPageOnEndorsementSearchAllEndorsementsWithMultiplePages() {
        Response response = given().get("/v1/endorsements?dummyData=true&limit=5&offset=1");

        response
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("totalPages", equalTo(3))
                .body("content.size()", equalTo(5))
                .body("pageable.pageNumber", equalTo(1))
                .body("pageable.pageSize", equalTo(5))
                .body("totalElements", equalTo(14));
    }

    @Test
    @DisplayName("Return 200, with the endorsements matching the given userID and skillID")
    public void itShouldReturn200OnEndorsementSearchGivenBothUserIDAndSkillID() {
        String userID = "73e0b7c4-d128-4e53-9501-0e7f4ff5a261";
        String skillID = "7a6b8876-44e3-4b18-8579-79e9d4a5f0c9";

        Response response =
                given().get("/v1/endorsements?dummyData=true&skillID=" + skillID + "&userID=" + userID);

        response
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("content", everyItem(hasKey("skill_id")))
                .body("content.skill_id", everyItem(equalTo(skillID)))
                .body("content", everyItem(hasKey("user_id")))
                .body("content.user_id", everyItem(equalTo(userID)))
                .body("content.size()", equalTo(1))
                .body("totalPages", equalTo(1))
                .body("pageable.pageNumber", equalTo(0))
                .body("pageable.pageSize", equalTo(10))
                .body("totalElements", equalTo(1));
    }

    @Test
    @DisplayName(
            "Return 204, when there are no endorsements present for the given userID in UUID form")
    public void itShouldReturn204OnEndorsementSearchWithValidUserIDButNotPresentInList() {
        String userID = UUID.randomUUID().toString();

        Response response = given().get("/v1/endorsements?dummyData=true&userID=" + userID);

        response.then().statusCode(204);
    }

    @Test
    @DisplayName(
            "Return 204, when there are no endorsements present for the given skillID in UUID form")
    public void itShouldReturn204OnEndorsementSearchWithValidSkillIDButNotPresentInList() {
        String skillID = UUID.randomUUID().toString();

        Response response = given().get("/v1/endorsements?dummyData=true&skillID=" + skillID);

        response.then().statusCode(204);
    }

    @Test
    @DisplayName("Return 400, given a userID which is not a UUID")
    public void itShouldReturn400OnEndorsementSearchWithInvalidUserID() {
        String userID = "invalid-user-id";

        Response response = given().get("/v1/endorsements?dummyData=true&userID=" + userID);

        response.then().statusCode(400);
    }

    @Test
    @DisplayName("Return 400, given a skillID which is not a UUID")
    public void itShouldReturn400OnEndorsementSearchWithInvalidSkillID() {
        String skillID = "invalid-skill-id";

        Response response = given().get("/v1/endorsements?dummyData=true&skillID=" + skillID);

        response.then().statusCode(400);
    }

    @Test
    @DisplayName("Return 204, given an offset value greater than maximum endorsements")
    public void itShouldReturn204OnEndorsementSearchWithOffsetGreaterThanMaximumEndorsements() {
        Response response = given().get("/v1/endorsements?dummyData=true&offset=10");

        response.then().statusCode(204);
    }

    @Test
    @Disabled
    @DisplayName("Return 401, when request is made without a valid cookie")
    public void itShouldReturn401OnEndorsementSearchWithoutCookie() {
        Response response = given().get("/v1/endorsements");

        response
                .then()
                .statusCode(401)
                .body(
                        "message",
                        equalTo(
                                "The access token provided is expired, revoked, malformed, or invalid for other reasons."));
    }

    @Test
    @DisplayName(
            "Return 200, when request is made using super user cookie and status is APPROVED/REJECTED")
    public void
            itShouldReturn200OnUpdateEndorsementStatusWithSuperUserCookieAndAcceptOrRejectEndorsementStatus() {
        UUID endorsementId = createEndorsementModel(true);
        Response response =
                given()
                        .cookies(RestAPIHelper.getSuperUserCookie())
                        .queryParam("status", EndorsementStatus.APPROVED.name())
                        .patch("/v1/endorsements/{id}", endorsementId);

        response
                .then()
                .statusCode(200)
                .body("data", equalTo(null))
                .body("message", equalTo("Successfully updated endorsement status"));
    }

    @Test
    @DisplayName(
            "Return 403, when request is made without using super user cookie and status is APPROVED/REJECTED")
    public void
            itShouldReturn403OnUpdateEndorsementStatusWithOutSuperUserCookieAndAcceptOrRejectEndorsementStatus() {
        UUID endorsementId = createEndorsementModel(true);
        Response response =
                given()
                        .cookies(RestAPIHelper.getUserCookie())
                        .queryParam("status", EndorsementStatus.APPROVED.name())
                        .patch("/v1/endorsements/{id}", endorsementId);

        response
                .then()
                .statusCode(403)
                .body("data", equalTo(null))
                .body("message", equalTo("Unauthorized, Access is only available to super users"));
    }

    @Test
    @DisplayName(
            "Return 400, when request is made with using super user cookie and status is invalid")
    public void
            itShouldReturn400OnUpdateEndorsementStatusWithSuperUserCookieAndEndorsementStatusIsInvalid() {
        UUID endorsementId = createEndorsementModel(true);
        Response response =
                given()
                        .cookies(RestAPIHelper.getSuperUserCookie())
                        .queryParam("status", "invalid-status")
                        .patch("/v1/endorsements/{id}", endorsementId);

        response
                .then()
                .statusCode(400)
                .body("data", equalTo(null))
                .body("message", equalTo("Invalid parameter endorsement status: invalid-status"));
    }

    @Test
    @DisplayName(
            "Return 400, when request is made with using super user cookie and status is PENDING")
    public void
            itShouldReturn400OnUpdateEndorsementStatusWithSuperUserCookieAndEndorsementStatusIsPending() {
        UUID endorsementId = createEndorsementModel(true);
        Response response =
                given()
                        .cookies(RestAPIHelper.getSuperUserCookie())
                        .queryParam("status", EndorsementStatus.PENDING.name())
                        .patch("/v1/endorsements/{id}", endorsementId);

        response
                .then()
                .statusCode(400)
                .body("data", equalTo(null))
                .body("message", equalTo("Invalid parameter endorsement status: PENDING"));
    }

    @Test
    @DisplayName(
            "Return 409, when request is made with using super user cookie and endorsement is already updated")
    public void
            itShouldReturn409OnUpdateEndorsementStatusWithSuperUserCookieAndEndorsementAlreadyUpdated() {
        UUID endorsementId = createEndorsementModel(false);
        Response response =
                given()
                        .cookies(RestAPIHelper.getSuperUserCookie())
                        .queryParam("status", EndorsementStatus.APPROVED.name())
                        .patch("/v1/endorsements/{id}", endorsementId);

        response
                .then()
                .statusCode(409)
                .body("data", equalTo(null))
                .body("message", equalTo("Endorsement is already updated. Cannot modify status"));
    }
}
