package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserRepository;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.repositories.SkillRepository;
import com.RDS.skilltree.utils.GenericResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/endorsements")
public class EndorsementController {
    private final UserRepository userRepository;
    private final EndorsementRepository endorsementRepository;
    private final SkillRepository skillRepository;

    @PostMapping
    // TODO : add a check for when a endorsement is already created by a user for a particular skill.
    public ResponseEntity<GenericResponse<EndorsementModel>> postEndorsement(
            @RequestBody @Valid CreateEndorsementDro endorsementDro) {

        String message = endorsementDro.getMessage();
        Integer skillId = endorsementDro.getSkillId();
        String endorseId = endorsementDro.getEndorseId();

        // TODO: Get this from security context once the login api is implemented.
        String endorserId = "ae7a6673c5574140838f209de4c644fc";

        if (Objects.equals(endorseId, endorserId)) {
            return new ResponseEntity<>(
                    new GenericResponse<>(null, "Self endorsement not allowed"),
                    HttpStatus.METHOD_NOT_ALLOWED);
        }

        Optional<Skill> skillDetails = skillRepository.findById(skillId);
        Optional<UserModel> endorseDetails = userRepository.findById(endorseId);
        Optional<UserModel> endorserDetails = userRepository.findById(endorserId);

        if (endorserDetails.isEmpty()) {
            return new ResponseEntity<>(
                    new GenericResponse<>(null, "Endorser details not found"), HttpStatus.NOT_FOUND);
        }

        if (endorseDetails.isEmpty()) {
            return new ResponseEntity<>(
                    new GenericResponse<>(null, "Endorse not found"), HttpStatus.NOT_FOUND);
        }

        if (skillDetails.isEmpty()) {
            return new ResponseEntity<>(
                    new GenericResponse<>(null, String.format("Skill id: %s not found.", skillId)),
                    HttpStatus.NOT_FOUND);
        }

        EndorsementModel newEndorsement =
                EndorsementModel.builder()
                        .endorser(endorserDetails.get())
                        .endorse(endorseDetails.get())
                        .skill(skillDetails.get())
                        .message(message)
                        .build();

        try {
            return new ResponseEntity<>(
                    new GenericResponse<>(endorsementRepository.save(newEndorsement), ""),
                    HttpStatus.CREATED);
        } catch (DataIntegrityViolationException error) {
            log.error(
                    "Error saving endorsement with skillId {} and endorse id {}, error: {}",
                    skillId,
                    endorseId,
                    error.getMessage());
            return new ResponseEntity<>(
                    new GenericResponse<>(null, "Something went wrong"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GenericResponse<EndorsementModel>> updateEndorsement(
            @PathVariable Integer id, @RequestBody UpdateEndorsementDro body) {
        Optional<EndorsementModel> endorsementDetails = endorsementRepository.findById(id);

        if (endorsementDetails.isEmpty()) {
            return new ResponseEntity<>(
                    new GenericResponse<>(null, "Endorsement not found"), HttpStatus.NOT_FOUND);
        }

        EndorsementModel endorsement = endorsementDetails.get();

        if (body.getMessage() != null) {
            endorsement.setMessage(body.getMessage());
        }

        try {
            return new ResponseEntity<>(new GenericResponse<>(endorsementRepository.save(endorsement), "Endorsement updated"), HttpStatus.OK);
        } catch (
                DataIntegrityViolationException error) {
            log.error("Error endorsement id: {}, error: {}", endorsement.getId(), error.getMessage());
            return new ResponseEntity<>(new GenericResponse<>(null, "Something went wrong please try again"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
