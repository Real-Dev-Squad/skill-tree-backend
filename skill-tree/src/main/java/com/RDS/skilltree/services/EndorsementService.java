package com.RDS.skilltree.services;

import com.RDS.skilltree.viewmodels.CreateEndorsementViewModel;
import com.RDS.skilltree.viewmodels.EndorsementViewModel;
import com.RDS.skilltree.viewmodels.UpdateEndorsementViewModel;
import java.util.List;

public interface EndorsementService {
    List<EndorsementViewModel> getAllEndorsementsBySkillId(Integer skillId);

    EndorsementViewModel create(CreateEndorsementViewModel endorsement);

    EndorsementViewModel update(Integer endorsementId, UpdateEndorsementViewModel endorsement);
}
