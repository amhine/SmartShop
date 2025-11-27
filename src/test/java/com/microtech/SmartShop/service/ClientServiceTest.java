package com.microtech.SmartShop.service;

import com.microtech.SmartShop.entity.Client;
import com.microtech.SmartShop.entity.enums.CustomerTier;
import com.microtech.SmartShop.entity.enums.Role;
import com.microtech.SmartShop.exception.EmailAlreadyUsedException;
import com.microtech.SmartShop.repository.ClientRepository;
import com.microtech.SmartShop.repository.UserRepository;
import com.microtech.SmartShop.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientServiceImpl clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateClient_Success() {
        Client client = new Client();
        client.setUsername("nihad");
        client.setEmail("nihad@gmail.com");
        client.setPassword("123");

        when(clientRepository.existsByEmail("nihad@gmail.com")).thenReturn(false);
        when(userRepository.findByUsername("nihad")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123")).thenReturn("hashed123");
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArgument(0));

        Client saved = clientService.createClient(client);

        assertEquals("hashed123", saved.getPassword());
        assertEquals(Role.Client, saved.getRole());
        assertEquals(CustomerTier.Basic, saved.getCustomer());
    }

    @Test
    void testCreateClient_EmailAlreadyUsed() {
        Client client = new Client();
        client.setEmail("exist@gmail.com");

        when(clientRepository.existsByEmail("exist@gmail.com")).thenReturn(true);

        assertThrows(EmailAlreadyUsedException.class, () -> clientService.createClient(client));
    }

    @Test
    void testCreateClient_UsernameAlreadyUsed() {
        Client client = new Client();
        client.setUsername("nihad");
        client.setEmail("new@gmail.com");

        when(clientRepository.existsByEmail("new@gmail.com")).thenReturn(false);
        when(userRepository.findByUsername("nihad")).thenReturn(Optional.of(new Client()));

        assertThrows(RuntimeException.class, () -> clientService.createClient(client));
    }

    @Test
    void testCreateClient_PasswordEncoded() {
        Client client = new Client();
        client.setUsername("newuser");
        client.setEmail("user@gmail.com");
        client.setPassword("pass");

        when(clientRepository.existsByEmail(client.getEmail())).thenReturn(false);
        when(userRepository.findByUsername(client.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArgument(0));

        Client saved = clientService.createClient(client);

        assertEquals("encodedPass", saved.getPassword());
    }
}
