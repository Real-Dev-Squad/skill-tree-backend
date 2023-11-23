package com.RDS.skilltree.Endorsement;

import java.util.Map;
import java.util.UUID;

public interface EndorsementService {
    EndorsementDTO getEndorsementById(UUID id);
    Map<String, Object> getEndorsementAsMap(UUID uuid);
}
