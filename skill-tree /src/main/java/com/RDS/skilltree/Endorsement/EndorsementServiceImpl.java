package com.RDS.skilltree.Endorsement;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EndorsementServiceImpl implements EndorsementService {
    private final EndorsementRepository endorsementRepository;

    @Override
    public EndorsementDTO getEndorsementById(UUID id) throws IllegalStateException {
        Optional<EndorsementModel> endorsementModel = endorsementRepository.findById(id);
        return EndorsementDTO.toDto(endorsementModel
                .orElseThrow(() -> new EntityNotFoundException("No endorsement with the id " + id + " found")));
    }

    @Override
    public Page<EndorsementModel> getEndorsements(PageRequest pageRequest) {
        return endorsementRepository.findAll(pageRequest);
    }
}