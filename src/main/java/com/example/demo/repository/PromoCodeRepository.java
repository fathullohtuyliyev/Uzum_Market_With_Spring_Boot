package com.example.demo.repository;

import com.example.demo.entity.Good;
import com.example.demo.entity.PromoCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, UUID>, JpaSpecificationExecutor<PromoCode> {
    @Modifying
    @Transactional
    @Query("update promo_code pc set pc.active=:active,pc.name=:name, pc.goods=:goods where pc.id=:id")
    void updatePromoCode(String name, boolean active, List<Good> goods, UUID id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from promo_code pc where pc.id=:id")
    void deleteWithId(UUID id);

    @Query(nativeQuery = true, value = "select * from promo_code pc where pc.good_id=:goodId")
    Page<PromoCode> findAllByGoodId(UUID goodId, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from promo_code pc where pc.good_id=:goodId")
    List<PromoCode> findAllByGoodId(UUID goodId);
    @Query(nativeQuery = true, value = "select count(pc.id) from promo_code pc where pc.good_id=:goodId")
    int findAllByGoodIdSize(UUID goodId);

    @Query(nativeQuery = true, value = "select * from promo_code pc where lower(pc.name) = lower(:name)")
    Optional<PromoCode> findByName(String name);
}