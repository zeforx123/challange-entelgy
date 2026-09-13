package com.entelgy.challange.service;

import com.entelgy.challange.dto.PaymentRequest;
import com.entelgy.challange.entity.Credit;
import com.entelgy.challange.entity.Payment;
import com.entelgy.challange.exception.BusinessException;
import com.entelgy.challange.repository.CreditRepository;
import com.entelgy.challange.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CreditRepository creditRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Credit credit;

    @BeforeEach
    void setUp() {
        credit = new Credit();

        credit.setId("CR-001");
        credit.setTitle("Crédito Personal");
        credit.setTotalAmount(new BigDecimal("1000"));
        credit.setPaidAmount(new BigDecimal("200"));
        credit.setOutstandingDebt(new BigDecimal("800"));
        credit.setStatus("ACTIVE");
    }

    @Test
    void shouldCreatePaymentSuccessfully() {

        PaymentRequest request = new PaymentRequest();
        request.setAmount(new BigDecimal("300"));

        when(creditRepository.findById("CR-001"))
                .thenReturn(Optional.of(credit));

        Payment payment = new Payment();
        payment.setCreditId("CR-001");
        payment.setAmount(new BigDecimal("300"));
        payment.setStatus("COMPLETED");

        when(paymentRepository.save(any(Payment.class)))
                .thenReturn(payment);

        Payment result = paymentService.createPayment("CR-001", request);

        assertNotNull(result);
        assertEquals("CR-001", result.getCreditId());
        assertEquals(
                new BigDecimal("300"),
                result.getAmount()
        );
        assertEquals("COMPLETED", result.getStatus());

        assertEquals(
                new BigDecimal("500"),
                credit.getPaidAmount()
        );

        assertEquals(
                new BigDecimal("500"),
                credit.getOutstandingDebt()
        );

        verify(creditRepository).save(credit);
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void shouldRejectPaymentGreaterThanOutstandingDebt() {

        PaymentRequest request = new PaymentRequest();
        request.setAmount(new BigDecimal("900"));

        when(creditRepository.findById("CR-001"))
                .thenReturn(Optional.of(credit));

        assertThrows(
                BusinessException.class,
                () -> paymentService.createPayment("CR-001", request)
        );

        verify(paymentRepository, never())
                .save(any(Payment.class));

        verify(creditRepository, never())
                .save(any(Credit.class));
    }

    @Test
    void shouldRejectPaymentWhenCreditDoesNotExist() {

        PaymentRequest request = new PaymentRequest();
        request.setAmount(new BigDecimal("300"));

        when(creditRepository.findById("CR-999"))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> paymentService.createPayment("CR-999", request)
        );

        verify(paymentRepository, never())
                .save(any(Payment.class));

        verify(creditRepository, never())
                .save(any(Credit.class));
    }

    @Test
    void shouldMarkCreditAsPaidWhenPaymentCoversOutstandingDebt() {

        PaymentRequest request = new PaymentRequest();
        request.setAmount(new BigDecimal("800"));

        when(creditRepository.findById("CR-001"))
                .thenReturn(Optional.of(credit));

        Payment payment = new Payment();
        payment.setCreditId("CR-001");
        payment.setAmount(new BigDecimal("800"));
        payment.setStatus("COMPLETED");

        when(paymentRepository.save(any(Payment.class)))
                .thenReturn(payment);

        Payment result = paymentService.createPayment("CR-001", request);

        assertNotNull(result);

        assertEquals(
                BigDecimal.ZERO,
                credit.getOutstandingDebt()
        );

        assertEquals(
                "PAID",
                credit.getStatus()
        );

        verify(creditRepository).save(credit);
        verify(paymentRepository).save(any(Payment.class));
    }
}