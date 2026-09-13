package com.entelgy.challange.repository;

import com.entelgy.challange.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceiptRepository extends JpaRepository<Receipt, String> {
    Optional<Receipt> findByPaymentId(String paymentId);
}