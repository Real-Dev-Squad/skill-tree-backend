package com.RDS.skilltree.Endorsement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/endorsements")
public class EndorsementController {

    @Autowired
    private EndorsementService endorsementService;

    @PostMapping("/")
    public EndorsementModel createEndorsement(@RequestBody EndorsementDRO endorsement) {
        return endorsementService.createEndorsement(endorsement);
    }

    @PutMapping("/")
    public EndorsementModel updateEndorsement(@RequestBody EndorsementModel endorsement) {
        return endorsementService.updateEndorsement(endorsement);
    }

    @GetMapping("/")
    public List<EndorsementModel> getAllEndorsements() {
        return endorsementService.getAllEndorsements();
    }

    @GetMapping("/{id}")
    public List<EndorsementModel> getEndorsementBySomeId(@PathVariable UUID id, @RequestParam String type) {
        if(type.equals("userId")) {
            return endorsementService.getEndorsementsByUserId(id);
        }else return endorsementService.getEndorsementsBySkillId(id);
    }
}
