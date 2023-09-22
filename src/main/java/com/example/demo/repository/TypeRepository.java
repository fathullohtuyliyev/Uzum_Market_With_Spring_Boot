package com.example.demo.repository;

import com.example.demo.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long>, JpaSpecificationExecutor<Type> {
    @Modifying
    @Transactional
    @Query(value = "update type t set t.name=:name, t.id=:id, t.roots=:roots, t.sub=:sub where t.id=:id")
    void updateType(String name, Long id, Type sub, List<Type> roots);

    @Query(value = "select count (t.id) from type t")
    int size();
}