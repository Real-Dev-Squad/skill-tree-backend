package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.Common.Response.GenericResponse;

import java.util.UUID;

public interface EndorsementService {
    EndorsementModel getEndorsementById(UUID id);

//    EndorsementModel createEndorsement(CreateEndorsementDro createEndorsementDro);

//    GenericResponse<Void> updateEndorsementStatus(UUID id, String status);
}
