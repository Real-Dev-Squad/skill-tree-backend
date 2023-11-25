package com.RDS.skilltree.EndorsementList;

//import com.RDS.skilltree.User.UserDRO;

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

//    public static EndorsementListModel toModel(EndorsementListDRO endorsementListDRO) {
//        return new EndorsementListModel(
//                endorsementListDRO.getEndorsementId(),
//                endorsementListDRO.getEndorserId(),
//                endorsementListDRO.getDescription(),
//                endorsementListDRO.getType()
//
//        );
//    }
}
