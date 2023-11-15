package com.RDS.skilltree.Skill;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public List<SkillDTO> getAllSkills(){
        return skillsService.getAllSkills();
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
