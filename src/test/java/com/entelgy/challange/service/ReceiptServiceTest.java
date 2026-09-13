package com.entelgy.challange.service;

import com.entelgy.challange.entity.Payment;
import com.entelgy.challange.entity.Receipt;
import com.entelgy.challange.exception.ResourceNotFoundException;
import com.entelgy.challange.repository.PaymentRepository;
import com.entelgy.challange.repository.ReceiptRepository;
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
class ReceiptServiceTest {

    @Mock
    private ReceiptRepository receiptRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private ReceiptService receiptService;

    @Test
    void shouldReturnExistingReceipt() {

        Payment payment = new Payment();
        payment.setId("PAY-001");
        payment.setCreditId("CR-001");
        payment.setAmount(new BigDecimal("500"));
        payment.setStatus("COMPLETED");

        Receipt receipt = new Receipt();
        receipt.setId("REC-001");
        receipt.setPaymentId("PAY-001");
        receipt.setCreditId("CR-001");
        receipt.setAmount(new BigDecimal("500"));
        receipt.setStatus("PAID");

        when(paymentRepository.findById("PAY-001"))
                .thenReturn(Optional.of(payment));

        when(receiptRepository.findByPaymentId("PAY-001"))
                .thenReturn(Optional.of(receipt));

        Receipt result = receiptService.getPaymentReceipt("PAY-001");

        assertNotNull(result);
        assertEquals("REC-001", result.getId());
        assertEquals("PAY-001", result.getPaymentId());
        assertEquals(new BigDecimal("500"), result.getAmount());
        assertEquals("PAID", result.getStatus());

        verify(receiptRepository, never())
                .save(any(Receipt.class));
    }

    @Test
    void shouldCreateReceiptWhenItDoesNotExist() {

        Payment payment = new Payment();
        payment.setId("PAY-002");
        payment.setCreditId("CR-001");
        payment.setAmount(new BigDecimal("300"));
        payment.setStatus("COMPLETED");

        when(paymentRepository.findById("PAY-002"))
                .thenReturn(Optional.of(payment));

        when(receiptRepository.findByPaymentId("PAY-002"))
                .thenReturn(Optional.empty());

        Receipt savedReceipt = new Receipt();
        savedReceipt.setId("REC-002");
        savedReceipt.setPaymentId("PAY-002");
        savedReceipt.setCreditId("CR-001");
        savedReceipt.setAmount(new BigDecimal("300"));
        savedReceipt.setStatus("PAID");

        when(receiptRepository.save(any(Receipt.class)))
                .thenReturn(savedReceipt);

        Receipt result = receiptService.getPaymentReceipt("PAY-002");

        assertNotNull(result);
        assertEquals("REC-002", result.getId());
        assertEquals("PAY-002", result.getPaymentId());
        assertEquals(new BigDecimal("300"), result.getAmount());
        assertEquals("PAID", result.getStatus());

        verify(receiptRepository).save(any(Receipt.class));
    }

    @Test
    void shouldRejectWhenPaymentDoesNotExist() {

        when(paymentRepository.findById("PAY-999"))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> receiptService.getPaymentReceipt("PAY-999")
        );

        verify(receiptRepository, never())
                .save(any(Receipt.class));
    }
}