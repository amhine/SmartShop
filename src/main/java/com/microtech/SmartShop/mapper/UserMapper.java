package com.microtech.SmartShop.mapper;

import com.microtech.SmartShop.dto.UserDTO;
import com.microtech.SmartShop.entity.Admin;
import com.microtech.SmartShop.entity.Client;
import com.microtech.SmartShop.entity.enums.Role;
import com.microtech.SmartShop.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDto(User user){
        if(user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        return dto;
    }

    public User toEntity(UserDTO dto){
        if(dto == null) return null;

        User user;
        if (dto.getRole() == Role.Client) {
            user = new Client();
        } else {
            user = new Admin();
        }

        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        return user;
    }
}
