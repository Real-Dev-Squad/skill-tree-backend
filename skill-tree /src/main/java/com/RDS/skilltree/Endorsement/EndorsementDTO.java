package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.EndorsementList.EndorsementListModel;
import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.User.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EndorsementDTO {

    private UUID id;
    private UserModel endorseId;
    private SkillModel skill;
    private List<EndorsementListModel> endorsersList;
    private EndorsementStatus status;
    private UserModel createdBy;

    public static EndorsementDTO toDTO(EndorsementModel endorsement) {
        return new EndorsementDTO(endorsement.getId(), endorsement.getUser(), endorsement.getSkill(), endorsement.getEndorsersList(), endorsement.getStatus(), endorsement.getCreatedBy());
    }
}
