package com.microtech.SmartShop.service.impl;

import com.microtech.SmartShop.dto.ClientDTO;
import com.microtech.SmartShop.entity.Client;
import com.microtech.SmartShop.entity.enums.CustomerTier;
import com.microtech.SmartShop.entity.enums.Role;
import com.microtech.SmartShop.exception.ClientAlreadyDeletedException;
import com.microtech.SmartShop.exception.ClientNotFoundException;
import com.microtech.SmartShop.exception.EmailAlreadyUsedException;
import com.microtech.SmartShop.mapper.ClientMapper;
import com.microtech.SmartShop.repository.ClientRepository;
import com.microtech.SmartShop.repository.UserRepository;
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

    @Autowired
    private ClientMapper clientMapper;

    @Override
    public Client createClient(Client client) {
        // Vérifie si le username existe déjà
        if (userRepository.findByUsername(client.getUsername()).isPresent()) {
            throw new RuntimeException("Username déjà utilisé !");
        }

        // Vérifie si l'email existe déjà
        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new EmailAlreadyUsedException("Email déjà utilisé !"); // <-- corrigé
        }

        // Encode le mot de passe
        client.setPassword(passwordEncoder.encode(client.getPassword()));

        // Définit le rôle
        client.setRole(Role.Client);

        // Définit le niveau client si non fourni
        if (client.getCustomer() == null) {
            client.setCustomer(CustomerTier.Basic);
        }

        return clientRepository.save(client);
    }

    @Override
    public ClientDTO findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client avec id " + id + " introuvable"));
        return clientMapper.toDto(client);
    }

    @Override
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client avec id " + id + " introuvable"));

        if (client.isDeleted()) {
            throw new ClientAlreadyDeletedException("Client déjà supprimé");
        }

        client.setDeleted(true);
        clientRepository.save(client);
    }
}
