package com.RDS.skilltree.viewmodels;

import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserSkillsModel;
import com.RDS.skilltree.models.Endorsement;
import com.RDS.skilltree.models.Skill;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class SkillRequestViewModel {
    private Integer skillId;
    private String skillName;
    private String endorseId;
    private List<MinimalEndorsementViewModel> endorsements;

    public SkillRequestViewModel(
            Integer id, String name, String endorseId, List<MinimalEndorsementViewModel> endorsements) {
        this.skillId = id;
        this.skillName = name;
        this.endorseId = endorseId;
        this.endorsements = endorsements;
    }

    public static SkillRequestViewModel toViewModel(
            UserSkillsModel userSkillsModel, List<Endorsement> endorsements) {
        Skill skill = userSkillsModel.getSkill();
        UserModel user = userSkillsModel.getUser();

        return new SkillRequestViewModel(
                skill.getId(),
                skill.getName(),
                user.getId(),
                endorsements.stream()
                        .map(MinimalEndorsementViewModel::toViewModel)
                        .collect(Collectors.toList()));
    }
}
