package com.RDS.skilltree.integration.skills;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.TestDataHelper.*;

import com.RDS.skilltree.TestContainerManager;
import com.RDS.skilltree.dtos.RdsGetUserDetailsResDto;
import com.RDS.skilltree.models.Endorsement;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.repositories.EndorsementRepository;
import com.RDS.skilltree.repositories.SkillRepository;
import com.RDS.skilltree.services.SkillService;
import com.RDS.skilltree.services.external.RdsService;
import com.RDS.skilltree.utils.JWTUtils;
import com.RDS.skilltree.viewmodels.EndorsementViewModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
public class GetEndorsementsBySkillIdIntegrationTest {
    @Autowired private MockMvc mockMvc;

    @Autowired private EndorsementRepository endorsementRepository;

    @MockBean private RdsService rdsService;

    @Autowired private SkillRepository skillRepository;

    @MockBean private JWTUtils jwtUtils;

    @Autowired private ObjectMapper objectMapper;

    private final String superUserId = "super-user-id";
    private final String userId1 = "user-id-1";
    private final String userId2 = "user-id-2";

    @Autowired private SkillService skillService;

    @BeforeEach
    void setUp() {
        // Clean up repositories
        skillRepository.deleteAll();
        endorsementRepository.deleteAll();

        // Setup super-user detail
        RdsGetUserDetailsResDto superUserDetails = createUserDetails(superUserId, true);

        // Setup normal-users detail
        RdsGetUserDetailsResDto user1Details = createUserDetails(userId1, false);
        RdsGetUserDetailsResDto user2Details = createUserDetails(userId2, false);

        // Setup RDS service mock responses
        when(rdsService.getUserDetails(superUserId)).thenReturn(superUserDetails);
        when(rdsService.getUserDetails(userId1)).thenReturn(user1Details);
        when(rdsService.getUserDetails(userId2)).thenReturn(user2Details);

        // Mock JWTUtils to bypass actual JWT verification
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("userId", String.class)).thenReturn(superUserId);
        when(jwtUtils.validateToken(anyString())).thenReturn(mockClaims);
    }

    private String createUrl(Integer skillId) {
        return "/v1/skills/" + skillId + "/endorsements";
    }

    private Skill createAndSaveSkill(String skillName) {
        return skillRepository.save(createSkill(skillName, superUserId));
    }

    private Endorsement createAndSaveEndorsement(
            Skill skill, String endorseId, String endorserId, String message) {
        Endorsement endorsement = createEndorsement(skill, endorseId, endorserId, message);
        return endorsementRepository.save(endorsement);
    }

    private EndorsementViewModel createEndorsementViewModel(Endorsement endorsement) {
        return TestDataHelper.createEndorsementViewModel(endorsement, rdsService);
    }

    private List<EndorsementViewModel> extractEndorsementsFromResult(MvcResult result)
            throws UnsupportedEncodingException, JsonProcessingException {
        String responseJson = result.getResponse().getContentAsString();
        return objectMapper.readValue(responseJson, new TypeReference<List<EndorsementViewModel>>() {});
    }

    private MvcResult performGetRequest(String url) throws Exception {
        return mockMvc
                .perform(MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    @DisplayName("Get endorsements for a skill with multiple endorsements")
    @WithCustomMockUser(
            username = superUserId,
            authorities = {"SUPERUSER"})
    public void getEndorsements_forSkillWithMultipleEndorsements_shouldReturnAllEndorsements()
            throws Exception {
        // set-up mock skill
        Skill skill = createAndSaveSkill("Java");

        // set-up mock endorsements
        String endorseId = userId1;
        String endorserId1 = userId2;
        String endorserId2 = superUserId;
        String endorsementMessage1 = "Good Java knowledge";
        String endorsementMessage2 = "Excellent Java skills";

        Endorsement endorsement1 =
                createAndSaveEndorsement(skill, endorseId, endorserId1, endorsementMessage1);
        Endorsement endorsement2 =
                createAndSaveEndorsement(skill, endorseId, endorserId2, endorsementMessage2);

        // perform get request
        MvcResult result = performGetRequest(createUrl(skill.getId()));

        // validate response
        assertThat(result.getResponse().getStatus()).isEqualTo(200);

        List<EndorsementViewModel> actualEndorsements = extractEndorsementsFromResult(result);
        List<EndorsementViewModel> expectedEndorsements =
                Arrays.asList(
                        createEndorsementViewModel(endorsement1), createEndorsementViewModel(endorsement2));

        assertThat(actualEndorsements).hasSize(expectedEndorsements.size());
        assertThat(actualEndorsements).usingRecursiveComparison().isEqualTo(expectedEndorsements);
    }

    @Test
    @DisplayName("Get endorsements for a skill with single endorsement")
    @WithCustomMockUser(
            username = superUserId,
            authorities = {"SUPERUSER"})
    public void getEndorsements_forSkillWithSingleEndorsement_shouldReturnOneEndorsement()
            throws Exception {
        // set-up mock skill
        Skill skill = createAndSaveSkill("Python");

        // create mock endorsement
        String endorseId = userId2;
        String endorserId = superUserId;
        String endorsementMessage = "Good Python knowledge";

        Endorsement endorsement =
                createAndSaveEndorsement(skill, endorseId, endorserId, endorsementMessage);

        // perform get request
        MvcResult result = performGetRequest(createUrl(skill.getId()));

        // validate response
        assertThat(result.getResponse().getStatus()).isEqualTo(200);

        List<EndorsementViewModel> actualEndorsements = extractEndorsementsFromResult(result);
        EndorsementViewModel expectedEndorsements = createEndorsementViewModel(endorsement);

        assertThat(actualEndorsements).hasSize(1);
        assertThat(actualEndorsements.get(0))
                .usingRecursiveComparison()
                .isEqualTo(expectedEndorsements);
    }

    @Test
    @DisplayName("Get endorsements for a skill with no endorsements")
    @WithCustomMockUser(
            username = superUserId,
            authorities = {"SUPERUSER"})
    public void getEndorsements_forSkillWithNoEndorsements_shouldReturnEmptyList() throws Exception {
        // set-up mock skill
        Skill skill = createAndSaveSkill("JavaScript");

        // perform get request without setting-up endorsement
        MvcResult result = performGetRequest(createUrl(skill.getId()));

        // validate response
        assertThat(result.getResponse().getStatus()).isEqualTo(200);

        List<EndorsementViewModel> actualEndorsements = extractEndorsementsFromResult(result);
        assertThat(actualEndorsements).isEmpty();
    }

    @Test
    @DisplayName("Get endorsements for non-existent skill ID")
    @WithCustomMockUser(
            username = superUserId,
            authorities = {"SUPERUSER"})
    public void getEndorsements_forNonExistentSkillId_shouldReturnEmptyList() throws Exception {
        // Use a non-existent skill ID (assuming 999 does not exist)
        Integer nonExistentSkillId = 999;

        MvcResult result = performGetRequest(createUrl(nonExistentSkillId));
        assertThat(result.getResponse().getStatus()).isEqualTo(200);

        List<EndorsementViewModel> actualEndorsements = extractEndorsementsFromResult(result);
        assertThat(actualEndorsements).isEmpty();
    }

    @Test
    @DisplayName("non super-user can access endorsements endpoint")
    @WithCustomMockUser(
            username = userId1,
            authorities = {"USER"})
    public void normalUser_canAccessEndorsements() throws Exception {
        // Setup skill and endorsements
        Skill skill = createAndSaveSkill("Java");
        String endorseId = userId1;
        String endorserId = superUserId;
        String endorsementMessage = "Good Java knowledge";

        Endorsement endorsement =
                createAndSaveEndorsement(skill, endorseId, endorserId, endorsementMessage);

        // Verify normal user can access the endpoint
        MvcResult result = performGetRequest(createUrl(skill.getId()));

        // validate response
        assertThat(result.getResponse().getStatus()).isEqualTo(200);

        List<EndorsementViewModel> actualEndorsements = extractEndorsementsFromResult(result);
        EndorsementViewModel expectedEndorsements = createEndorsementViewModel(endorsement);

        assertThat(actualEndorsements).hasSize(1);
        assertThat(actualEndorsements.get(0))
                .usingRecursiveComparison()
                .isEqualTo(expectedEndorsements);
    }

    @Test
    @DisplayName("Unauthenticated request returns 401")
    public void unauthenticatedRequest_returnsUnauthorized() throws Exception {
        // set-up mock skill
        Skill skill = createAndSaveSkill("Java");

        MvcResult result = performGetRequest(createUrl(skill.getId()));
        assertThat(result.getResponse().getStatus()).isEqualTo(401);
    }
}
