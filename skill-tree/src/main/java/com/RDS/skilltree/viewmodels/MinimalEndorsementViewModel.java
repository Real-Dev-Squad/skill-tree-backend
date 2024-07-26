package com.RDS.skilltree.viewmodels;

import com.RDS.skilltree.models.Endorsement;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MinimalEndorsementViewModel {
    private Integer id;
    private String endorserId;
    private String endorsementDate;
    private String message;

    public static MinimalEndorsementViewModel toViewModel(Endorsement endorsement) {
        MinimalEndorsementViewModel endorsementViewModel = new MinimalEndorsementViewModel();

        endorsementViewModel.setId(endorsement.getId());
        endorsementViewModel.setMessage(endorsement.getMessage());
        endorsementViewModel.setEndorsementDate(endorsement.getCreatedAt().toString());
        endorsementViewModel.setEndorserId(endorsement.getEndorser().getId());

        return endorsementViewModel;
    }
}
