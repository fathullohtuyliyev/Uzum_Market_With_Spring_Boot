package com.example.demo.repository;

import com.example.demo.entity.DeliveryPoint;
import com.example.demo.entity.Order;
import com.example.demo.entity.PaymentType;
import com.example.demo.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {
    @Transactional
    @Modifying
    @Query(value = "update order o set o.update=:update, o.deliveryPoint=:deliveryPoind, o.paymentType=:paymentType where o.id=:id")
    void updateOrder(LocalDateTime update, DeliveryPoint deliveryPoint, PaymentType paymentType, UUID id);

    @Query(nativeQuery = true, value = "select * from order o where o.auth_user_id=?1")
    Page<Order> findAllByUserId(UUID userId, Pageable pageable);

    @Query(nativeQuery = true,value = "select count (o.id) from order o where o.auth_user_id=?1")
    Integer sizeAllByUserId(UUID userId);

    @Query(nativeQuery = true, value = "select * from order o where o.auth_user_id=?1")
    List<Order> findAllByUserId(UUID userId);

    @Modifying
    @Transactional
    @Query(value = "update order o set o.update=:update,o.status=:status where o.id=:id")
    void updateOrder(LocalDateTime update, Status status, UUID id);
}