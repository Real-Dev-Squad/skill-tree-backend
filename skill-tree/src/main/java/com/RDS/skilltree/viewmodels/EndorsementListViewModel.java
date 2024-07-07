package com.RDS.skilltree.viewmodels;

import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.models.Endorsement;
import com.RDS.skilltree.models.Skill;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class EndorsementListViewModel {
    private List<EndorsementSummaryViewModel> endorsements;
    private List<UserViewModel> users;
    private List<SkillViewModel> skills;

    public static EndorsementListViewModel toViewModel(List<Skill> skills, List<Endorsement> endorsements, List<UserModel> users) {
        if (skills == null || endorsements == null || users == null) {
            throw new IllegalArgumentException("Input lists cannot be null");
        }

        EndorsementListViewModel viewModel = new EndorsementListViewModel();
        viewModel.setEndorsements(endorsements.stream().map(EndorsementSummaryViewModel::toViewModel).collect(Collectors.toList()));
        viewModel.setUsers(users.stream().map(UserViewModel::toViewModel).collect(Collectors.toList()));
        viewModel.setSkills(skills.stream().map(SkillViewModel::toViewModel).collect(Collectors.toList()));

        return viewModel;
    }
}
