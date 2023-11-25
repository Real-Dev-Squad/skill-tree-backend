package com.RDS.skilltree.EndorsementList;

import com.RDS.skilltree.User.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class EndorsementListDTO {
    private UUID id;
    private String description;
    private UserModel endorser;
    private EndorsementType type;

    public static EndorsementListDTO toDTO(EndorsementListModel endorsementListModel) {
        return EndorsementListDTO.builder()
                .endorser(endorsementListModel.getEndorser())
                .description(endorsementListModel.getDescription())
                .type(endorsementListModel.getType())
                .build();
    }
}
