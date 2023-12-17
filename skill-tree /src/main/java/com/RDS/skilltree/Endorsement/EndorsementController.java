package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.Common.Response.GenericResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/v1/endorsements")
@Slf4j
@RequiredArgsConstructor
public class EndorsementController {
    private final EndorsementService endorsementService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<EndorsementDTO>> getEndorsementById(@PathVariable(value = "id", required = true) String id){
        try {
            UUID uuid = UUID.fromString(id);
            EndorsementDTO response = endorsementService.getEndorsementById(uuid);
            return ResponseEntity.ok().body(new GenericResponse<EndorsementDTO>(response, "Data retrieved successfully"));
        } catch (IllegalArgumentException e) {
            String message = "Invalid UUID: " + id;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse<EndorsementDTO>(null, message));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericResponse<EndorsementDTO>(null, e.getMessage()));
        } catch (Exception e) {
            String message = "Something went wrong. Please contact admin.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericResponse<EndorsementDTO>(null, message));
        }
    }
}
