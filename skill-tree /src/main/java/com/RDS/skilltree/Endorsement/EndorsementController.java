package com.RDS.skilltree.Endorsement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${API_V1_PREFIX}/endorsements")
public class EndorsementController {

    private final EndorsementService endorsementService;

    @Autowired
    public EndorsementController(EndorsementService endorsementService) {
        this.endorsementService = endorsementService;
    }

    @GetMapping()
    public List<EndorsementModel> getEndorsements() {
        return endorsementService.getEndorsements();
    }
}
