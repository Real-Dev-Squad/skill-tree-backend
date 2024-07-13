package com.RDS.skilltree.viewmodels;

import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserSkillsModel;
import com.RDS.skilltree.models.Endorsement;
import com.RDS.skilltree.models.Skill;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class SkillRequestViewModel {
    private Integer id;
    private String name;
    private String endorseId;
    private List<MinimalEndorsementViewModel> endorsements;

    public static SkillRequestViewModel toViewModel(UserSkillsModel userSkillsModel, List<Endorsement> endorsements) {
        SkillRequestViewModel skillRequestViewModel = new SkillRequestViewModel();

        Skill skill = userSkillsModel.getSkill();
        UserModel user = userSkillsModel.getUser();


        skillRequestViewModel.setId(skill.getId());
        skillRequestViewModel.setName(skill.getName());
        skillRequestViewModel.setEndorseId(user.getId());
        skillRequestViewModel.setEndorsements(endorsements.stream().map(MinimalEndorsementViewModel::toViewModel).collect(Collectors.toList()));

        return skillRequestViewModel;
    }
}
