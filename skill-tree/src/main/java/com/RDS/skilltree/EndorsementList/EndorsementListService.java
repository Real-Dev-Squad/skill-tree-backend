package com.RDS.skilltree.EndorsementList;

import com.RDS.skilltree.Endorsement.EndorsementModel;
import com.RDS.skilltree.Endorsement.EndorsementRepository;
import com.RDS.skilltree.Exceptions.NoEntityException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class EndorsementListService {

    private final EndorsementListRepository endorsementListRepository;
    private final EndorsementRepository endorsementRepository;

    public EndorsementListService(
            EndorsementListRepository endorsementListRepository,
            EndorsementRepository endorsementRepository) {
        this.endorsementListRepository = endorsementListRepository;
        this.endorsementRepository = endorsementRepository;
    }

    public EndorsementListModel createEndorsementListEntry(EndorsementListDRO endorsementListDRO) {
        EndorsementListModel endorsementListEntry = new EndorsementListModel();

        UUID endorserId = endorsementListDRO.getEndorserId();
        UUID endorsementId = endorsementListDRO.getEndorsementId();

        Optional<EndorsementModel> endorsementOptional = endorsementRepository.findById(endorsementId);
        if (endorsementOptional.isPresent()) {

            endorsementListEntry.setEndorserId(endorserId);
            endorsementListEntry.setEndorsement(endorsementOptional.get());
            endorsementListEntry.setDescription(endorsementListDRO.getDescription());
            endorsementListEntry.setType(endorsementListDRO.getType());
            endorsementListRepository.save(endorsementListEntry);
            return endorsementListEntry;

        } else {
            throw new NoEntityException("Endorsement with id:" + endorsementId + " not found");
        }
    }
}
