package com.RDS.skilltree.integration.skills;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.RDS.skilltree.TestContainerManager;
import com.RDS.skilltree.dtos.RdsGetUserDetailsResDto;
import com.RDS.skilltree.dtos.SkillRequestActionRequestDto;
import com.RDS.skilltree.enums.SkillTypeEnum;
import com.RDS.skilltree.enums.UserSkillStatusEnum;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.models.UserSkills;
import com.RDS.skilltree.repositories.SkillRepository;
import com.RDS.skilltree.repositories.UserSkillRepository;
import com.RDS.skilltree.services.external.RdsService;
import com.RDS.skilltree.utils.JWTUtils;
import com.RDS.skilltree.viewmodels.RdsUserViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import utils.WithCustomMockUser;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SkillRequestActionIntegrationTest extends TestContainerManager {
    @Autowired private MockMvc mockMvc;
    @Autowired private UserSkillRepository userSkillRepository;
    @Autowired private SkillRepository skillRepository;
    @MockBean private RdsService rdsService;
    @MockBean private JWTUtils jwtUtils;
    @Autowired private ObjectMapper objectMapper;
    private Skill skill;
    private final String baseRoute = "/v1/skills/requests";

    @BeforeEach
    void setUp() {
        // Clean up repositories
        skillRepository.deleteAll();
        userSkillRepository.deleteAll();

        // Setup super-user detail
        RdsUserViewModel superUser = new RdsUserViewModel();
        superUser.setId("super-user-id");
        RdsUserViewModel.Roles roles = new RdsUserViewModel.Roles();
        roles.setSuper_user(true);
        superUser.setRoles(roles);

        RdsGetUserDetailsResDto superUserDetails = new RdsGetUserDetailsResDto();
        superUserDetails.setUser(superUser);

        // Setup mock skill
        skill = new Skill();
        skill.setName("Java");
        skill.setType(SkillTypeEnum.ATOMIC);
        skill.setCreatedBy("super-user-id");
        skill = skillRepository.save(skill);

        // Setup mock user-skill
        UserSkills userSkill = new UserSkills();
        userSkill.setSkill(skill);
        userSkill.setUserId("test-user-id");
        userSkill.setStatus(UserSkillStatusEnum.PENDING);
        userSkillRepository.save(userSkill);

        // Setup RDS service mock responses
        when(rdsService.getUserDetails("super-user-id")).thenReturn(superUserDetails);

        // Mock JWTUtils to bypass actual JWT verification
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("userId", String.class)).thenReturn("super-user-id");
        when(jwtUtils.validateToken(anyString())).thenReturn(mockClaims);
    }

    @Test
    @DisplayName("Happy flow - Super user can approve a skill request")
    @WithCustomMockUser(
            username = "super-user-id",
            authorities = {"SUPERUSER"})
    public void approveSkillRequest_validRequest_shouldApproveSkillRequest() throws Exception {
        SkillRequestActionRequestDto requestDto = new SkillRequestActionRequestDto();
        requestDto.setEndorseId("test-user-id");
        requestDto.setAction(UserSkillStatusEnum.APPROVED);
        String requestBody = objectMapper.writeValueAsString(requestDto);

        mockMvc
                .perform(
                        MockMvcRequestBuilders.post(baseRoute + "/" + skill.getId() + "/action")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("approved"));

        // Verify the status was updated in database
        UserSkills updatedUserSkill =
                userSkillRepository.findByUserIdAndSkillId("test-user-id", skill.getId()).get(0);
        Assertions.assertEquals(UserSkillStatusEnum.APPROVED, updatedUserSkill.getStatus());
    }

    @Test
    @DisplayName("Happy flow - Super user can reject a skill request")
    @WithCustomMockUser(
            username = "super-user-id",
            authorities = {"SUPERUSER"})
    public void rejectSkillRequest_validRequest_shouldRejectSkillRequest() throws Exception {
        SkillRequestActionRequestDto requestDto = new SkillRequestActionRequestDto();
        requestDto.setEndorseId("test-user-id");
        requestDto.setAction(UserSkillStatusEnum.REJECTED);
        String requestBody = objectMapper.writeValueAsString(requestDto);

        mockMvc
                .perform(
                        MockMvcRequestBuilders.post(baseRoute + "/" + skill.getId() + "/action")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("rejected"));

        // Verify the status was updated in database
        UserSkills updatedUserSkill =
                userSkillRepository.findByUserIdAndSkillId("test-user-id", skill.getId()).get(0);
        Assertions.assertEquals(UserSkillStatusEnum.REJECTED, updatedUserSkill.getStatus());
    }

    @Test
    @DisplayName("Error case - Request with non-existent skill ID")
    @WithCustomMockUser(
            username = "super-user-id",
            authorities = {"SUPERUSER"})
    public void approveSkillRequest_NonExistentSkillId_ShouldFail() throws Exception {
        SkillRequestActionRequestDto requestDto = new SkillRequestActionRequestDto();
        requestDto.setEndorseId("test-user-id");
        requestDto.setAction(UserSkillStatusEnum.APPROVED);
        String requestBody = objectMapper.writeValueAsString(requestDto);

        mockMvc
                .perform(
                        MockMvcRequestBuilders.post(baseRoute + "/123/action")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Error case - Request with non-existent user ID")
    @WithCustomMockUser(
            username = "super-user-id",
            authorities = {"SUPERUSER"})
    public void approveSkillRequest_NonExistentUserId_ShouldFail() throws Exception {
        SkillRequestActionRequestDto requestDto = new SkillRequestActionRequestDto();
        requestDto.setEndorseId("non-existent-user");
        requestDto.setAction(UserSkillStatusEnum.APPROVED);
        String requestBody = objectMapper.writeValueAsString(requestDto);

        mockMvc
                .perform(
                        MockMvcRequestBuilders.post(baseRoute + "/" + skill.getId() + "/action")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Validation test - Missing required fields")
    @WithCustomMockUser(
            username = "super-user-id",
            authorities = {"SUPERUSER"})
    public void approveSkillRequest_MissingRequiredFields_ShouldFail() throws Exception {
        String requestBody = "{}";

        mockMvc
                .perform(
                        MockMvcRequestBuilders.post(baseRoute + "/" + skill.getId() + "/action")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Authorization test - Non-super user cannot access endpoint")
    @WithCustomMockUser(
            username = "normal-user",
            authorities = {"USER"})
    public void approveSkillRequest_NonSuperUser_ShouldFail() throws Exception {
        SkillRequestActionRequestDto requestDto = new SkillRequestActionRequestDto();
        requestDto.setEndorseId("test-user-id");
        requestDto.setAction(UserSkillStatusEnum.APPROVED);
        String requestBody = objectMapper.writeValueAsString(requestDto);

        mockMvc
                .perform(
                        MockMvcRequestBuilders.post(baseRoute + "/" + skill.getId() + "/action")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
