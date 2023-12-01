package com.RDS.skilltree.Endorsement;
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
        if (endorsementModel.isPresent()) {
            return EndorsementDTO.toDto(endorsementModel.get());
        } else {
            String errorMessage = "No endorsement with the id " + id + " found";
            throw new IllegalStateException(errorMessage);
        }
    }
}
