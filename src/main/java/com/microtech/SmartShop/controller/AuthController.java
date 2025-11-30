package com.microtech.SmartShop.controller;

import com.microtech.SmartShop.dto.LoginRequest;
import com.microtech.SmartShop.dto.UserDTO;
import com.microtech.SmartShop.entity.User;
import com.microtech.SmartShop.repository.UserRepository;
import com.microtech.SmartShop.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        User user = authService.login(request, session);
        return ResponseEntity.ok(new UserDTO(user));
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        if (session == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST).build();
        }

        session.invalidate();
        return ResponseEntity.ok().build();
    }

}
