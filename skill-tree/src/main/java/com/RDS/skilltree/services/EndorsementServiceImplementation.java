package com.RDS.skilltree.services;

import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserRepository;
import com.RDS.skilltree.User.UserSkillStatusEnum;
import com.RDS.skilltree.User.UserSkillsModel;
import com.RDS.skilltree.exceptions.EndorsementNotFoundException;
import com.RDS.skilltree.exceptions.SelfEndorsementNotAllowedException;
import com.RDS.skilltree.exceptions.SkillNotFoundException;
import com.RDS.skilltree.exceptions.UserNotFoundException;
import com.RDS.skilltree.models.Endorsement;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.repositories.EndorsementRepository;
import com.RDS.skilltree.repositories.SkillRepository;
import com.RDS.skilltree.repositories.UserSkillsRepository;
import com.RDS.skilltree.viewmodels.CreateEndorsementViewModel;
import com.RDS.skilltree.viewmodels.EndorsementListViewModel;
import com.RDS.skilltree.viewmodels.EndorsementViewModel;
import com.RDS.skilltree.viewmodels.UpdateEndorsementViewModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EndorsementServiceImplementation implements EndorsementService {
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final EndorsementRepository endorsementRepository;
    private final UserSkillsRepository userSkillsRepository;

    @Override
    public Page<EndorsementListViewModel> getAllEndorsements(Pageable pageable) {
        Page<UserSkillsModel> userSkillsPage = userSkillsRepository.findAllByStatus(UserSkillStatusEnum.PENDING, pageable);
        List<UserSkillsModel> userSkills = userSkillsPage.getContent();

        // Extract skills, endorsements, and users
        List<Skill> pendingSkills = userSkills.stream()
                .map(UserSkillsModel::getSkill)
                .distinct()
                .toList();

        List<UserModel> users = userSkills.stream()
                .map(UserSkillsModel::getUser)
                .distinct()
                .toList();

        List<Endorsement> allEndorsements = new ArrayList<>();

        for (Skill skill : pendingSkills) {
            List<Endorsement> endorsements = endorsementRepository.getAllBySkillId(skill.getId());
            allEndorsements.addAll(endorsements);
        }

        EndorsementListViewModel viewModel = EndorsementListViewModel.toViewModel(pendingSkills, allEndorsements, users);
        return new PageImpl<>(Collections.singletonList(viewModel), pageable, userSkillsPage.getTotalElements());
    }

    @Override
    public Page<EndorsementViewModel> getAllEndorsementsBySkillId(
            Integer skillId, Pageable pageable) {
        Page<Endorsement> endorsementPage = endorsementRepository.findBySkillId(skillId, pageable);
        return endorsementPage.map(EndorsementViewModel::toViewModel);
    }

    @Override
    // TODO : add a check for when a endorsement is already created by a user for a particular skill.
    public EndorsementViewModel create(CreateEndorsementViewModel endorsementViewModel) {
        String message = endorsementViewModel.getMessage();
        Integer skillId = endorsementViewModel.getSkillId();
        String endorseId = endorsementViewModel.getEndorseId();

        // TODO: Get this from security context once the login api is implemented.
        String endorserId = "ae7a6673c5574140838f209de4c644fc";

        if (Objects.equals(endorseId, endorserId)) {
            throw new SelfEndorsementNotAllowedException("Self endorsement not allowed");
        }

        Optional<Skill> skillDetails = skillRepository.findById(skillId);
        Optional<UserModel> endorseDetails = userRepository.findById(endorseId);
        Optional<UserModel> endorserDetails = userRepository.findById(endorserId);

        if (endorserDetails.isEmpty()) {
            throw new UserNotFoundException("Cannot create endorsement for the current user");
        }

        if (endorseDetails.isEmpty()) {
            throw new UserNotFoundException("Endorse not found");
        }

        if (skillDetails.isEmpty()) {
            throw new SkillNotFoundException(String.format("Skill id: %s not found", skillId));
        }

        // TODO : add entry in user_skills table when a endorsement is created but no user to skill mapping is added
        // to the table.
//        Optional<UserSkillsModel> userSkillsMapping = userSkillsRepository.findByUserIdAndSkillId(endorserId, skillId)

        Endorsement endorsement = new Endorsement();

        endorsement.setMessage(message);
        endorsement.setSkill(skillDetails.get());
        endorsement.setEndorse(endorseDetails.get());
        endorsement.setEndorser(endorserDetails.get());

        return EndorsementViewModel.toViewModel(endorsementRepository.save(endorsement));
    }

    @Override
    public EndorsementViewModel update(Integer endorsementId, UpdateEndorsementViewModel body) {
        Optional<Endorsement> exitingEndorsement = endorsementRepository.findById(endorsementId);

        if (exitingEndorsement.isEmpty()) {
            throw new EndorsementNotFoundException(
                    String.format("Endorsement with id: %s not found", endorsementId));
        }

        Endorsement endorsement = exitingEndorsement.get();
        String updatedMessage = body.getMessage();

        if (updatedMessage != null) {
            endorsement.setMessage(updatedMessage);
        }

        return EndorsementViewModel.toViewModel(endorsementRepository.save(endorsement));
    }
}
