package com.RDS.skilltree.services;

import com.RDS.skilltree.dtos.RdsGetUserDetailsResDto;
import com.RDS.skilltree.dtos.SkillRequestsDto;
import com.RDS.skilltree.enums.UserSkillStatusEnum;
import com.RDS.skilltree.exceptions.InternalServerErrorException;
import com.RDS.skilltree.exceptions.NoEntityException;
import com.RDS.skilltree.exceptions.SkillAlreadyExistsException;
import com.RDS.skilltree.models.Endorsement;
import com.RDS.skilltree.models.JwtUser;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.models.UserSkills;
import com.RDS.skilltree.repositories.EndorsementRepository;
import com.RDS.skilltree.repositories.SkillRepository;
import com.RDS.skilltree.repositories.UserSkillRepository;
import com.RDS.skilltree.services.external.RdsService;
import com.RDS.skilltree.utils.GenericResponse;
import com.RDS.skilltree.viewmodels.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkillServiceImplementation implements SkillService {
    private static final Logger log = LoggerFactory.getLogger(SkillServiceImplementation.class);
    private final RdsService rdsService;
    private final SkillRepository skillRepository;
    private final UserSkillRepository userSkillRepository;
    private final EndorsementRepository endorsementRepository;

    private static UserViewModel getUserModalFromRdsDetails(
            String id, RdsGetUserDetailsResDto rdsDetails) {
        String firstName =
                rdsDetails.getUser().getFirst_name() != null ? rdsDetails.getUser().getFirst_name() : "";
        String lastName =
                rdsDetails.getUser().getLast_name() != null ? rdsDetails.getUser().getLast_name() : "";

        return UserViewModel.builder().id(id).name(firstName + ' ' + lastName).build();
    }

    @Override
    public List<SkillViewModel> getAll() {
        return skillRepository.findAll().stream()
                .map(SkillViewModel::toViewModel)
                .collect(Collectors.toList());
    }

    @Override
    public SkillRequestsDto getAllRequests(boolean devMode) {
        JwtUser jwtDetails =
                (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        RdsGetUserDetailsResDto userDetails = rdsService.getUserDetails(jwtDetails.getRdsUserId());
        RdsUserViewModel.Roles userRole = userDetails.getUser().getRoles();
        String userId = userDetails.getUser().getId();

        List<UserSkills> skillRequests = null;

        if (userRole.isSuper_user()) {
            skillRequests = userSkillRepository.findAll();
        } else {
            if (devMode) {
                skillRequests = userSkillRepository.findUserSkillsByEndorserId(userId);
            } else {
                skillRequests = userSkillRepository.findUserSkillsByEndorserIdLegacy(userId);
            }
        }

        if (skillRequests == null) {
            throw new InternalServerErrorException("Unable to fetch skill requests");
        }

        SkillRequestsWithUserDetailsViewModel skillRequestsWithUserDetails =
                toSkillRequestsWithUserDetailsViewModel(skillRequests);

        return SkillRequestsDto.toDto(
                skillRequestsWithUserDetails.getSkillRequests(), skillRequestsWithUserDetails.getUsers());
    }

    @Override
    public SkillRequestsDto getRequestsByStatus(UserSkillStatusEnum status, boolean devMode) {
        JwtUser jwtDetails =
                (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        RdsGetUserDetailsResDto userDetails = rdsService.getUserDetails(jwtDetails.getRdsUserId());
        RdsUserViewModel.Roles userRole = userDetails.getUser().getRoles();
        String userId = userDetails.getUser().getId();

        List<UserSkills> skillRequests;

        if (!devMode || userRole.isSuper_user()) {
            skillRequests = userSkillRepository.findByStatus(status);
        } else {
            skillRequests = userSkillRepository.findByStatusAndEndorserId(status, userId);
        }

        if (skillRequests == null) {
            throw new InternalServerErrorException("Unable to fetch skill requests");
        }

        SkillRequestsWithUserDetailsViewModel skillRequestsWithUserDetails =
                toSkillRequestsWithUserDetailsViewModel(skillRequests);

        return SkillRequestsDto.toDto(
                skillRequestsWithUserDetails.getSkillRequests(), skillRequestsWithUserDetails.getUsers());
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

    @Override
    public GenericResponse<String> approveRejectSkillRequest(
            Integer skillId, String endorseId, UserSkillStatusEnum action) {
        List<UserSkills> existingSkillRequest =
                userSkillRepository.findByUserIdAndSkillId(endorseId, skillId);

        if (existingSkillRequest.isEmpty()) {
            log.info("Skill request not found! endorseId: {} and skillId: {}", endorseId, skillId);
            throw new NoEntityException("Skill request not found");
        }

        UserSkills updatedSkillRequest = existingSkillRequest.get(0);
        updatedSkillRequest.setStatus(action);

        userSkillRepository.save(updatedSkillRequest);
        return new GenericResponse<>("Skill {}", action.toString().toLowerCase());
    }

    private SkillRequestsWithUserDetailsViewModel toSkillRequestsWithUserDetailsViewModel(
            List<UserSkills> skills) {

        // store all users data that are a part of this request
        Map<String, UserViewModel> userViewModelMap = new HashMap<>();

        List<SkillRequestViewModel> skillRequests =
                skills.stream()
                        .map(
                                skill -> {
                                    Integer skillId = skill.getSkill().getId();
                                    String endorseId = skill.getUserId();

                                    List<Endorsement> endorsements;

                                    endorsements =
                                            endorsementRepository.findByEndorseIdAndSkillId(endorseId, skillId);

                                    // Add details of the endorsers
                                    endorsements.forEach(
                                            endorsement -> {
                                                String endorserId = endorsement.getEndorserId();
                                                if (!userViewModelMap.containsKey(endorserId)) {
                                                    RdsGetUserDetailsResDto endorserRdsDetails =
                                                            rdsService.getUserDetails(endorserId);
                                                    UserViewModel endorserDetails =
                                                            getUserModalFromRdsDetails(endorserId, endorserRdsDetails);
                                                    userViewModelMap.put(endorserId, endorserDetails);
                                                }
                                            });

                                    if (!userViewModelMap.containsKey(endorseId)) {
                                        RdsGetUserDetailsResDto endorseRdsDetails =
                                                rdsService.getUserDetails(endorseId);
                                        UserViewModel endorseDetails =
                                                getUserModalFromRdsDetails(endorseId, endorseRdsDetails);
                                        userViewModelMap.put(endorseId, endorseDetails);
                                    }

                                    return SkillRequestViewModel.toViewModel(skill, endorsements);
                                })
                        .toList();

        return SkillRequestsWithUserDetailsViewModel.builder()
                .skillRequests(skillRequests)
                .users(userViewModelMap.values().stream().toList())
                .build();
    }

    private Skill toEntity(CreateSkillViewModel viewModel) {
        Skill entity = new Skill();
        BeanUtils.copyProperties(viewModel, entity);
        return entity;
    }
}
