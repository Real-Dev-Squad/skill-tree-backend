package com.RDS.skilltree.Skill;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public String createSkill(@RequestBody(required = true)  SkillDRO skillDRO){
        return skillsService.createSkill(skillDRO);
    }

    @GetMapping("/")
    public Page<SkillDTO> getAllSkills(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return skillsService.getAllSkills(pageable);
    }

    @GetMapping("/name/{name}")
    public SkillDTO getSkillByName(@PathVariable(value = "name", required = true) String name){
        return skillsService.getSkillByName(name);
    }
    @GetMapping("/{id}")
    public SkillDTO getSkillById(@PathVariable(value = "id", required = true) UUID id){
        return skillsService.getSkillById(id);
    }

}
