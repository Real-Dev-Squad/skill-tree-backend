package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.utils.TrackedProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EndorsementDTO extends TrackedProperties {
    private Integer id;
    private String endorseId;
    private Integer skillId;

    //    public static EndorsementDTO toDto(EndorsementModel endorsementModel) {
    //        EndorsementDTO endorsementDTO =
    //                EndorsementDTO.builder()
    //                        .id(endorsementModel.getId())
    //                        .endorseId(endorsementModel.getEndorseId())
    //                        .skillId(endorsementModel.getSkillId())
    //                        .build();
    //        endorsementDTO.setCreatedAt(endorsementModel.getCreatedAt());
    //        endorsementDTO.setUpdatedAt(endorsementModel.getUpdatedAt());
    //        return endorsementDTO;
    //    }
}
