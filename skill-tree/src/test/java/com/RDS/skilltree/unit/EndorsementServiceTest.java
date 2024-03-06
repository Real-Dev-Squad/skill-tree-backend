package com.RDS.skilltree.unit;

import com.RDS.skilltree.Endorsement.EndorsementDRO;
import com.RDS.skilltree.Endorsement.EndorsementDTO;
import com.RDS.skilltree.Endorsement.EndorsementModel;
import com.RDS.skilltree.Endorsement.EndorsementRepository;
import com.RDS.skilltree.Endorsement.EndorsementServiceImpl;
import com.RDS.skilltree.Endorsement.EndorsementModelFromJSON;
import com.RDS.skilltree.Exceptions.NoEntityException;
import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.Skill.SkillRepository;
import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.testcontainers.shaded.com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EndorsementServiceTest {
    @Mock
    private EndorsementRepository endorsementRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    @Autowired
    private EndorsementServiceImpl endorsementService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(endorsementService, "dummyEndorsementDataPath", "dummy-data/endorsements/endorsements.json");
    }

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
    @DisplayName("Get endorsements given a valid userID")
    void itShouldGetEndorsementsGivenUserID() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String skillIDString = UUID.randomUUID().toString();
        String userIDString = UUID.randomUUID().toString();

        List<EndorsementModelFromJSON> dummyEndorsements = new ArrayList<>();
        dummyEndorsements.add(new EndorsementModelFromJSON(
                UUID.randomUUID(),
                UUID.fromString(userIDString),
                UUID.fromString(skillIDString),
                "PENDING",
                LocalDateTime.now(),
                UUID.randomUUID(),
                LocalDateTime.now(),
                UUID.randomUUID()
        ));

        when(objectMapper.readValue(ArgumentMatchers.<InputStream>any(), ArgumentMatchers.<TypeReference<List<EndorsementModelFromJSON>>>any()))
                .thenReturn(dummyEndorsements);
        Page<EndorsementModelFromJSON> result = endorsementService.getEndorsementsFromDummyData(pageRequest, null, userIDString);

        assertEquals(new PageImpl<>(dummyEndorsements, pageRequest, dummyEndorsements.size()), result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Get endorsements given a valid skillID")
    public void itShouldReturnEndorsementsGivenSkillID() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String skillIDString = UUID.randomUUID().toString();
        String userIDString = UUID.randomUUID().toString();

        List<EndorsementModelFromJSON> dummyEndorsements = new ArrayList<>();
        dummyEndorsements.add(new EndorsementModelFromJSON(
                UUID.randomUUID(),
                UUID.fromString(userIDString),
                UUID.fromString(skillIDString),
                "APPROVED",
                LocalDateTime.now(),
                UUID.randomUUID(),
                LocalDateTime.now(),
                UUID.randomUUID()
        ));

        when(objectMapper.readValue(ArgumentMatchers.<InputStream>any(), ArgumentMatchers.<TypeReference<List<EndorsementModelFromJSON>>>any()))
                .thenReturn(dummyEndorsements);
        Page<EndorsementModelFromJSON> result = endorsementService.getEndorsementsFromDummyData(pageRequest, skillIDString, null);

        assertEquals(new PageImpl<>(dummyEndorsements, pageRequest, dummyEndorsements.size()), result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Return paginated result having 2 pages when number of endorsements with a given userID is 15")
    public void itShouldReturnPaginatedResultOnSearch() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String userIDString = UUID.randomUUID().toString();

        List<EndorsementModelFromJSON> dummyEndorsements = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            dummyEndorsements.add(new EndorsementModelFromJSON(
                    UUID.randomUUID(),
                    UUID.fromString(userIDString),
                    UUID.randomUUID(),
                    "APPROVED",
                    LocalDateTime.now(),
                    UUID.randomUUID(),
                    LocalDateTime.now(),
                    UUID.randomUUID()
            ));
        }

        when(objectMapper.readValue(ArgumentMatchers.<InputStream>any(), ArgumentMatchers.<TypeReference<List<EndorsementModelFromJSON>>>any()))
                .thenReturn(dummyEndorsements);
        Page<EndorsementModelFromJSON> result = endorsementService.getEndorsementsFromDummyData(pageRequest, null, userIDString);

        assertEquals(2, result.getTotalPages());
        assertEquals(15, result.getTotalElements());
    }

    @Test
    @DisplayName("Return empty endorsement list given a valid userID but skillID which is not present")
    public void itShouldReturnEmptyDataGivenUserIDAndSkillIDNotPresent() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String skillIDString = UUID.randomUUID().toString();
        String userIDString = UUID.randomUUID().toString();

        List<EndorsementModelFromJSON> dummyEndorsements = new ArrayList<>();
        dummyEndorsements.add(new EndorsementModelFromJSON(
                UUID.randomUUID(),
                UUID.fromString(userIDString),
                UUID.randomUUID(),
                "APPROVED",
                LocalDateTime.now(),
                UUID.randomUUID(),
                LocalDateTime.now(),
                UUID.randomUUID()
        ));
        List<EndorsementModelFromJSON> endorsementsResult = new ArrayList<>();

        when(objectMapper.readValue(ArgumentMatchers.<InputStream>any(), ArgumentMatchers.<TypeReference<List<EndorsementModelFromJSON>>>any()))
                .thenReturn(dummyEndorsements);
        Page<EndorsementModelFromJSON> result = endorsementService.getEndorsementsFromDummyData(pageRequest, skillIDString, userIDString);

        assertEquals(new PageImpl<>(endorsementsResult, pageRequest, endorsementsResult.size()), result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    @DisplayName("Return empty endorsement list given a userID which is not present")
    public void itShouldReturnEmptyDataGivenUserIDNotPresent() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String skillIDString = UUID.randomUUID().toString();
        String userIDString = UUID.randomUUID().toString();

        List<EndorsementModelFromJSON> dummyEndorsements = new ArrayList<>();
        dummyEndorsements.add(new EndorsementModelFromJSON(
                UUID.randomUUID(),
                UUID.fromString(userIDString),
                UUID.fromString(skillIDString),
                "APPROVED",
                LocalDateTime.now(),
                UUID.randomUUID(),
                LocalDateTime.now(),
                UUID.randomUUID()
        ));
        List<EndorsementModelFromJSON> endorsementsResult = new ArrayList<>();

        when(objectMapper.readValue(ArgumentMatchers.<InputStream>any(), ArgumentMatchers.<TypeReference<List<EndorsementModelFromJSON>>>any()))
                .thenReturn(dummyEndorsements);
        Page<EndorsementModelFromJSON> result = endorsementService.getEndorsementsFromDummyData(pageRequest, UUID.randomUUID().toString(), null);

        assertEquals(new PageImpl<>(endorsementsResult, pageRequest, endorsementsResult.size()), result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    @DisplayName("Return IO exception on error reading data")
    void itShouldReturnIOExceptionIfErrorReadingData() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String skillIDString = null;
        String userIDString = null;
        when(objectMapper.readValue(ArgumentMatchers.<InputStream>any(), ArgumentMatchers.<TypeReference<List<EndorsementModelFromJSON>>>any())).thenThrow(new IOException("Error reading data"));

        assertThrows(IOException.class, () -> endorsementService.getEndorsementsFromDummyData(pageRequest, skillIDString, userIDString));
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

    @Test
    void testCreateEndorsement() {
        // Mock data
        UUID userId = UUID.randomUUID();
        UUID skillId = UUID.randomUUID();
        UUID endorsementId  = UUID.randomUUID();
        EndorsementDRO endorsementDRO = new EndorsementDRO();
        endorsementDRO.setUserId(userId);
        endorsementDRO.setSkillId(skillId);

        UserModel mockUser = UserModel.builder().id(userId).build();
        SkillModel mockSkill =  SkillModel.builder().id(skillId).build();
         EndorsementModel mockEndorsement = EndorsementModel.builder()
                 .id(endorsementId)
                 .user(mockUser)
                 .skill(mockSkill)
                 .build();
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