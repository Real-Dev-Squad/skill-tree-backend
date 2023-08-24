package com.RDS.skilltree.Endorsement;

import java.util.List;
import java.util.UUID;

public interface EndorsementService {
    EndorsementModel createEndorsement(EndorsementDRO endorsement);

    EndorsementModel updateEndorsement(EndorsementModel endorsement);

    List<EndorsementModel> getAllEndorsements();

    EndorsementModel getEndorsementById(UUID id);

    List<EndorsementModel> getEndorsementsByUserId(UUID userId);

    List<EndorsementModel> getEndorsementsBySkillId(UUID skillId);

    List<EndorsementModel> getEndorsementsByUserIdAndSkillId(UUID userId, UUID skillId);

}
