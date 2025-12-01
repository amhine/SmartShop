package Controller;
import com.microtech.SmartShop.controller.AuthController;
import com.microtech.SmartShop.dto.LoginRequest;
import com.microtech.SmartShop.dto.UserDTO;
import com.microtech.SmartShop.entity.Admin;
import com.microtech.SmartShop.entity.User;
import com.microtech.SmartShop.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static com.microtech.SmartShop.entity.enums.Role.Admin;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
public class AuthControllerTest {
    @Mock
    private AuthService authService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testLogin_Success() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("admin123");

        User user = new Admin();
        user.setId(1L);
        user.setUsername("admin");

        when(authService.login(request, session)).thenReturn(user);

        ResponseEntity<UserDTO> response = authController.login(request, session);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("admin", response.getBody().getUsername());
        verify(authService, times(1)).login(request, session);
    }
    @Test
    void testLogout_Success() {
        ResponseEntity<Void> response = authController.logout(session);
        verify(session, times(1)).invalidate();
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testLogout_NoSession() {
        ResponseEntity<Void> response = authController.logout(null);
        assertEquals(400, response.getStatusCode().value());
    }
}
