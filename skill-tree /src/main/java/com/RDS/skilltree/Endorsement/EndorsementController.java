package com.RDS.skilltree.Endorsement;

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
    public ResponseEntity<?> postEndorsement(@RequestBody EndorsementDRO endorsementDRO) {
        try {
            return new ResponseEntity<>(endorsementService.createEndorsement(endorsementDRO), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }
}
