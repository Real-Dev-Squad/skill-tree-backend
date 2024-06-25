package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.Common.Response.GenericResponse;
import com.RDS.skilltree.Skill.Skill;
import com.RDS.skilltree.Skill.SkillRepository;
import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/v1/endorsements")
@Slf4j
@RequiredArgsConstructor
public class EndorsementController {
    private final UserRepository userRepository;
    private final EndorsementRepository endorsementRepository;
    private final EndorsementService endorsementService;
    private final SkillRepository skillRepository;

    //    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    //    public ResponseEntity<GenericResponse<EndorsementDTO>> getEndorsementById(
    //            @PathVariable(value = "id", required = true) String id) {
    //        try {
    //            UUID uuid = UUID.fromString(id);
    //            EndorsementDTO response = endorsementService.getEndorsementById(uuid);
    //            return ResponseEntity.ok()
    //                    .body(new GenericResponse<EndorsementDTO>(response, "Data retrieved
    // successfully"));
    //        } catch (IllegalArgumentException e) {
    //            String message = "Invalid UUID: " + id;
    //            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    //                    .body(new GenericResponse<EndorsementDTO>(null, message));
    //        } catch (EntityNotFoundException e) {
    //            return ResponseEntity.status(HttpStatus.NOT_FOUND)
    //                    .body(new GenericResponse<EndorsementDTO>(null, e.getMessage()));
    //        } catch (Exception e) {
    //            String message = "Something went wrong. Please contact admin.";
    //            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                    .body(new GenericResponse<EndorsementDTO>(null, message));
    //        }
    //    }

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
