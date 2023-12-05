package com.RDS.skilltree.Endorsement;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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
public class EndorsementController {
    private final EndorsementService endorsementService;

    public EndorsementController(EndorsementService endorsementService) {
        this.endorsementService = endorsementService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEndorsementById(@PathVariable(value = "id", required = true) String id){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            UUID uuid = UUID.fromString(id);
            EndorsementDTO response = endorsementService.getEndorsementById(uuid);
            return ResponseEntity.ok().headers(headers).body(response);
        } catch (IllegalArgumentException e) {
            String jsonResponse = "{\"message\": \"" + "Invalid UUID: " + id + "\"}";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(jsonResponse);
        } catch (EntityNotFoundException e) {
            String jsonResponse = "{\"message\": \"" + e.getMessage() + "\"}";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).body(jsonResponse);
        } catch (Exception e) {
            String jsonResponse = "{\"message\": \"" + "Something went wrong. Please contact admin." + "\"}";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(jsonResponse);
        }
    }
}

