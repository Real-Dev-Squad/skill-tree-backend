package com.RDS.skilltree;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Skill Tree",
                version = "1.0.0",
                description = "Skill Tree"
        ),
        tags = @Tag(
                name = "Skill Tree",
                description = "Skill Tree"
        )
)
public class SkillTreeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkillTreeApplication.class, args);
    }
}
