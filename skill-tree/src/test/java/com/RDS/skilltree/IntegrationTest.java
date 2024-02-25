package com.RDS.skilltree;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class IntegrationTest extends TestContainerManager{

    @Value("${cookieName}")
    private String cookieName;


    @Test
    void testValidTokenInCookie() throws Exception {
        // Replace "yourCookieName" with the actual cookie name
        String validToken = "validToken";

//        mockMvc.perform(MockMvcRequestBuilders.get("/your-protected-endpoint")
//                        .cookie("yourCookieName", validToken))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.request().attribute("yourExpectedAttribute", yourExpectedValue));
    }
}
