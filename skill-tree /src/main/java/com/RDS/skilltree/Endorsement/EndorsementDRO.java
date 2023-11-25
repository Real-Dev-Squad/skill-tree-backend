package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.EndorsementList.EndorsementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EndorsementDRO {
    private UUID userId;
    private UUID skillId;
    private String description;
    private UUID endorsementId;
    private UUID endorserId;
    private EndorsementType type;

    public static EndorsementModel toModel(EndorsementDRO endorsementDRO) {
        return new EndorsementModel();
    }
}
