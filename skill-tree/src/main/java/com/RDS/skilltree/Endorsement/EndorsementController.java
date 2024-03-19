package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.Common.Response.GenericResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/endorsements")
@Slf4j
@RequiredArgsConstructor
public class EndorsementController {
    private final EndorsementService endorsementService;

    @GetMapping(value = "")
    public ResponseEntity<Page<EndorsementModelFromJSON>> getAllEndorsements(
            @RequestParam(name = "offset", defaultValue = "0", required = false) @Min(0) int offset,
            @RequestParam(name = "limit", defaultValue = "10", required = false) @Min(1) int limit,
            @RequestParam(name = "skillID", required = false) String skillID,
            @RequestParam(name = "userID", required = false) String userID)
            throws IOException {
        PageRequest pageRequest = PageRequest.of(offset, limit);
        Page<EndorsementModelFromJSON> pagedEndorsements =
                endorsementService.getEndorsementsFromDummyData(pageRequest, skillID, userID);
        if (pagedEndorsements.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(pagedEndorsements);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<EndorsementDTO>> getEndorsementById(
            @PathVariable(value = "id", required = true) String id) {
        try {
            UUID uuid = UUID.fromString(id);
            EndorsementDTO response = endorsementService.getEndorsementById(uuid);
            return ResponseEntity.ok()
                    .body(new GenericResponse<EndorsementDTO>(response, "Data retrieved successfully"));
        } catch (IllegalArgumentException e) {
            String message = "Invalid UUID: " + id;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GenericResponse<EndorsementDTO>(null, message));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new GenericResponse<EndorsementDTO>(null, e.getMessage()));
        } catch (Exception e) {
            String message = "Something went wrong. Please contact admin.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericResponse<EndorsementDTO>(null, message));
        }
    }

    @PostMapping(value = "")
    public ResponseEntity<GenericResponse<EndorsementDTO>> postEndorsement(
            @RequestBody @Valid EndorsementDRO endorsementDRO) {

        EndorsementModel endorsementModel = endorsementService.createEndorsement(endorsementDRO);
        if (endorsementModel != null)
            return new ResponseEntity<>(
                    new GenericResponse<EndorsementDTO>(EndorsementDTO.toDto(endorsementModel), ""),
                    HttpStatus.CREATED);
        return new ResponseEntity<>(
                new GenericResponse<EndorsementDTO>(null, "Failed to create endorsement"),
                HttpStatus.BAD_REQUEST);
    }
}
