package com.microtech.SmartShop.controller;

import com.microtech.SmartShop.dto.UserDTO;
import com.microtech.SmartShop.entity.User;
import com.microtech.SmartShop.mapper.UserMapper;
import com.microtech.SmartShop.repository.UserRepository;
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
    private  UserMapper userMapper;

    @PostMapping("/login")
    public String login(@RequestBody UserDTO loginDTO, HttpSession session){
        Optional<User> useropt = userRepository.findByUsername(loginDTO.getUsername());
        if(useropt.isEmpty()){
            throw new RuntimeException("Utilisateur non trouvé");
        }
        User user = useropt.get();
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            throw new RuntimeException("Password incorrect");
        }
        session.setAttribute("user", user);
        return "Connexion réussie pour l'utilisateur " + user.getUsername();
    }
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "Déconnexion réussie !";
    }
}
