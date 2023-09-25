package com.example.demo.controller;

import com.example.demo.dto.auth_user_dto.AuthUserGetDto;
import com.example.demo.exception.BadParamException;
import com.example.demo.exception.ForbiddenAccessException;
import com.example.demo.service.AdminService;
import com.example.demo.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api.admin")
@PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
public class AdminController {
    private final AdminService adminService;
    private final AuthUserService authUserService;
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
    @GetMapping("/get-all-users-data")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Page<AuthUserGetDto>> getUsers(@RequestParam String page,
                                                         @RequestParam String size){
        try {
            Page<AuthUserGetDto> users = authUserService
                    .users(PageRequest.of(Integer.parseInt(page), Integer.parseInt(size)));
            return ResponseEntity.ok(users);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
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
