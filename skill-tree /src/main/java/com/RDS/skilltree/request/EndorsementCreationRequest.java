package com.RDS.skilltree.request;

import com.RDS.skilltree.enums.Type;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EndorsementCreationRequest {
    private String skillName;
    private String endorserUserId;
    private String endorseeUserId;
    private Type endorsementType;
    private String description;
}
