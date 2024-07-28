package com.RDS.skilltree.viewmodels;

import com.RDS.skilltree.models.Endorsement;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EndorsementViewModel {
    private Integer id;
    private SkillViewModel skill;
    private UserViewModel endorse;
    private UserViewModel endorser;
    private String message;

    public static EndorsementViewModel toViewModel(
            Endorsement endorsement, UserViewModel endorse, UserViewModel endorser) {

        return EndorsementViewModel.builder()
                .id(endorsement.getId())
                .skill(SkillViewModel.toViewModel((endorsement.getSkill())))
                .endorse(endorse)
                .endorser(endorser)
                .message(endorsement.getMessage())
                .build();
    }
}
