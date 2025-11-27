package com.microtech.SmartShop.service;

import com.microtech.SmartShop.dto.UserDTO;
import com.microtech.SmartShop.entity.Client;
import com.microtech.SmartShop.entity.User;
import com.microtech.SmartShop.exception.AuthException;
import com.microtech.SmartShop.mapper.UserMapper;
import com.microtech.SmartShop.repository.UserRepository;
import com.microtech.SmartShop.service.impl.AuthServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccess() {
        UserDTO loginDTO = new UserDTO();
        loginDTO.setUsername("nihad");
        loginDTO.setPassword("1234");

        User user = new Client();
        user.setId(1L);
        user.setUsername("nihad");
        user.setPassword("$2a$10$encodedpassword");

        UserDTO mappedDTO = new UserDTO();
        mappedDTO.setId(1L);
        mappedDTO.setUsername("nihad");

        when(userRepository.findByUsername("nihad")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("1234", user.getPassword())).thenReturn(true);
        when(userMapper.toDto(user)).thenReturn(mappedDTO);

        UserDTO result = authService.login(loginDTO, session);

        assertNotNull(result);
        assertEquals("nihad", result.getUsername());

        verify(session, times(1)).setAttribute("USER_ID", 1L);
    }

    @Test
    void testLoginUserNotFound() {
        UserDTO loginDTO = new UserDTO();
        loginDTO.setUsername("unknown");

        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.login(loginDTO, session);
        });

        assertEquals("utilisateur non trouve", exception.getMessage());

        verify(session, never()).setAttribute(any(), any());
    }

    @Test
    void testLoginWrongPassword() {
        UserDTO loginDTO = new UserDTO();
        loginDTO.setUsername("nihad");
        loginDTO.setPassword("wrongpass");

        User user = new Client();
        user.setUsername("nihad");
        user.setPassword("$2a$10$encodedpassword");

        when(userRepository.findByUsername("nihad")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpass", user.getPassword())).thenReturn(false);

        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.login(loginDTO, session);
        });

        assertEquals("Mot de passe incorrect", exception.getMessage());

        verify(session, never()).setAttribute(any(), any());
    }
}
