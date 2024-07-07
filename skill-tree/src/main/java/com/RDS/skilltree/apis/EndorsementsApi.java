package com.RDS.skilltree.apis;

import com.RDS.skilltree.User.UserRoleEnum;
import com.RDS.skilltree.annotations.AuthorizedRoles;
import com.RDS.skilltree.services.EndorsementService;
import com.RDS.skilltree.viewmodels.CreateEndorsementViewModel;
import com.RDS.skilltree.viewmodels.EndorsementListViewModel;
import com.RDS.skilltree.viewmodels.EndorsementViewModel;
import com.RDS.skilltree.viewmodels.UpdateEndorsementViewModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/endorsements")
@AuthorizedRoles({UserRoleEnum.USER, UserRoleEnum.SUPERUSER})
public class EndorsementsApi {
    private final EndorsementService endorsementService;

    @GetMapping
    public ResponseEntity<Page<EndorsementListViewModel>> getAllEndorsements(
            @RequestParam(name = "offset", defaultValue = "0", required = false) @Min(0) int offset,
            @RequestParam(name = "limit", defaultValue = "10", required = false) @Min(1) int limit) {
        PageRequest pageRequest = PageRequest.of(offset, limit);
        return ResponseEntity.ok(endorsementService.getAllEndorsements(pageRequest));
    }

    @PostMapping
    public ResponseEntity<EndorsementViewModel> create(
            @Valid @RequestBody CreateEndorsementViewModel endorsement) {
        return new ResponseEntity<>(endorsementService.create(endorsement), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EndorsementViewModel> update(
            @PathVariable Integer id, @Valid @RequestBody UpdateEndorsementViewModel body) {
        return new ResponseEntity<>(endorsementService.update(id, body), HttpStatus.OK);
    }
}
