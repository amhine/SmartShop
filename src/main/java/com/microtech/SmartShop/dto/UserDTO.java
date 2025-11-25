package com.microtech.SmartShop.dto;

import com.microtech.SmartShop.entity.enums.Role;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private Role role;
}
