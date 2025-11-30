package com.microtech.SmartShop.service.impl;

import com.microtech.SmartShop.dto.ClientCreateDto;
import com.microtech.SmartShop.dto.ClientDTO;
import com.microtech.SmartShop.dto.CommandeDTO;
import com.microtech.SmartShop.entity.Client;
import com.microtech.SmartShop.entity.enums.CustomerTier;
import com.microtech.SmartShop.exception.ClientAlreadyDeletedException;
import com.microtech.SmartShop.exception.ClientNotFoundException;
import com.microtech.SmartShop.mapper.ClientMapper;
import com.microtech.SmartShop.mapper.CommandeMapper;
import com.microtech.SmartShop.repository.ClientRepository;
import com.microtech.SmartShop.repository.CommandeRepository;
import com.microtech.SmartShop.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final CommandeRepository commandeRepository;
    private final ClientMapper clientMapper;
    private final CommandeMapper commandeMapper;

    @Override
    public ClientDTO createClient(ClientCreateDto dto) {
        Client client = clientMapper.toEntity(dto);
        String hashedPassword = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt());
        client.setPassword(hashedPassword);
        return clientMapper.toDto(clientRepository.save(client));
    }

    @Override
    public ClientDTO getClient(Long id) {
        return clientRepository.findById(id)
                .map(clientMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    @Override
    public Client getClientEntity(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    @Override
    public ClientDTO updateClient(Long id, ClientCreateDto dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        client.setNom(dto.getNom());
        client.setEmail(dto.getEmail());
        return clientMapper.toDto(clientRepository.save(client));
    }

    private void calculateTier(Client client) {
        int orders = client.getTotalOrders();
        BigDecimal spent = client.getTotalSpent();
        if (orders >= 20 || spent.compareTo(BigDecimal.valueOf(15000)) >= 0) {
            client.setCustomer(CustomerTier.Platinum);
        } else if (orders >= 10 || spent.compareTo(BigDecimal.valueOf(5000)) >= 0) {
            client.setCustomer(CustomerTier.Gold);
        } else if (orders >= 3 || spent.compareTo(BigDecimal.valueOf(1000)) >= 0) {
            client.setCustomer(CustomerTier.Silver);
        } else {
            client.setCustomer(CustomerTier.Basic);
        }
    }

    @Override
    public void recalculateClientTier(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        calculateTier(client);
        clientRepository.save(client);
    }


    @Override
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client introuvable"));

        if (Boolean.TRUE.equals(client.getDeleted())) {
            throw new ClientAlreadyDeletedException("Client déjà supprimé");
        }

        client.setDeleted(true);
        clientRepository.save(client);
    }


    @Override
    public List<CommandeDTO> getCommandes(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        return commandeRepository.findByClient(client)
                .stream()
                .map(commandeMapper::toDto)
                .toList();
    }


}
