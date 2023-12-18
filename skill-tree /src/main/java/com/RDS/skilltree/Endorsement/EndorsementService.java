package com.RDS.skilltree.Endorsement;
import java.util.List;
import java.util.UUID;

public interface EndorsementService {
    EndorsementDTO getEndorsementById(UUID id);
    List<EndorsementModel> getEndorsements();
}
