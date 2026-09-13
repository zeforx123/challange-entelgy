package com.entelgy.challange.service;

import com.entelgy.challange.dto.CreditRequest;
import org.springframework.stereotype.Service;
import java.util.*;
import com.entelgy.challange.repository.CreditRepository;
import com.entelgy.challange.entity.Credit;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.entelgy.challange.exception.ResourceNotFoundException;

@Service
public class CreditService {
    private final CreditRepository creditRepository;

    public CreditService(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    public List<Credit> getCredits() {
        return this.creditRepository.findAll();
    }

    public Credit getCreditDetail(String id) {

        return this.creditRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Crédito no encontrado")
                );
    }

    public Credit createCredit(CreditRequest request) {

        Credit credit = new Credit();

        credit.setTitle(request.getTitle());
        credit.setTotalAmount(request.getTotalAmount());
        credit.setPaidAmount(BigDecimal.ZERO);
        credit.setOutstandingDebt(request.getTotalAmount());
        credit.setStatus("ACTIVE");
        credit.setCreatedAt(LocalDateTime.now());
        credit.setUpdatedAt(LocalDateTime.now());

        return this.creditRepository.save(credit);
    }
}
