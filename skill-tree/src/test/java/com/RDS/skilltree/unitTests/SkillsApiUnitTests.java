package com.RDS.skilltree.unitTests;

import com.RDS.skilltree.dtos.RdsGetUserDetailsResDto;
import com.RDS.skilltree.enums.SkillTypeEnum;
import com.RDS.skilltree.exceptions.SkillAlreadyExistsException;
import com.RDS.skilltree.models.JwtUser;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.repositories.SkillRepository;
import com.RDS.skilltree.services.SkillService;
import com.RDS.skilltree.services.SkillServiceImplementation;
import com.RDS.skilltree.services.external.RdsService;
import com.RDS.skilltree.viewmodels.CreateSkillViewModel;
import com.RDS.skilltree.viewmodels.RdsUserViewModel;
import com.RDS.skilltree.viewmodels.SkillViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SkillsApiUnitTests {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private RdsService rdsService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private SkillServiceImplementation skillService; // Class containing the getAll() method

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
    }

    @Test
    @DisplayName("Test the getAll() method returns all the created skills, where the skills return in a sorted order based on skill-Id.")
    public void getAllSkillsHappyFlow()
    {

        Skill skill1 = new Skill();
        skill1.setId(1);
        skill1.setName("Java");
        skill1.setType(SkillTypeEnum.ATOMIC);

        Skill skill2 = new Skill();
        skill2.setId(2);
        skill2.setName("Spring Boot");
        skill2.setType(SkillTypeEnum.ATOMIC);


        // Mock the repository's behavior
        when(skillRepository.findAll()).thenReturn(Arrays.asList(skill1, skill2));

        // Act: Call the method to be tested
        List<SkillViewModel> result = skillService.getAll();

        // Assert: Verify the result
        assertEquals(2, result.size());
        assertEquals("Java", result.get(0).getName());
        assertEquals("Spring Boot", result.get(1).getName());

        // Verify that the repository was called once
        verify(skillRepository, times(1)).findAll();
    }


    @Test
    public void testCreateSkill_Success() {
        // Arrange: Mock request and setup mocks
        CreateSkillViewModel createSkill = new CreateSkillViewModel();
        createSkill.setName("python");
        createSkill.setType(SkillTypeEnum.ATOMIC);

        RdsUserViewModel viewModel = new RdsUserViewModel();
        viewModel.setId("123");

        RdsGetUserDetailsResDto userDetails = new RdsGetUserDetailsResDto();
        userDetails.setUser(viewModel);


        Skill skillEntity = new Skill();
        skillEntity.setId(5);
        skillEntity.setName("python");
        skillEntity.setType(SkillTypeEnum.ATOMIC);
        skillEntity.setCreatedBy(userDetails.getUser().getId());

        SkillViewModel expectedViewModel = SkillViewModel.toViewModel(skillEntity);

        // Mock behavior
        when(skillRepository.existsByName("python")).thenReturn(false);

        when(skillRepository.saveAndFlush(any(Skill.class))).thenReturn(skillEntity);

        // Act: Call the method
        SkillViewModel actualViewModel = skillService.create(createSkill);

        // Assert: Verify the result
        assertNotNull(actualViewModel);
        assertEquals(expectedViewModel.getId(), actualViewModel.getId());
        assertEquals(expectedViewModel.getName(), actualViewModel.getName());
        assertEquals(expectedViewModel.getType(), actualViewModel.getType());

        // Verify that the repository and service were called correctly
        verify(skillRepository, times(1)).existsByName("python");
        verify(skillRepository, times(1)).saveAndFlush(any(Skill.class));

    }


    @Test
    public void testCreateSkill_AlreadyExists() {
        // Arrange
        CreateSkillViewModel createSkill = new CreateSkillViewModel();
        createSkill.setName("python");
        createSkill.setType(SkillTypeEnum.ATOMIC);

        // Mock behavior
        when(skillRepository.existsByName("python")).thenReturn(true);

        // Act & Assert: Verify that exception is thrown
        SkillAlreadyExistsException exception = assertThrows(
                SkillAlreadyExistsException.class,
                () -> skillService.create(createSkill),
                "Skill with name "+createSkill.getName()+"already exists"
        );

        assertEquals("Skill with name python already exists", exception.getMessage());

        // Verify repository was called once, but saveAndFlush wasn't called
        verify(skillRepository, times(1)).existsByName("python");
        verify(skillRepository, never()).saveAndFlush(any(Skill.class));
    }




}
