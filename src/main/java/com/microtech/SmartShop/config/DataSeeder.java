package com.microtech.SmartShop.config;

import com.microtech.SmartShop.entity.Admin;
import com.microtech.SmartShop.entity.User;
import com.microtech.SmartShop.entity.enums.Role;
import com.microtech.SmartShop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new Admin();
            admin.setUsername("nihad");
            admin.setPassword(passwordEncoder.encode("nihad123"));
            admin.setRole(Role.Admin);
            userRepository.save(admin);

            System.out.println("Admin par défaut créé : username=admin, password=admin123");
        } else {
            System.out.println("Admin déjà existant, seed ignoré");
        }

    }
}
