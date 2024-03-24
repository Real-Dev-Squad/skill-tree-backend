package com.RDS.skilltree.EndorsementList;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
