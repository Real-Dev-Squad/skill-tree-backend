package com.RDS.skilltree.viewmodels;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class SkillRequestsWithUserDetailsViewModel {
    private List<SkillRequestViewModel> skillRequests;
   private  List<UserViewModel> users;
}
