package com.example.demo.repository;

import com.example.demo.entity.DeliveryPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryPointRepository extends JpaRepository<DeliveryPoint, Long>, JpaSpecificationExecutor<DeliveryPoint> {
    @Query(value = "select count (d.id) from delivery_point d")
    int findAllSize();
}