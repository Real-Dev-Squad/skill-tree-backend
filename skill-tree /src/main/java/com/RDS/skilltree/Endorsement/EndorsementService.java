package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.Exceptions.NoEntityException;
import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.Skill.SkillRepository;
import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EndorsementService {
    private final EndorsementRepository endorsementRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    public EndorsementService(EndorsementRepository endorsementRepository, UserRepository userRepository, SkillRepository skillRepository) {
        this.endorsementRepository = endorsementRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
    }

    public List<EndorsementDTO> getAllEndorsements() {
        List<EndorsementModel> endorsementList = endorsementRepository.findAll();
        if (!endorsementList.isEmpty()) return endorsementList.stream().map(EndorsementDTO::toDTO).toList();
        return null;
    }


    public EndorsementModel createEndorsement(EndorsementDRO endorsementDRO) {
        UUID userId = endorsementDRO.getUserId();
        UUID skillId = endorsementDRO.getSkillId();
        Optional<UserModel> userOptional = userRepository.findById(userId);
        Optional<SkillModel> skillOptional = skillRepository.findById(skillId);
        EndorsementModel endorsementModel = new EndorsementModel();
        if (userOptional.isPresent() && skillOptional.isPresent()) {
            endorsementModel.setUser(userOptional.get());
            endorsementModel.setSkill(skillOptional.get());
            endorsementRepository.save(endorsementModel);
            return endorsementModel;
        } else {
            throw new NoEntityException("User with id:" + userId + " not found");
        }
    }
}
