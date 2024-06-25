package com.RDS.skilltree.apis;

import com.RDS.skilltree.services.SkillService;
import com.RDS.skilltree.viewmodels.CreateSkillViewModel;
import com.RDS.skilltree.viewmodels.SkillViewModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<SkillViewModel> create(@Valid @RequestBody CreateSkillViewModel skill) {
        return ResponseEntity.ok(skillService.create(skill));
    }
}
