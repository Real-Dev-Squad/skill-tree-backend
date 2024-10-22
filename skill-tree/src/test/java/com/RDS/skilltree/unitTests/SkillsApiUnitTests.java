package com.RDS.skilltree.unitTests;

import com.RDS.skilltree.dtos.RdsGetUserDetailsResDto;
import com.RDS.skilltree.dtos.SkillRequestsDto;
import com.RDS.skilltree.enums.SkillTypeEnum;
import com.RDS.skilltree.enums.UserRoleEnum;
import com.RDS.skilltree.enums.UserSkillStatusEnum;
import com.RDS.skilltree.exceptions.NoEntityException;
import com.RDS.skilltree.exceptions.SkillAlreadyExistsException;
import com.RDS.skilltree.models.Endorsement;
import com.RDS.skilltree.models.JwtUser;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.models.UserSkills;
import com.RDS.skilltree.repositories.EndorsementRepository;
import com.RDS.skilltree.repositories.SkillRepository;
import com.RDS.skilltree.repositories.UserSkillRepository;
import com.RDS.skilltree.services.SkillService;
import com.RDS.skilltree.services.SkillServiceImplementation;
import com.RDS.skilltree.services.external.RdsService;
import com.RDS.skilltree.utils.GenericResponse;
import com.RDS.skilltree.viewmodels.CreateSkillViewModel;
import com.RDS.skilltree.viewmodels.RdsUserViewModel;
import com.RDS.skilltree.viewmodels.SkillRequestsWithUserDetailsViewModel;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SkillsApiUnitTests {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private UserSkillRepository userSkillRepository;



    @Mock
    private RdsService rdsService;

    @Mock
    private Authentication authentication;

    @Mock
    private SkillRequestsWithUserDetailsViewModel skillRequestsWithUserDetailsViewModel;

    @InjectMocks
    private SkillServiceImplementation skillService; // Class containing the getAll() method

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
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
    @DisplayName("Test the create() skill method, which returns the created skill data")
    public void testCreateSkill_Success() {
        // Arrange: Mock request and setup mocks
        CreateSkillViewModel createSkill = new CreateSkillViewModel();
        createSkill.setName("python");
        createSkill.setType(SkillTypeEnum.ATOMIC);

        // Mock JwtUser with rdsUserId and role
        JwtUser jwtUser = new JwtUser("123", UserRoleEnum.USER);



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

        when(authentication.getPrincipal()).thenReturn(jwtUser);// Return mock JwtUser
        when(rdsService.getUserDetails(jwtUser.getRdsUserId())).thenReturn(userDetails);  // Use rdsUserId
        when(skillRepository.saveAndFlush(any(Skill.class))).thenReturn(skillEntity);





        when(skillRepository.existsByName("python")).thenReturn(false);



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
    @DisplayName("Test the create() skill method, when an already present skill is being added")
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

    //To be modified



@Test
@DisplayName("To Test the approveRejectSkillRequest() method, when action is approved")
public void testApproveRejectSkillRequest(){

    // Arrange
    Integer skillId = 1;
    String endorseId = "123";
    UserSkillStatusEnum action = UserSkillStatusEnum.APPROVED;

     Skill skill1 = new Skill();
     skill1.setId(1);

    List<UserSkills> existingSkillRequest = new ArrayList<>();
    UserSkills userSkill1 = new UserSkills();
    userSkill1.setId("1");
    userSkill1.setUserId("123");
    userSkill1.setStatus(UserSkillStatusEnum.PENDING);
    userSkill1.setSkill(skill1);
    existingSkillRequest.add(userSkill1);

    when(userSkillRepository.findByUserIdAndSkillId(userSkill1.getUserId(), skill1.getId())).thenReturn(existingSkillRequest);
    when(userSkillRepository.save(userSkill1)).thenReturn(userSkill1);


    // Act: Call the method
    GenericResponse<String> response=  skillService.approveRejectSkillRequest(skillId,endorseId,action);

// Assert
    assertNotNull(response);
    assertEquals("approved", response.getMessage()); // Depending on the action
    assertEquals(UserSkillStatusEnum.APPROVED, userSkill1.getStatus());


}

    @Test
    @DisplayName("To Test the approveRejectSkillRequest() method, when action is rejected")
    public void testApproveRejectSkillRequest_Reject() {
        // Arrange
        Integer skillId = 2;
        String endorseId = "456";
        UserSkillStatusEnum action = UserSkillStatusEnum.REJECTED;

        Skill skill1 = new Skill();
        skill1.setId(2);

        UserSkills mockSkillRequest = new UserSkills();
        mockSkillRequest.setSkill(skill1);
        mockSkillRequest.setUserId(endorseId);
        mockSkillRequest.setStatus(UserSkillStatusEnum.PENDING);

        List<UserSkills> existingSkillRequest = new ArrayList<>();
        existingSkillRequest.add(mockSkillRequest);

        // Mock repository behavior
        when(userSkillRepository.findByUserIdAndSkillId(endorseId, skillId)).thenReturn(existingSkillRequest);
        when(userSkillRepository.save(mockSkillRequest)).thenReturn(mockSkillRequest);

        // Act
        GenericResponse<String> response = skillService.approveRejectSkillRequest(skillId, endorseId, action);

        // Assert
        assertNotNull(response);
        assertEquals("rejected", response.getMessage()); // Depending on the action
        assertEquals(UserSkillStatusEnum.REJECTED, mockSkillRequest.getStatus());

        // Verify that the save method was called
        verify(userSkillRepository, times(1)).save(mockSkillRequest);
    }

    @Test
    @DisplayName("To Test the approveRejectSkillRequest() method, when no skill requests are present")
    public void testApproveRejectSkillRequest_NoEntityException() {
        // Arrange
        Integer skillId = 3;
        String endorseId = "789";
        UserSkillStatusEnum action = UserSkillStatusEnum.APPROVED;

        // Mock repository behavior for no entity found
        when(userSkillRepository.findByUserIdAndSkillId(endorseId, skillId)).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(NoEntityException.class, () -> {
            skillService.approveRejectSkillRequest(skillId, endorseId, action);
        });

        // Verify that the save method was never called
        verify(userSkillRepository, never()).save(any(UserSkills.class));
    }







}
