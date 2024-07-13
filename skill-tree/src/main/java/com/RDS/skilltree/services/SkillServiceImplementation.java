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
import com.RDS.skilltree.viewmodels.CreateSkillViewModel;
import com.RDS.skilltree.viewmodels.SkillRequestViewModel;
import com.RDS.skilltree.viewmodels.SkillViewModel;
import com.RDS.skilltree.viewmodels.UserViewModel;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkillServiceImplementation implements SkillService {
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

                                    uniqueUsers.add(
                                            // TODO : call rds backend to get user details
                                            UserViewModel.toViewModel(new UserModel(pendingSkill.getUser().getId(), "")));

                                    endorsements.forEach(
                                            endorsement -> {
                                                UserModel endorser = endorsement.getEndorser();
                                                // TODO : call rds backend to get user details
                                                uniqueUsers.add(
                                                        UserViewModel.toViewModel(new UserModel(endorser.getId(), "")));
                                            });

                                    return SkillRequestViewModel.toViewModel(pendingSkill, endorsements);
                                })
                        .toList();

        return SkillRequestsDto.toDto(skillRequests, new ArrayList<>(uniqueUsers));
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
