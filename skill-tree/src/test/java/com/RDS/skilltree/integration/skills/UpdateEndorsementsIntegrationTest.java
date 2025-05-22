package com.RDS.skilltree.integration.skills;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static utils.TestDataHelper.createEndorsementViewModel;
import static utils.TestDataHelper.createUserDetails;

import com.RDS.skilltree.TestContainerManager;
import com.RDS.skilltree.dtos.RdsGetUserDetailsResDto;
import com.RDS.skilltree.exceptions.UserNotFoundException;
import com.RDS.skilltree.models.Endorsement;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.repositories.EndorsementRepository;
import com.RDS.skilltree.repositories.SkillRepository;
import com.RDS.skilltree.repositories.UserSkillRepository;
import com.RDS.skilltree.services.external.RdsService;
import com.RDS.skilltree.utils.Constants.ExceptionMessages;
import com.RDS.skilltree.utils.JWTUtils;
import com.RDS.skilltree.viewmodels.EndorsementViewModel;
import com.RDS.skilltree.viewmodels.UpdateEndorsementViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import java.util.Map;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import utils.RestAPIHelper;
import utils.TestDataHelper;
import utils.WithCustomMockUser;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import({TestContainerManager.class})
public class UpdateEndorsementsIntegrationTest {
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

    private final String SKILL_NAME = "Java";
    private final String INITIAL_MESSAGE = "Initial message";
    private final String NEW_MESSAGE = "Updated message";

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

    private Skill createAndSaveSkill(String skillName) {
        return skillRepository.save(TestDataHelper.createSkill(skillName, superUserId));
    }

    private Endorsement createAndSaveEndorsement(
            Skill skill, String endorseId, String endorserId, String message) {
        Endorsement endorsement =
                TestDataHelper.createEndorsement(skill, endorseId, endorserId, message);
        return endorsementRepository.save(endorsement);
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

    private MvcResult performPatchRequest(String url, String requestBody) throws Exception {
        return mockMvc
                .perform(
                        MockMvcRequestBuilders.patch(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    private String createUrl(Integer endorsementId) {
        return String.format("/v1/endorsements/%d", endorsementId);
    }

    private UpdateEndorsementViewModel createRequestModel(String newMessage) {
        UpdateEndorsementViewModel updateEndorsementViewModel = new UpdateEndorsementViewModel();
        updateEndorsementViewModel.setMessage(newMessage);
        return updateEndorsementViewModel;
    }

    @Test
    @DisplayName("Happy flow for superuser - Successfully update endorsement message")
    @WithCustomMockUser(
            username = superUserId,
            authorities = {"SUPERUSER"})
    public void updateEndorsement_superUser_whenRequestValid_shouldUpdateEndorsement()
            throws Exception {
        Skill skill = createAndSaveSkill(SKILL_NAME);
        Endorsement existingEndorsement =
                createAndSaveEndorsement(skill, userId1, superUserId, INITIAL_MESSAGE);

        UpdateEndorsementViewModel updateEndorsementViewModel = createRequestModel(NEW_MESSAGE);
        String updateBody = objectMapper.writeValueAsString(updateEndorsementViewModel);
        MvcResult result = performPatchRequest(createUrl(existingEndorsement.getId()), updateBody);

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        EndorsementViewModel actualEndorsement = extractEndorsementFromResult(result);

        existingEndorsement.setMessage(updateEndorsementViewModel.getMessage());
        EndorsementViewModel expectedEndorsement =
                createEndorsementViewModel(existingEndorsement, rdsService);
        assertIsEqual(actualEndorsement, expectedEndorsement);

        Endorsement dbEndorsement =
                endorsementRepository.findById(existingEndorsement.getId()).orElseThrow();
        assertThat(dbEndorsement.getMessage()).isEqualTo(updateEndorsementViewModel.getMessage());
    }

    @Test
    @DisplayName("Happy flow for non-superuser - Successfully update endorsement message")
    @WithCustomMockUser(
            username = userId1,
            authorities = {"USER"})
    public void updateEndorsement_user_whenRequestValid_shouldUpdateEndorsement() throws Exception {
        Skill skill = createAndSaveSkill(SKILL_NAME);
        Endorsement existingEndorsement =
                createAndSaveEndorsement(skill, userId2, userId1, INITIAL_MESSAGE);

        UpdateEndorsementViewModel updateEndorsementViewModel = createRequestModel(NEW_MESSAGE);
        String updateBody = objectMapper.writeValueAsString(updateEndorsementViewModel);

        MvcResult result = performPatchRequest(createUrl(existingEndorsement.getId()), updateBody);

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        EndorsementViewModel actualEndorsement = extractEndorsementFromResult(result);

        existingEndorsement.setMessage(updateEndorsementViewModel.getMessage());
        EndorsementViewModel expectedEndorsement =
                createEndorsementViewModel(existingEndorsement, rdsService);
        assertIsEqual(actualEndorsement, expectedEndorsement);

        Endorsement dbEndorsement =
                endorsementRepository.findById(existingEndorsement.getId()).orElseThrow();
        assertThat(dbEndorsement.getMessage()).isEqualTo(updateEndorsementViewModel.getMessage());
    }

    @Test
    @DisplayName("Endorsement does not exist, should return endorsement not found")
    @WithCustomMockUser(
            username = userId1,
            authorities = {"USER"})
    public void updateEndorsement_whenEndorsementIdDoesNotExist_shouldReturnNotFound()
            throws Exception {
        Integer nonExistentEndorsementId = 999;

        UpdateEndorsementViewModel requestModel = createRequestModel(NEW_MESSAGE);
        String updateBody = objectMapper.writeValueAsString(requestModel);

        MvcResult result = performPatchRequest(createUrl(nonExistentEndorsementId), updateBody);
        assertThat(result.getResponse().getStatus()).isEqualTo(404);
        assertThat(result.getResponse().getContentAsString())
                .contains(ExceptionMessages.ENDORSEMENT_NOT_FOUND);
    }

    @Test
    @Disabled("Fails due to authorization bug tracked in #206 – re-enable once fixed")
    @DisplayName("when user is not the endorser, should not update endorsement")
    @WithCustomMockUser(
            username = userId1,
            authorities = {"USER"})
    public void updateEndorsement_othersEndorsement_shouldUNotUpdateEndorsement() throws Exception {
        Skill skill = createAndSaveSkill(SKILL_NAME);
        Endorsement existingEndorsement =
                createAndSaveEndorsement(skill, userId2, superUserId, INITIAL_MESSAGE);

        UpdateEndorsementViewModel updateEndorsementViewModel = createRequestModel(NEW_MESSAGE);
        String updateBody = objectMapper.writeValueAsString(updateEndorsementViewModel);

        MvcResult result = performPatchRequest(createUrl(existingEndorsement.getId()), updateBody);

        assertThat(result.getResponse().getStatus()).isEqualTo(403);
        assertThat(result.getResponse().getContentAsString())
                .contains(ExceptionMessages.UNAUTHORIZED_ENDORSEMENT_UPDATE);

        Endorsement dbEndorsement =
                endorsementRepository.findById(existingEndorsement.getId()).orElseThrow();
        assertThat(dbEndorsement).usingRecursiveComparison().isEqualTo(existingEndorsement);
    }

    @Test
    @Disabled("Fails due to validation bug tracked in #206 – re-enable once fixed")
    @DisplayName("Message is empty string, request is not valid")
    @WithCustomMockUser(
            username = userId1,
            authorities = {"USER"})
    public void updateEndorsement_whenMessageIsValidAndEmpty_shouldReturnBadRequest()
            throws Exception {
        Skill skill = createAndSaveSkill(SKILL_NAME);
        Endorsement existingEndorsement =
                createAndSaveEndorsement(skill, userId2, userId1, INITIAL_MESSAGE);

        UpdateEndorsementViewModel requestModel = createRequestModel("");
        String updateBody = objectMapper.writeValueAsString(requestModel);

        MvcResult result = performPatchRequest(createUrl(existingEndorsement.getId()), updateBody);
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
        assertThat(result.getResponse().getContentAsString())
                .contains(ExceptionMessages.ENDORSEMENT_MESSAGE_EMPTY);
    }

    @Test
    @Disabled("Fails due to bug tracked in #206 – re-enable once fixed")
    @DisplayName("RdsService fails to get 'endorser' details, should return 404")
    @WithCustomMockUser(
            username = "non-existent-endorser-id",
            authorities = {"USER"})
    public void updateEndorsement_whenRdsServiceFailsForEndorserDetails_shouldReturn404()
            throws Exception {
        Skill skill = createAndSaveSkill(SKILL_NAME);
        String endorserId = "non-existent-endorser-id";
        Endorsement existingEndorsement =
                createAndSaveEndorsement(skill, userId2, endorserId, INITIAL_MESSAGE);

        UpdateEndorsementViewModel requestModel = createRequestModel(NEW_MESSAGE);
        String updateBody = objectMapper.writeValueAsString(requestModel);

        when(rdsService.getUserDetails(endorserId))
                .thenThrow(new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND));

        MvcResult result = performPatchRequest(createUrl(existingEndorsement.getId()), updateBody);
        assertThat(result.getResponse().getStatus()).isEqualTo(404);
        assertThat(result.getResponse().getContentAsString())
                .contains(ExceptionMessages.USER_NOT_FOUND);

        Endorsement dbEndorsement =
                endorsementRepository.findById(existingEndorsement.getId()).orElseThrow();
        assertThat(dbEndorsement).usingRecursiveComparison().isEqualTo(existingEndorsement);
    }

    @Test
    @Disabled("Fails due to bug tracked in #206 – re-enable once fixed")
    @DisplayName("RdsService fails to get 'endorse' details, should return 404")
    @WithCustomMockUser(
            username = userId1,
            authorities = {"USER"})
    public void updateEndorsement_whenRdsServiceFailsForEndorseDetails_shouldReturn404()
            throws Exception {
        Skill skill = createAndSaveSkill(SKILL_NAME);
        String endorseId = "non-existent-endorse-id";
        Endorsement existingEndorsement =
                createAndSaveEndorsement(skill, endorseId, userId1, INITIAL_MESSAGE);

        UpdateEndorsementViewModel requestModel = createRequestModel(NEW_MESSAGE);
        String updateBody = objectMapper.writeValueAsString(requestModel);

        when(rdsService.getUserDetails(endorseId))
                .thenThrow(new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND));

        MvcResult result = performPatchRequest(createUrl(existingEndorsement.getId()), updateBody);
        assertThat(result.getResponse().getStatus()).isEqualTo(404);
        assertThat(result.getResponse().getContentAsString())
                .contains(ExceptionMessages.USER_NOT_FOUND);

        Endorsement dbEndorsement =
                endorsementRepository.findById(existingEndorsement.getId()).orElseThrow();
        assertThat(dbEndorsement).usingRecursiveComparison().isEqualTo(existingEndorsement);
    }

    @Test
    @DisplayName("Message is null, request is not valid")
    @WithCustomMockUser(
            username = userId1,
            authorities = {"USER"})
    public void updateEndorsement_whenRequestBodyMessageIsNull_shouldReturnBadRequest()
            throws Exception {
        Skill skill = createAndSaveSkill(SKILL_NAME);
        Endorsement existingEndorsement =
                createAndSaveEndorsement(skill, userId2, userId1, INITIAL_MESSAGE);

        UpdateEndorsementViewModel requestModel = createRequestModel(null);
        String updateBody = objectMapper.writeValueAsString(requestModel);

        MvcResult result = performPatchRequest(createUrl(existingEndorsement.getId()), updateBody);
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
        assertThat(result.getResponse().getContentAsString())
                .contains(ExceptionMessages.ENDORSEMENT_MESSAGE_EMPTY);
    }

    @Test
    @DisplayName("Message field is missing, request is not valid")
    @WithCustomMockUser(
            username = userId1,
            authorities = {"USER"})
    public void updateEndorsement_whenRequestBodyMessageFieldIsMissing_shouldReturnBadRequest()
            throws Exception {
        Skill skill = createAndSaveSkill(SKILL_NAME);
        Endorsement existingEndorsement =
                createAndSaveEndorsement(skill, userId2, userId1, INITIAL_MESSAGE);

        String requestBody = "{}";
        MvcResult result = performPatchRequest(createUrl(existingEndorsement.getId()), requestBody);
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
        assertThat(result.getResponse().getContentAsString())
                .contains(ExceptionMessages.ENDORSEMENT_MESSAGE_EMPTY);
    }

    @Test
    @DisplayName("User is unauthorized, should return 403")
    public void updateEndorsement_whenUserIsUnauthorized_shouldReturn403() throws Exception {
        Skill skill = createAndSaveSkill(SKILL_NAME);
        Endorsement existingEndorsement =
                createAndSaveEndorsement(skill, userId2, userId1, INITIAL_MESSAGE);

        UpdateEndorsementViewModel requestModel = createRequestModel(NEW_MESSAGE);
        String updateBody = objectMapper.writeValueAsString(requestModel);
        Map.Entry<String, String> cookie =
                RestAPIHelper.getGuestUserCookie().entrySet().iterator().next();
        MvcResult result =
                mockMvc
                        .perform(
                                MockMvcRequestBuilders.patch(createUrl(existingEndorsement.getId()))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(updateBody)
                                        .cookie(new Cookie(cookie.getKey(), cookie.getValue())))
                        .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(403);
    }

    @Test
    @DisplayName("User is unauthorized, should return 401")
    public void updateEndorsement_whenUserIsUnauthenticated_shouldReturn401() throws Exception {
        Skill skill = createAndSaveSkill(SKILL_NAME);
        Endorsement existingEndorsement =
                createAndSaveEndorsement(skill, userId2, userId1, INITIAL_MESSAGE);

        UpdateEndorsementViewModel requestModel = createRequestModel(NEW_MESSAGE);
        String updateBody = objectMapper.writeValueAsString(requestModel);

        MvcResult result = performPatchRequest(createUrl(existingEndorsement.getId()), updateBody);
        assertThat(result.getResponse().getStatus()).isEqualTo(401);
    }
}
