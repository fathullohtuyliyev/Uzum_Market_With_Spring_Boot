package com.example.demo.repository;

import com.example.demo.entity.AuthUser;
import com.example.demo.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, UUID>, JpaSpecificationExecutor<AuthUser> {
    @Query(value = "from auth_user u where u.email=:email and u.active=true")
    Optional<AuthUser> findByEmailAndActiveTrue(String email);

    @Query(value = "from auth_user u where u.id=:id and u.active=true")
    Optional<AuthUser> findAuthUserByIdAndActiveTrue(UUID id);

    @Modifying
    @Transactional
    @Async
    @Query(value = "update auth_user set online=false where id=?1")
    void updateAuthUserOnlineFalseById(UUID id);

    @Modifying
    @Transactional
    @Async
    @Query(value = "update auth_user set online=true where id=?1")
    void updateAuthUserOnlineTrueById(UUID id);

    @Transactional
    @Modifying
    @Async
    @Query(value = "update auth_user set active=:active where email=:email")
    void updateAuthUserActiveByEmail(boolean active, String email);


    @Modifying
    @Transactional
    @Query(value = "update auth_user set firstName=:firstName,gender=:gender,lastName=:lastName,images=:images,birthdate=:birthdate where id=:id")
    void updateAuthUser(String firstName, String lastName, String images, Gender gender, LocalDate birthdate,UUID id);

    @Query(value = "select count(au.id) from auth_user au")
    Integer findAllSize();

    @Query(value = "select au.active from auth_user au where au.id=?1")
    Boolean isActive(UUID userId);

    @Query(value = "select exists (select u.email, u.phone from auth_user u where u.phone = :phone or u.email = :email)")
    Boolean existsAuthUserByPhoneAndEmail(String phone, String email);

    @Query(value = "select exists (select u.email from auth_user u where u.email=:email)")
    Boolean existsAuthUserByEmail(String email);

    @Query(value = "select exists (select u.phone from auth_user u where u.phone=:phone)")
    Boolean existsAuthUserByPhone(String phone);

    @Modifying
    @Transactional
    @Async
    @Query(value = "update auth_user u set u.temporaryPassword=:temporaryPassword where u.email=:email")
    void updatePassword(String email, String temporaryPassword);

    @Query(value = "select exists (select u.email from auth_user u where u.email=:email and u.active=true)")
    Boolean existsAuthUserByEmailAndActiveTrue(String email);

    /*@Query(value = "select exists (select u.email from auth_user u join u.roles r where u.email=?1 and u.active=true and 'SUPER_ADMIN' in r.name)")
    Boolean isAdmin(String adminEmail);*/

    @Modifying
    @Transactional
    @Query(value = "update auth_user u set u.email=:newEmail where u.email=:oldEmail")
    void updateEmailForAdmin(String oldEmail, String newEmail);

    @Transactional
    @Modifying
    @Async
    @Query(value = "update auth_user set active=:active where id=:id")
    void updateAuthUserActiveById(UUID id, boolean active);

    Optional<AuthUser> findByEmail(String email);
}