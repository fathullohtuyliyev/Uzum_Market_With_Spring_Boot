package com.example.demo.repository;

import com.example.demo.entity.AuthUser;
import com.example.demo.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, UUID>, JpaSpecificationExecutor<UserData> {
   @Query(value = "select count (d.id) from user_data d join d.user.roles r where d.user=:user and d.user.active=true and r in ('ADMIN') or r in ('SUPER_ADMIN')")
   int count(AuthUser user);
}