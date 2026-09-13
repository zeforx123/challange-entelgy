package com.entelgy.challange.controller;

import com.entelgy.challange.dto.CreditRequest;
import com.entelgy.challange.service.CreditService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.entelgy.challange.entity.Credit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
    name = "Credits",
    description = "Operaciones relacionadas con los créditos"
)
@RestController
@RequestMapping("/api/credits")
public class CreditController {

    private final CreditService creditService;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @Operation(
        summary = "Consultar créditos",
        description = "Obtiene la lista de créditos registrados."
    )
    @GetMapping
    public List<Credit> getCredits() {
        return this.creditService.getCredits();
    }

    @Operation(
        summary = "Consultar crédito",
        description = "Obtiene el detalle de un crédito por su identificador."
    )
    @GetMapping("/{id}")
    public Credit getCreditDetail(@PathVariable String id) {
        return this.creditService.getCreditDetail(id);
    }

    @Operation(
        summary = "Crear crédito",
        description = "Crea un nuevo crédito."
    )
    @PostMapping
    public Credit createCredit(@Valid @RequestBody CreditRequest request) {
        return this.creditService.createCredit(request);
    }
}
