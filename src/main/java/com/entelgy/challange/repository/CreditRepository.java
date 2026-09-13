package com.entelgy.challange.repository;

import com.entelgy.challange.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRepository extends JpaRepository<Credit, String> {
}