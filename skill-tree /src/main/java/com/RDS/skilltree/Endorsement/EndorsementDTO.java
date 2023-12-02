package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.EndorsementList.EndorsementListModel;
import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.User.UserModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EndorsementDTO {
    private UUID id;
    private UserModel user;
    private SkillModel skill;
    private EndorsementStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private UserModel createdBy;
    private UserModel updatedBy;
    private List<EndorsementListModel> endorsersList;

    public static EndorsementDTO toDto(EndorsementModel endorsementModel) {
        return EndorsementDTO.builder()
                .id(endorsementModel.getId())
                .user(endorsementModel.getUser())
                .skill(endorsementModel.getSkill())
                .status(endorsementModel.getStatus())
                .createdAt(endorsementModel.getCreatedAt())
                .createdBy(endorsementModel.getCreatedBy())
                .updatedAt(endorsementModel.getUpdatedAt())
                .updatedBy(endorsementModel.getUpdatedBy())
                .endorsersList(endorsementModel.getEndorsersList())
                .build();
    }
}
