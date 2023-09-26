package com.example.demo.repository;

import com.example.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from auth_user_role ar where ar.role_id=?1 and ar.role_id=?2")
    void removeRoleAuthUser(Long roleId, UUID userId);

    @Query(value = "select exists(select r.name from role r where r.name=:name)")
    Boolean existsRoleByName(String name);

    @Query(value = "from role r where r.name=:name")
    Optional<Role> findByName(String name);

    @Query(value = "select count (r.name) from role r")
    int allSize();
}