package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.Skill.SkillRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EndorsementServiceImpl implements EndorsementService {
    private final EndorsementRepository endorsementRepository;
    private final SkillRepository skillRepository;

    @Override
    public EndorsementModel getEndorsementById(Integer id) throws IllegalStateException {
        Optional<EndorsementModel> endorsementModel = endorsementRepository.findById(id);
        return endorsementModel.orElseThrow(
                () -> new EntityNotFoundException("No endorsement with the id " + id + " found"));
    }
}
