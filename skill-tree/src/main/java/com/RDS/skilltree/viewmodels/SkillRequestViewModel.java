package com.RDS.skilltree.viewmodels;

import com.RDS.skilltree.enums.UserSkillStatusEnum;
import com.RDS.skilltree.models.Endorsement;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.models.UserSkills;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SkillRequestViewModel {
    private Integer skillId;
    private String skillName;
    private String endorseId;
    private UserSkillStatusEnum status;
    private List<MinimalEndorsementViewModel> endorsements;

    public SkillRequestViewModel(
            Integer id, String name, String endorseId, List<MinimalEndorsementViewModel> endorsements, UserSkillStatusEnum status) {
        this.skillId = id;
        this.skillName = name;
        this.endorseId = endorseId;
        this.endorsements = endorsements;
        this.status = status;
    }

    public static SkillRequestViewModel toViewModel(
            UserSkills userSkills, List<Endorsement> endorsements) {
        Skill skill = userSkills.getSkill();
        String userId = userSkills.getUserId();
        UserSkillStatusEnum status = userSkills.getStatus();

        return new SkillRequestViewModel(
                skill.getId(),
                skill.getName(),
                userId,
                endorsements.stream()
                        .map(MinimalEndorsementViewModel::toViewModel)
                        .collect(Collectors.toList()), status);
    }
}
