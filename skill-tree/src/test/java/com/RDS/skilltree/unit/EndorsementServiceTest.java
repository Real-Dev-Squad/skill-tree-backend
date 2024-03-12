package com.RDS.skilltree.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.RDS.skilltree.Endorsement.EndorsementDRO;
import com.RDS.skilltree.Endorsement.EndorsementDTO;
import com.RDS.skilltree.Endorsement.EndorsementModel;
import com.RDS.skilltree.Endorsement.EndorsementRepository;
import com.RDS.skilltree.Endorsement.EndorsementServiceImpl;
import com.RDS.skilltree.Exceptions.NoEntityException;
import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.Skill.SkillRepository;
import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
public class EndorsementServiceTest {
    @Mock private EndorsementRepository endorsementRepository;

    @Mock private UserRepository userRepository;

    @Mock private SkillRepository skillRepository;

    @InjectMocks @Autowired private EndorsementServiceImpl endorsementService;

    @Test
    public void itShouldGetEndorsementsById() {
        UUID endorsementId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID skillId = UUID.randomUUID();

        UserModel userModel = UserModel.builder().id(userId).build();
        SkillModel skillModel = SkillModel.builder().id(skillId).build();
        EndorsementModel endorsementModel =
                EndorsementModel.builder().id(endorsementId).user(userModel).skill(skillModel).build();
        endorsementModel.setCreatedAt(Instant.now());
        endorsementModel.setUpdatedAt(Instant.now());
        endorsementModel.setCreatedBy(userModel);
        endorsementModel.setUpdatedBy(userModel);

        when(endorsementRepository.findById(endorsementId)).thenReturn(Optional.of(endorsementModel));

        EndorsementDTO result = endorsementService.getEndorsementById(endorsementId);

        assertNotNull(result);
        assertEquals(
                endorsementId,
                result.getId(),
                "The Endorsement Id doesn't matches the expected endorsement Id");
    }

    @Test
    public void itShouldHandleEndorsementNotFound() {
        UUID nonExistentEndorsementId = UUID.randomUUID();
        when(endorsementRepository.findById(nonExistentEndorsementId)).thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> endorsementService.getEndorsementById(nonExistentEndorsementId));

        // Verify the exception message
        assertEquals(
                "No endorsement with the id " + nonExistentEndorsementId + " found",
                exception.getMessage());
    }

    @Test
    void testCreateEndorsement() {
        // Mock data
        UUID userId = UUID.randomUUID();
        UUID skillId = UUID.randomUUID();
        UUID endorsementId = UUID.randomUUID();
        EndorsementDRO endorsementDRO = new EndorsementDRO();
        endorsementDRO.setUserId(userId);
        endorsementDRO.setSkillId(skillId);

        UserModel mockUser = UserModel.builder().id(userId).build();
        SkillModel mockSkill = SkillModel.builder().id(skillId).build();
        EndorsementModel mockEndorsement =
                EndorsementModel.builder().id(endorsementId).user(mockUser).skill(mockSkill).build();
        mockEndorsement.setCreatedAt(Instant.now());
        mockEndorsement.setUpdatedAt(Instant.now());
        mockEndorsement.setCreatedBy(mockUser);
        mockEndorsement.setUpdatedBy(mockUser);

        // Mock the repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(mockSkill));
        when(endorsementRepository.save(any(EndorsementModel.class))).thenReturn(mockEndorsement);

        // Call the service method
        EndorsementModel result = endorsementService.createEndorsement(endorsementDRO);

        // Verify the interactions
        verify(endorsementRepository, times(1)).save(any(EndorsementModel.class));

        // Assertions
        assertNotNull(result);
        assertEquals(userId, result.getUser().getId());
        assertEquals(skillId, result.getSkill().getId());
    }

    @Test
    void testCreateEndorsementWithInvalidUser() {
        UUID userId = UUID.randomUUID();
        UUID skillId = UUID.randomUUID();
        EndorsementDRO endorsementDRO = new EndorsementDRO();
        endorsementDRO.setUserId(userId);
        endorsementDRO.setSkillId(skillId);

        // Mock the repository behavior for an invalid user
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Assert that a NoEntityException is thrown
        NoEntityException exception =
                assertThrows(
                        NoEntityException.class, () -> endorsementService.createEndorsement(endorsementDRO));
        assertEquals("User with id:" + userId + " not found", exception.getMessage());

        // Verify that save method is not called
        verify(endorsementRepository, never()).save(any(EndorsementModel.class));
    }

    @Test
    void testCreateEndorsementWithInvalidSkill() {
        UUID userId = UUID.randomUUID();
        UUID skillId = UUID.randomUUID();
        EndorsementDRO endorsementDRO = new EndorsementDRO();
        endorsementDRO.setUserId(userId);
        endorsementDRO.setSkillId(skillId);

        UserModel mockUser = new UserModel();
        mockUser.setId(userId);

        // Mock the repository behavior for an invalid skill
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(skillRepository.findById(skillId)).thenReturn(Optional.empty());

        // Assert that a NoEntityException is thrown
        NoEntityException exception =
                assertThrows(
                        NoEntityException.class, () -> endorsementService.createEndorsement(endorsementDRO));
        assertEquals("Skill with id:" + skillId + " not found", exception.getMessage());

        // Verify that save method is not called
        verify(endorsementRepository, never()).save(any(EndorsementModel.class));
    }
}
