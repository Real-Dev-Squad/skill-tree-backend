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
    public ResponseEntity<EndorsementModel> postEndorsement(@RequestBody EndorsementDRO endorsementDRO) {

        return new ResponseEntity<>(endorsementService.createEndorsement(endorsementDRO), HttpStatus.OK);
    }
}
