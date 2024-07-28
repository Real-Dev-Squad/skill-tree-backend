package com.RDS.skilltree.viewmodels;

import com.RDS.skilltree.models.Endorsement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EndorsementViewModel {
    private Integer id;
    private SkillViewModel skill;
    private UserViewModel endorse;
    private UserViewModel endorser;
    private String message;

    public static EndorsementViewModel toViewModel(Endorsement endorsement, UserViewModel endorse, UserViewModel endorser) {
//        EndorsementViewModel viewModel = new EndorsementViewModel();
//        BeanUtils.copyProperties(endorsement, viewModel);

//        viewModel.setSkill(SkillViewModel.toViewModel(endorsement.getSkill()));
//        viewModel.setEndorse(endorse);
//        viewModel.setEndorser(endorser);

        return new EndorsementViewModel(
                endorsement.getId(),
                SkillViewModel.toViewModel(endorsement.getSkill()),
                endorse,
                endorser,
                endorsement.getMessage()
        );
    }
}
