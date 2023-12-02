package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.EndorsementList.EndorsementListModel;
import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.utils.TrackedProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EndorsementDTO extends TrackedProperties {
    private UUID id;
    private UserModel user;
    private SkillModel skill;
    private EndorsementStatus status;
    private List<EndorsementListModel> endorsersList;

    public static EndorsementDTO toDto(EndorsementModel endorsementModel) {
        EndorsementDTO endorsementDTO = EndorsementDTO.builder()
                .id(endorsementModel.getId())
                .user(endorsementModel.getUser())
                .skill(endorsementModel.getSkill())
                .status(endorsementModel.getStatus())
                .endorsersList(endorsementModel.getEndorsersList())
                .build();
        endorsementDTO.setCreatedAt(endorsementModel.getCreatedAt());
        endorsementDTO.setUpdatedAt(endorsementModel.getUpdatedAt());
        endorsementDTO.setCreatedBy(endorsementModel.getCreatedBy());
        endorsementDTO.setUpdatedBy(endorsementModel.getUpdatedBy());
        return endorsementDTO;
    }
}
