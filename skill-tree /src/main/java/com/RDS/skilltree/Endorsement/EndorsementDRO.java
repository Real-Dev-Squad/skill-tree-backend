package com.RDS.skilltree.Endorsement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EndorsementDRO {
    private UUID userId;
    private UUID skillId;

    public static EndorsementModel toModel(EndorsementDRO endorsementDRO) {
        return new EndorsementModel();
    }
}
