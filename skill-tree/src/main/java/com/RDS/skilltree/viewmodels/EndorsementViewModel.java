package com.RDS.skilltree.viewmodels;

import com.RDS.skilltree.models.Endorsement;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class EndorsementViewModel {
    private Integer id;
    private SkillViewModel skill;
    private UserViewModel endorse;
    private UserViewModel endorser;
    private String message;

    public static EndorsementViewModel toViewModel(Endorsement endorsement) {
        EndorsementViewModel viewModel = new EndorsementViewModel();
        BeanUtils.copyProperties(endorsement, viewModel);

        viewModel.setSkill(SkillViewModel.toViewModel(endorsement.getSkill()));
        viewModel.setEndorser(UserViewModel.toViewModel(endorsement.getEndorser()));
        viewModel.setEndorse(UserViewModel.toViewModel(endorsement.getEndorse()));

        return viewModel;
    }
}
