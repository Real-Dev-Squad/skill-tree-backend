package com.RDS.skilltree.Endorsement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EndorsementDTO {
    private UUID id;
    private UUID user;
    private UUID skill;
    private EndorsementStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID createdBy;
    private UUID updatedBy;

    public static EndorsementDTO toDto(EndorsementModel endorsementModel) {
        return EndorsementDTO.builder()
                .id(endorsementModel.getId())
                .user(endorsementModel.getUser().getId())
                .skill(endorsementModel.getSkill().getId())
                .status(endorsementModel.getStatus())
                .createdAt(endorsementModel.getCreatedAt())
                .createdBy(endorsementModel.getCreatedBy().getId())
                .updatedAt(endorsementModel.getUpdatedAt())
                .updatedBy(endorsementModel.getUpdatedBy().getId())
                .build();
    }
}
