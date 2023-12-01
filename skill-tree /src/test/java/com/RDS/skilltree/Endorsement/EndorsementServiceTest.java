package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.User.UserModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class EndorsementServiceTest {
    @Mock
    private EndorsementRepository endorsementRepository;

    @InjectMocks
    @Autowired
    private EndorsementServiceImpl underTest;

    @Test
    public void itShouldGetEndorsementsById() {
        UUID endorsementId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID skillId = UUID.randomUUID();

        UserModel userModel = new UserModel();
        userModel.setId(userId);
        SkillModel skillModel = new SkillModel();
        skillModel.setId(skillId);
        EndorsementModel endorsementModel = EndorsementModel.builder()
                .id(endorsementId)
                .user(userModel)
                .skill(skillModel)
                .build();
        endorsementModel.setCreatedAt(Instant.now());
        endorsementModel.setUpdatedAt(Instant.now());
        endorsementModel.setCreatedBy(userModel);
        endorsementModel.setUpdatedBy(userModel);

        // Given
        when(endorsementRepository.findById(endorsementId)).thenReturn(Optional.of(endorsementModel));

        // When
        EndorsementDTO result = underTest.getEndorsementById(endorsementId);

        // Then
        assertNotNull(result);
        assertEquals("The Endorsement Id doesn't matches the expected endorsement Id", endorsementId, result.getId());
    }

    @Test
    public void itShouldHandleEndorsementNotFound() {
        // Given
        UUID nonExistentEndorsementId = UUID.randomUUID();
        when(endorsementRepository.findById(nonExistentEndorsementId)).thenReturn(Optional.empty());

        // When , Then
        assertThatThrownBy(() -> underTest.getEndorsementById(nonExistentEndorsementId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No endorsement with the id " + nonExistentEndorsementId + " found");
    }

}