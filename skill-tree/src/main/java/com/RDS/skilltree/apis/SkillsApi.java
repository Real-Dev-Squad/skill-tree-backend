package com.RDS.skilltree.apis;

import com.RDS.skilltree.services.SkillService;
import com.RDS.skilltree.viewmodels.SkillViewModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/skills")
public class SkillsApi {
    private final SkillService skillService;

    @GetMapping
    public ResponseEntity<List<SkillViewModel>> getAll() {
        return ResponseEntity.ok(skillService.getAll());
    }
}
