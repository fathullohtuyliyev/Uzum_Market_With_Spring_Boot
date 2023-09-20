package com.example.demo.repository;

import com.example.demo.entity.Good;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface GoodRepository extends JpaRepository<Good, UUID>, JpaSpecificationExecutor<Good> {
    Good findByIdAndBlockedFalse(UUID id);
}