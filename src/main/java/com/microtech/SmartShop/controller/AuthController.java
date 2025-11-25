package com.microtech.SmartShop.controller;

import com.microtech.SmartShop.dto.UserDTO;
import com.microtech.SmartShop.entity.User;
import com.microtech.SmartShop.mapper.UserMapper;
import com.microtech.SmartShop.repository.UserRepository;
import com.microtech.SmartShop.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public UserDTO login(@RequestBody UserDTO loginDTO, HttpSession session){
        return authService.login(loginDTO, session);
    }


    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "Déconnexion réussie !";
    }
}
