package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.Common.Response.GenericResponse;
import com.RDS.skilltree.Exceptions.NoEntityException;
import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.Skill.SkillRepository;
import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserRepository;
import com.RDS.skilltree.User.UserRole;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EndorsementServiceImpl implements EndorsementService {
    private final EndorsementRepository endorsementRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    @Override
    public EndorsementDTO getEndorsementById(UUID id) throws IllegalStateException {
        Optional<EndorsementModel> endorsementModel = endorsementRepository.findById(id);
        return EndorsementDTO.toDto(
                endorsementModel.orElseThrow(
                        () -> new EntityNotFoundException("No endorsement with the id " + id + " found")));
    }

    @Override
    public Page<EndorsementModel> getEndorsements(PageRequest pageRequest) {
        return endorsementRepository.findAll(pageRequest);
    }

    @Override
    public Page<EndorsementModelFromJSON> getEndorsementsFromDummyData(
            PageRequest pageRequest, String skillID, String userID) throws IOException {

        // TODO: temporary stub, implement in followup PR
        return null;
    }

    @Override
    public EndorsementModel createEndorsement(EndorsementDRO endorsementDRO) {
        UUID userId = endorsementDRO.getUserId();
        UUID skillId = endorsementDRO.getSkillId();
        Optional<UserModel> userOptional = userRepository.findById(userId);
        Optional<SkillModel> skillOptional = skillRepository.findById(skillId);
        if (userOptional.isPresent() && skillOptional.isPresent()) {
            EndorsementModel endorsementModel =
                    EndorsementModel.builder().user(userOptional.get()).skill(skillOptional.get()).build();

            return endorsementRepository.save(endorsementModel);
        } else {
            if (userOptional.isEmpty())
                throw new NoEntityException("User with id:" + userId + " not found");
            throw new NoEntityException("Skill with id:" + skillId + " not found");
        }
    }

    @Override
    public ResponseEntity<GenericResponse<Void>> updateEndorsementStatus(String id, String status) {
        UserModel user =
                (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.getRole().equals(UserRole.SUPERUSER))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new GenericResponse<>(null, "Unauthorized"));
        if (!(Objects.equals(status, EndorsementStatus.APPROVED.name())
                || Objects.equals(status, EndorsementStatus.REJECTED.name()))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GenericResponse<>(null, "Invalid Status: " + status));
        }
        try {
            UUID uuid = UUID.fromString(id);
            Optional<EndorsementModel> optionalEndorsementModel = endorsementRepository.findById(uuid);
            if (optionalEndorsementModel.isPresent()) {
                EndorsementModel updatedEndorsementModel =
                        EndorsementModel.builder()
                                .id(optionalEndorsementModel.get().getId())
                                .user(optionalEndorsementModel.get().getUser())
                                .skill(optionalEndorsementModel.get().getSkill())
                                .endorsersList(optionalEndorsementModel.get().getEndorsersList())
                                .status(EndorsementStatus.valueOf(status))
                                .build();
                endorsementRepository.save(updatedEndorsementModel);
                return ResponseEntity.ok()
                        .body(new GenericResponse<>(null, "Successfully updated endorsement status"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GenericResponse<>(null, "Invalid UUID: " + id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GenericResponse<>(null, "Invalid UUID: " + id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new GenericResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericResponse<>(null, "Something went wrong. Please contact admin."));
        }
    }
}
