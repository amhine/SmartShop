package com.microtech.SmartShop.service;

import com.microtech.SmartShop.dto.UserDTO;
import jakarta.servlet.http.HttpSession;

public interface AuthService {
    UserDTO login(UserDTO loginDTO, HttpSession session);
    void logout(HttpSession session);
}
