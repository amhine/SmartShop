package com.microtech.SmartShop.service.impl;

import com.microtech.SmartShop.dto.ClientDTO;
import com.microtech.SmartShop.dto.CommandeDTO;
import com.microtech.SmartShop.entity.Client;
import com.microtech.SmartShop.entity.Commande;
import com.microtech.SmartShop.entity.enums.CustomerTier;
import com.microtech.SmartShop.entity.enums.Role;
import com.microtech.SmartShop.exception.ClientAlreadyDeletedException;
import com.microtech.SmartShop.exception.ClientNotFoundException;
import com.microtech.SmartShop.exception.EmailAlreadyUsedException;
import com.microtech.SmartShop.mapper.ClientMapper;
import com.microtech.SmartShop.mapper.CommandeMapper;
import com.microtech.SmartShop.repository.ClientRepository;
import com.microtech.SmartShop.repository.CommandeRepository;
import com.microtech.SmartShop.repository.UserRepository;
import com.microtech.SmartShop.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    private CommandeRepository commandeRepository;

    @Override
    public Client createClient(Client client) {
        if (userRepository.findByUsername(client.getUsername()).isPresent()) {
            throw new RuntimeException("Username déjà utilisé !");
        }

        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new EmailAlreadyUsedException("Email déjà utilisé !"); // <-- corrigé
        }

        client.setPassword(passwordEncoder.encode(client.getPassword()));

        client.setRole(Role.Client);

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

        if (Boolean.TRUE.equals(client.getDeleted())) {
            throw new ClientAlreadyDeletedException("Client déjà supprimé");
        }


        client.setDeleted(true);
        clientRepository.save(client);
    }
    public List<CommandeDTO> getCommandes(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        List<Commande> commandes = commandeRepository.findByClient(client);

        return commandes.stream()
                .map(CommandeMapper::toDTO)
                .toList();
    }
}
