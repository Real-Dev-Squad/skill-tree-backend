package com.RDS.skilltree.services;

import com.RDS.skilltree.dtos.RdsGetUserDetailsResDto;
import com.RDS.skilltree.dtos.SkillRequestsDto;
import com.RDS.skilltree.enums.UserSkillStatusEnum;
import com.RDS.skilltree.exceptions.SkillAlreadyExistsException;
import com.RDS.skilltree.models.Endorsement;
import com.RDS.skilltree.models.JwtUser;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.models.UserSkills;
import com.RDS.skilltree.repositories.EndorsementRepository;
import com.RDS.skilltree.repositories.SkillRepository;
import com.RDS.skilltree.repositories.UserSkillRepository;
import com.RDS.skilltree.services.external.RdsService;
import com.RDS.skilltree.viewmodels.CreateSkillViewModel;
import com.RDS.skilltree.viewmodels.SkillRequestViewModel;
import com.RDS.skilltree.viewmodels.SkillViewModel;
import com.RDS.skilltree.viewmodels.UserViewModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkillServiceImplementation implements SkillService {
    private final RdsService rdsService;
    private final SkillRepository skillRepository;
    private final UserSkillRepository userSkillRepository;
    private final EndorsementRepository endorsementRepository;

    @Override
    public List<SkillViewModel> getAll() {
        return skillRepository.findAll().stream()
                .map(SkillViewModel::toViewModel)
                .collect(Collectors.toList());
    }

    @Override
    public SkillRequestsDto getAllRequests() {
        List<UserSkills> pendingSkills = userSkillRepository.findByStatus(UserSkillStatusEnum.PENDING);

        // store all users data that are a part of this request
        Map<String, UserViewModel> userDetails = new HashMap<>();

        // make a list of all pending skill requests with their endorsement details
        List<SkillRequestViewModel> skillRequests =
                pendingSkills.stream()
                        .map(
                                skill -> {
                                    Integer skillId = skill.getSkill().getId();

                                    String endorseId = skill.getUserId();

                                    // Get all endorsement for a specific skill and user Id
                                    List<Endorsement> endorsements =
                                            endorsementRepository.findByEndorseIdAndSkillId(endorseId, skillId);

                                    if (!userDetails.containsKey(endorseId)) {
                                        RdsGetUserDetailsResDto endorseRdsDetails =
                                                rdsService.getUserDetails(endorseId);
                                        UserViewModel endorseDetails =
                                                getUserModalFromRdsDetails(endorseId, endorseRdsDetails);
                                        userDetails.put(endorseId, endorseDetails);
                                    }

                                    endorsements.forEach(
                                            endorsement -> {
                                                String endorserId = endorsement.getEndorserId();

                                                if (!userDetails.containsKey(endorserId)) {
                                                    RdsGetUserDetailsResDto endorserRdsDetails =
                                                            rdsService.getUserDetails(endorserId);
                                                    UserViewModel endorserDetails =
                                                            getUserModalFromRdsDetails(endorserId, endorserRdsDetails);
                                                    userDetails.put(endorserId, endorserDetails);
                                                }
                                            });

                                    return SkillRequestViewModel.toViewModel(skill, endorsements);
                                })
                        .toList();

        return SkillRequestsDto.toDto(skillRequests, userDetails.values().stream().toList());
    }

    private static UserViewModel getUserModalFromRdsDetails(
            String id, RdsGetUserDetailsResDto rdsDetails) {
        String firstName =
                rdsDetails.getUser().getFirst_name() != null ? rdsDetails.getUser().getFirst_name() : "";
        String lastName =
                rdsDetails.getUser().getLast_name() != null ? rdsDetails.getUser().getLast_name() : "";

        return UserViewModel.builder().id(id).name(firstName + ' ' + lastName).build();
    }

    @Override
    public SkillViewModel create(CreateSkillViewModel skill) {
        if (skillRepository.existsByName(skill.getName())) {
            throw new SkillAlreadyExistsException(
                    String.format("Skill with name %s already exists", skill.getName()));
        }

        JwtUser jwtDetails =
                (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        RdsGetUserDetailsResDto userDetails = rdsService.getUserDetails(jwtDetails.getRdsUserId());

        Skill newSkill = toEntity(skill);
        newSkill.setCreatedBy(userDetails.getUser().getId());

        return SkillViewModel.toViewModel(skillRepository.saveAndFlush(newSkill));
    }

    private Skill toEntity(CreateSkillViewModel viewModel) {
        Skill entity = new Skill();
        BeanUtils.copyProperties(viewModel, entity);
        return entity;
    }
}
