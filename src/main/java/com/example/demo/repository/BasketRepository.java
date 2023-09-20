package com.example.demo.repository;

import com.example.demo.entity.Basket;
import org.springframework.data.domain.Page;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface BasketRepository extends JpaRepository<Basket, UUID>, JpaSpecificationExecutor<Basket> {
    @Modifying
    @Query(nativeQuery = true,value = "insert into basket(user_id,good_id) values (?1,?2)")
    void saveNewBasket(UUID userId,UUID goodId);

    @Query(nativeQuery = true,value = "select * from basket b where b.user_id=?1")
    Basket getAllByUserId(UUID userId);

    @Query(nativeQuery = true,value = "select * from basket b where b.user_id=?1")
    Page<Basket> findAllByUserId(UUID userId, Pageable pageable);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from basket b where b.good_id=:goodId and b.user_id=:userId")
    void deleteByGoodIdAndUserId(UUID goodId, UUID userId);

    @Query(value = "select count (b.id) from basket b")
    int findAllSize();

    @Query(nativeQuery = true,value = "select count (b.id) from basket b where b.user_id=:userId")
    int findAllSize(UUID userId);

    @Query(nativeQuery = true,value = "select * from basket b where b.user_id=:userId")
    List<Basket> findAllByUserId(UUID userId);
}