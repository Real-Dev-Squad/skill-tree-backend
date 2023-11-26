package com.RDS.skilltree.unit;

import com.RDS.skilltree.Endorsement.EndorsementDRO;
import com.RDS.skilltree.Endorsement.EndorsementModel;
import com.RDS.skilltree.Endorsement.EndorsementRepository;
import com.RDS.skilltree.Endorsement.EndorsementService;
import com.RDS.skilltree.Exceptions.NoEntityException;
import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.Skill.SkillRepository;
import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EndorsementServiceTest {

    @Mock
    private EndorsementRepository endorsementRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private EndorsementService endorsementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEndorsement() {
        // Mock data
        UUID userId = UUID.randomUUID();
        UUID skillId = UUID.randomUUID();
        EndorsementDRO endorsementDRO = new EndorsementDRO();
        endorsementDRO.setUserId(userId);
        endorsementDRO.setSkillId(skillId);

        UserModel mockUser = new UserModel();
        mockUser.setId(userId);

        SkillModel mockSkill = new SkillModel();
        mockSkill.setId(skillId);

        // Mock the repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(mockSkill));

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
        NoEntityException exception = assertThrows(NoEntityException.class,
                () -> endorsementService.createEndorsement(endorsementDRO));
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
        NoEntityException exception = assertThrows(NoEntityException.class,
                () -> endorsementService.createEndorsement(endorsementDRO));
        assertEquals("Skill with id:" + skillId + " not found", exception.getMessage());

        // Verify that save method is not called
        verify(endorsementRepository, never()).save(any(EndorsementModel.class));
    }
}
