package com.RDS.skilltree.viewmodels;

import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserSkillsModel;
import com.RDS.skilltree.models.Endorsement;
import com.RDS.skilltree.models.Skill;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SkillRequestViewModel {
    private Integer skillId;
    private String skillName;
    private String endorseId;
    private List<MinimalEndorsementViewModel> endorsements;

    public static SkillRequestViewModel toViewModel(
            UserSkillsModel userSkillsModel, List<Endorsement> endorsements) {
        SkillRequestViewModel skillRequestViewModel = new SkillRequestViewModel();

        Skill skill = userSkillsModel.getSkill();
        UserModel user = userSkillsModel.getUser();

        skillRequestViewModel.setSkillId(skill.getId());
        skillRequestViewModel.setSkillName(skill.getName());
        skillRequestViewModel.setEndorseId(user.getId());
        skillRequestViewModel.setEndorsements(
                endorsements.stream()
                        .map(MinimalEndorsementViewModel::toViewModel)
                        .collect(Collectors.toList()));

        return skillRequestViewModel;
    }
}
