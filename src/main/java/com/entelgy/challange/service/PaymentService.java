package com.entelgy.challange.service;

import com.entelgy.challange.dto.PaymentRequest;
import com.entelgy.challange.entity.Credit;
import com.entelgy.challange.entity.Payment;
import com.entelgy.challange.repository.CreditRepository;
import com.entelgy.challange.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import com.entelgy.challange.exception.BusinessException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CreditRepository creditRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            CreditRepository creditRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.creditRepository = creditRepository;
    }
    @Transactional
    public Payment createPayment(
            String creditId,
            PaymentRequest request
    ) {

        Credit credit = creditRepository.findById(creditId)
                .orElseThrow(() ->
                        new RuntimeException("Crédito no encontrado")
                );

        if (request.getAmount().compareTo(credit.getOutstandingDebt()) > 0) {
            throw new BusinessException(
                    "El monto del pago supera la deuda pendiente"
            );
        }

        Payment payment = new Payment();

        payment.setCreditId(creditId);
        payment.setAmount(request.getAmount());
        payment.setStatus("COMPLETED");
        payment.setCreatedAt(LocalDateTime.now());

        credit.setPaidAmount(
                credit.getPaidAmount().add(request.getAmount())
        );

        credit.setOutstandingDebt(
                credit.getOutstandingDebt().subtract(request.getAmount())
        );

        credit.setUpdatedAt(LocalDateTime.now());

        if (credit.getOutstandingDebt().compareTo(BigDecimal.ZERO) == 0) {
            credit.setStatus("PAID");
        }

        creditRepository.save(credit);

        return paymentRepository.save(payment);
    }
}