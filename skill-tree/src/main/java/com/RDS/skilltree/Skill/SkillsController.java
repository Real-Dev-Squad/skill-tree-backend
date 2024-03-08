package com.RDS.skilltree.Skill;

import com.RDS.skilltree.utils.MessageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/v1/skills")
public class SkillsController {
    private final SkillsService skillsService;

    public SkillsController(SkillsService skillsService){
        this.skillsService = skillsService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createSkill(@RequestBody(required = true) @Valid  SkillDRO skillDRO){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(skillsService.createSkill(skillDRO));
        } catch(DataIntegrityViolationException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Cannot create entry for Skill as Skill name is duplicate"));
        } catch(Exception ex){
            log.error("There is some error in storing the skills, error message: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @GetMapping("/")
    public Page<SkillDTO> getAllSkills(
            @RequestParam(value = "offset", defaultValue = "0", required = false) @Min(0) int offset,
            @RequestParam(value = "limit", defaultValue = "10", required = false) @Min(1) int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        return skillsService.getAllSkills(pageable);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getSkillByName(@PathVariable(value = "name", required = true) String name){
        SkillDTO skillDTO = skillsService.getSkillByName(name);
        if (ObjectUtils.isEmpty(skillDTO)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Skill not found with the given name"));
        }
        return ResponseEntity.ok(skillDTO);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getSkillById(@PathVariable(value = "id", required = true) UUID id){
        SkillDTO skillDTO = skillsService.getSkillById(id);
        if (ObjectUtils.isEmpty(skillDTO)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Skill not found with given Id"));
        }
        return ResponseEntity.ok(skillDTO);
    }

}
