package com.RDS.skilltree.dtos;

import com.RDS.skilltree.viewmodels.SkillRequestViewModel;
import com.RDS.skilltree.viewmodels.UserViewModel;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SkillRequestsDto {
    private List<SkillRequestViewModel> requests;
    private List<UserViewModel> users;

    public static SkillRequestsDto toDto(
            List<SkillRequestViewModel> skillRequests, List<UserViewModel> users) {
        SkillRequestsDto skillRequestsDto = new SkillRequestsDto();
        skillRequestsDto.setRequests(skillRequests);
        skillRequestsDto.setUsers(users);

        return skillRequestsDto;
    }
}
