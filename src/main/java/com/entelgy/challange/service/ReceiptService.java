package com.entelgy.challange.service;

import com.entelgy.challange.entity.Payment;
import com.entelgy.challange.entity.Receipt;
import com.entelgy.challange.repository.PaymentRepository;
import com.entelgy.challange.repository.ReceiptRepository;
import org.springframework.stereotype.Service;
import com.entelgy.challange.exception.ResourceNotFoundException;

import java.time.LocalDateTime;

@Service
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final PaymentRepository paymentRepository;

    public ReceiptService(
            ReceiptRepository receiptRepository,
            PaymentRepository paymentRepository
    ) {
        this.receiptRepository = receiptRepository;
        this.paymentRepository = paymentRepository;
    }

    public Receipt getPaymentReceipt(String paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pago no encontrado")
                );

        return receiptRepository.findByPaymentId(paymentId)
                .orElseGet(() -> {

                    Receipt receipt = new Receipt();

                    receipt.setPaymentId(payment.getId());
                    receipt.setCreditId(payment.getCreditId());
                    receipt.setAmount(payment.getAmount());
                    receipt.setDate(LocalDateTime.now());
                    receipt.setStatus("PAID");

                    return receiptRepository.save(receipt);
                });
    }
}