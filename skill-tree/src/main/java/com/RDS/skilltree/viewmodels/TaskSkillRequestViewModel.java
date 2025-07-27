package com.RDS.skilltree.viewmodels;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskSkillRequestViewModel {
    @NotEmpty(message = "Skill IDs list cannot be empty")
    private List<Integer> skillIds;
}
