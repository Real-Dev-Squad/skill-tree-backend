package com.RDS.skilltree.Endorsement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class EndorsementController {
    @Autowired
    private EndorsementService endorsementService;

    @GetMapping("/endorsement")
    public ResponseEntity<List<EndorsementDTO>> getEndorsements() {
        return new ResponseEntity<>(endorsementService.getAllEndorsements(), HttpStatus.OK);
    }

    @PostMapping("/endorsement")
    public ResponseEntity<EndorsementModel> postEndorsement(@RequestBody EndorsementDRO endorsementDRO) {

        return new ResponseEntity<>(endorsementService.createEndorsement(endorsementDRO), HttpStatus.CREATED);
    }
}
