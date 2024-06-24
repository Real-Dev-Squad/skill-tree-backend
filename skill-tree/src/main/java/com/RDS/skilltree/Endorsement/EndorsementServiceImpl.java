package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.Common.Response.GenericResponse;
import com.RDS.skilltree.Exceptions.NoEntityException;
import com.RDS.skilltree.Skill.Skill;
import com.RDS.skilltree.Skill.SkillRepository;
import com.RDS.skilltree.User.JwtUserModel;
import com.RDS.skilltree.User.UserRoleEnum;
import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EndorsementServiceImpl implements EndorsementService {
    private final EndorsementRepository endorsementRepository;
    private final SkillRepository skillRepository;

    @Override
    public EndorsementDTO getEndorsementById(UUID id) throws IllegalStateException {
        Optional<EndorsementModel> endorsementModel = endorsementRepository.findById(id);
        return EndorsementDTO.toDto(
                endorsementModel.orElseThrow(
                        () -> new EntityNotFoundException("No endorsement with the id " + id + " found")));
    }

    @Override
    public EndorsementModel createEndorsement(EndorsementDRO endorsementDRO) {
        String userId = endorsementDRO.getEndorseId();
        Integer skillId = endorsementDRO.getSkillId();

        Optional<Skill> skillOptional = skillRepository.findById(skillId);
        if (skillOptional.isPresent()) {
            EndorsementModel endorsementModel =
                    EndorsementModel.builder().endorseId(userId).skillId(skillOptional.get().getId()).build();

            return endorsementRepository.save(endorsementModel);
        } else {

            throw new NoEntityException("Skill with id:" + skillId + " not found");
        }
    }

    @Override
    public GenericResponse<Void> updateEndorsementStatus(UUID id, String status) {
        JwtUserModel user =
                (JwtUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.getRole().equals(UserRoleEnum.SUPERUSER)) {
            throw new AccessDeniedException("Unauthorized, Access is only available to super users");
        }


        Optional<EndorsementModel> optionalEndorsementModel = endorsementRepository.findById(id);
        if (optionalEndorsementModel.isPresent()) {
            EndorsementModel updatedEndorsementModel =
                    EndorsementModel.builder()
                            .id(optionalEndorsementModel.get().getId())
                            .endorseId(optionalEndorsementModel.get().getEndorseId())
                            .skillId(optionalEndorsementModel.get().getSkillId())
                            .build();
            endorsementRepository.save(updatedEndorsementModel);
            return new GenericResponse<>(null, "Successfully updated endorsement status");
        }
        throw new NoEntityException("No endorsement with id " + id + " was found");
    }
}
