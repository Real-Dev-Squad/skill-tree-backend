package com.RDS.skilltree.Skill;

import com.RDS.skilltree.Common.Response.GenericResponse;
import com.RDS.skilltree.User.JwtUserModel;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/skills")
public class SkillsController {
    private final SkillRepository repository;

    public SkillsController(SkillRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public GenericResponse<List<Skill>> getAllSkills(@RequestParam(required = false) String name) {
        repository.findByName(name);
        return new GenericResponse<>(repository.findAll(), null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // TODO : Return only the valid fields by using a DTO.
    public GenericResponse<Skill> createSkill(Authentication authentication, @RequestBody(required = true) @Valid SkillDRO skill) {
        JwtUserModel userDetails = (JwtUserModel) authentication.getPrincipal();

        if (repository.findByName(skill.getName()).isPresent()) {
            return new GenericResponse<>(null, String.format("Skill with name %s already exists", skill.getName()));
        }

        Skill newSkill = Skill.builder()
                .name(skill.getName())
                .type(skill.getType())
                // TODO : use the id from userDetails once login is implemented
                .createdBy(UUID.fromString("ae7a6673-c557-41e0-838f-209de4c644fc"))
                .isDeleted(false)
                .build();

        newSkill.setCreatedAt(Instant.now());

        try {
            return new GenericResponse<>(repository.save(newSkill), "Skill created");
        } catch (DataIntegrityViolationException error) {
            log.error("Error saving skill {}, error: {}", skill.getName(), error.getMessage());
            return new GenericResponse<>(null, "Something went wrong please try again");
        }
    }
}
