package com.RDS.skilltree.Skill;

import com.RDS.skilltree.Common.Response.GenericResponse;
import com.RDS.skilltree.Endorsement.EndorsementRepository;
import com.RDS.skilltree.User.JwtUserModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final EndorsementRepository endorsementRepository;

    public SkillsController(SkillRepository repository, EndorsementRepository endorsementRepository) {
        this.repository = repository;
        this.endorsementRepository = endorsementRepository;
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

        // TODO : return a correct http status instead of 201
        if (repository.findByName(skill.getName()).isPresent()) {
            return new GenericResponse<>(null, String.format("Skill with name %s already exists", skill.getName()));
        }

        Skill newSkill = Skill.builder()
                .name(skill.getName())
                .type(skill.getType())
                // TODO : use the id from userDetails once login is implemented
                .createdBy("ae7a6673c5574140838f209de4c644fc")
//                .isDeleted(false)
                .build();

//        newSkill.setCreatedAt(Instant.now());

        try {
            return new GenericResponse<>(repository.save(newSkill), "Skill created");
        } catch (DataIntegrityViolationException error) {
            log.error("Error saving skill {}, error: {}", skill.getName(), error.getMessage());
            // TODO : return a correct http status instead of 201
            return new GenericResponse<>(null, "Something went wrong please try again");
        }
    }

    @GetMapping("/{id}/endorsements")
    public ResponseEntity<Page<?>> getEndorsementsBySkillId(
            @RequestParam(name = "offset", defaultValue = "0", required = false) @Min(0) int offset,
            @RequestParam(name = "limit", defaultValue = "10", required = false) @Min(1) int limit,
            @PathVariable(value = "id") Integer skillID) {
        PageRequest pageRequest = PageRequest.of(offset, limit);
        return ResponseEntity.ok(endorsementRepository.findBySkillId(skillID, pageRequest));
    }
}
