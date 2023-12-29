package com.RDS.skilltree.EndorsementList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EndorsementListDRO {
    private String description;
    private UUID endorsementId;
    private UUID endorserId;
    private EndorsementType type;

}
