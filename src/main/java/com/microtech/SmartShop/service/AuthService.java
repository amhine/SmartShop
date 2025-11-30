package com.microtech.SmartShop.service;

import com.microtech.SmartShop.dto.LoginRequest;
import com.microtech.SmartShop.dto.UserDTO;
import com.microtech.SmartShop.entity.User;
import jakarta.servlet.http.HttpSession;

public interface AuthService {
    User login(LoginRequest request, HttpSession session) ;

    void logout(HttpSession session);
}
