package com.microtech.SmartShop.controller;

import com.microtech.SmartShop.dto.ClientDTO;
import com.microtech.SmartShop.entity.Client;
import com.microtech.SmartShop.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @PostMapping("/create")
    public Client create(@Valid @RequestBody Client client) {
        return clientService.createClient(client);
    }

    @GetMapping("/{id}")
    public ClientDTO getClientById(@PathVariable Long id){
        return clientService.findById(id);
    }

}
