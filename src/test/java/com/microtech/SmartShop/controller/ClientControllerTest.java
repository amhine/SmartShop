package com.microtech.SmartShop.controller;

import com.microtech.SmartShop.dto.ClientDTO;
import com.microtech.SmartShop.entity.Client;
import com.microtech.SmartShop.entity.User;
import com.microtech.SmartShop.entity.enums.Role;
import com.microtech.SmartShop.exception.AccessDeniedException;
import com.microtech.SmartShop.service.ClientService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientControllerTest {

    @InjectMocks
    private ClientController clientController;

    @Mock
    private ClientService clientService;

    @Mock
    private HttpSession session;

    private User adminUser;
    private User clientUser;
    private Client testClient;
    private ClientDTO testClientDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        adminUser = new Client();
        adminUser.setId(1L);
        adminUser.setRole(Role.Admin);

        clientUser = new Client();
        clientUser.setId(2L);
        clientUser.setRole(Role.Client);

        testClient = new Client();
        testClient.setId(2L);
        testClient.setNom("Amine");

        testClientDTO = new ClientDTO();
        testClientDTO.setId(2L);
        testClientDTO.setNom("Amine");
    }


    @Test
    void testCreateClient_AsAdmin_Success() {
        when(session.getAttribute("user")).thenReturn(adminUser);
        when(clientService.createClient(any(Client.class))).thenReturn(testClient);

        Client result = clientController.create(testClient, session);

        assertNotNull(result);
        assertEquals("Amine", result.getNom());
        verify(clientService, times(1)).createClient(testClient);
    }

    @Test
    void testCreateClient_AsClient_ThrowsAccessDenied() {
        when(session.getAttribute("user")).thenReturn(clientUser);

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> clientController.create(testClient, session)
        );

        assertEquals("Vous n'avez pas la permission de créer un client", exception.getMessage());
        verify(clientService, never()).createClient(any(Client.class));
    }

    @Test
    void testGetClientById_AsAdmin_Success() {
        when(session.getAttribute("user")).thenReturn(adminUser);
        when(clientService.findById(2L)).thenReturn(testClientDTO);

        ClientDTO result = clientController.getClientById(2L, session);

        assertNotNull(result);
        assertEquals("Amine", result.getNom());
    }

    @Test
    void testGetClientById_AsClientOwn_Success() {
        when(session.getAttribute("user")).thenReturn(clientUser);
        when(clientService.findById(2L)).thenReturn(testClientDTO);

        clientUser.setId(2L);

        ClientDTO result = clientController.getClientById(2L, session);

        assertNotNull(result);
        assertEquals("Amine", result.getNom());
    }

    @Test
    void testGetClientById_AsClientOther_ThrowsAccessDenied() {
        when(session.getAttribute("user")).thenReturn(clientUser);
        clientUser.setId(3L);
        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> clientController.getClientById(2L, session)
        );

        assertEquals("Vous ne pouvez consulter que vos propres données", exception.getMessage());
    }

    @Test
    void testDeleteClient_AsAdmin_Success() {
        when(session.getAttribute("user")).thenReturn(adminUser);

        ResponseEntity<Void> response = clientController.deleteClient(2L, session);

        assertEquals(200, response.getStatusCode().value());
        verify(clientService, times(1)).deleteClient(2L);
    }

    @Test
    void testDeleteClient_AsClient_ThrowsAccessDenied() {
        when(session.getAttribute("user")).thenReturn(clientUser);

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> clientController.deleteClient(2L, session)
        );

        assertEquals("Vous n'avez pas la permission de supprimer un client", exception.getMessage());
        verify(clientService, never()).deleteClient(anyLong());
    }
}
