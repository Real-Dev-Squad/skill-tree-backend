package com.RDS.skilltree.services;

import com.RDS.skilltree.User.*;
import com.RDS.skilltree.dtos.SkillRequestsDto;
import com.RDS.skilltree.exceptions.SkillAlreadyExistsException;
import com.RDS.skilltree.exceptions.UserNotFoundException;
import com.RDS.skilltree.models.Endorsement;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.repositories.EndorsementRepository;
import com.RDS.skilltree.repositories.SkillRepository;
import com.RDS.skilltree.repositories.UserSkillRepository;
import com.RDS.skilltree.services.external.RdsService;
import com.RDS.skilltree.viewmodels.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillServiceImplementation implements SkillService {
    private final RdsService rdsService;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
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
        List<UserSkillsModel> pendingSkills =
                userSkillRepository.findByStatus(UserSkillStatusEnum.PENDING);
        Set<UserViewModel> uniqueUsers = new HashSet<>();

        List<SkillRequestViewModel> skillRequests =
                pendingSkills.stream()
                        .map(
                                pendingSkill -> {
                                    List<Endorsement> endorsements =
                                            endorsementRepository.findBySkillId(pendingSkill.getSkill().getId());

                                    String endorseRdsUserId = pendingSkill.getUser().getRdsUserId();
                                    RdsUserViewModel rdsDetails = rdsService.getUserDetails(endorseRdsUserId);
                                    UserViewModel endorseDetails = getUserModalFromRdsDetails(pendingSkill.getUser().getId(), rdsDetails);
                                    
                                    uniqueUsers.add(endorseDetails);

                                    endorsements.forEach(
                                            endorsement -> {
                                                UserModel endorser = endorsement.getEndorser();

                                                RdsUserViewModel rdsUserDetails = rdsService.getUserDetails(endorser.getRdsUserId());
                                                UserViewModel endorserDetails = getUserModalFromRdsDetails(endorser.getId(), rdsUserDetails);

                                                uniqueUsers.add(endorserDetails);
                                            });

                                    return SkillRequestViewModel.toViewModel(pendingSkill, endorsements);
                                })
                        .toList();

        return SkillRequestsDto.toDto(skillRequests, new ArrayList<>(uniqueUsers));
    }

    private static UserViewModel getUserModalFromRdsDetails(String id, RdsUserViewModel rdsDetails) {
        String firstName = rdsDetails.getUser().getFirst_name() != null ? rdsDetails.getUser().getFirst_name() : "";
        String lastName = rdsDetails.getUser().getLast_name() != null ? rdsDetails.getUser().getLast_name() : "";

        String username = firstName + ' ' + lastName;

        UserViewModel user = new UserViewModel();
        user.setId(id);
        user.setName(username);

        return user;
    }

    @Override
    public SkillViewModel create(CreateSkillViewModel skill) {
        if (skillRepository.existsByName(skill.getName())) {
            throw new SkillAlreadyExistsException(
                    String.format("Skill with name %s already exists", skill.getName()));
        }

        JwtUserModel jwtDetails =
                (JwtUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // TODO : user the userId from jwtDetails after the login api is implemented
        String userId = "ae7a6673c5574140838f209de4c644fc";
        Optional<UserModel> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException("unable to create skill for the current user");
        }

        Skill newSkill = toEntity(skill);
        newSkill.setCreatedBy(user.get());

        return SkillViewModel.toViewModel(skillRepository.saveAndFlush(newSkill));
    }

    private Skill toEntity(CreateSkillViewModel viewModel) {
        Skill entity = new Skill();
        BeanUtils.copyProperties(viewModel, entity);
        return entity;
    }
}
