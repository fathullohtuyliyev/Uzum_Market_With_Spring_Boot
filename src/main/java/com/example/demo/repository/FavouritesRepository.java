package com.example.demo.repository;

import com.example.demo.entity.Favourites;
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
    @Query(nativeQuery = true,value = "insert into favourites(user_id,good_id) values (?1,?2)")
    void saveToFavourites(UUID userId,UUID goodId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from favourites f where f.user_id=?1,f.good_id=?2")
    void removeFromFavourites(UUID userId,UUID goodId);
}