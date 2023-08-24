package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.Skill.SkillRepository;
import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class EndorsementServiceImpl implements EndorsementService {

    @Autowired
    private EndorsementRepository endorsementRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public EndorsementModel createEndorsement(EndorsementDRO endorsement) {


        EndorsementModel endorsementModel = new EndorsementModel();
        UserModel user = userRepository.findById(endorsement.getUserId()).get();
        SkillModel skillModel = skillRepository.findById(endorsement.getSkillId()).get();

        endorsementModel.setCreatedAt(Instant.now());
        endorsementModel.setUser(user);
        endorsementModel.setSkill(skillModel);

        return endorsementRepository.save(endorsementModel);
    }

    @Override
    public EndorsementModel updateEndorsement(EndorsementModel endorsement) {
        return endorsementRepository.save(endorsement);
    }

    @Override
    public List<EndorsementModel> getAllEndorsements() {
        return endorsementRepository.findAll();
    }

    @Override
    public EndorsementModel getEndorsementById(UUID id) {
        return endorsementRepository.findById(id).orElse(null);
    }

    @Override
    public List<EndorsementModel> getEndorsementsByUserId(UUID userId) {
        return endorsementRepository.findByUserId(userId);
    }

    @Override
    public List<EndorsementModel> getEndorsementsBySkillId(UUID skillId) {
        return endorsementRepository.findBySkillId(skillId);
    }

    @Override
    public List<EndorsementModel> getEndorsementsByUserIdAndSkillId(UUID userId, UUID skillId) {
        return null;
    }
}
