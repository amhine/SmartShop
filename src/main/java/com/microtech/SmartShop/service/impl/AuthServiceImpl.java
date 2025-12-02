package com.microtech.SmartShop.service.impl;

import com.microtech.SmartShop.dto.LoginRequest;
import com.microtech.SmartShop.entity.User;
import com.microtech.SmartShop.exception.AuthException;
import com.microtech.SmartShop.repository.UserRepository;
import com.microtech.SmartShop.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public User login(LoginRequest request, HttpSession session) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthException("Utilisateur non trouvé"));

        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new AuthException("Mot de passe incorrect");
        }

        session.setAttribute("user", user);
        return user;
    }


    @Override
    public void logout(HttpSession session) {
        session.invalidate();
    }
}