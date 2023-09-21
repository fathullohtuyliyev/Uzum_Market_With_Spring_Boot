package com.example.demo.service;

import com.example.demo.entity.AuthUser;
import com.example.demo.exception.BadParamException;
import com.example.demo.exception.ForbiddenAccessException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.AuthUserRepository;
import com.example.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AuthUserRepository authUserRepository;
    private final RoleRepository roleRepository;

    @Override
    public void updateEmail(String adminEmail, String oldEmail, String newEmail) {
        try {
            if(!authUserRepository.isAdmin(adminEmail)){
                throw new ForbiddenAccessException();
            }
            if (!authUserRepository.existsAuthUserByEmail(oldEmail)) {
                throw new BadParamException();
            }
            authUserRepository.updateEmailForAdmin(oldEmail, newEmail);
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public void updateRole(UUID userId, String role) {
         try {
             if (!roleRepository.existsRoleByName(role)) {
                 throw new NotFoundException();
             }
             AuthUser authUser = authUserRepository.findAuthUserByIdAndActiveTrue(userId)
                     .orElseThrow(NotFoundException::new);
             List<String> roles = authUser.getRoles();
             roles.add(role);
             authUser.setRoles(roles);
             authUserRepository.save(authUser);
         }catch (Exception e){
             e.printStackTrace();
             Arrays.stream(e.getStackTrace())
                     .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
             throw new RuntimeException();
         }
    }

    @Override
    public void updateActivity(UUID userId, boolean active) {
        try {
            authUserRepository.updateAuthUserActiveById(userId, active);
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }
}
