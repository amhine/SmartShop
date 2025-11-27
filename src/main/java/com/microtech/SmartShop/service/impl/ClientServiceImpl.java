package com.microtech.SmartShop.service.impl;

import com.microtech.SmartShop.entity.Client;
import com.microtech.SmartShop.entity.enums.CustomerTier;
import com.microtech.SmartShop.entity.enums.Role;
import com.microtech.SmartShop.repository.ClientRepository;
import com.microtech.SmartShop.repository.UserRepository;
import com.microtech.SmartShop.service.AuthService;
import com.microtech.SmartShop.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Client createClient(Client client){
        if (userRepository.findByUsername(client.getUsername()).isPresent()) {
            throw new RuntimeException("Username déjà utilisé !");
        }
        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new RuntimeException("Email déjà utilisé !");
        }
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        client.setRole(Role.Client);
        if (client.getCustomer() == null) {
            client.setCustomer(CustomerTier.Basic);
        }
        return clientRepository.save(client);

    }

}
