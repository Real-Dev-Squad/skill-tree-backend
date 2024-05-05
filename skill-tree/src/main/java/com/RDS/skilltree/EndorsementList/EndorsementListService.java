package com.RDS.skilltree.EndorsementList;

import com.RDS.skilltree.Endorsement.EndorsementModel;
import com.RDS.skilltree.Endorsement.EndorsementRepository;
import com.RDS.skilltree.Exceptions.NoEntityException;
import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EndorsementListService {
    @Autowired private final EndorsementListRepository endorsementListRepository;
    private final EndorsementRepository endorsementRepository;
    private final UserRepository userRepository;

    public EndorsementListService(
            EndorsementListRepository endorsementListRepository,
            EndorsementRepository endorsementRepository,
            UserRepository userRepository) {
        this.endorsementListRepository = endorsementListRepository;
        this.endorsementRepository = endorsementRepository;
        this.userRepository = userRepository;
    }

    public EndorsementListModel createEndorsementListEntry(EndorsementListDRO endorsementListDRO) {
        EndorsementListModel endorsementListEntry = new EndorsementListModel();

        UUID endorserId = endorsementListDRO.getEndorserId();
        UUID endorsementId = endorsementListDRO.getEndorsementId();

        Optional<EndorsementModel> endorsementOptional = endorsementRepository.findById(endorsementId);
        if ( endorsementOptional.isPresent()) {

            endorsementListEntry.setEndorserId(endorserId);
            endorsementListEntry.setEndorsement(endorsementOptional.get());
            endorsementListEntry.setDescription(endorsementListDRO.getDescription());
            endorsementListEntry.setType(endorsementListDRO.getType());
            endorsementListRepository.save(endorsementListEntry);
            return endorsementListEntry;

        } else {
//            if (endorserOptional.isEmpty())
//                throw new NoEntityException("User with id:" + endorserId + " not found");
            throw new NoEntityException("Endorsement with id:" + endorsementId + " not found");
        }
    }
}
