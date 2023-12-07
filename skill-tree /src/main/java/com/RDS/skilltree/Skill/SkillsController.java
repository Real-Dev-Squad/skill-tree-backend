package com.RDS.skilltree.Skill;

import com.RDS.skilltree.utils.MessageResponse;
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
    public ResponseEntity<?> createSkill(@RequestBody(required = true) SkillDRO skillDRO){
        if (skillDRO.getCreatedBy() == null || skillDRO.getType() == null || skillDRO.getName() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("CreatedBy, Type and Name are mandatory values, anyone cannot be null"));
        }
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(skillsService.createSkill(skillDRO));
        } catch(DataIntegrityViolationException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Cannot create entry for Skill as Skill name is duplicate"));
        }
    }

    @GetMapping("/")
    public Page<SkillDTO> getAllSkills(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
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
