package com.RDS.skilltree.request;

import com.RDS.skilltree.enums.Behaviour;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EndorsementCreationRequest {
    private String skillName;
    private String endorserUserId;
    private String endorseeUserId;
    private Behaviour endorsementBehaviour;
    private String description;
}
