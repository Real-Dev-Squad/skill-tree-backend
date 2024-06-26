package com.RDS.skilltree.apis;

import com.RDS.skilltree.services.EndorsementService;
import com.RDS.skilltree.services.SkillService;
import com.RDS.skilltree.viewmodels.CreateSkillViewModel;
import com.RDS.skilltree.viewmodels.EndorsementViewModel;
import com.RDS.skilltree.viewmodels.SkillViewModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/skills")
public class SkillsApi {
    private final SkillService skillService;
    private final EndorsementService endorsementService;

    @GetMapping
    public ResponseEntity<List<SkillViewModel>> getAll() {
        return ResponseEntity.ok(skillService.getAll());
    }

    @PostMapping
    public ResponseEntity<SkillViewModel> create(@Valid @RequestBody CreateSkillViewModel skill) {
        return ResponseEntity.ok(skillService.create(skill));
    }

    @GetMapping("/{id}/endorsements")
    public ResponseEntity<Page<EndorsementViewModel>> getEndorsementsBySkillId(
            @RequestParam(name = "offset", defaultValue = "0", required = false) @Min(0) int offset,
            @RequestParam(name = "limit", defaultValue = "10", required = false) @Min(1) int limit,
            @PathVariable(value = "id") Integer skillID) {
        PageRequest pageRequest = PageRequest.of(offset, limit);
        return ResponseEntity.ok(endorsementService.getAllEndorsementsBySkillId(skillID, pageRequest));

    }
}
