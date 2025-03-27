package com.RDS.skilltree.apis;

import com.RDS.skilltree.annotations.AuthorizedRoles;
import com.RDS.skilltree.enums.UserRoleEnum;
import com.RDS.skilltree.models.JwtUser;
import com.RDS.skilltree.services.TaskSkillService;
import com.RDS.skilltree.viewmodels.CreateTaskSkillViewModel;
import com.RDS.skilltree.viewmodels.TaskSkillRequestViewModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/tasks")
@Validated
public class TaskSkillApi {

    private final TaskSkillService taskSkillService;

    @AuthorizedRoles({UserRoleEnum.SUPERUSER})
    @PostMapping("/{taskId}/skills")
    public ResponseEntity<CreateTaskSkillViewModel> createTaskSkills(
            @PathVariable @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Task ID must be valid")
                    String taskId,
            @Valid @RequestBody TaskSkillRequestViewModel request) {
        JwtUser currentUser =
                (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String createdBy = currentUser.getRdsUserId();
        CreateTaskSkillViewModel response =
                taskSkillService.createTaskSkills(taskId, request.getSkillIds(), createdBy);
        return ResponseEntity.ok(response);
    }
}
