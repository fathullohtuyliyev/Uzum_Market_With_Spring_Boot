package com.example.demo.repository;

import com.example.demo.entity.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentType, Integer>, JpaSpecificationExecutor<PaymentType> {
    @Query(value = "update payment_type p set p.active=?1 where p.id=?2")
    @Modifying
    @Transactional
    void updateActive(boolean active, UUID id);
}