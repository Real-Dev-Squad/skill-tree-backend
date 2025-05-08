package com.RDS.skilltree.services;

import com.RDS.skilltree.dtos.RdsGetUserDetailsResDto;
import com.RDS.skilltree.exceptions.EndorsementAlreadyExistsException;
import com.RDS.skilltree.exceptions.EndorsementNotFoundException;
import com.RDS.skilltree.exceptions.SelfEndorsementNotAllowedException;
import com.RDS.skilltree.exceptions.SkillNotFoundException;
import com.RDS.skilltree.models.Endorsement;
import com.RDS.skilltree.models.JwtUser;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.models.UserSkills;
import com.RDS.skilltree.repositories.EndorsementRepository;
import com.RDS.skilltree.repositories.SkillRepository;
import com.RDS.skilltree.repositories.UserSkillRepository;
import com.RDS.skilltree.services.external.RdsService;
import com.RDS.skilltree.viewmodels.CreateEndorsementViewModel;
import com.RDS.skilltree.viewmodels.EndorsementViewModel;
import com.RDS.skilltree.viewmodels.UpdateEndorsementViewModel;
import com.RDS.skilltree.viewmodels.UserViewModel;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EndorsementServiceImplementation implements EndorsementService {
    private static final Logger log = LoggerFactory.getLogger(EndorsementServiceImplementation.class);
    private final RdsService rdsService;
    private final SkillRepository skillRepository;
    private final EndorsementRepository endorsementRepository;
    private final UserSkillRepository userSkillRepository;

    @Override
    public List<EndorsementViewModel> getAllEndorsementsBySkillId(Integer skillId) {
        List<Endorsement> endorsements = endorsementRepository.findBySkillId(skillId);

        // store all users data that are a part of this request
        Map<String, UserViewModel> userDetails = new HashMap<>();

        return endorsements.stream()
                .map(
                        endorsement -> {
                            String endorseId = endorsement.getEndorseId();
                            String endorserId = endorsement.getEndorserId();

                            if (!userDetails.containsKey(endorseId)) {
                                RdsGetUserDetailsResDto endorseDetails =
                                        rdsService.getUserDetails(endorsement.getEndorseId());
                                userDetails.put(endorseId, UserViewModel.toViewModel(endorseDetails.getUser()));
                            }

                            if (!userDetails.containsKey(endorserId)) {
                                RdsGetUserDetailsResDto endorserDetails = rdsService.getUserDetails(endorserId);
                                userDetails.put(endorserId, UserViewModel.toViewModel(endorserDetails.getUser()));
                            }

                            return EndorsementViewModel.toViewModel(
                                    endorsement, userDetails.get(endorseId), userDetails.get(endorserId));
                        })
                .toList();
    }

    @Override
    public EndorsementViewModel create(CreateEndorsementViewModel endorsementViewModel) {
        String message = endorsementViewModel.getMessage();
        Integer skillId = endorsementViewModel.getSkillId();
        String endorseId = endorsementViewModel.getEndorseId();

        JwtUser jwtDetails =
                (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String endorserId = jwtDetails.getRdsUserId();

        if (Objects.equals(endorseId, endorserId)) {
            log.info(
                    "Self endorsement not allowed, endorseId: {}, endorserId: {}", endorseId, endorserId);
            throw new SelfEndorsementNotAllowedException("Self endorsement not allowed");
        }

        Optional<Skill> skillDetails = skillRepository.findById(skillId);

        if (skillDetails.isEmpty()) {
            log.info("Skill id: {} not found", skillId);
            throw new SkillNotFoundException("Skill does not exist");
        }

        if (endorsementRepository.existsByEndorseIdAndEndorserIdAndSkillId(
                endorseId, endorserId, skillId)) {
            log.info(
                    "Endorsement already exists for endorseId: {}, endorserId: {}, skillId: {}",
                    endorseId,
                    endorserId,
                    skillId);
            throw new EndorsementAlreadyExistsException("Endorsement already exists");
        }

        RdsGetUserDetailsResDto endorseDetails = rdsService.getUserDetails(endorseId);
        RdsGetUserDetailsResDto endorserDetails = rdsService.getUserDetails(endorserId);

        List<UserSkills> userSkillEntry =
                userSkillRepository.findByUserIdAndSkillId(endorseId, skillId);

        Endorsement endorsement =
                Endorsement.builder()
                        .skill(skillDetails.get())
                        .message(message)
                        .endorseId(endorseId)
                        .endorserId(endorserId)
                        .build();

        // If a skill request is not created then create one
        // This is because there is no specific api to create a skill request at the time of writing
        if (userSkillEntry.isEmpty()) {
            UserSkills userSkills =
                    UserSkills.builder().userId(endorseId).skill(skillDetails.get()).build();

            userSkillRepository.save(userSkills);
        }

        Endorsement newEndorsement = endorsementRepository.save(endorsement);

        return EndorsementViewModel.toViewModel(
                newEndorsement,
                UserViewModel.toViewModel(endorseDetails.getUser()),
                UserViewModel.toViewModel(endorserDetails.getUser()));
    }

    @Override
    public EndorsementViewModel update(Integer endorsementId, UpdateEndorsementViewModel body) {
        Optional<Endorsement> exitingEndorsement = endorsementRepository.findById(endorsementId);

        if (exitingEndorsement.isEmpty()) {
            log.info(String.format("Endorsement with id: %s not found", endorsementId));
            throw new EndorsementNotFoundException("Endorsement not found");
        }

        Endorsement endorsement = exitingEndorsement.get();
        String updatedMessage = body.getMessage();

        if (updatedMessage != null) {
            endorsement.setMessage(updatedMessage);
        }

        Endorsement savedEndorsementDetails = endorsementRepository.save(endorsement);
        RdsGetUserDetailsResDto endorseDetails =
                rdsService.getUserDetails(savedEndorsementDetails.getEndorseId());
        RdsGetUserDetailsResDto endorserDetails =
                rdsService.getUserDetails(savedEndorsementDetails.getEndorserId());

        return EndorsementViewModel.toViewModel(
                savedEndorsementDetails,
                UserViewModel.toViewModel(endorseDetails.getUser()),
                UserViewModel.toViewModel(endorserDetails.getUser()));
    }
}
