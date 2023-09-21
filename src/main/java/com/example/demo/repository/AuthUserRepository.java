package com.example.demo.repository;

import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Role;
import com.example.demo.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, UUID>, JpaSpecificationExecutor<AuthUser> {
    @Query(value = "from auth_user u where u.email=:email and u.active=true")
    AuthUser findByEmailAndActiveTrue(String email);

    @Query(value = "from auth_user u where u.id=:id and u.active=true")
    AuthUser findAuthUserByIdAndActiveTrue(UUID id);

    @Modifying
    @Transactional
    void updateAuthUserOnlineFalseById(UUID id);

    @Modifying
    @Transactional
    void updateAuthUserOnlineTrueById(UUID id);

    @Transactional
    @Modifying
    @Query(value = "update auth_user set active=:blocked where id=:id")
    void updateAuthUserBlockedById(boolean blocked,UUID id);

    @Transactional
    @Modifying
    @Query(value = "update auth_user set role=?1 where id=?2")
    void updateRole(Role role,UUID id);

    @Modifying
    @Transactional
    @Query(value = "update auth_user set phone=:phone,firstName=:firstName,gender=:gender,lastName=:lastName,imagePath=:imagePath,birthdate=:birthdate where id=:id")
    void updateAuthUser(String phone, String firstName, String lastName, String imagePath, Gender gender, LocalDate birthdate,UUID id);

    @Query(value = "select count(au.id) from auth_user au")
    int findAllSize();

    @Query(value = "select au.active from auth_user au where au.id=?1")
    boolean isActive(UUID userId);

    @Query(value = "select exists (select u.email, u.phone from auth_user u where u.phone = :phone or u.email = :email)")
    boolean existsAuthUserByPhoneAndEmail(String phone, String email);

    @Query(value = "select exists (select u.email from auth_user u where u.email=:email)")
    boolean existsAuthUserByEmail(String email);

    @Modifying
    @Transactional
    @Async
    @Query(value = "update auth_user u set u.temporaryPassword=:temporaryPassword where u.email=:email")
    void updatePassword(String email, String temporaryPassword);
}