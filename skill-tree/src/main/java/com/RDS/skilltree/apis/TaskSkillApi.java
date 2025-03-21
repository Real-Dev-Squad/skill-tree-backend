package com.RDS.skilltree.apis;

import com.RDS.skilltree.annotations.AuthorizedRoles;
import com.RDS.skilltree.enums.UserRoleEnum;
import com.RDS.skilltree.exceptions.TaskSkillAssociationAlreadyExistsException;
import com.RDS.skilltree.models.JwtUser;
import com.RDS.skilltree.services.TaskSkillService;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/tasks")
public class TaskSkillApi {

    private final TaskSkillService taskSkillService;

    @AuthorizedRoles({UserRoleEnum.SUPERUSER})
    @PostMapping("/{taskId}/skills")
    public ResponseEntity<ApiResponse> createTaskSkills(
            @PathVariable String taskId, @RequestBody TaskSkillsRequest request) {
        // Extract the authenticated user's details from the security context.
        JwtUser currentUser =
                (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String createdBy = currentUser.getRdsUserId(); // Save the user's unique identifier
        taskSkillService.createTaskSkills(taskId, request.getSkillIds(), createdBy);
        return ResponseEntity.ok(new ApiResponse("Skills are linked to task successfully!"));
    }

    @ExceptionHandler(TaskSkillAssociationAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleTaskSkillAssociationAlreadyExistsException(
            TaskSkillAssociationAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(ex.getMessage()));
    }

    @Data
    public static class TaskSkillsRequest {
        private List<Integer> skillIds;
    }

    @Data
    public static class ApiResponse {
        private final String message;
    }
}
