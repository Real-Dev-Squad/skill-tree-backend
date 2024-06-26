package com.RDS.skilltree.services;

import com.RDS.skilltree.models.Endorsement;
import com.RDS.skilltree.repositories.EndorsementRepository;
import com.RDS.skilltree.viewmodels.EndorsementViewModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EndorsementServiceImplementation implements EndorsementService {
    private final EndorsementRepository endorsementRepository;

    @Override
    public Page<EndorsementViewModel> getAllEndorsementsBySkillId(Integer skillId, Pageable pageable) {
        Page<Endorsement> endorsementPage = endorsementRepository.findBySkillId(skillId, pageable);
        return endorsementPage.map(EndorsementViewModel::toViewModel);
    }
}
