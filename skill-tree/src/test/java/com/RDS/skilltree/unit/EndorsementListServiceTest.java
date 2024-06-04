package com.RDS.skilltree.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.RDS.skilltree.Endorsement.EndorsementModel;
import com.RDS.skilltree.Endorsement.EndorsementRepository;
import com.RDS.skilltree.EndorsementList.*;
import com.RDS.skilltree.Exceptions.NoEntityException;
import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class EndorsementListServiceTest {

    @Mock private EndorsementListRepository endorsementListRepository;

    @Mock private EndorsementRepository endorsementRepository;

    @Mock private UserRepository userRepository;

    @InjectMocks private EndorsementListService endorsementListService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEndorsementListEntry() {
        // Mock data
        UUID endorserId = UUID.randomUUID();
        UUID endorsementId = UUID.randomUUID();
        EndorsementListDRO endorsementListDRO = new EndorsementListDRO();
        endorsementListDRO.setEndorserId(endorserId);
        endorsementListDRO.setEndorsementId(endorsementId);
        endorsementListDRO.setDescription("Test Description");
        endorsementListDRO.setType(EndorsementType.POSITIVE);

        UserModel mockUser = new UserModel();
        mockUser.setId(endorserId);

        EndorsementModel mockEndorsement = EndorsementModel.builder().id(endorsementId).build();

        // Mock the repository behavior
        when(userRepository.findById(endorserId)).thenReturn(Optional.of(mockUser));
        when(endorsementRepository.findById(endorsementId)).thenReturn(Optional.of(mockEndorsement));

        // Call the service method
        EndorsementListModel result =
                endorsementListService.createEndorsementListEntry(endorsementListDRO);

        // Verify the interactions
        verify(endorsementListRepository, times(1)).save(any(EndorsementListModel.class));

        // Assertions
        assertNotNull(result);
        assertEquals(endorserId, result.getEndorserId());
        assertEquals(endorsementId, result.getEndorsement().getId());
        assertEquals("Test Description", result.getDescription());
        assertEquals(EndorsementType.POSITIVE, result.getType());
    }

    @Test
    void testCreateEndorsementListEntryWithInvalidEndorsement() {
        UUID endorserId = UUID.randomUUID();
        UUID endorsementId = UUID.randomUUID();
        EndorsementListDRO endorsementListDRO = new EndorsementListDRO();
        endorsementListDRO.setEndorserId(endorserId);
        endorsementListDRO.setEndorsementId(endorsementId);

        UserModel mockUser = new UserModel();
        mockUser.setId(endorserId);

        // Mock the repository behavior for an invalid endorsement
        when(userRepository.findById(endorserId)).thenReturn(Optional.of(mockUser));
        when(endorsementRepository.findById(endorsementId)).thenReturn(Optional.empty());

        // Assert that a NoEntityException is thrown
        NoEntityException exception =
                assertThrows(
                        NoEntityException.class,
                        () -> endorsementListService.createEndorsementListEntry(endorsementListDRO));
        assertEquals("Endorsement with id:" + endorsementId + " not found", exception.getMessage());

        // Verify that save method is not called
        verify(endorsementListRepository, never()).save(any(EndorsementListModel.class));
    }
}
