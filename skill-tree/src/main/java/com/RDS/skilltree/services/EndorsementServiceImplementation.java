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
import com.RDS.skilltree.repositories.UserSkillRepository;
import com.RDS.skilltree.viewmodels.CreateEndorsementViewModel;
import com.RDS.skilltree.viewmodels.EndorsementViewModel;
import com.RDS.skilltree.viewmodels.UpdateEndorsementViewModel;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EndorsementServiceImplementation implements EndorsementService {
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final EndorsementRepository endorsementRepository;
    private final UserSkillRepository userSkillRepository;

    @Override
    public List<EndorsementViewModel> getAllEndorsementsBySkillId(Integer skillId) {
        List<Endorsement> endorsements = endorsementRepository.findBySkillId(skillId);
        return endorsements.stream().map(EndorsementViewModel::toViewModel).toList();
    }

    @Override
    // TODO : add a check for when a endorsement is already created by a user for a particular skill.
    public EndorsementViewModel create(CreateEndorsementViewModel endorsementViewModel) {
        String message = endorsementViewModel.getMessage();
        Integer skillId = endorsementViewModel.getSkillId();
        String endorseId = endorsementViewModel.getEndorseId();

        // TODO: Get this from security context once the login api is implemented.
        String endorserId = "cf8893a16cee42cc94387a9bd086ed46";

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

        List<UserSkillsModel> userSkillEntry =
                userSkillRepository.findByUserIdAndSkillId(endorseId, skillId);
        Endorsement endorsement = new Endorsement();

        endorsement.setMessage(message);
        endorsement.setSkill(skillDetails.get());
        endorsement.setEndorse(endorseDetails.get());
        endorsement.setEndorser(endorserDetails.get());

        if (userSkillEntry.isEmpty()) {
            UserSkillsModel userSkillsModel = new UserSkillsModel();
            userSkillsModel.setUser(endorseDetails.get());
            userSkillsModel.setSkill(skillDetails.get());
            userSkillsModel.setStatus(UserSkillStatusEnum.PENDING);

            userSkillRepository.save(userSkillsModel);
        }

        Endorsement newEndorsement = endorsementRepository.save(endorsement);

        return EndorsementViewModel.toViewModel(newEndorsement);
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
