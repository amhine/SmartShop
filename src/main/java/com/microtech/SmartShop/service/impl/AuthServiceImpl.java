package com.microtech.SmartShop.service.impl;

import com.microtech.SmartShop.dto.UserDTO;
import com.microtech.SmartShop.entity.User;
import com.microtech.SmartShop.mapper.UserMapper;
import com.microtech.SmartShop.repository.UserRepository;
import com.microtech.SmartShop.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDTO login(UserDTO loginDTO , HttpSession session){
        Optional<User> userOpt = userRepository.findByUsername(loginDTO.getUsername());
        if(userOpt.isEmpty()){
            throw new RuntimeException("utilisateur non trouve");
        }
        User user = userOpt.get();
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        session.setAttribute("user", user);
        return userMapper.toDto(user);
    }

    @Override
    public void logout(HttpSession session) {
        session.invalidate();
    }

}
