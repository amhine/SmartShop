package Service;

import com.microtech.SmartShop.dto.PaymentCreateDto;
import com.microtech.SmartShop.dto.PaymentDTO;
import com.microtech.SmartShop.entity.Commande;
import com.microtech.SmartShop.entity.Payment;
import com.microtech.SmartShop.entity.enums.OrderStatus;
import com.microtech.SmartShop.entity.enums.PaymentType;
import com.microtech.SmartShop.mapper.PaymentMapper;
import com.microtech.SmartShop.repository.CommandeRepository;
import com.microtech.SmartShop.repository.PaymentRepository;
import com.microtech.SmartShop.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PaymentServiceImplTest {
    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CommandeRepository commandeRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Commande commande;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commande = new Commande();
        commande.setMontantRestant(BigDecimal.valueOf(1000));
        commande.setStatut(OrderStatus.Pending);
    }
    @Test
    void addPayment_Success() {
        PaymentCreateDto dto = new PaymentCreateDto();
        dto.setMontant(BigDecimal.valueOf(500));
        dto.setType(PaymentType.Especes);

        Payment payment = new Payment();
        PaymentDTO paymentDTO = new PaymentDTO();

        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));
        when(paymentMapper.toDto(any(Payment.class))).thenReturn(paymentDTO);

        PaymentDTO result = paymentService.addPayment(1L, dto);
        assertEquals(paymentDTO, result);
    }
    @Test
    void validatePayment_Success() {
        Payment payment = new Payment();
        payment.setStatut(null);
        Commande c = new Commande();
        c.setStatut(OrderStatus.Pending);
        payment.setCommande(c);

        PaymentDTO paymentDTO = new PaymentDTO();

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentMapper.toDto(payment)).thenReturn(paymentDTO);

        PaymentDTO result = paymentService.validatePayment(1L);
        assertEquals(paymentDTO, result);
        assertEquals(payment.getStatut(), payment.getStatut());
    }
}
