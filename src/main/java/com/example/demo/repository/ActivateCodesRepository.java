package com.example.demo.repository;

import com.example.demo.entity.ActivateCodes;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivateCodesRepository extends JpaRepository<ActivateCodes, Integer>, JpaSpecificationExecutor<ActivateCodes> {
    @Query(value = "from activate_codes c where c.code=:code")
    Optional<ActivateCodes> findByCode(Integer code);
}