package com.RDS.skilltree.apis;

import com.RDS.skilltree.User.UserRoleEnum;
import com.RDS.skilltree.annotations.AuthorizedRoles;
import com.RDS.skilltree.dtos.SkillRequestsDto;
import com.RDS.skilltree.services.EndorsementService;
import com.RDS.skilltree.services.SkillService;
import com.RDS.skilltree.viewmodels.CreateSkillViewModel;
import com.RDS.skilltree.viewmodels.EndorsementViewModel;
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
@AuthorizedRoles({UserRoleEnum.USER, UserRoleEnum.SUPERUSER})
public class SkillsApi {
    private final SkillService skillService;
    private final EndorsementService endorsementService;

    @GetMapping
    public ResponseEntity<List<SkillViewModel>> getAll() {
        return ResponseEntity.ok(skillService.getAll());
    }

    @GetMapping("/requests")
    public ResponseEntity<SkillRequestsDto> getAllRequests() {
        return ResponseEntity.ok(skillService.getAllRequests());
    }

    @PostMapping
    @AuthorizedRoles({UserRoleEnum.SUPERUSER})
    public ResponseEntity<SkillViewModel> create(@Valid @RequestBody CreateSkillViewModel skill) {
        return ResponseEntity.ok(skillService.create(skill));
    }

    @GetMapping("/{id}/endorsements")
    public ResponseEntity<List<EndorsementViewModel>> getEndorsementsBySkillId(@PathVariable(value = "id") Integer skillID) {
        return ResponseEntity.ok(endorsementService.getAllEndorsementsBySkillId(skillID));
    }
}
