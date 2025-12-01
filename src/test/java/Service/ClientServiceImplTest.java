package Service;
import com.microtech.SmartShop.dto.ClientCreateDto;
import com.microtech.SmartShop.dto.ClientDTO;
import com.microtech.SmartShop.entity.Client;
import com.microtech.SmartShop.mapper.ClientMapper;
import com.microtech.SmartShop.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
public class ClientServiceImplTest {
    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private com.microtech.SmartShop.service.impl.ClientServiceImpl clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void createClient_Success() {
        ClientCreateDto dto = new ClientCreateDto();
        dto.setNom("Nihad");
        dto.setEmail("test@example.com");
        dto.setPassword("123");

        Client clientEntity = new Client();
        ClientDTO clientDTO = new ClientDTO();

        when(clientMapper.toEntity(dto)).thenReturn(clientEntity);
        when(clientRepository.save(clientEntity)).thenReturn(clientEntity);
        when(clientMapper.toDto(clientEntity)).thenReturn(clientDTO);

        ClientDTO result = clientService.createClient(dto);
        assertNotNull(result);
        verify(clientRepository, times(1)).save(clientEntity);
    }
    @Test
    void getClient_Success() {
        Client client = new Client();
        ClientDTO clientDTO = new ClientDTO();
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientMapper.toDto(client)).thenReturn(clientDTO);

        ClientDTO result = clientService.getClient(1L);
        assertEquals(clientDTO, result);
    }

    @Test
    void getClient_NotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> clientService.getClient(1L));
    }
}
