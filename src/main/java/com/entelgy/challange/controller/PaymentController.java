package com.entelgy.challange.controller;

import com.entelgy.challange.dto.PaymentRequest;
import com.entelgy.challange.entity.Payment;
import com.entelgy.challange.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
    name = "Payments",
    description = "Operaciones relacionadas con los pagos"
)
@RestController
@RequestMapping("/api/credits")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(
        summary = "Registrar pago",
        description = "Registra un pago para un crédito y actualiza la deuda pendiente."
    )
    @PostMapping("/{creditId}/payments")
    public Payment createPayment(
        @PathVariable String creditId,
        @Valid @RequestBody PaymentRequest request
    ) {
        return this.paymentService.createPayment(creditId, request);
    }
}