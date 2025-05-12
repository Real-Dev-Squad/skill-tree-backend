package utils;

import com.RDS.skilltree.dtos.RdsGetUserDetailsResDto;
import com.RDS.skilltree.enums.SkillTypeEnum;
import com.RDS.skilltree.models.Endorsement;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.services.external.RdsService;
import com.RDS.skilltree.viewmodels.EndorsementViewModel;
import com.RDS.skilltree.viewmodels.RdsUserViewModel;
import com.RDS.skilltree.viewmodels.UserViewModel;

public class TestDataHelper {
    public static RdsGetUserDetailsResDto createUserDetails(String userId, boolean isSuperUser) {
        RdsUserViewModel user = new RdsUserViewModel();
        user.setId(userId);

        // set a dummy name
        user.setFirst_name(userId);
        user.setLast_name(userId);

        RdsUserViewModel.Roles roles = new RdsUserViewModel.Roles();
        roles.setSuper_user(isSuperUser);
        user.setRoles(roles);

        RdsGetUserDetailsResDto userDetails = new RdsGetUserDetailsResDto();
        userDetails.setUser(user);
        return userDetails;
    }

    public static EndorsementViewModel createEndorsementViewModel(
            Endorsement endorsement, RdsService rdsService) {
        RdsUserViewModel endorseDetail =
                rdsService.getUserDetails(endorsement.getEndorseId()).getUser();
        RdsUserViewModel endorserDetail =
                rdsService.getUserDetails(endorsement.getEndorserId()).getUser();

        UserViewModel endorse =
                UserViewModel.builder()
                        .id(endorseDetail.getId())
                        .name(endorseDetail.getFirst_name() + " " + endorseDetail.getLast_name())
                        .build();
        UserViewModel endorser =
                UserViewModel.builder()
                        .id(endorserDetail.getId())
                        .name(endorserDetail.getFirst_name() + " " + endorserDetail.getLast_name())
                        .build();

        return EndorsementViewModel.toViewModel(endorsement, endorse, endorser);
    }

    public static Endorsement createEndorsement(
            Skill skill, String endorseId, String endorserId, String message) {
        return Endorsement.builder()
                .endorserId(endorserId)
                .endorseId(endorseId)
                .skill(skill)
                .message(message)
                .build();
    }

    public static Skill createSkill(String skillName, String userId) {
        return Skill.builder().name(skillName).createdBy(userId).type(SkillTypeEnum.ATOMIC).build();
    }
}
