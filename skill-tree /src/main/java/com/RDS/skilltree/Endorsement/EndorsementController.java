package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.Exceptions.NoEntityException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class EndorsementController {
    @Autowired
    private EndorsementService endorsementService;


    @PostMapping("/endorsement")
    public ResponseEntity<?> postEndorsement(@RequestBody @Valid EndorsementDRO endorsementDRO) {
        try {
            EndorsementModel endorsementModel = endorsementService.createEndorsement(endorsementDRO);
            if (endorsementModel != null)
                return new ResponseEntity<>(endorsementModel, HttpStatus.CREATED);
            return new ResponseEntity<>("Failed to create endorsement", HttpStatus.BAD_REQUEST);

        } catch (NoEntityException | IllegalArgumentException e) {
            String message = e.getMessage();
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }
}
