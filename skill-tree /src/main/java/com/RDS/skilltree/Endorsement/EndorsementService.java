package com.RDS.skilltree.Endorsement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface EndorsementService {
    EndorsementDTO getEndorsementById(UUID id);
    Page<EndorsementModel> getEndorsements(PageRequest pageRequest);
}
