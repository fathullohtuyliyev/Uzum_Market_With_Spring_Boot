package com.example.demo.repository;

import com.example.demo.entity.AuthUser;
import com.example.demo.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, UUID>, JpaSpecificationExecutor<UserData> {
   @Query(value = "select count (d.id) from user_data d join d.user.roles r where d.user=:user and d.expireTime<now() and d.user.active=true and r in ('ADMIN') or r in ('SUPER_ADMIN')")
   int count(AuthUser user);

   @Modifying
   @Transactional
   @Async
   @Query(value = "delete from user_data d where d.expireTime<=now()")
   void deleteExpireData();

   @Modifying
   @Transactional
   @Async
   @Query(value = "delete from user_data d where d.data=:data")
   void deleteByUserData(String data);

}