package com.example.demo.repository;

import com.example.demo.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long>, JpaSpecificationExecutor<Type> {
    @Modifying
    @Transactional
    @Query(value = "update type t set t.name=:name, t.id=:id, t.root=:root, t.sub=:sub")
    void updateType(String name, Long id, Long sub, Long root);

    @Query(value = "select count (t.id) from type t")
    int size();
}