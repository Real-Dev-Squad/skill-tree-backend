package com.RDS.skilltree.Endorsement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EndorsementDRO {
    private EndorsementStatus status;

    private UUID userId;

    private UUID skillId;

}
