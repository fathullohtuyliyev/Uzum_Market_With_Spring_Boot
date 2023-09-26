package com.example.demo.service;

import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Role;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.AuthUserRepository;
import com.example.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AuthUserRepository authUserRepository;
    private final RoleRepository roleRepository;

    @Override
    public void updateEmail(String adminEmail, String oldEmail, String newEmail) {

            authUserRepository.findByEmail(oldEmail)
                            .orElseThrow(NotFoundException::new);
            authUserRepository.updateEmailForAdmin(oldEmail, newEmail);
    }

    @Override
    public void addRole(UUID userId, String role) {
            Role foundedRole = roleRepository.findByName(role)
                    .orElseThrow(NotFoundException::new);
            AuthUser authUser = authUserRepository.findById(userId)
                    .orElseThrow(NotFoundException::new);
            Set<Role> roles = authUser.getRoles();
            roles.add(foundedRole);
            authUser.setRoles(roles);

            Set<AuthUser> authUsers = foundedRole.getAuthUsers();
            authUsers.add(authUser);
            foundedRole.setAuthUsers(authUsers);
            roleRepository.save(foundedRole);
    }
    @Override
    public void removeRole(UUID userId, String role) {
            Role foundedRole = roleRepository.findByName(role)
                    .orElseThrow(NotFoundException::new);
            AuthUser authUser = authUserRepository.findById(userId)
                    .orElseThrow(NotFoundException::new);
            Set<Role> roles = authUser.getRoles();
            roles.remove(foundedRole);
            authUser.setRoles(roles);

            Set<AuthUser> authUsers = foundedRole.getAuthUsers();
            authUsers.remove(authUser);
            foundedRole.setAuthUsers(authUsers);
            roleRepository.save(foundedRole);
    }


    @Override
    public void updateActivity(UUID userId, boolean active) {
            AuthUser authUser = authUserRepository.findById(userId)
                    .orElseThrow(NotFoundException::new);
            authUserRepository.updateAuthUserActiveById(authUser.getId(), active);
    }
}
