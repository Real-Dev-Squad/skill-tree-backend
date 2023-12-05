package com.RDS.skilltree.Endorsement;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EndorsementServiceImpl implements EndorsementService {
    private final EndorsementRepository endorsementRepository;

    public EndorsementServiceImpl(EndorsementRepository endorsementRepository) {
        this.endorsementRepository = endorsementRepository;
    }

    @Override
    public EndorsementDTO getEndorsementById(UUID id) throws IllegalStateException {
        Optional<EndorsementModel> endorsementModel = endorsementRepository.findById(id);
        return EndorsementDTO.toDto(endorsementModel.orElseThrow(() -> new EntityNotFoundException("No endorsement with the id " + id + " found")));
    }
}
