package com.RDS.skilltree.EndorsementList;

import com.RDS.skilltree.User.UserModel;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class EndorsementListDTO {
    private UUID id;
    private String description;
    private UserModel endorser;
    private EndorsementType type;
}
