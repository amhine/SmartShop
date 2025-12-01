package com.microtech.SmartShop.controller;

import com.microtech.SmartShop.dto.PaymentCreateDto;
import com.microtech.SmartShop.dto.PaymentDTO;
import com.microtech.SmartShop.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payements")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/{commandeId}")
    public ResponseEntity<PaymentDTO> addPayment(
            @PathVariable Long commandeId,
            @RequestBody @Valid PaymentCreateDto dto) {

        PaymentDTO payment = paymentService.addPayment(commandeId, dto);
        return ResponseEntity.ok(payment);
    }
}
