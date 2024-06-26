package com.RDS.skilltree.apis;

import com.RDS.skilltree.services.EndorsementService;
import com.RDS.skilltree.viewmodels.CreateEndorsementViewModel;
import com.RDS.skilltree.viewmodels.EndorsementViewModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/endorsements")
public class EndorsementsApi {
    private final EndorsementService endorsementService;

    @PostMapping
    public ResponseEntity<EndorsementViewModel> create(@Valid @RequestBody CreateEndorsementViewModel endorsement) {
        return new ResponseEntity<>(endorsementService.create(endorsement), HttpStatus.CREATED);
    }
}
