package com.microtech.SmartShop.config;

import com.microtech.SmartShop.entity.Admin;
import com.microtech.SmartShop.entity.enums.Role;
import com.microtech.SmartShop.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.findByUsername("nihad").isEmpty()) {

            String envPassword = System.getenv("ADMIN_PASSWORD");
            if (envPassword == null || envPassword.isBlank()) {
                throw new IllegalStateException("ADMIN_PASSWORD n'est pas defini !");
            }

            Admin admin = new Admin();
            admin.setUsername("nihad");

            String hashedPassword = BCrypt.hashpw(envPassword, BCrypt.gensalt());
            admin.setPassword(hashedPassword);

            admin.setRole(Role.Admin);

            userRepository.save(admin);

            System.out.println("Admin seede ");
        }
    }
}
