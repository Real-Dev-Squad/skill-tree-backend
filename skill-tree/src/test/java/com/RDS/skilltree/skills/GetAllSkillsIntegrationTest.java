package com.RDS.skilltree.skills;

import com.RDS.skilltree.enums.SkillTypeEnum;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.repositories.SkillRepository;
import com.RDS.skilltree.services.SkillService;
import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GetAllSkillsIntegrationTest {

    @Autowired private SkillService skillService;

    @Autowired private SkillRepository skillRepository;

    @Autowired private MockMvc mockMvc;

    private Cookie authCookie;

    @BeforeEach
    public void setUp() {
        authCookie =
                new Cookie(
                        "rds-session-v2-development",
                        "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJzOXpRVW00WGJWRXo3eHpSa2FadiIsInJvbGUiOiJzdXBlcl91c2VyIiwiaWF0IjoxNzI4NjY0NjA2LCJleHAiOjE3MzEyNTY2MDZ9.EyOFKrVcbleuTjUGic3GzOzYRDoLU4IShyoboe0MHlvWFOAfU2pchpXLE4NcyvdGUZ_tvoUecHd4kUkR8MkhxnkRNU3HE7N-1c1tFeYXZL0KfScJE9YzDXAl113Hx3eZVvYbhNjNUttbDlH4s_kR6YABC3sdbLGKEiLfmp9VeAs");

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
    }

    @Test
    @DisplayName("happy flow - returns all skills that are in db")
    public void getAllSkillsHappyFlow() throws Exception {

        mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/v1/skills")
                                .cookie(authCookie)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Java"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Springboot"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("if no skills available, return empty list")
    public void noSkillsAvailable_shouldReturnEmptyList() throws Exception {
        skillRepository.deleteAll();

        mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/v1/skills")
                                .cookie(authCookie)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("if invalid cookie, return 401")
    public void ifInvalidCoolie_returnUnauthorized() throws Exception {
        mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/v1/skills")
                                .cookie(new Cookie("cookie1", "eyJhbGciOiJSUz.eyJhbGciOiJSUz.EyJhbGciOiJSUz"))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
