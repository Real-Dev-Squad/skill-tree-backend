package com.RDS.skilltree.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.RDS.skilltree.Common.Response.GenericResponse;
import com.RDS.skilltree.Endorsement.*;
import com.RDS.skilltree.Exceptions.EntityAlreadyExistsException;
import com.RDS.skilltree.Exceptions.InvalidParameterException;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(
                endorsementService, "dummyEndorsementDataPath", "dummy-data/endorsements.json");
    }

    @AfterEach
    public void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    private void setupUpdateEndorsementTests(Boolean useSuperUserRole) {
        UserModel userModel = new UserModel();
        if (useSuperUserRole) {
            userModel.setRole(UserRole.SUPERUSER);
        } else {
            userModel.setRole(UserRole.USER);
        }
        when(auth.getPrincipal()).thenReturn(userModel);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void itShouldGetEndorsementsById() {
        UUID endorsementId = UUID.randomUUID();
        UUID endorserId = UUID.randomUUID();
        UUID skillId = UUID.randomUUID();

        SkillModel skillModel = SkillModel.builder().id(skillId).build();
        EndorsementModel endorsementModel =
                EndorsementModel.builder()
                        .id(endorsementId)
                        .endorseeId(endorserId)
                        .skill(skillModel)
                        .build();
        endorsementModel.setCreatedAt(Instant.now());
        endorsementModel.setUpdatedAt(Instant.now());
        endorsementModel.setCreatedBy(endorsementId);
        endorsementModel.setUpdatedBy(endorsementId);

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
        UUID endorserId = UUID.randomUUID();
        UUID skillId = UUID.randomUUID();
        UUID endorsementId = UUID.randomUUID();
        EndorsementDRO endorsementDRO = new EndorsementDRO();
        endorsementDRO.setEndorseeId(endorserId);
        endorsementDRO.setSkillId(skillId);

        SkillModel mockSkill = SkillModel.builder().id(skillId).build();
        EndorsementModel mockEndorsement =
                EndorsementModel.builder()
                        .id(endorsementId)
                        .endorseeId(endorserId)
                        .skill(mockSkill)
                        .build();
        mockEndorsement.setCreatedAt(Instant.now());
        mockEndorsement.setUpdatedAt(Instant.now());
        mockEndorsement.setCreatedBy(endorserId);
        mockEndorsement.setUpdatedBy(endorserId);

        // Mock the repository behavior
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(mockSkill));
        when(endorsementRepository.save(any(EndorsementModel.class))).thenReturn(mockEndorsement);

        // Call the service method
        EndorsementModel result = endorsementService.createEndorsement(endorsementDRO);

        // Verify the interactions
        verify(endorsementRepository, times(1)).save(any(EndorsementModel.class));

        // Assertions
        assertNotNull(result);
        assertEquals(endorserId, result.getEndorseeId());
        assertEquals(skillId, result.getSkill().getId());
    }

    @Test
    void testCreateEndorsementWithInvalidSkill() {
        UUID endorserId = UUID.randomUUID();
        UUID skillId = UUID.randomUUID();
        EndorsementDRO endorsementDRO = new EndorsementDRO();
        endorsementDRO.setEndorseeId(endorserId);
        endorsementDRO.setSkillId(skillId);

        // Mock the repository behavior for an invalid skill
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
    @DisplayName(
            "Return unauthorized access, given user is not a super user to update endorsement status")
    public void itShouldReturnUnauthorizedGivenUserIsNotSuperUser() {
        setupUpdateEndorsementTests(false);

        UUID endorsementId = UUID.randomUUID();
        String status = EndorsementStatus.APPROVED.name();

        AccessDeniedException exception =
                assertThrows(
                        AccessDeniedException.class,
                        () -> endorsementService.updateEndorsementStatus(endorsementId, status));
        assertEquals("Unauthorized, Access is only available to super users", exception.getMessage());
        verify(endorsementRepository, never()).save(any(EndorsementModel.class));
    }

    @Test
    @DisplayName("Return invalid status given status is pending")
    public void itShouldReturnInvalidStatusGivenEndorsementStatusIsPending() {
        setupUpdateEndorsementTests(true);

        UUID endorsementId = UUID.randomUUID();
        String status = EndorsementStatus.PENDING.name();

        InvalidParameterException exception =
                assertThrows(
                        InvalidParameterException.class,
                        () -> endorsementService.updateEndorsementStatus(endorsementId, status));
        assertEquals("Invalid parameter endorsement status: " + status, exception.getMessage());
        verify(endorsementRepository, never()).save(any(EndorsementModel.class));
    }

    @Test
    @DisplayName("Return invalid status given status is invalid")
    public void itShouldReturnInvalidStatusGivenInvalidEndorsementStatus() {
        setupUpdateEndorsementTests(true);

        UUID endorsementId = UUID.randomUUID();
        String status = "invalid-status";

        InvalidParameterException exception =
                assertThrows(
                        InvalidParameterException.class,
                        () -> endorsementService.updateEndorsementStatus(endorsementId, status));
        assertEquals("Invalid parameter endorsement status: " + status, exception.getMessage());
        verify(endorsementRepository, never()).save(any(EndorsementModel.class));
    }

    @Test
    @DisplayName("Return cannot modify status given status is already updated")
    public void itShouldThrowEntityAlreadyExistsExceptionGivenEndorsementIsUpdated() {
        setupUpdateEndorsementTests(true);

        UUID endorseeId = UUID.randomUUID();
        UUID skillId = UUID.randomUUID();
        UUID endorsementId = UUID.randomUUID();

        SkillModel mockSkill = SkillModel.builder().id(skillId).build();
        EndorsementModel mockEndorsement =
                EndorsementModel.builder()
                        .id(endorsementId)
                        .status(EndorsementStatus.APPROVED)
                        .endorseeId(endorseeId)
                        .skill(mockSkill)
                        .build();
        mockEndorsement.setCreatedAt(Instant.now());
        mockEndorsement.setUpdatedAt(Instant.now());
        mockEndorsement.setCreatedBy(endorseeId);
        mockEndorsement.setUpdatedBy(endorseeId);

        when(endorsementRepository.findById(endorsementId)).thenReturn(Optional.of(mockEndorsement));

        EntityAlreadyExistsException exception =
                assertThrows(
                        EntityAlreadyExistsException.class,
                        () ->
                                endorsementService.updateEndorsementStatus(
                                        endorsementId, EndorsementStatus.APPROVED.name()));
        assertEquals("Endorsement is already updated. Cannot modify status", exception.getMessage());
        verify(endorsementRepository, never()).save(any(EndorsementModel.class));
    }

    @Test
    @DisplayName("Return endorsement not found given an unknown endorsement id")
    public void itShouldReturnEndorsementNotFoundGivenUnknownEndorsementId() {
        setupUpdateEndorsementTests(true);

        UUID nonExistentEndorsementId = UUID.randomUUID();
        String status = EndorsementStatus.APPROVED.name();

        when(endorsementRepository.findById(nonExistentEndorsementId)).thenReturn(Optional.empty());

        NoEntityException exception =
                assertThrows(
                        NoEntityException.class,
                        () -> endorsementService.updateEndorsementStatus(nonExistentEndorsementId, status));
        assertEquals(
                "No endorsement with id " + nonExistentEndorsementId + " was found",
                exception.getMessage());
        verify(endorsementRepository, never()).save(any(EndorsementModel.class));
    }

    @Test
    @DisplayName(
            "Update endorsement status given a valid endorsement id and status is approved or rejected")
    public void itShouldUpdateEndorsementStatusGivenEndorsementIdAndStatusApprovedOrRejected() {
        setupUpdateEndorsementTests(true);

        UUID endorseeId = UUID.randomUUID();
        UUID skillId = UUID.randomUUID();
        UUID endorsementId = UUID.randomUUID();
        EndorsementStatus status = EndorsementStatus.APPROVED;

        SkillModel mockSkill = SkillModel.builder().id(skillId).build();
        EndorsementModel mockEndorsement =
                EndorsementModel.builder()
                        .id(endorsementId)
                        .status(EndorsementStatus.PENDING)
                        .endorseeId(endorseeId)
                        .skill(mockSkill)
                        .build();
        mockEndorsement.setCreatedAt(Instant.now());
        mockEndorsement.setUpdatedAt(Instant.now());
        mockEndorsement.setCreatedBy(endorseeId);
        mockEndorsement.setUpdatedBy(endorseeId);

        when(endorsementRepository.findById(endorsementId)).thenReturn(Optional.of(mockEndorsement));

        GenericResponse<Void> result =
                endorsementService.updateEndorsementStatus(endorsementId, status.name());
        assertEquals("Successfully updated endorsement status", result.getMessage());

        verify(endorsementRepository, times(1)).save(any(EndorsementModel.class));

        EndorsementModel updatedMockEndorsement =
                EndorsementModel.builder()
                        .id(endorsementId)
                        .endorseeId(endorseeId)
                        .skill(mockSkill)
                        .status(EndorsementStatus.APPROVED)
                        .build();
        mockEndorsement.setCreatedAt(Instant.now());
        mockEndorsement.setUpdatedAt(Instant.now());
        mockEndorsement.setCreatedBy(endorseeId);
        mockEndorsement.setUpdatedBy(endorseeId);

        when(endorsementRepository.findById(endorsementId))
                .thenReturn(Optional.of(updatedMockEndorsement));
        Optional<EndorsementModel> updatedEndorsement = endorsementRepository.findById(endorsementId);
        assertTrue(updatedEndorsement.isPresent());
        assertEquals(EndorsementStatus.APPROVED, updatedEndorsement.get().getStatus());
    }
}
