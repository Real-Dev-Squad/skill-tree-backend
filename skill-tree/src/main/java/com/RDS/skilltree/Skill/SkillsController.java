package com.RDS.skilltree.Skill;

import com.RDS.skilltree.Common.Response.GenericResponse;
import com.RDS.skilltree.Endorsement.EndorsementRepository;
import com.RDS.skilltree.User.JwtUserModel;
import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/skills")
public class SkillsController {
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final EndorsementRepository endorsementRepository;

    @GetMapping
    public GenericResponse<List<Skill>> getAllSkills(@RequestParam(required = false) String name) {
        skillRepository.findByName(name);
        return new GenericResponse<>(skillRepository.findAll(), null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // TODO : Return only the valid fields by using a DTO.
    public GenericResponse<Skill> createSkill(
            Authentication authentication, @RequestBody(required = true) @Valid SkillDRO skill) {
        JwtUserModel userDetails = (JwtUserModel) authentication.getPrincipal();

        String userId = "ae7a6673c5574140838f209de4c644fc";
        Optional<UserModel> user = userRepository.findById(userId);

        // TODO : return a correct http status instead of 201
        if (skillRepository.findByName(skill.getName()).isPresent()) {
            return new GenericResponse<>(
                    null, String.format("Skill with name %s already exists", skill.getName()));
        }

        // TODO : return a correct http status instead of 201
        if (user.isEmpty()) {
            return new GenericResponse<>(null, "User not found");
        }

        Skill newSkill =
                Skill.builder().name(skill.getName()).type(skill.getType()).createdBy(user.get()).build();

        try {
            return new GenericResponse<>(skillRepository.save(newSkill), "Skill created");
        } catch (DataIntegrityViolationException error) {
            log.error("Error saving skill {}, error: {}", skill.getName(), error.getMessage());
            // TODO : return a correct http status instead of 201
            return new GenericResponse<>(null, "Something went wrong please try again");
        }
    }

    @GetMapping("/{id}/endorsements")
    public ResponseEntity<Page<?>> getEndorsementsBySkillId(
            @RequestParam(name = "offset", defaultValue = "0", required = false) @Min(0) int offset,
            @RequestParam(name = "limit", defaultValue = "10", required = false) @Min(1) int limit,
            @PathVariable(value = "id") Integer skillID) {
        PageRequest pageRequest = PageRequest.of(offset, limit);
        return ResponseEntity.ok(endorsementRepository.findBySkillId(skillID, pageRequest));
    }
}
