package com.RDS.skilltree.apis;

import com.RDS.skilltree.annotations.AuthorizedRoles;
import com.RDS.skilltree.dtos.CreateEndorsementRequestDto;
import com.RDS.skilltree.dtos.SkillRequestActionRequestDto;
import com.RDS.skilltree.dtos.SkillRequestsDto;
import com.RDS.skilltree.enums.UserRoleEnum;
import com.RDS.skilltree.enums.UserSkillStatusEnum;
import com.RDS.skilltree.services.EndorsementService;
import com.RDS.skilltree.services.SkillService;
import com.RDS.skilltree.utils.GenericResponse;
import com.RDS.skilltree.viewmodels.CreateEndorsementViewModel;
import com.RDS.skilltree.viewmodels.CreateSkillViewModel;
import com.RDS.skilltree.viewmodels.EndorsementViewModel;
import com.RDS.skilltree.viewmodels.SkillViewModel;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/requests")
    public ResponseEntity<SkillRequestsDto> getAllRequests(
            @RequestParam(value = "status", required = false) UserSkillStatusEnum status) {
        if (status != null) {
            return ResponseEntity.ok(skillService.getRequestsByStatus(status));
        }

        return ResponseEntity.ok(skillService.getAllRequests());
    }

    @PostMapping("/requests/{skillId}/action")
    @AuthorizedRoles({UserRoleEnum.SUPERUSER})
    public ResponseEntity<GenericResponse<String>> approveRejectSkillRequest(
            @PathVariable(value = "skillId") Integer skillId,
            @Valid @RequestBody SkillRequestActionRequestDto skillRequestAction) {
        return ResponseEntity.ok(
                skillService.approveRejectSkillRequest(
                        skillId, skillRequestAction.getEndorseId(), skillRequestAction.getAction()));
    }

    @PostMapping
    @AuthorizedRoles({UserRoleEnum.SUPERUSER})
    public ResponseEntity<SkillViewModel> create(@Valid @RequestBody CreateSkillViewModel skill) {
        return ResponseEntity.ok(skillService.create(skill));
    }

    @GetMapping("/{id}/endorsements")
    public ResponseEntity<List<EndorsementViewModel>> getEndorsementsBySkillId(
            @PathVariable(value = "id") Integer skillID) {
        return ResponseEntity.ok(endorsementService.getAllEndorsementsBySkillId(skillID));
    }

    @PostMapping("/{id}/endorsements")
    public ResponseEntity<EndorsementViewModel> create(
            @PathVariable(value = "id") Integer skillID,
            @Valid @RequestBody CreateEndorsementRequestDto endorsementRequest) {
        return new ResponseEntity<>(
                endorsementService.create(
                        CreateEndorsementViewModel.toViewModel(
                                skillID, endorsementRequest.getEndorseId(), endorsementRequest.getMessage())),
                HttpStatus.CREATED);
    }
}
