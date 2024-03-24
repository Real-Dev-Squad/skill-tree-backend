package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.Exceptions.NoEntityException;
import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.Skill.SkillRepository;
import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EndorsementServiceImpl implements EndorsementService {
    private final EndorsementRepository endorsementRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    @Override
    public EndorsementDTO getEndorsementById(UUID id) throws IllegalStateException {
        Optional<EndorsementModel> endorsementModel = endorsementRepository.findById(id);
        return EndorsementDTO.toDto(
                endorsementModel.orElseThrow(
                        () -> new EntityNotFoundException("No endorsement with the id " + id + " found")));
    }

    @Override
    public Page<EndorsementModel> getEndorsements(PageRequest pageRequest) {
        return endorsementRepository.findAll(pageRequest);
    }

    @Override
    public Page<EndorsementModelFromJSON> getEndorsementsFromDummyData(
            PageRequest pageRequest, String skillID, String userID) throws IOException {

        // TODO: temporary stub, implement in followup PR
        return null;
    }

    @Override
    public EndorsementModel createEndorsement(EndorsementDRO endorsementDRO) {
        UUID userId = endorsementDRO.getUserId();
        UUID skillId = endorsementDRO.getSkillId();
        Optional<UserModel> userOptional = userRepository.findById(userId);
        Optional<SkillModel> skillOptional = skillRepository.findById(skillId);
        if (userOptional.isPresent() && skillOptional.isPresent()) {
            EndorsementModel endorsementModel =
                    EndorsementModel.builder().user(userOptional.get()).skill(skillOptional.get()).build();

            return endorsementRepository.save(endorsementModel);
        } else {
            if (userOptional.isEmpty())
                throw new NoEntityException("User with id:" + userId + " not found");
            throw new NoEntityException("Skill with id:" + skillId + " not found");
        }
    }
}
