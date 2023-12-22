package com.RDS.skilltree.unit;

import com.RDS.skilltree.Endorsement.EndorsementDTO;
import com.RDS.skilltree.Endorsement.EndorsementModel;
import com.RDS.skilltree.Endorsement.EndorsementRepository;
import com.RDS.skilltree.Endorsement.EndorsementServiceImpl;
import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.User.UserModel;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EndorsementServiceTest {
    @Mock
    private EndorsementRepository endorsementRepository;

    @InjectMocks
    @Autowired
    private EndorsementServiceImpl endorsementService;

    @Test
    public void itShouldGetEndorsementsById() {
        UUID endorsementId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID skillId = UUID.randomUUID();

        UserModel userModel = UserModel.builder().id(userId).build();
        SkillModel skillModel = SkillModel.builder().id(skillId).build();
        EndorsementModel endorsementModel = EndorsementModel.builder()
                .id(endorsementId)
                .user(userModel)
                .skill(skillModel)
                .build();
        endorsementModel.setCreatedAt(Instant.now());
        endorsementModel.setUpdatedAt(Instant.now());
        endorsementModel.setCreatedBy(userModel);
        endorsementModel.setUpdatedBy(userModel);

        when(endorsementRepository.findById(endorsementId)).thenReturn(Optional.of(endorsementModel));

        EndorsementDTO result = endorsementService.getEndorsementById(endorsementId);

        assertNotNull(result);
        assertEquals(endorsementId, result.getId(), "The Endorsement Id doesn't matches the expected endorsement Id");
    }

    @Test
    public void itShouldHandleEndorsementNotFound() {
        UUID nonExistentEndorsementId = UUID.randomUUID();
        when(endorsementRepository.findById(nonExistentEndorsementId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> endorsementService.getEndorsementById(nonExistentEndorsementId));

        // Verify the exception message
        assertEquals("No endorsement with the id " + nonExistentEndorsementId + " found", exception.getMessage());
    }
}