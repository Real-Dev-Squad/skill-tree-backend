package com.RDS.skilltree.services;

import com.RDS.skilltree.viewmodels.CreateEndorsementViewModel;
import com.RDS.skilltree.viewmodels.EndorsementViewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EndorsementService {
    Page<EndorsementViewModel> getAllEndorsementsBySkillId(Integer skillId, Pageable pageable);

    EndorsementViewModel create(CreateEndorsementViewModel endorsement);
}
