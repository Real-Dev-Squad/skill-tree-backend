package com.RDS.skilltree.Endorsement;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface EndorsementService {
    EndorsementDTO getEndorsementById(UUID id);

    Page<EndorsementModel> getEndorsements(PageRequest pageRequest);

    EndorsementModel createEndorsement(EndorsementDRO endorsementDRO);
}
