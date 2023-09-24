package com.example.demo.repository;

import com.example.demo.entity.PaymentType;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentType, Integer>, JpaSpecificationExecutor<PaymentType> {
    @Query(value = "update payment_type p set p.active=?1, p.name=?2 where p.id=?3")
    @Modifying
    @Transactional
    void updateActive(boolean active, @NotBlank String name, Integer id);

    PaymentType findByName(String paymentType);

    @Query(nativeQuery = true,value = "delete from public.payment_type p where p.id=?1")
    @Modifying
    @Transactional
    void deleteWithId(Integer id);
}