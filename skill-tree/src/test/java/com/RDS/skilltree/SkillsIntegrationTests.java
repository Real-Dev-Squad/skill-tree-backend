package com.RDS.skilltree;

import com.RDS.skilltree.Skill.*;
import com.RDS.skilltree.User.*;
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
public class SkillsIntegrationTests extends TestContainerManager {
    private UserRepository userRepository;
    private SkillRepository skillRepository;
    private final UserService userService;
    private final SkillsService skillsService;
    private UserDTO user;
    private SkillDTO skill;

    @Autowired
    public SkillsIntegrationTests(UserService userService, UserRepository userRepository, SkillsService skillsService, SkillRepository skillRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.skillsService = skillsService;
        this.skillRepository = skillRepository;
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
        skillRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Return 200, on all skills")
    public void testAPIReturns200_OnAllSkillsFound() {
        Response response = given()
                .queryParam("offset", 0)
                .queryParam("limit", 1)
                .get("/v1/skills/");

        response.then()
                .statusCode(200)
                .body("content", hasSize(1))
                .body("content[0].type", equalTo("ATOMIC"))
                .body("content[0].name", equalTo("Java"))
                .body("content[0].users", empty())
                .body("totalPages", equalTo(1))
                .body("totalElements", equalTo(1))
                .body("last", equalTo(true))
                .body("size", equalTo(1))
                .body("number", equalTo(0))
                .body("numberOfElements", equalTo(1))
                .body("empty", equalTo(false));
    }

    @Test
    @DisplayName("Return 200, on no skills found")
    public void testAPIReturns200_OnNoSkillsFound() {
        skillRepository.deleteAll();
        Response response = given()
                .get("/v1/skills/");

        response.then()
                .statusCode(200)
                .body("content", hasSize(0))
                .body("totalPages", equalTo(0))
                .body("totalElements", equalTo(0))
                .body("last", equalTo(true))
                .body("size", equalTo(10))
                .body("number", equalTo(0))
                .body("numberOfElements", equalTo(0))
                .body("empty", equalTo(true));
    }

    @Test
    @DisplayName("Return 200, on skill found given skillId")
    public void testAPIReturns200_OnSkillFoundById() {
        UUID skillId = skill.getId();

        Response response = given()
                .pathParam("id", skillId)
                .get("/v1/skills/{id}");

        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("id", equalTo(String.valueOf(skillId)))
                .body("name", equalTo("Java"))
                .body("type", equalTo("ATOMIC"));
    }

    @Test
    @DisplayName("Return 404, on skill not found given SkillId")
    public void testAPIReturns404_OnSkillNotFound() {
        UUID skillId = UUID.randomUUID();
        Response response = given()
                .pathParam("id", skillId)
                .get("/v1/skills/{id}");

        response.then()
                .statusCode(404)
                .body("message", equalTo("Skill not found with given Id"));

    }

    @Test
    @DisplayName("Return 200, on skill with given name")
    public void testAPIReturns200_OnSkillFoundGivenName() {
        Response response = given()
                .pathParam("name", "Java")
                .get("/v1/skills/name/{name}");

        response.then()
                .statusCode(200)
                .contentType("application/json")
                .body("name", equalTo("Java"))
                .body("id", equalTo(skill.getId().toString()))
                .body("type", equalTo("ATOMIC"));
    }

    @Test
    @DisplayName("Return 404, if skill given skill name is not found")
    public void testAPIReturns404_OnSkillGivenSkillNameNotFound() {
        Response response = given()
                .pathParam("name", "Go")
                .get("/v1/skills/name/{name}");


        response.then()
                .statusCode(404)
                .body("message", equalTo("Skill not found with the given name"));
    }

    @Test
    @DisplayName("Return 400, if createdBy is not passed for Skill creation")
    public void testAPIReturns400_OnCreatedByNotPassedForSKillCreation() {
        SkillDRO skillDRO = SkillDRO.builder()
                .name("Go")
                .type(SkillType.ATOMIC)
                .build();

        Response response = given()
                .contentType("application/json")
                .body(skillDRO)
                .post("/v1/skills/");

        response.then()
                .statusCode(400)
                .body("data",equalTo(null))
                .body("message", equalTo("Created by user Id cannot be null"));
    }

    @Test
    @DisplayName("Return 400, if type is not passed for Skill creation")
    public void testAPIReturns400_OnTypeNotPassedForSkillCreation() {
        SkillDRO skillDRO = SkillDRO.builder()
                .name("Go")
                .createdBy(user.getId())
                .build();

        Response response = given()
                .contentType("application/json")
                .body(skillDRO)
                .post("/v1/skills/");

        response.then()
                .statusCode(400)
                .body("data",equalTo(null))
                .body("message", equalTo("SkillType cannot be null"));
    }

    @Test
    @DisplayName("Return 400, if name is not passed for Skill creation")
    public void testAPIReturns400_OnNameNotPassedForSkillCreation() {
        SkillDRO skillDRO = SkillDRO.builder()
                .type(SkillType.ATOMIC)
                .createdBy(user.getId())
                .build();

        Response response = given()
                .contentType("application/json")
                .body(skillDRO)
                .post("/v1/skills/");

        response.then()
                .statusCode(400)
                .body("data",equalTo(null))
                .body("message", equalTo("Name cannot be null"));
    }

    @Test
    @DisplayName("Return 409, if name is already used for Skill creation")
    public void testAPIReturns409_OnNameAlreadyUsedForSkillCreation() {
        SkillDRO skillDRO = SkillDRO.builder()
                .type(SkillType.ATOMIC)
                .name("Java")
                .createdBy(user.getId())
                .build();

        Response response = given()
                .contentType("application/json")
                .body(skillDRO)
                .post("/v1/skills/");

        response.then()
                .statusCode(409)
                .body("message", equalTo("Cannot create entry for Skill as Skill name is duplicate"));
    }

    @Test
    @DisplayName("Return 201, on successful Skill creation")
    public void testAPIReturns201_OnSuccessfulSkillCreation() {
        SkillDRO skillDRO = SkillDRO.builder()
                .type(SkillType.ATOMIC)
                .name("Go")
                .createdBy(user.getId())
                .build();

        Response response = given()
                .contentType("application/json")
                .body(skillDRO)
                .post("/v1/skills/");

        response.then()
                .statusCode(201)
                .contentType("application/json")
                .body("name", equalTo("Go"))
                .body("type", equalTo("ATOMIC"));
    }
}
