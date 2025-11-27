package com.microtech.SmartShop.controller;

import com.microtech.SmartShop.entity.Client;
import com.microtech.SmartShop.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @PostMapping("/create")
    public Client create(@RequestBody Client client) {
        return clientService.createClient(client);
    }
}
