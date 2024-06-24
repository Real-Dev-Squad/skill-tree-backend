//package com.RDS.skilltree.Skill;
//
//import com.RDS.skilltree.utils.MessageResponse;
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.Min;
//import java.util.UUID;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.ObjectUtils;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@Slf4j
//@RequestMapping("/v1/skills")
//public class SkillsController {
//    private final SkillsService skillsService;
//
//    public SkillsController(SkillsService skillsService) {
//        this.skillsService = skillsService;
//    }
//
//    @PostMapping("/")
//    public ResponseEntity<?> createSkill(@RequestBody(required = true) @Valid SkillDRO skillDRO) {
//        try {
//            return ResponseEntity.status(HttpStatus.CREATED).body(skillsService.createSkill(skillDRO));
//        } catch (DataIntegrityViolationException ex) {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(new MessageResponse("Cannot create entry for Skill as Skill name is duplicate"));
//        } catch (Exception ex) {
//            log.error(
//                    "There is some error in storing the skills, error message: {}", ex.getMessage(), ex);
//            throw ex;
//        }
//    }
//
//    @GetMapping("/")
//    public Page<SkillDTO> getAllSkills(
//            @RequestParam(value = "offset", defaultValue = "0", required = false) @Min(0) int offset,
//            @RequestParam(value = "limit", defaultValue = "10", required = false) @Min(1) int limit) {
//        Pageable pageable = PageRequest.of(offset, limit);
//        return skillsService.getAllSkills(pageable);
//    }
//
//    @GetMapping("/name/{name}")
//    public ResponseEntity<?> getSkillByName(
//            @PathVariable(value = "name", required = true) String name) {
//        SkillDTO skillDTO = skillsService.getSkillByName(name);
//        if (ObjectUtils.isEmpty(skillDTO)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new MessageResponse("Skill not found with the given name"));
//        }
//        return ResponseEntity.ok(skillDTO);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getSkillById(@PathVariable(value = "id", required = true) UUID id) {
//        SkillDTO skillDTO = skillsService.getSkillById(id);
//        if (ObjectUtils.isEmpty(skillDTO)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new MessageResponse("Skill not found with given Id"));
//        }
//        return ResponseEntity.ok(skillDTO);
//    }
//}

package com.RDS.skilltree.Skill;

import com.RDS.skilltree.Common.Response.GenericResponse;
import com.RDS.skilltree.User.JwtUserModel;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collections;
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
        if (name != null && !name.isEmpty()) {
            List<Skill> skills = repository.findBy()
                    .map(Collections::singletonList)
                    .orElseGet(Collections::emptyList);

            ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase("name");
            Example<Skill> skills1 = Example.of(skills, matcher);
            
            return new GenericResponse<>(skills, null);
        }
        return new GenericResponse<>(repository.findAll(), null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Skill createSkill(Authentication authentication, @RequestBody(required = true) @Valid SkillDRO skill) {
        JwtUserModel userDetails = (JwtUserModel) authentication.getPrincipal();

        Skill newSkill = Skill.builder()
                .name(skill.getName())
                .type(skill.getType())
                // TODO : use the id from userDetails once login is implemented
                .createdBy(UUID.fromString("ae7a6673-c557-41e0-838f-209de4c644fc"))
                .isDeleted(false)
                .build();

        newSkill.setCreatedAt(Instant.now());
        newSkill.setUpdatedAt(Instant.now());

        try {
            return repository.save(newSkill);
        } catch (DataIntegrityViolationException error) {
            log.error("Error saving skill {}, error: {}", skill.getName(), error.getMessage());
            throw error;
        }
    }
}
