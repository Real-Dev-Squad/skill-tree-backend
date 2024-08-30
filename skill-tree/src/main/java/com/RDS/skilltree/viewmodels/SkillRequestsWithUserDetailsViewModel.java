package com.RDS.skilltree.viewmodels;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SkillRequestsWithUserDetailsViewModel {
    private List<SkillRequestViewModel> skillRequests;
    private List<UserViewModel> users;
}
