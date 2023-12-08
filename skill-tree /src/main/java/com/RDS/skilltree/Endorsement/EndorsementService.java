package com.RDS.skilltree.Endorsement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EndorsementService {

    private final EndorsementRepository endorsementRepository;

    @Autowired
    public EndorsementService(EndorsementRepository endorsementRepository) {
        this.endorsementRepository = endorsementRepository;
    }

    public List<EndorsementModel> getEndorsements(){
        return endorsementRepository.findAll();
    }
}
