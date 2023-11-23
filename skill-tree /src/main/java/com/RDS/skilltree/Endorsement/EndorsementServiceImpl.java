package com.RDS.skilltree.Endorsement;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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

    public Map<String, Object> getEndorsementAsMap(UUID uuid) {
        EndorsementDTO endorsementDTO = getEndorsementById(uuid);

        Map<String, Object> endorsementMap = new HashMap<>();
        endorsementMap.put("id", endorsementDTO.getId());
        endorsementMap.put("user", endorsementDTO.getUser());
        endorsementMap.put("skill", endorsementDTO.getSkill());
        endorsementMap.put("status", endorsementDTO.getStatus());
        endorsementMap.put("createdAt", endorsementDTO.getCreatedAt());
        endorsementMap.put("updatedAt", endorsementDTO.getUpdatedAt());
        endorsementMap.put("createdBy", endorsementDTO.getCreatedBy());
        endorsementMap.put("updatedBy", endorsementDTO.getUpdatedBy());

        return endorsementMap;
    }
}
