package com.entelgy.challange.controller;

import com.entelgy.challange.entity.Receipt;
import com.entelgy.challange.service.ReceiptService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
    name = "Receipts",
    description = "Operaciones relacionadas con las constancias de pago"
)
@RestController
@RequestMapping("/api/payments")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @Operation(
        summary = "Generar constancia de pago",
        description = "Obtiene la constancia de pago asociada a un pago. Si no existe, la genera."
    )
    @GetMapping("/{paymentId}/receipt")
    public Receipt getPaymentReceipt(
        @PathVariable String paymentId
    ) {
        return receiptService.getPaymentReceipt(paymentId);
    }
}