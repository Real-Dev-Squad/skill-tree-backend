package com.RDS.skilltree.Skill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/skills")
public class SkillController {
    @Autowired
    private SkillService skillService;

    @PostMapping("/")
    public SkillModel createSkill(@RequestBody SkillModel skill) {
        return skillService.createSkill(skill);
    }

    @GetMapping("/")
    public List<SkillDto> getAllSkills() {
        return skillService.getAllSkills();
    }
}
