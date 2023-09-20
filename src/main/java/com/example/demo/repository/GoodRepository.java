package com.example.demo.repository;

import com.example.demo.entity.Color;
import com.example.demo.entity.Good;
import com.example.demo.entity.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@Repository
public interface GoodRepository extends JpaRepository<Good, UUID>, JpaSpecificationExecutor<Good> {
    Good findByIdAndBlockedFalse(UUID id);

    @Modifying
    @Transactional
    @Query(value = "update good g set g.count=:count,g.color=:color,g.type=:type,g.name=:name,g.commentsId=:commentsId,g.description=:description,g.imagesId=:imagesId,g.ordersCount=:ordersCount,g.discountPrice=:discountPrice,g.price=:price where g.id=:id")
    void updateGood(UUID id, Color color, String name, Type type, Integer count, UUID commentsId, String description, UUID imagesId, Double price, Integer ordersCount, Double discountPrice);

    @Modifying
    @Transactional
    @Query(value = "update good g set g.blocked=true")
    void updateGoodBlockedTrueById(UUID id);

    @Query(nativeQuery = true,value = "select * from good g inner join type t on g.type_id=t.id inner join color c on g.color_id=c.id and g.blocked=false where (g.name in (?1) or t.name in (?1) or c.name in (?1) or g.price in (?2))")
    Page<Good> findAllByName(List<String> names, List<Double> prices, Pageable pageable);

    @Query(nativeQuery = true,value = "select * from good g inner join type t on g.type_id=t.id inner join color c on g.color_id=c.id and g.blocked=false where (g.name in (?1) or t.name in (?1) or c.name in (?1))")
    Page<Good> findAllByName(List<String> names, Pageable pageable);
    @Query(nativeQuery = true,value = "SELECT g FROM good g WHERE(?1 IS NULL OR g.color_id = ?1) AND(?2 IS NULL OR g.price >= ?2) AND(?3 IS NULL OR g.price <= ?3) AND(?4 IS NULL OR g.type_id = ?4)")
    Page<Good> findByCriteria(Long colorId, Double startPrice, Double endPrice, Long typeId, Pageable pageable);
}