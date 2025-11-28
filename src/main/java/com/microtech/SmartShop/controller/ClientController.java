package com.microtech.SmartShop.controller;

import com.microtech.SmartShop.dto.ClientDTO;
import com.microtech.SmartShop.dto.CommandeDTO;
import com.microtech.SmartShop.entity.Client;
import com.microtech.SmartShop.entity.User;
import com.microtech.SmartShop.entity.enums.Role;
import com.microtech.SmartShop.exception.AccessDeniedException;
import com.microtech.SmartShop.service.ClientService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/create")
    public Client create(@Valid @RequestBody Client client, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || currentUser.getRole() != Role.Admin) {
            throw new AccessDeniedException("Vous n'avez pas la permission de créer un client");
        }
        return clientService.createClient(client);
    }

    @GetMapping("/{id}")
    public ClientDTO getClientById(@PathVariable Long id, HttpSession session){
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            throw new AccessDeniedException("Non authentifié");
        }

        if (currentUser.getRole() == Role.Client && !currentUser.getId().equals(id)) {
            throw new AccessDeniedException("Vous ne pouvez consulter que vos propres données");
        }

        return clientService.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || currentUser.getRole() != Role.Admin) {
            throw new AccessDeniedException("Vous n'avez pas la permission de supprimer un client");
        }

        clientService.deleteClient(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/commande")
    public ResponseEntity<List<CommandeDTO>> getClientCommandes(@PathVariable Long id) {
        List<CommandeDTO> commandes = clientService.getCommandes(id);
        return ResponseEntity.ok(commandes);
    }
}
