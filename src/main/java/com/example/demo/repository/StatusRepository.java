package com.example.demo.repository;

import com.example.demo.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer>, JpaSpecificationExecutor<Status> {
    @Modifying
    @Transactional
    @Query(value = "update status s set s.name=:newName where s.name=:oldName")
    void updateByName(String newName, String oldName);

    @Query(value = "from status s where s.name=:name")
    Optional<Status> findByName(String name);

    @Query(value = "select count (s.id) from status s")
    int size();
}