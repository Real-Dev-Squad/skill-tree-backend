package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.EndorsementList.EndorsementListModel;
import com.RDS.skilltree.Skill.SkillDTO;
import com.RDS.skilltree.utils.TrackedProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EndorsementDTO extends TrackedProperties {
    private UUID id;
    private UUID endorseeId;
    private SkillDTO skill;
    private EndorsementStatus status;
    private List<EndorsementListModel> endorsersList;

    public static EndorsementDTO toDto(EndorsementModel endorsementModel) {
        EndorsementDTO endorsementDTO =
                EndorsementDTO.builder()
                        .id(endorsementModel.getId())
                        .endorseeId(endorsementModel.getEndorseeId())
                        .skill(SkillDTO.toDto(endorsementModel.getSkill()))
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
