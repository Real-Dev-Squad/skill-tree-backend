package com.RDS.skilltree.Endorsement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.UUID;

public interface EndorsementService {
    EndorsementDTO getEndorsementById(UUID id);
    Page<EndorsementModel> getEndorsements(PageRequest pageRequest);
    Page<EndorsementModelFromJSON> getEndorsementsFromDummyData(PageRequest pageRequest,String userIDString,String skillIDString) throws IOException;
    EndorsementModel createEndorsement(EndorsementDRO endorsementDRO);
}
