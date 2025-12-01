package Controller;

import com.microtech.SmartShop.controller.ClientController;
import com.microtech.SmartShop.dto.ClientCreateDto;
import com.microtech.SmartShop.dto.ClientDTO;
import com.microtech.SmartShop.entity.Client;
import com.microtech.SmartShop.entity.User;
import com.microtech.SmartShop.entity.enums.Role;
import com.microtech.SmartShop.exception.AccessDeniedException;
import com.microtech.SmartShop.service.ClientService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateClient_AsAdmin_Success() {
        User admin = new Client();
        admin.setRole(Role.Admin);
        when(session.getAttribute("user")).thenReturn(admin);

        ClientCreateDto dto = new ClientCreateDto();
        ClientDTO clientDTO = new ClientDTO();
        when(clientService.createClient(dto)).thenReturn(clientDTO);

        ResponseEntity<ClientDTO> response = clientController.createClient(dto, session);

        Assertions.assertEquals(200, response.getStatusCode().value());
        verify(clientService, times(1)).createClient(dto);
    }

    @Test
    void testCreateClient_AsClient_AccessDenied() {
        User client = new Client();
        client.setRole(Role.Client);
        when(session.getAttribute("user")).thenReturn(client);

        ClientCreateDto dto = new ClientCreateDto();

        assertThrows(AccessDeniedException.class, () -> {
            clientController.createClient(dto, session);
        });
    }
    @Test
    void testGetClientById_AsClientOwnData() {
        User client = new Client();
        client.setRole(Role.Client);
        client.setId(1L);
        when(session.getAttribute("user")).thenReturn(client);

        ClientDTO clientDTO = new ClientDTO();
        when(clientService.getClient(1L)).thenReturn(clientDTO);

        ResponseEntity<ClientDTO> response = clientController.getClientById(1L, session);
        Assertions.assertEquals(200, response.getStatusCode().value());
    }
    @Test
    void testGetClientById_AsClientOtherData_AccessDenied() {
        User client = new Client();
        client.setRole(Role.Client);
        client.setId(1L);
        when(session.getAttribute("user")).thenReturn(client);

        assertThrows(AccessDeniedException.class, () -> {
            clientController.getClientById(2L, session);
        });
    }
}
