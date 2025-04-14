package com.RDS.skilltree.integration.skills;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.RDS.skilltree.TestContainerManager;
import com.RDS.skilltree.dtos.RdsGetUserDetailsResDto;
import com.RDS.skilltree.enums.SkillTypeEnum;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.repositories.SkillRepository;
import com.RDS.skilltree.services.external.RdsService;
import com.RDS.skilltree.utils.JWTUtils;
import com.RDS.skilltree.viewmodels.RdsUserViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
public class CreateSkillIntegrationTest extends TestContainerManager {
    @Autowired private MockMvc mockMvc;
    @Autowired private SkillRepository skillRepository;
    @MockBean private RdsService rdsService;
    @MockBean private JWTUtils jwtUtils;
    @Autowired private ObjectMapper objectMapper;

    private final String baseRoute = "/v1/skills";

    @BeforeEach
    void setUp() {
        // Clean up repository
        skillRepository.deleteAll();

        // Setup super-user detail
        RdsUserViewModel superUser = new RdsUserViewModel();
        superUser.setId("super-user-id");
        RdsUserViewModel.Roles roles = new RdsUserViewModel.Roles();
        roles.setSuper_user(true);
        superUser.setRoles(roles);

        RdsGetUserDetailsResDto superUserDetails = new RdsGetUserDetailsResDto();
        superUserDetails.setUser(superUser);

        // Setup RDS service mock responses
        when(rdsService.getUserDetails("super-user-id")).thenReturn(superUserDetails);

        // Mock JWTUtils to bypass actual JWT verification
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("userId", String.class)).thenReturn("super-user-id");
        when(jwtUtils.validateToken(anyString())).thenReturn(mockClaims);
    }

    @Test
    @DisplayName("Happy flow - Super user can create a new skill")
    @WithCustomMockUser(
            username = "super-user-id",
            authorities = {"SUPERUSER"})
    public void createSkill_validRequest_shouldCreateSkill() throws Exception {
        String requestBody = "{" + "\"name\": \"Java\"," + "\"type\": \"ATOMIC\"" + "}";

        mockMvc
                .perform(
                        MockMvcRequestBuilders.post(baseRoute)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Java"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("ATOMIC"));

        assert skillRepository.existsByName("Java");
    }

    @Test
    @DisplayName("Error case - Cannot create duplicate skill")
    @WithCustomMockUser(
            username = "super-user-id",
            authorities = {"SUPERUSER"})
    public void createSkill_duplicateName_shouldFail() throws Exception {
        Skill existingSkill = new Skill();
        existingSkill.setName("Java");
        existingSkill.setType(SkillTypeEnum.ATOMIC);
        existingSkill.setCreatedBy("super-user-id");
        skillRepository.save(existingSkill);

        String requestBody = "{" + "\"name\": \"Java\"," + "\"type\": \"ATOMIC\"" + "}";

        mockMvc
                .perform(
                        MockMvcRequestBuilders.post(baseRoute)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is(409));
    }

    @Test
    @DisplayName("Validation test - Missing required fields")
    @WithCustomMockUser(
            username = "super-user-id",
            authorities = {"SUPERUSER"})
    public void createSkill_missingRequiredFields_shouldFail() throws Exception {
        String requestBody = "{" + "\"type\": \"ATOMIC\"" + "}";

        mockMvc
                .perform(
                        MockMvcRequestBuilders.post(baseRoute)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    @DisplayName("Validation test - Invalid skill type")
    @WithCustomMockUser(
            username = "super-user-id",
            authorities = {"SUPERUSER"})
    public void createSkill_invalidSkillType_shouldFail() throws Exception {
        String requestBody = "{" + "\"name\": \"Java\"," + "\"type\": \"INVALID_TYPE\"" + "}";

        mockMvc
                .perform(
                        MockMvcRequestBuilders.post(baseRoute)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is(500));
    }

    @Test
    @DisplayName("Authorization test - Non-super user cannot create skill")
    @WithCustomMockUser(
            username = "normal-user",
            authorities = {"USER"})
    public void createSkill_nonSuperUser_shouldFail() throws Exception {
        String requestBody = "{" + "\"name\": \"Java\"," + "\"type\": \"ATOMIC\"" + "}";

        mockMvc
                .perform(
                        MockMvcRequestBuilders.post(baseRoute)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is(403));
    }
}
