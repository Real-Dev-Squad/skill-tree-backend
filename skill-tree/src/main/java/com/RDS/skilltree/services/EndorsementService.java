package com.RDS.skilltree.services;

import com.RDS.skilltree.viewmodels.CreateEndorsementViewModel;
import com.RDS.skilltree.viewmodels.EndorsementListViewModel;
import com.RDS.skilltree.viewmodels.EndorsementViewModel;
import com.RDS.skilltree.viewmodels.UpdateEndorsementViewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EndorsementService {
    Page<EndorsementListViewModel> getAllEndorsements(Pageable pageable);

    Page<EndorsementViewModel> getAllEndorsementsBySkillId(Integer skillId, Pageable pageable);

    EndorsementViewModel create(CreateEndorsementViewModel endorsement);

    EndorsementViewModel update(Integer endorsementId, UpdateEndorsementViewModel endorsement);
}
