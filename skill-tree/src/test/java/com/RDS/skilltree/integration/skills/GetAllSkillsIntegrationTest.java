package com.RDS.skilltree.integration.skills;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.RDS.skilltree.TestContainerManager;
import com.RDS.skilltree.enums.SkillTypeEnum;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.repositories.SkillRepository;
import com.RDS.skilltree.services.SkillService;
import com.RDS.skilltree.services.external.RdsService;
import com.RDS.skilltree.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import java.util.Arrays;
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
public class GetAllSkillsIntegrationTest extends TestContainerManager {

    @Autowired private SkillService skillService;

    @Autowired private SkillRepository skillRepository;

    @Autowired private MockMvc mockMvc;
    @MockBean private JWTUtils jwtUtils;

    @MockBean private RdsService rdsService;

    private final String route = "/v1/skills";

    @BeforeEach
    public void setUp() {
        skillRepository.deleteAll();
        Skill skill1 = new Skill();
        skill1.setName("Java");
        skill1.setType(SkillTypeEnum.ATOMIC);
        skill1.setCreatedBy("s9zQUm4XbVEz7xzRkaZv");

        Skill skill2 = new Skill();
        skill2.setName("Springboot");
        skill2.setType(SkillTypeEnum.ATOMIC);
        skill2.setCreatedBy("s9zQUm4XbVEz7xzRkaZv");

        skillRepository.saveAll(Arrays.asList(skill1, skill2));

        // Mock JWTUtils to bypass actual JWT verification
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("userId", String.class)).thenReturn("rds-user");
        when(jwtUtils.validateToken(anyString())).thenReturn(mockClaims);
    }

    @Test
    @WithCustomMockUser(
            username = "rds-user",
            authorities = {"SUPERUSER"})
    @DisplayName("happy flow - returns all skills that are in db")
    public void getAllSkillsHappyFlow() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get(route).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Java"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Springboot"));
    }

    @Test
    @DisplayName("if no skills available, return empty list")
    @WithCustomMockUser(
            username = "rds-user",
            authorities = {"SUPERUSER"})
    public void noSkillsAvailable_shouldReturnEmptyList() throws Exception {
        skillRepository.deleteAll();

        mockMvc
                .perform(MockMvcRequestBuilders.get(route).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("if invalid cookie, return 401")
    public void ifInvalidCoolie_returnUnauthorized() throws Exception {
        Cookie authCookie =
                new Cookie(
                        "cookie",
                        "eyJhbGciOiJSUzI1NiIsInR5cCI.eyJ1c2VySWQiOiI2N2lSeXJOTWQ.E-EtcPOj7Ca5l8JuE0hwky0rRikYSNZBvC");

        mockMvc
                .perform(
                        MockMvcRequestBuilders.get(route).cookie(authCookie).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
