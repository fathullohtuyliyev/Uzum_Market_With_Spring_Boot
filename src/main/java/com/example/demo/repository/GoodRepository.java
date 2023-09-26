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

import java.util.Optional;
import java.util.UUID;
@Repository
public interface GoodRepository extends JpaRepository<Good, UUID>, JpaSpecificationExecutor<Good> {
    Optional<Good> findByIdAndBlockedFalse(UUID id);

    @Modifying
    @Transactional
    @Query(value = "update good set count=:count,color=:color,type=:type,name=:name,description=:description,images=:images,ordersCount=:ordersCount,discountPrice=:discountPrice,price=:price, videoPath=:videoPath where id=:id")
    void updateGood(UUID id, Color color, String name, Type type, Integer count, String description, List<String> images, Double price, Integer ordersCount, Double discountPrice, String videoPath);

    @Modifying
    @Transactional
    @Query(value = "update good set blocked=?1 where id=?2")
    void updateGoodBlockedById(boolean blocked, UUID id);

    @Query(nativeQuery = true,value = "select * from good g inner join color c on g.blocked=false and lower(c.name) = any(:names) inner join type t on g.blocked=false and lower(t.name) = any(:names) where lower(g.name) = any(:names) and ((:prices) is null or g.price = any(:prices))")
    Page<Good> findAllByName(List<String> names, List<Double> prices, Pageable pageable);

    @Query(value = "from good g where g.blocked=false and (?1 is null or g.color = ?1) and(?2 is null or g.price >= ?2) and(?3 is null or g.price <= ?3) and(?4 is null or g.type = ?4)")
    Page<Good> findByCriteria(Color color, Double startPrice, Double endPrice, Type type, Pageable pageable);

    @Query(value = "from good where blocked=false")
    Page<Good> findAllByBlockedFalse(Pageable pageable);
}