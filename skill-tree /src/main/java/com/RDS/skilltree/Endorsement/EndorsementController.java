package com.RDS.skilltree.Endorsement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

import static com.RDS.skilltree.Endorsement.JsonApiResponseConverter.convertToHashMap;

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
        try {
            UUID uuid = UUID.fromString(id);
            EndorsementDTO response = endorsementService.getEndorsementById(uuid);
            Map<String, Object> responseData = convertToHashMap(response);
            return ResponseEntity.ok(responseData);

        } catch (IllegalArgumentException e) {
            String message = "Invalid UUID: " + id;
            ApiResponse<String> response = new ApiResponse<>(null, HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST.toString(),message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (IllegalStateException e) {
            String message = e.getMessage();
            ApiResponse<String> response = new ApiResponse<>(null, HttpStatus.NOT_FOUND.value(),HttpStatus.NOT_FOUND.toString(),message);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            String message = e.getMessage();
            ApiResponse<String> response = new ApiResponse<>(null, HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST.toString(),message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}

