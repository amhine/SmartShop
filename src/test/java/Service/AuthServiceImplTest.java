package Service;

import com.microtech.SmartShop.dto.LoginRequest;
import com.microtech.SmartShop.entity.Admin;
import com.microtech.SmartShop.entity.User;
import com.microtech.SmartShop.exception.AuthException;
import com.microtech.SmartShop.repository.UserRepository;
import com.microtech.SmartShop.service.impl.AuthServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_Success() {
        User user = new Admin();
        user.setUsername("admin");
        user.setPassword("admin123");

        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("admin123");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        User result = authService.login(request, session);

        assertEquals(user.getUsername(), result.getUsername());
        verify(session, times(1)).setAttribute("user", user);
    }

    @Test
    void login_UserNotFound() {
        LoginRequest request = new LoginRequest();
        request.setUsername("unknown");
        request.setPassword("123");

        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(AuthException.class, () -> authService.login(request, session));
    }

    @Test
    void login_WrongPassword() {
        User user = new Admin();
        user.setUsername("admin");
        user.setPassword("admin123");

        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("wrong");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        assertThrows(AuthException.class, () -> authService.login(request, session));
    }

    @Test
    void logout_Success() {
        authService.logout(session);
        verify(session, times(1)).invalidate();
    }
}
