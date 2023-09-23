package com.example.demo.controller;

import com.example.demo.exception.BadParamException;
import com.example.demo.exception.ForbiddenAccessException;
import com.example.demo.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api.admin")
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
public class AdminController {
    private final AdminService adminService;
    @PutMapping("/update-email")
    public void updateEmail(@RequestParam Map<String, String> param){
        String adminEmail = param.get("admin");
        String oldEmail = param.get("old");
        String newEmail = param.get("new");
        adminService.updateEmail(adminEmail,oldEmail,newEmail);
    }
    @PutMapping("/update-role")
    public void updateRole(@RequestParam String userId,
                           @RequestParam String role){
        if (role.equals("SUPER_ADMIN")) {
            throw new ForbiddenAccessException();
        }
        try {
            adminService.updateRole(UUID.fromString(userId),role);
        }catch (IllegalArgumentException e){
            throw new BadParamException();
        }
    }
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/update-for-special")
    public void updateRoleToSuperAdmin(@RequestParam String userId,
                           @RequestParam String role){
        try {
            adminService.updateRole(UUID.fromString(userId), role);
        }catch (IllegalArgumentException e){
            throw new BadParamException();
        }
    }
    @PutMapping("/update-activity")
    public void updateActivity(@RequestParam String userId,
                               @RequestParam Boolean activity){
        try {
            adminService.updateActivity(UUID.fromString(userId),activity);
        }catch (IllegalArgumentException e){
            throw new BadParamException();
        }
    }
}
