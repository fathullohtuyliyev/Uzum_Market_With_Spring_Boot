package com.example.demo.repository;

import com.example.demo.entity.AuthUser;
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
    @Query(value = "from basket b where b.user=?1")
    Page<Basket> findAllByUserId(AuthUser authUser, Pageable pageable);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from basket b where b.good_id=:goodId and b.user_id=:userId")
    void deleteByGoodIdAndUserId(UUID goodId, UUID userId);

    @Query(value = "select count (b.id) from basket b")
    Integer findAllSize();

    @Query(value = "select count (b.id) from basket b where b.user=:user")
    Integer findAllSize(AuthUser user);

    @Query(value = "from basket b where b.user=:user")
    List<Basket> findAllByUserId(AuthUser user);
}