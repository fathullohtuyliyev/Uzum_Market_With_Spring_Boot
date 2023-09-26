package com.example.demo.repository;

import com.example.demo.entity.Favourites;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
public interface FavouritesRepository extends JpaRepository<Favourites, UUID>, JpaSpecificationExecutor<Favourites> {

    @Modifying
    @Transactional
    @CacheEvict(key = "#userId",value = "favourites")
    @Query(nativeQuery = true,value = "delete from favourites f where f.user_id=?1 and f.good_id=?2")
    void removeFromFavourites(UUID userId,UUID goodId);

    @Cacheable(key = "#userId",value = "favourites")
    @Query(nativeQuery = true, value = "select * from favourites f where f.user_id=:userId")
    Page<Favourites> findAllByUserId(UUID userId, Pageable of);

    @Query(value = "select count (f.id) from favourites f where f.user.id=?1")
    Integer findSizeByUserId(UUID userId);

    @Query(value = "from favourites f where f.user.id=:userId")
    List<Favourites> findAllByUserId(UUID userId);
}