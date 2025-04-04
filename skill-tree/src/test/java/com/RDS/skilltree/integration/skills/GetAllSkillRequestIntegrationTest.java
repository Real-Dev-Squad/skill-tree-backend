package com.RDS.skilltree.integration.skills;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.RDS.skilltree.dtos.RdsGetUserDetailsResDto;
import com.RDS.skilltree.enums.SkillTypeEnum;
import com.RDS.skilltree.enums.UserSkillStatusEnum;
import com.RDS.skilltree.models.Endorsement;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.models.UserSkills;
import com.RDS.skilltree.repositories.EndorsementRepository;
import com.RDS.skilltree.repositories.SkillRepository;
import com.RDS.skilltree.repositories.UserSkillRepository;
import com.RDS.skilltree.services.external.RdsService;
import com.RDS.skilltree.utils.JWTUtils;
import com.RDS.skilltree.viewmodels.RdsUserViewModel;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import utils.CustomResultMatchers;
import utils.WithCustomMockUser;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GetAllSkillRequestIntegrationTest {
    @Autowired private MockMvc mockMvc;

    @Autowired private UserSkillRepository userSkillRepository;

    @Autowired private EndorsementRepository endorsementRepository;

    @MockBean private RdsService rdsService;

    @Autowired private SkillRepository skillRepository;

    @MockBean private JWTUtils jwtUtils;

    private final String route = "/v1/skills/requests";

    @BeforeEach
    void setUp() {
        // Clean up repositories
        skillRepository.deleteAll();
        endorsementRepository.deleteAll();
        userSkillRepository.deleteAll();

        // Setup super-user detail
        RdsUserViewModel superUser = new RdsUserViewModel();
        superUser.setId("super-user-id");
        RdsUserViewModel.Roles roles = new RdsUserViewModel.Roles();
        roles.setSuper_user(true);
        superUser.setRoles(roles);

        RdsGetUserDetailsResDto superUserDetails = new RdsGetUserDetailsResDto();
        superUserDetails.setUser(superUser);

        // Setup normal-users detail
        RdsUserViewModel normalUser = new RdsUserViewModel();
        normalUser.setId("user-id");
        RdsUserViewModel.Roles normalUserRoles = new RdsUserViewModel.Roles();
        normalUserRoles.setSuper_user(false);
        normalUser.setRoles(normalUserRoles);

        RdsGetUserDetailsResDto normalUserDetails = new RdsGetUserDetailsResDto();
        normalUserDetails.setUser(normalUser);

        RdsUserViewModel normalUser2 = new RdsUserViewModel();
        normalUser2.setId("user-id-2");
        normalUser2.setRoles(normalUserRoles);
        RdsGetUserDetailsResDto normalUser2Details = new RdsGetUserDetailsResDto();
        normalUser2Details.setUser(normalUser2);

        // Setup mock skills
        Skill skill1 = new Skill();
        skill1.setName("Java");
        skill1.setType(SkillTypeEnum.ATOMIC);
        skill1.setCreatedBy("super-user-id");

        Skill skill2 = new Skill();
        skill2.setName("Springboot");
        skill2.setType(SkillTypeEnum.ATOMIC);
        skill2.setCreatedBy("super-user-id");

        skillRepository.save(skill1);
        skillRepository.save(skill2);

        // Setup mock user-skills
        UserSkills userSkills1 = new UserSkills();
        userSkills1.setSkill(skill1);
        userSkills1.setUserId("user-id");
        userSkills1.setStatus(UserSkillStatusEnum.PENDING);

        UserSkills userSkills2 = new UserSkills();
        userSkills2.setSkill(skill2);
        userSkills2.setUserId("user-id");
        userSkills2.setStatus(UserSkillStatusEnum.APPROVED);

        UserSkills userSkills3 = new UserSkills();
        userSkills3.setSkill(skill2);
        userSkills3.setUserId("user-id-2");
        userSkills3.setStatus(UserSkillStatusEnum.PENDING);

        userSkillRepository.save(userSkills1);
        userSkillRepository.save(userSkills2);
        userSkillRepository.save(userSkills3);

        // Setup mock endorsements
        Endorsement endorsement1 = new Endorsement();
        endorsement1.setId(1);
        endorsement1.setEndorserId("super-user-id");
        endorsement1.setEndorseId("user-id");
        endorsement1.setSkill(skill1);
        endorsement1.setMessage("endorsement message");

        Endorsement endorsement2 = new Endorsement();
        endorsement2.setId(3);
        endorsement2.setEndorserId("user-id-2");
        endorsement2.setEndorseId("user-id");
        endorsement2.setSkill(skill2);
        endorsement2.setMessage("skill2 for user-id");

        Endorsement endorsement3 = new Endorsement();
        endorsement3.setId(4);
        endorsement3.setEndorserId("super-user-id");
        endorsement3.setEndorseId("user-id-2");
        endorsement3.setSkill(skill2);
        endorsement3.setMessage("skill2 for user-id-2");

        endorsementRepository.save(endorsement1);
        endorsementRepository.save(endorsement2);
        endorsementRepository.save(endorsement3);

        // Setup RDS service mock responses
        when(rdsService.getUserDetails("super-user-id")).thenReturn(superUserDetails);
        when(rdsService.getUserDetails("user-id-2")).thenReturn(normalUser2Details);
        when(rdsService.getUserDetails("user-id")).thenReturn(normalUserDetails);

        // Mock JWTUtils to bypass actual JWT verification
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("userId", String.class)).thenReturn("super-user-id");
        when(jwtUtils.validateToken(anyString())).thenReturn(mockClaims);
    }

    @Test
    @DisplayName("Happy flow for SuperUser - should return all requests")
    @WithCustomMockUser(
            username = "super-user-id",
            authorities = {"SUPERUSER"})
    public void getAllRequests_asSuperUser_shouldReturnAllRequests() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get(route).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(CustomResultMatchers.hasSkillRequest("Java", "user-id", "PENDING"))
                .andExpect(
                        CustomResultMatchers.hasEndorsement(
                                "Java", "user-id", "super-user-id", "endorsement message"))
                .andExpect(CustomResultMatchers.hasSkillRequest("Springboot", "user-id", "APPROVED"))
                .andExpect(
                        CustomResultMatchers.hasEndorsement(
                                "Springboot", "user-id", "user-id-2", "skill2 for user-id"))
                .andExpect(CustomResultMatchers.hasSkillRequest("Springboot", "user-id-2", "PENDING"))
                .andExpect(
                        CustomResultMatchers.hasEndorsement(
                                "Springboot", "user-id-2", "super-user-id", "skill2 for user-id-2"))
                .andExpect(CustomResultMatchers.hasUser("user-id", " "))
                .andExpect(CustomResultMatchers.hasUser("user-id-2", " "))
                .andExpect(CustomResultMatchers.hasUser("super-user-id", " "));
    }

    @Test
    @DisplayName("Happy flow for normal user - Get all requests where user is endorser")
    @WithCustomMockUser(
            username = "user-id-2",
            authorities = {"USER"})
    public void getAllRequests_asNormalUser_shouldReturnAllRequestsByEndorser() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get(route).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(CustomResultMatchers.hasSkillRequest("Springboot", "user-id", "APPROVED"))
                .andExpect(
                        CustomResultMatchers.hasEndorsement(
                                "Springboot", "user-id", "user-id-2", "skill2 for user-id"))
                .andExpect(CustomResultMatchers.hasUser("user-id", " "))
                .andExpect(CustomResultMatchers.hasUser("user-id-2", " "));
    }

    @Test
    @DisplayName("Filter requests by status - should return filtered requests")
    @WithCustomMockUser(
            username = "super-user-id",
            authorities = {"SUPERUSER"})
    public void getAllRequests_ByStatus_ShouldReturnFilteredRequests() throws Exception {
        mockMvc
                .perform(
                        MockMvcRequestBuilders.get(route + "?status=APPROVED")
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(CustomResultMatchers.hasSkillRequest("Springboot", "user-id", "APPROVED"))
                .andExpect(
                        CustomResultMatchers.hasEndorsement(
                                "Springboot", "user-id", "user-id-2", "skill2 for user-id"))
                .andExpect(CustomResultMatchers.hasUser("user-id", " "))
                .andExpect(CustomResultMatchers.hasUser("user-id-2", " "));
    }

    @Test
    @DisplayName("If no skill Requests endorsed by user then return empty lists")
    @WithCustomMockUser(
            username = "user-id",
            authorities = {"USER"})
    public void noSkillRequestsEndorsedByUser_ShouldReturnEmptyLists() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get(route).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.requests").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.users").isEmpty());
    }

    @Test
    @DisplayName("If no matching skill requests by status then return empty lists")
    @WithCustomMockUser(
            username = "user-id",
            authorities = {"USER"})
    public void noMatchingRequestsByStatus_ShouldReturnEmptyLists() throws Exception {
        mockMvc
                .perform(
                        MockMvcRequestBuilders.get(route + "?status=REJECTED")
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.requests").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.users").isEmpty());
    }

    @Test
    @DisplayName("If no skill requests in DB - return empty lists")
    @WithCustomMockUser(
            username = "super-user-id",
            authorities = {"SUPERUSER"})
    public void getAllRequests_NoData_ShouldReturnEmptyLists() throws Exception {
        skillRepository.deleteAll();
        endorsementRepository.deleteAll();
        userSkillRepository.deleteAll();

        mockMvc
                .perform(MockMvcRequestBuilders.get(route).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.requests").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.users").isEmpty());
    }

    @Test
    @DisplayName("if invalid cookie, return 401")
    public void ifInvalidCoolie_ShouldReturnUnauthorized() throws Exception {
        Cookie authCookie =
                new Cookie(
                        "cookie",
                        "eyJhbGciOiJSUzI1NiIsInR5cCI.eyJ1c2VySWQiOiI2N2lSeXJOTWQ.E-EtcPOj7Ca5l8JuE0hwky0rRikYSNZBvC");

        mockMvc
                .perform(
                        MockMvcRequestBuilders.get(route)
                                .cookie(authCookie)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
