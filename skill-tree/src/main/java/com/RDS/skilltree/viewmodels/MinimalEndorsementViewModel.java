package com.RDS.skilltree.viewmodels;

import com.RDS.skilltree.models.Endorsement;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MinimalEndorsementViewModel {
    private Integer id;
    private String endorserId;
    private String endorsementDate;
    private String message;

    public static MinimalEndorsementViewModel toViewModel(Endorsement endorsement) {
        return MinimalEndorsementViewModel.builder()
                .id(endorsement.getId())
                .message(endorsement.getMessage())
                .endorserId(endorsement.getEndorserId())
                .endorsementDate(endorsement.getCreatedAt().toString())
                .build();
    }
}
