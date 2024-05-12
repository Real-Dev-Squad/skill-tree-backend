package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.Common.Response.GenericResponse;
import java.io.IOException;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface EndorsementService {
    EndorsementDTO getEndorsementById(UUID id);

    Page<EndorsementModel> getEndorsements(PageRequest pageRequest);

    /* TODO:Dummy JSON code, needs to be changed as part of #103 */
    Page<EndorsementModelFromJSON> getEndorsementsFromDummyData(
            PageRequest pageRequest, String skillID, String userID) throws IOException;

    EndorsementModel createEndorsement(EndorsementDRO endorsementDRO);

    GenericResponse<Void> updateEndorsementStatus(UUID id, String status);
}
