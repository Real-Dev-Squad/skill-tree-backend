package com.RDS.skilltree.unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.RDS.skilltree.dtos.RdsGetUserDetailsResDto;
import com.RDS.skilltree.models.Endorsement;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.repositories.EndorsementRepository;
import com.RDS.skilltree.services.EndorsementServiceImplementation;
import com.RDS.skilltree.services.external.RdsService;
import com.RDS.skilltree.viewmodels.EndorsementViewModel;
import com.RDS.skilltree.viewmodels.RdsUserViewModel;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EndorsementsApiUnitTests {

    @Mock private EndorsementRepository endorsementRepository;

    @Mock private RdsService rdsService;

    @InjectMocks private EndorsementServiceImplementation endorsementService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    @DisplayName("To test getAllEndorsementsBySkillId() method")
    public void testGetAllEndorsementsBySkillId() {
        List<Endorsement> endorsements = new ArrayList<>();

        Integer skillId = 1;

        // initializing a skill
        Skill skill1 = new Skill();
        skill1.setId(skillId);
        skill1.setName("python");

        // initializing an endorsement
        Endorsement endorsement1 = new Endorsement();
        endorsement1.setId(1);
        endorsement1.setSkill(skill1);
        endorsement1.setEndorserId("456");
        endorsement1.setEndorseId("123");
        endorsement1.setMessage("Please approve my endorsement for this skill");

        endorsements.add(endorsement1);

        RdsGetUserDetailsResDto rdsGetUserDetailsResDto1 = new RdsGetUserDetailsResDto();
        RdsUserViewModel rdsUserViewModel1 = new RdsUserViewModel();
        rdsUserViewModel1.setId("123");
        rdsGetUserDetailsResDto1.setUser(rdsUserViewModel1);

        RdsGetUserDetailsResDto rdsGetUserDetailsResDto2 = new RdsGetUserDetailsResDto();
        RdsUserViewModel rdsUserViewModel2 = new RdsUserViewModel();
        rdsUserViewModel2.setId("456");
        rdsGetUserDetailsResDto2.setUser(rdsUserViewModel2);

        // Mock behaviour
        when(endorsementRepository.findBySkillId(skillId)).thenReturn(endorsements);
        when(rdsService.getUserDetails(endorsement1.getEndorseId()))
                .thenReturn(rdsGetUserDetailsResDto1);
        when(rdsService.getUserDetails(endorsement1.getEndorserId()))
                .thenReturn(rdsGetUserDetailsResDto2);

        // Act

        List<EndorsementViewModel> endorsmentsBySkillId =
                endorsementService.getAllEndorsementsBySkillId(skillId);

        assert (endorsmentsBySkillId.size() == 1);
        assertEquals("456", endorsmentsBySkillId.get(0).getEndorser().getId());
        assertEquals("123", endorsmentsBySkillId.get(0).getEndorse().getId());
        assertEquals("python", endorsmentsBySkillId.get(0).getSkill().getName());
    }
}
