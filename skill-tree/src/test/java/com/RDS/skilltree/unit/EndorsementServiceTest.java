package com.RDS.skilltree.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.RDS.skilltree.Common.Response.GenericResponse;
import com.RDS.skilltree.Endorsement.*;
import com.RDS.skilltree.Exceptions.NoEntityException;
import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.Skill.SkillRepository;
import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserRepository;
import com.RDS.skilltree.User.UserRole;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class EndorsementServiceTest {
    @Mock private EndorsementRepository endorsementRepository;

    @Mock private UserRepository userRepository;

    @Mock private SkillRepository skillRepository;

    @Mock private ObjectMapper objectMapper;

    @InjectMocks @Autowired private EndorsementServiceImpl endorsementService;

    @Mock private Authentication auth;

    @Disabled // Change to @BeforeEach while enabling endorsement search v1 related tests
    public void setUp() {
        ReflectionTestUtils.setField(
                endorsementService, "dummyEndorsementDataPath", "dummy-data/endorsements.json");
    }

    @AfterEach
    public void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

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
    @DisplayName("Get endorsements given a valid skillID")
    @Disabled
    public void itShouldReturnEndorsementsGivenSkillID() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String skillID = UUID.randomUUID().toString();
        String userID = UUID.randomUUID().toString();

        List<EndorsementModelFromJSON> dummyEndorsements = new ArrayList<>();
        dummyEndorsements.add(
                new EndorsementModelFromJSON(
                        UUID.randomUUID(),
                        UUID.fromString(userID),
                        UUID.fromString(skillID),
                        "APPROVED",
                        LocalDateTime.now(),
                        UUID.randomUUID(),
                        LocalDateTime.now(),
                        UUID.randomUUID()));

        when(objectMapper.readValue(
                        ArgumentMatchers.<InputStream>any(),
                        ArgumentMatchers.<TypeReference<List<EndorsementModelFromJSON>>>any()))
                .thenReturn(dummyEndorsements);
        Page<EndorsementModelFromJSON> result =
                endorsementService.getEndorsementsFromDummyData(pageRequest, skillID, null);

        assertEquals(new PageImpl<>(dummyEndorsements, pageRequest, dummyEndorsements.size()), result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Get endorsements given a valid userID")
    @Disabled
    void itShouldGetEndorsementsGivenUserID() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String skillID = UUID.randomUUID().toString();
        String userID = UUID.randomUUID().toString();

        List<EndorsementModelFromJSON> dummyEndorsements = new ArrayList<>();
        dummyEndorsements.add(
                new EndorsementModelFromJSON(
                        UUID.randomUUID(),
                        UUID.fromString(userID),
                        UUID.fromString(skillID),
                        "PENDING",
                        LocalDateTime.now(),
                        UUID.randomUUID(),
                        LocalDateTime.now(),
                        UUID.randomUUID()));

        when(objectMapper.readValue(
                        ArgumentMatchers.<InputStream>any(),
                        ArgumentMatchers.<TypeReference<List<EndorsementModelFromJSON>>>any()))
                .thenReturn(dummyEndorsements);
        Page<EndorsementModelFromJSON> result =
                endorsementService.getEndorsementsFromDummyData(pageRequest, null, userID);

        assertEquals(new PageImpl<>(dummyEndorsements, pageRequest, dummyEndorsements.size()), result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Get endorsements given an invalid userID")
    @Disabled
    public void itShouldThrowErrorIfInvalidUserIDIsGiven() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String skillID = UUID.randomUUID().toString();
        String userID = UUID.randomUUID().toString();

        List<EndorsementModelFromJSON> dummyEndorsements = new ArrayList<>();
        dummyEndorsements.add(
                new EndorsementModelFromJSON(
                        UUID.randomUUID(),
                        UUID.fromString(userID),
                        UUID.fromString(skillID),
                        "PENDING",
                        LocalDateTime.now(),
                        UUID.randomUUID(),
                        LocalDateTime.now(),
                        UUID.randomUUID()));

        when(objectMapper.readValue(
                        ArgumentMatchers.<InputStream>any(),
                        ArgumentMatchers.<TypeReference<List<EndorsementModelFromJSON>>>any()))
                .thenReturn(dummyEndorsements);

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        endorsementService.getEndorsementsFromDummyData(pageRequest, null, "invalid-user-id"));
    }

    @Test
    @DisplayName("Get endorsements given an invalid skillID")
    @Disabled
    public void itShouldThrowIllegalArgumentExceptionIfInvalidSkillIDIsGiven() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String skillID = UUID.randomUUID().toString();
        String userID = UUID.randomUUID().toString();

        List<EndorsementModelFromJSON> dummyEndorsements = new ArrayList<>();
        dummyEndorsements.add(
                new EndorsementModelFromJSON(
                        UUID.randomUUID(),
                        UUID.fromString(userID),
                        UUID.fromString(skillID),
                        "PENDING",
                        LocalDateTime.now(),
                        UUID.randomUUID(),
                        LocalDateTime.now(),
                        UUID.randomUUID()));

        when(objectMapper.readValue(
                        ArgumentMatchers.<InputStream>any(),
                        ArgumentMatchers.<TypeReference<List<EndorsementModelFromJSON>>>any()))
                .thenReturn(dummyEndorsements);

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        endorsementService.getEndorsementsFromDummyData(pageRequest, "invalid-skill-id", null));
    }

    @Test
    @DisplayName("Get endorsements given a valid userID and invalid skillID")
    @Disabled
    public void itShouldThrowIllegalArgumentExceptionIfInvalidUserIDIsGiven() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String skillID = UUID.randomUUID().toString();
        String userID = UUID.randomUUID().toString();

        List<EndorsementModelFromJSON> dummyEndorsements = new ArrayList<>();
        dummyEndorsements.add(
                new EndorsementModelFromJSON(
                        UUID.randomUUID(),
                        UUID.fromString(userID),
                        UUID.fromString(skillID),
                        "PENDING",
                        LocalDateTime.now(),
                        UUID.randomUUID(),
                        LocalDateTime.now(),
                        UUID.randomUUID()));

        when(objectMapper.readValue(
                        ArgumentMatchers.<InputStream>any(),
                        ArgumentMatchers.<TypeReference<List<EndorsementModelFromJSON>>>any()))
                .thenReturn(dummyEndorsements);

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        endorsementService.getEndorsementsFromDummyData(
                                pageRequest, "invalid-skill-id", UUID.randomUUID().toString()));
    }

    @Test
    @DisplayName(
            "Return paginated result having 2 pages when number of endorsements with a given userID is 15")
    @Disabled
    public void itShouldReturnPaginatedResultOnSearch() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String userID = UUID.randomUUID().toString();

        List<EndorsementModelFromJSON> dummyEndorsements = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            dummyEndorsements.add(
                    new EndorsementModelFromJSON(
                            UUID.randomUUID(),
                            UUID.fromString(userID),
                            UUID.randomUUID(),
                            "APPROVED",
                            LocalDateTime.now(),
                            UUID.randomUUID(),
                            LocalDateTime.now(),
                            UUID.randomUUID()));
        }

        when(objectMapper.readValue(
                        ArgumentMatchers.<InputStream>any(),
                        ArgumentMatchers.<TypeReference<List<EndorsementModelFromJSON>>>any()))
                .thenReturn(dummyEndorsements);
        Page<EndorsementModelFromJSON> result =
                endorsementService.getEndorsementsFromDummyData(pageRequest, null, userID);

        assertEquals(2, result.getTotalPages());
        assertEquals(15, result.getTotalElements());
    }

    @Test
    @DisplayName("Return empty page when accessing out of bound page in paginated result")
    @Disabled
    public void itShouldReturnEmptyPaginatedResultOnSearch() throws IOException {
        PageRequest pageRequest = PageRequest.of(10, 10);
        String userID = UUID.randomUUID().toString();

        List<EndorsementModelFromJSON> dummyEndorsements = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            dummyEndorsements.add(
                    new EndorsementModelFromJSON(
                            UUID.randomUUID(),
                            UUID.fromString(userID),
                            UUID.randomUUID(),
                            "APPROVED",
                            LocalDateTime.now(),
                            UUID.randomUUID(),
                            LocalDateTime.now(),
                            UUID.randomUUID()));
        }

        when(objectMapper.readValue(
                        ArgumentMatchers.<InputStream>any(),
                        ArgumentMatchers.<TypeReference<List<EndorsementModelFromJSON>>>any()))
                .thenReturn(dummyEndorsements);
        Page<EndorsementModelFromJSON> result =
                endorsementService.getEndorsementsFromDummyData(pageRequest, null, userID);

        assertEquals(Page.empty(pageRequest), result);
    }

    @Test
    @DisplayName(
            "Return empty endorsement list given a valid userID but skillID which is not present")
    @Disabled
    public void itShouldReturnEmptyDataGivenUserIDAndSkillIDNotPresent() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String skillID = UUID.randomUUID().toString();
        String userID = UUID.randomUUID().toString();

        List<EndorsementModelFromJSON> dummyEndorsements = new ArrayList<>();
        dummyEndorsements.add(
                new EndorsementModelFromJSON(
                        UUID.randomUUID(),
                        UUID.fromString(userID),
                        UUID.randomUUID(),
                        "APPROVED",
                        LocalDateTime.now(),
                        UUID.randomUUID(),
                        LocalDateTime.now(),
                        UUID.randomUUID()));
        List<EndorsementModelFromJSON> endorsementsResult = new ArrayList<>();

        when(objectMapper.readValue(
                        ArgumentMatchers.<InputStream>any(),
                        ArgumentMatchers.<TypeReference<List<EndorsementModelFromJSON>>>any()))
                .thenReturn(dummyEndorsements);
        Page<EndorsementModelFromJSON> result =
                endorsementService.getEndorsementsFromDummyData(pageRequest, skillID, userID);

        assertEquals(
                new PageImpl<>(endorsementsResult, pageRequest, endorsementsResult.size()), result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    @DisplayName("Return empty page when no endorsements are present in dummy data")
    @Disabled
    public void itShouldReturnEmptyDataWhenNoEndorsementsArePresent() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String skillID = UUID.randomUUID().toString();
        String userID = UUID.randomUUID().toString();

        List<EndorsementModelFromJSON> dummyEndorsements = new ArrayList<>();

        when(objectMapper.readValue(
                        ArgumentMatchers.<InputStream>any(),
                        ArgumentMatchers.<TypeReference<List<EndorsementModelFromJSON>>>any()))
                .thenReturn(dummyEndorsements);
        Page<EndorsementModelFromJSON> result =
                endorsementService.getEndorsementsFromDummyData(pageRequest, skillID, userID);

        assertEquals(Page.empty(pageRequest), result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    @DisplayName("Return empty endorsement list given a userID which is not present")
    @Disabled
    public void itShouldReturnEmptyDataGivenUserIDNotPresent() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String skillID = UUID.randomUUID().toString();
        String userID = UUID.randomUUID().toString();

        List<EndorsementModelFromJSON> dummyEndorsements = new ArrayList<>();
        dummyEndorsements.add(
                new EndorsementModelFromJSON(
                        UUID.randomUUID(),
                        UUID.fromString(userID),
                        UUID.fromString(skillID),
                        "APPROVED",
                        LocalDateTime.now(),
                        UUID.randomUUID(),
                        LocalDateTime.now(),
                        UUID.randomUUID()));
        List<EndorsementModelFromJSON> endorsementsResult = new ArrayList<>();

        when(objectMapper.readValue(
                        ArgumentMatchers.<InputStream>any(),
                        ArgumentMatchers.<TypeReference<List<EndorsementModelFromJSON>>>any()))
                .thenReturn(dummyEndorsements);
        Page<EndorsementModelFromJSON> result =
                endorsementService.getEndorsementsFromDummyData(
                        pageRequest, null, UUID.randomUUID().toString());

        assertEquals(
                new PageImpl<>(endorsementsResult, pageRequest, endorsementsResult.size()), result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    @DisplayName("Return empty endorsement list given empty userID and skillID")
    @Disabled
    public void itShouldReturnEmptyDataGivenEmptyUserIDAndSkillID() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String skillID = UUID.randomUUID().toString();
        String userID = UUID.randomUUID().toString();

        List<EndorsementModelFromJSON> dummyEndorsements = new ArrayList<>();
        dummyEndorsements.add(
                new EndorsementModelFromJSON(
                        UUID.randomUUID(),
                        UUID.fromString(userID),
                        UUID.fromString(skillID),
                        "APPROVED",
                        LocalDateTime.now(),
                        UUID.randomUUID(),
                        LocalDateTime.now(),
                        UUID.randomUUID()));

        when(objectMapper.readValue(
                        ArgumentMatchers.<InputStream>any(),
                        ArgumentMatchers.<TypeReference<List<EndorsementModelFromJSON>>>any()))
                .thenReturn(dummyEndorsements);
        Page<EndorsementModelFromJSON> result =
                endorsementService.getEndorsementsFromDummyData(pageRequest, "", "");

        assertEquals(new PageImpl<>(dummyEndorsements, pageRequest, dummyEndorsements.size()), result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Return empty endorsement list given a skillID which is not present")
    @Disabled
    public void itShouldReturnEmptyDataGivenSkillIDNotPresent() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String skillID = UUID.randomUUID().toString();
        String userID = UUID.randomUUID().toString();

        List<EndorsementModelFromJSON> dummyEndorsements = new ArrayList<>();
        dummyEndorsements.add(
                new EndorsementModelFromJSON(
                        UUID.randomUUID(),
                        UUID.fromString(userID),
                        UUID.fromString(skillID),
                        "APPROVED",
                        LocalDateTime.now(),
                        UUID.randomUUID(),
                        LocalDateTime.now(),
                        UUID.randomUUID()));
        List<EndorsementModelFromJSON> endorsementsResult = new ArrayList<>();

        when(objectMapper.readValue(
                        ArgumentMatchers.<InputStream>any(),
                        ArgumentMatchers.<TypeReference<List<EndorsementModelFromJSON>>>any()))
                .thenReturn(dummyEndorsements);
        Page<EndorsementModelFromJSON> result =
                endorsementService.getEndorsementsFromDummyData(
                        pageRequest, UUID.randomUUID().toString(), null);

        assertEquals(
                new PageImpl<>(endorsementsResult, pageRequest, endorsementsResult.size()), result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    @DisplayName("Return empty endorsement list given a skillID and userID which is not present")
    @Disabled
    public void itShouldReturnEmptyDataGivenSkillIDAndUserIDNotPresent() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String skillID = UUID.randomUUID().toString();
        String userID = UUID.randomUUID().toString();

        List<EndorsementModelFromJSON> dummyEndorsements = new ArrayList<>();
        dummyEndorsements.add(
                new EndorsementModelFromJSON(
                        UUID.randomUUID(),
                        UUID.fromString(userID),
                        UUID.fromString(skillID),
                        "APPROVED",
                        LocalDateTime.now(),
                        UUID.randomUUID(),
                        LocalDateTime.now(),
                        UUID.randomUUID()));
        List<EndorsementModelFromJSON> endorsementsResult = new ArrayList<>();

        when(objectMapper.readValue(
                        ArgumentMatchers.<InputStream>any(),
                        ArgumentMatchers.<TypeReference<List<EndorsementModelFromJSON>>>any()))
                .thenReturn(dummyEndorsements);
        Page<EndorsementModelFromJSON> result =
                endorsementService.getEndorsementsFromDummyData(
                        pageRequest, UUID.randomUUID().toString(), UUID.randomUUID().toString());

        assertEquals(
                new PageImpl<>(endorsementsResult, pageRequest, endorsementsResult.size()), result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    @DisplayName("Return IO exception on error reading data")
    @Disabled
    void itShouldReturnIOExceptionIfErrorReadingData() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String skillID = null;
        String userID = null;
        when(objectMapper.readValue(
                        ArgumentMatchers.<InputStream>any(),
                        ArgumentMatchers.<TypeReference<List<EndorsementModelFromJSON>>>any()))
                .thenThrow(new IOException("Error reading data"));

        assertThrows(
                IOException.class,
                () -> endorsementService.getEndorsementsFromDummyData(pageRequest, skillID, userID));
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

    @Test
    @DisplayName("Return unauthorized given user is not a super user")
    public void itShouldReturnUnauthorizedIfUserIsNotSuperUser() {
        UserModel userModel = new UserModel();
        userModel.setRole(UserRole.USER);
        when(auth.getPrincipal()).thenReturn(userModel);
        SecurityContextHolder.getContext().setAuthentication(auth);

        UUID endorsementId = UUID.randomUUID();
        String status = EndorsementStatus.APPROVED.name();

        AccessDeniedException exception =
                assertThrows(
                        AccessDeniedException.class,
                        () -> endorsementService.updateEndorsementStatus(endorsementId.toString(), status));
        assertEquals("Unauthorized access", exception.getMessage());
        verify(endorsementRepository, never()).save(any(EndorsementModel.class));
    }

    @Test
    @DisplayName("Return invalid status given status is not approved or rejected")
    public void itShouldReturnInvalidStatusIfStatusIsNotApprovedOrRejected() {
        UserModel userModel = new UserModel();
        userModel.setRole(UserRole.SUPERUSER);
        when(auth.getPrincipal()).thenReturn(userModel);
        SecurityContextHolder.getContext().setAuthentication(auth);

        UUID endorsementId = UUID.randomUUID();
        String status = EndorsementStatus.PENDING.name();

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> endorsementService.updateEndorsementStatus(endorsementId.toString(), status));
        assertEquals("Invalid endorsement status: " + status, exception.getMessage());
        verify(endorsementRepository, never()).save(any(EndorsementModel.class));
    }

    @Test
    @DisplayName("Return invalid id given a invalid endorsement id")
    public void itShouldThrowIllegalArgumentExceptionIfInvalidEndorsementId() {
        UserModel userModel = new UserModel();
        userModel.setRole(UserRole.SUPERUSER);
        when(auth.getPrincipal()).thenReturn(userModel);
        SecurityContextHolder.getContext().setAuthentication(auth);

        String invalidUUID = "jdsmjfdhfg6Bo6VdwW0ih....";
        String status = EndorsementStatus.APPROVED.name();

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> endorsementService.updateEndorsementStatus(invalidUUID, status));
        assertEquals("Invalid endorsement id: " + invalidUUID, exception.getMessage());
        verify(endorsementRepository, never()).save(any(EndorsementModel.class));
    }

    @Test
    @DisplayName("Return endorsement not found given an unknown endorsement id")
    public void itShouldReturnEndorsementNotFoundGivenUnknownEndorsementId() {
        UserModel userModel = new UserModel();
        userModel.setRole(UserRole.SUPERUSER);
        when(auth.getPrincipal()).thenReturn(userModel);
        SecurityContextHolder.getContext().setAuthentication(auth);

        UUID nonExistentEndorsementId = UUID.randomUUID();
        String status = EndorsementStatus.APPROVED.name();

        when(endorsementRepository.findById(nonExistentEndorsementId)).thenReturn(Optional.empty());

        NoEntityException exception =
                assertThrows(
                        NoEntityException.class,
                        () ->
                                endorsementService.updateEndorsementStatus(
                                        nonExistentEndorsementId.toString(), status));
        assertEquals(
                "No endorsement with id " + nonExistentEndorsementId + " was found",
                exception.getMessage());
        verify(endorsementRepository, never()).save(any(EndorsementModel.class));
    }

    @Test
    @DisplayName(
            "Update endorsement status given a valid endorsement id and status is approved or rejected")
    public void itShouldUpdateEndorsementStatusGivenEndorsementIdAndStatusApprovedOrRejected() {
        UserModel userModel = new UserModel();
        userModel.setRole(UserRole.SUPERUSER);
        when(auth.getPrincipal()).thenReturn(userModel);
        SecurityContextHolder.getContext().setAuthentication(auth);

        UUID userId = UUID.randomUUID();
        UUID skillId = UUID.randomUUID();
        UUID endorsementId = UUID.randomUUID();
        EndorsementStatus status = EndorsementStatus.APPROVED;

        UserModel mockUser = UserModel.builder().id(userId).build();
        SkillModel mockSkill = SkillModel.builder().id(skillId).build();
        EndorsementModel mockEndorsement =
                EndorsementModel.builder().id(endorsementId).user(mockUser).skill(mockSkill).build();
        mockEndorsement.setCreatedAt(Instant.now());
        mockEndorsement.setUpdatedAt(Instant.now());
        mockEndorsement.setCreatedBy(mockUser);
        mockEndorsement.setUpdatedBy(mockUser);

        when(endorsementRepository.findById(endorsementId)).thenReturn(Optional.of(mockEndorsement));

        GenericResponse<Void> result =
                endorsementService.updateEndorsementStatus(endorsementId.toString(), status.name());
        assertEquals("Successfully updated endorsement status", result.getMessage());

        verify(endorsementRepository, times(1)).save(any(EndorsementModel.class));
    }
}
