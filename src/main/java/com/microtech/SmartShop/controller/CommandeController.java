package com.microtech.SmartShop.controller;

import com.microtech.SmartShop.dto.CommandeCreateDto;
import com.microtech.SmartShop.dto.CommandeDTO;
import com.microtech.SmartShop.entity.enums.OrderStatus;
import com.microtech.SmartShop.service.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;

//    @GetMapping
//    public ResponseEntity<List<CommandeDTO>> getAllCommandes() {
//        return ResponseEntity.ok(commandeService.getAllCommandes());
//    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeDTO> getCommande(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.getCommande(id));
    }

    @PostMapping
    public ResponseEntity<CommandeDTO> createCommande(@RequestBody CommandeCreateDto dto) {
        return ResponseEntity.ok(commandeService.createCommande(dto));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<CommandeDTO> confirmCommande(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.confirmCommande(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<CommandeDTO> cancelCommande(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.cancelCommande(id));
    }

//    @PutMapping("/{id}/status")
//    public ResponseEntity<CommandeDTO> updateStatus(
//            @PathVariable Long id,
//            @RequestParam OrderStatus statut
//    ) {
//        return ResponseEntity.ok(commandeService.updateStatus(id, statut));
//    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
//        commandeService.deleteCommande(id);
//        return ResponseEntity.noContent().build();
//    }
}
