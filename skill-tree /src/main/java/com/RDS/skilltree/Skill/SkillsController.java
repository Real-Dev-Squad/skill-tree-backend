package com.RDS.skilltree.Skill;

import com.RDS.skilltree.Exceptions.NoEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
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
    public String createSkill(@RequestBody(required = true) SkillDRO skillDRO){
        return skillsService.createSkill(skillDRO);
    }

    @GetMapping("/")
    public Page<SkillDTO> getAllSkills(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return skillsService.getAllSkills(pageable);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<SkillDTO> getSkillByName(@PathVariable(value = "name", required = true) String name){
        SkillDTO skillDTO = skillsService.getSkillByName(name);
        if (ObjectUtils.isEmpty(skillDTO)){
            throw new NoEntityException("No skill found for given name: "+ name);
        }
        return ResponseEntity.ok(skillDTO);
    }
    @GetMapping("/{id}")
    public ResponseEntity<SkillDTO> getSkillById(@PathVariable(value = "id", required = true) UUID id){
        SkillDTO skillDTO = skillsService.getSkillById(id);
        if (ObjectUtils.isEmpty(skillDTO)){
            throw new NoEntityException("No skill found for the given Id"+ id);
        }
        return ResponseEntity.ok(skillDTO);
    }

}
