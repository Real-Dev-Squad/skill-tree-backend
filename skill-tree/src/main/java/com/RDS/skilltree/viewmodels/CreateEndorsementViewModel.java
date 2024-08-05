package com.RDS.skilltree.viewmodels;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateEndorsementViewModel {
    private Integer skillId;
    private String endorseId;
    private String message;

    public static CreateEndorsementViewModel toViewModel(
            Integer skillId, String endorseId, String message) {

        return CreateEndorsementViewModel.builder()
                .skillId(skillId)
                .endorseId(endorseId)
                .message(message)
                .build();
    }
}
