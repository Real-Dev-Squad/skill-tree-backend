package com.RDS.skilltree.EndorsementList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class EndorsementListController {

    @Autowired
    private EndorsementListService endorsementListService;

    @PostMapping("/list")
    public ResponseEntity<EndorsementListModel> endorse(@RequestBody EndorsementListDRO endorsementListDRO) {
        return new ResponseEntity<>(endorsementListService.createEndorsementListEntry(endorsementListDRO), HttpStatus.OK);

    }
}
