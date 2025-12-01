package com.microtech.SmartShop.service;

import com.microtech.SmartShop.dto.PaymentCreateDto;
import com.microtech.SmartShop.dto.PaymentDTO;

public interface PaymentService {
    PaymentDTO addPayment(Long commandeId, PaymentCreateDto dto);
}
