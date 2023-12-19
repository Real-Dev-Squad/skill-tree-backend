package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.User.UserModel;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

        EndorsementDTO result = underTest.getEndorsementById(endorsementId);

        assertNotNull(result);
        assertEquals(endorsementId, result.getId(), "The Endorsement Id doesn't matches the expected endorsement Id");
    }

    @Test
    public void itShouldHandleEndorsementNotFound() {
        UUID nonExistentEndorsementId = UUID.randomUUID();
        when(endorsementRepository.findById(nonExistentEndorsementId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getEndorsementById(nonExistentEndorsementId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No endorsement with the id " + nonExistentEndorsementId + " found");
    }

}