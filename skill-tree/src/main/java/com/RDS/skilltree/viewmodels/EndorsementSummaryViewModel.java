package com.RDS.skilltree.viewmodels;

import com.RDS.skilltree.models.Endorsement;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EndorsementSummaryViewModel {
    private Integer id;
    private Integer skillId;
    private String endorseId;
    private String endorserId;
    private String message;

    public static EndorsementSummaryViewModel toViewModel(Endorsement endorsement) {
        EndorsementSummaryViewModel model = new EndorsementSummaryViewModel();
        model.setId(endorsement.getId());
        model.setSkillId(endorsement.getSkill().getId());
        model.setEndorseId(endorsement.getEndorse().getId());
        model.setEndorserId(endorsement.getEndorser().getId());
        model.setMessage(endorsement.getMessage());
        return model;
    }
}