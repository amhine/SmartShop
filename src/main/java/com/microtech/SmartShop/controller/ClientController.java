package com.microtech.SmartShop.controller;

import com.microtech.SmartShop.dto.ClientCreateDto;
import com.microtech.SmartShop.dto.ClientDTO;
import com.microtech.SmartShop.dto.CommandeDTO;
import com.microtech.SmartShop.entity.User;
import com.microtech.SmartShop.entity.enums.Role;
import com.microtech.SmartShop.exception.AccessDeniedException;
import com.microtech.SmartShop.mapper.CommandeMapper;
import com.microtech.SmartShop.repository.CommandeRepository;
import com.microtech.SmartShop.service.ClientService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor

public class ClientController {

    private final ClientService clientService;
    private final CommandeRepository commandeRepository;
    private final CommandeMapper commandeMapper;

    @PostMapping("/create")
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientCreateDto dto ,HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || currentUser.getRole() != Role.Admin) {
            throw new AccessDeniedException("Vous n'avez pas la permission de créer un client");
        }
        return ResponseEntity.ok(clientService.createClient(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id, HttpSession session){
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            throw new AccessDeniedException("Non authentifié");
        }

        if (currentUser.getRole() == Role.Client && !currentUser.getId().equals(id)) {
            throw new AccessDeniedException("Vous ne pouvez consulter que vos propres données");
        }

        return ResponseEntity.ok(clientService.getClient(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || currentUser.getRole() != Role.Admin) {
            throw new AccessDeniedException("Vous n'avez pas la permission de supprimer un client");
        }
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/commande")
    public ResponseEntity<List<CommandeDTO>> getClientOrders(@PathVariable Long id) {
        return ResponseEntity.ok(commandeRepository.findByClientId(id).stream()
                .map(commandeMapper::toDto)
                .collect(Collectors.toList()));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @Valid @RequestBody ClientCreateDto dto, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            throw new AccessDeniedException("Non authentifié");
        }

        return ResponseEntity.ok(clientService.updateClient(id, dto));
    }

}
