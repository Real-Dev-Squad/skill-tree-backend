package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.Exceptions.NoEntityException;
import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.Skill.SkillRepository;
import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EndorsementServiceImpl implements EndorsementService {
    private final EndorsementRepository endorsementRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    private final ObjectMapper objectMapper;

    @Value("${endorsements.dummmy-data.path}")
    private  String dummyEndorsementDataPath;

    private static final Logger logger = LoggerFactory.getLogger(EndorsementServiceImpl.class);

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

    public Page<EndorsementModelFromJSON> getEndorsementsFromDummyData(PageRequest pageRequest, String skillIDString, String userIDString) throws IOException {

        try {
            List<EndorsementModelFromJSON> endorsementModelFromJSONList = readEndorsementsFromJSON();
            List<EndorsementModelFromJSON> filteredEndorsements = filterEndorsements(endorsementModelFromJSONList, skillIDString, userIDString);
            return createPagedEndorsements(filteredEndorsements, pageRequest);
        } catch (IOException e) {
            logger.error("Error reading endorsements JSON data: {}", e.getMessage());
            throw new IOException("Error reading endorsements JSON data", e);
        }
    }

    private List<EndorsementModelFromJSON> readEndorsementsFromJSON() throws IOException {
        ClassPathResource resource = new ClassPathResource(dummyEndorsementDataPath);
        return objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {});
    }

    private List<EndorsementModelFromJSON> filterEndorsements(List<EndorsementModelFromJSON> endorsements, String skillIDString, String userIDString) {
        return endorsements.stream()
                .filter(endorsement -> isMatchingSkillId(endorsement, skillIDString))
                .filter(endorsement -> isMatchingUserId(endorsement, userIDString))
                .collect(Collectors.toList());
    }

    private boolean isMatchingSkillId(EndorsementModelFromJSON endorsement, String skillIDString) {
        return skillIDString == null || skillIDString.isEmpty() || endorsement.getSkillId().equals(UUID.fromString(skillIDString));
    }

    private boolean isMatchingUserId(EndorsementModelFromJSON endorsement, String userIDString) {
        return userIDString == null || userIDString.isEmpty() || endorsement.getUserID().equals(UUID.fromString(userIDString));
    }

    private Page<EndorsementModelFromJSON> createPagedEndorsements(List<EndorsementModelFromJSON> endorsements, PageRequest pageRequest) {
        int startIdx = (int) pageRequest.getOffset();
        int endIdx = Math.min(startIdx + pageRequest.getPageSize(), endorsements.size());
        List<EndorsementModelFromJSON> currentPageEndorsements = endorsements.subList(startIdx, endIdx);
        return new PageImpl<>(currentPageEndorsements, pageRequest, endorsements.size());
    }




    @Override
    public EndorsementModel createEndorsement(EndorsementDRO endorsementDRO) {
        UUID userId = endorsementDRO.getUserId();
        UUID skillId = endorsementDRO.getSkillId();
        Optional<UserModel> userOptional = userRepository.findById(userId);
        Optional<SkillModel> skillOptional = skillRepository.findById(skillId);
        if (userOptional.isPresent() && skillOptional.isPresent()) {
        EndorsementModel endorsementModel =  EndorsementModel.builder()
                .user(userOptional.get())
                .skill(skillOptional.get())
                .build();

            return endorsementRepository.save(endorsementModel);
        } else {
            if (userOptional.isEmpty())
                throw new NoEntityException("User with id:" + userId + " not found");
            throw new NoEntityException("Skill with id:" + skillId + " not found");
        }
    }
}