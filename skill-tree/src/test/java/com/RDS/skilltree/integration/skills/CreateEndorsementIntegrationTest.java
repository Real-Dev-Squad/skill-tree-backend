package com.RDS.skilltree.integration.skills;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static utils.TestDataHelper.createUserDetails;

import com.RDS.skilltree.TestContainerManager;
import com.RDS.skilltree.dtos.CreateEndorsementRequestDto;
import com.RDS.skilltree.dtos.RdsGetUserDetailsResDto;
import com.RDS.skilltree.exceptions.EndorsementAlreadyExistsException;
import com.RDS.skilltree.exceptions.SelfEndorsementNotAllowedException;
import com.RDS.skilltree.exceptions.SkillNotFoundException;
import com.RDS.skilltree.models.Endorsement;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.models.UserSkills;
import com.RDS.skilltree.repositories.EndorsementRepository;
import com.RDS.skilltree.repositories.SkillRepository;
import com.RDS.skilltree.repositories.UserSkillRepository;
import com.RDS.skilltree.services.external.RdsService;
import com.RDS.skilltree.utils.JWTUtils;
import com.RDS.skilltree.viewmodels.EndorsementViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import utils.TestDataHelper;
import utils.WithCustomMockUser;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import({TestContainerManager.class})
public class CreateEndorsementIntegrationTest {
    @Autowired private EndorsementRepository endorsementRepository;
    @Autowired private SkillRepository skillRepository;
    @Autowired private UserSkillRepository userSkillRepository;

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private RdsService rdsService;
    @MockBean private JWTUtils jwtUtils;

    private final String superUserId = "super-user-id";
    private final String userId1 = "user-id-1";
    private final String userId2 = "user-id-2";

    private final String SKILL_NAME = "Spring Boot";
    private final String ENDORSEMENT_MESSAGE = "Proficient in Spring Boot";
    private final String SKILL_NOT_FOUND_MESSAGE = "Skill does not exist";
    private final String SELF_ENDORSEMENT_MESSAGE = "Self endorsement not allowed";
    private final String ENDORSEMENT_ALREADY_EXISTS_MESSAGE = "Endorsement already exists";

    @BeforeEach
    void setUp() {
        skillRepository.deleteAll();
        endorsementRepository.deleteAll();
        userSkillRepository.deleteAll();

        RdsGetUserDetailsResDto superUserDetails = createUserDetails(superUserId, true);

        RdsGetUserDetailsResDto user1Details = createUserDetails(userId1, false);
        RdsGetUserDetailsResDto user2Details = createUserDetails(userId2, false);

        when(rdsService.getUserDetails(superUserId)).thenReturn(superUserDetails);
        when(rdsService.getUserDetails(userId1)).thenReturn(user1Details);
        when(rdsService.getUserDetails(userId2)).thenReturn(user2Details);
    }

    private String createUrl(Integer skillId) {
        return "/v1/skills/" + skillId + "/endorsements";
    }

    private MvcResult performPostRequest(String url, String requestBody) throws Exception {
        return mockMvc
                .perform(
                        MockMvcRequestBuilders.post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andReturn();
    }

    private Skill createAndSaveSkill(String skillName) {
        return skillRepository.save(TestDataHelper.createSkill(skillName, superUserId));
    }

    private CreateEndorsementRequestDto createEndorsementRequest(String endorseId, String message) {
        CreateEndorsementRequestDto endorsementRequest = new CreateEndorsementRequestDto();
        endorsementRequest.setEndorseId(endorseId);
        endorsementRequest.setMessage(message);
        return endorsementRequest;
    }

    private EndorsementViewModel createExpectedEndorsement(
            Skill skill, String endorseId, String endorserId, String message) {
        Endorsement endorsement =
                TestDataHelper.createEndorsement(skill, endorseId, endorserId, message);
        return TestDataHelper.createEndorsementViewModel(endorsement, rdsService);
    }

    private EndorsementViewModel extractEndorsementFromResult(MvcResult result) throws Exception {
        String responseJson = result.getResponse().getContentAsString();
        return objectMapper.readValue(responseJson, EndorsementViewModel.class);
    }

    private void assertIsEqual(
            EndorsementViewModel actualEndorsement, EndorsementViewModel expectedEndorsement) {
        expectedEndorsement.setId(actualEndorsement.getId());
        assertThat(actualEndorsement).usingRecursiveComparison().isEqualTo(expectedEndorsement);
    }

    @Test
    @DisplayName("Happy flow for superuser - create endorsement for a skill")
    @WithCustomMockUser(
            username = superUserId,
            authorities = {"SUPERUSER"})
    public void createEndorsement_asSuperUser_shouldCreateEndorsementSuccessfully() throws Exception {
        Skill skill = createAndSaveSkill(SKILL_NAME);
        String endorseId = userId1;
        String endorserId = superUserId;
        String message = ENDORSEMENT_MESSAGE;

        String requestBody =
                objectMapper.writeValueAsString(createEndorsementRequest(endorseId, message));

        MvcResult result = performPostRequest(createUrl(skill.getId()), requestBody);

        assertThat(result.getResponse().getStatus()).isEqualTo(201);
        assertThat(result.getResponse().getContentAsString()).isNotEmpty();

        EndorsementViewModel actualEndorsement = extractEndorsementFromResult(result);
        EndorsementViewModel expectedEndorsement =
                createExpectedEndorsement(skill, endorseId, endorserId, message);

        assertIsEqual(actualEndorsement, expectedEndorsement);
    }

    @Test
    @DisplayName("Happy flow for regular user - create endorsement for a skill")
    @WithCustomMockUser(
            username = userId2,
            authorities = {"USER"})
    public void createEndorsement_asRegularUser_shouldCreateEndorsementSuccessfully()
            throws Exception {
        Skill skill = createAndSaveSkill(SKILL_NAME);
        String endorseId = userId1;
        String endorserId = userId2;
        String message = ENDORSEMENT_MESSAGE;

        String requestBody =
                objectMapper.writeValueAsString(createEndorsementRequest(endorseId, message));

        MvcResult result = performPostRequest(createUrl(skill.getId()), requestBody);

        assertThat(result.getResponse().getStatus()).isEqualTo(201);
        assertThat(result.getResponse().getContentAsString()).isNotEmpty();

        EndorsementViewModel actualEndorsement = extractEndorsementFromResult(result);
        EndorsementViewModel expectedEndorsement =
                createExpectedEndorsement(skill, endorseId, endorserId, message);

        assertIsEqual(actualEndorsement, expectedEndorsement);
    }

    @Test
    @DisplayName("Error case - self-endorsement not allowed")
    @WithCustomMockUser(
            username = userId1,
            authorities = {"USER"})
    public void createEndorsement_selfEndorsement_shouldReturnError() throws Exception {
        Skill skill = createAndSaveSkill(SKILL_NAME);
        String endorseId = userId1; // self-endorsement

        String requestBody =
                objectMapper.writeValueAsString(createEndorsementRequest(endorseId, ENDORSEMENT_MESSAGE));

        MvcResult result = performPostRequest(createUrl(skill.getId()), requestBody);

        assertThat(result.getResponse().getStatus()).isEqualTo(405);

        assertThat(result.getResolvedException())
                .isInstanceOf(SelfEndorsementNotAllowedException.class);
        assertThat(requireNonNull(result.getResolvedException()).getMessage())
                .isEqualTo(SELF_ENDORSEMENT_MESSAGE);

        assertThat(endorsementRepository.count()).isZero();
    }

    @Test
    @DisplayName("Error case - non-existent skill ID")
    @WithCustomMockUser(
            username = userId2,
            authorities = {"USER"})
    public void createEndorsement_nonExistentSkill_shouldReturnError() throws Exception {
        Integer nonExistentSkillId = 9999;

        String requestBody =
                objectMapper.writeValueAsString(createEndorsementRequest(userId1, ENDORSEMENT_MESSAGE));

        MvcResult result = performPostRequest(createUrl(nonExistentSkillId), requestBody);

        assertThat(result.getResponse().getStatus()).isEqualTo(404);

        assertThat(result.getResolvedException()).isInstanceOf(SkillNotFoundException.class);
        assertThat(requireNonNull(result.getResolvedException()).getMessage())
                .isEqualTo(SKILL_NOT_FOUND_MESSAGE);

        assertThat(endorsementRepository.count()).isZero();
    }

    @Test
    @DisplayName("Edge case - first endorsement creates UserSkills entry")
    @WithCustomMockUser(
            username = superUserId,
            authorities = {"SUPERUSER"})
    public void createEndorsement_firstEndorsement_shouldCreateUserSkillsEntry() throws Exception {
        Skill skill = createAndSaveSkill(SKILL_NAME);
        String endorseId = userId1;
        String endorserId = superUserId;
        String message = ENDORSEMENT_MESSAGE;

        // no user-skills entry exists before the test
        assertThat(userSkillRepository.findByUserIdAndSkillId(endorseId, skill.getId())).isEmpty();

        String requestBody =
                objectMapper.writeValueAsString(createEndorsementRequest(endorseId, message));
        MvcResult result = performPostRequest(createUrl(skill.getId()), requestBody);

        assertThat(result.getResponse().getStatus()).isEqualTo(201);

        // user-skills entry was created
        assertThat(userSkillRepository.findByUserIdAndSkillId(endorseId, skill.getId())).isNotEmpty();

        EndorsementViewModel actualEndorsement = extractEndorsementFromResult(result);
        EndorsementViewModel expectedEndorsement =
                createExpectedEndorsement(skill, endorseId, endorserId, message);

        assertIsEqual(actualEndorsement, expectedEndorsement);
    }

    @Test
    @DisplayName("Edge case - endorsement with existing UserSkills entry")
    @WithCustomMockUser(
            username = superUserId,
            authorities = {"SUPERUSER"})
    public void createEndorsement_withExistingUserSkills_shouldCreateEndorsement() throws Exception {
        Skill skill = createAndSaveSkill(SKILL_NAME);
        String endorseId = userId1;
        String endorserId = superUserId;
        String message = ENDORSEMENT_MESSAGE;

        // user-skills entry before the test
        UserSkills userSkill = new UserSkills();
        userSkill.setUserId(endorseId);
        userSkill.setSkill(skill);
        userSkillRepository.save(userSkill);

        String requestBody =
                objectMapper.writeValueAsString(createEndorsementRequest(endorseId, message));
        MvcResult result = performPostRequest(createUrl(skill.getId()), requestBody);

        assertThat(result.getResponse().getStatus()).isEqualTo(201);

        EndorsementViewModel actualEndorsement = extractEndorsementFromResult(result);
        EndorsementViewModel expectedEndorsement =
                createExpectedEndorsement(skill, endorseId, endorserId, message);

        assertIsEqual(actualEndorsement, expectedEndorsement);

        // no duplicate user-skills entry created
        assertThat(userSkillRepository.findByUserIdAndSkillId(endorseId, skill.getId())).hasSize(1);
    }

    @Test
    @DisplayName("Error case - if endorsement already exists, do not create a duplicate")
    @WithCustomMockUser(
            username = userId2,
            authorities = {"USER"})
    public void createEndorsement_withExistingEndorsement_shouldNotCreateDuplicateEndorsement()
            throws Exception {
        Skill skill = createAndSaveSkill(SKILL_NAME);

        String requestBody =
                objectMapper.writeValueAsString(createEndorsementRequest(userId1, ENDORSEMENT_MESSAGE));

        // create the first endorsement
        performPostRequest(createUrl(skill.getId()), requestBody);

        // assert the first endorsement was created
        assertThat(endorsementRepository.count()).isEqualTo(1);

        // try to create a duplicate endorsement
        MvcResult result = performPostRequest(createUrl(skill.getId()), requestBody);

        assertThat(result.getResponse().getStatus()).isEqualTo(409);
        assertThat(result.getResolvedException()).isInstanceOf(EndorsementAlreadyExistsException.class);
        assertThat(requireNonNull(result.getResolvedException()).getMessage())
                .isEqualTo(ENDORSEMENT_ALREADY_EXISTS_MESSAGE);

        assertThat(endorsementRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName(
            "Edge case - if endorsement exists on a different skill, should create endorsement on new skill")
    @WithCustomMockUser(
            username = userId2,
            authorities = {"USER"})
    public void
            createEndorsement_withExistingEndorsementOnDiffSkill_shouldCreateEndorsementOnNewSkill()
                    throws Exception {
        Skill skill1 = createAndSaveSkill(SKILL_NAME);
        Skill skill2 = createAndSaveSkill("Python");

        String endorseId = userId1;
        String endorserId = userId2;
        String message = ENDORSEMENT_MESSAGE;

        String requestBody =
                objectMapper.writeValueAsString(createEndorsementRequest(userId1, ENDORSEMENT_MESSAGE));

        // create the endorsement for skill1
        MvcResult result1 = performPostRequest(createUrl(skill1.getId()), requestBody);

        EndorsementViewModel actualEndorsement1 = extractEndorsementFromResult(result1);
        EndorsementViewModel expectedEndorsement1 =
                createExpectedEndorsement(skill1, endorseId, endorserId, message);

        assertIsEqual(actualEndorsement1, expectedEndorsement1);

        assertThat(endorsementRepository.count()).isEqualTo(1);

        // create the endorsement for skill2
        MvcResult result2 = performPostRequest(createUrl(skill2.getId()), requestBody);

        assertThat(result2.getResponse().getStatus()).isEqualTo(201);

        EndorsementViewModel actualEndorsement2 = extractEndorsementFromResult(result2);
        EndorsementViewModel expectedEndorsement2 =
                createExpectedEndorsement(skill2, endorseId, endorserId, message);

        assertIsEqual(actualEndorsement2, expectedEndorsement2);

        // assert both endorsements were created
        assertThat(endorsementRepository.count()).isEqualTo(2);
    }
}
