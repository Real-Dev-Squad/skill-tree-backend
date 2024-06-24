package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.Common.Response.GenericResponse;

import java.io.IOException;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface EndorsementService {
    EndorsementDTO getEndorsementById(UUID id);

    EndorsementModel createEndorsement(EndorsementDRO endorsementDRO);

    GenericResponse<Void> updateEndorsementStatus(UUID id, String status);
}
