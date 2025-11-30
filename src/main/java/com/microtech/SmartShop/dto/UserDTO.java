package com.microtech.SmartShop.dto;

import com.microtech.SmartShop.entity.User;
import com.microtech.SmartShop.entity.enums.Role;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private Role role;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
    }
}
