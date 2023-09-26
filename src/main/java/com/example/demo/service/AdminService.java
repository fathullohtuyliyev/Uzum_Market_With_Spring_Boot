package com.example.demo.service;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public interface AdminService {
    void updateEmail(String adminEmail,String oldEmail, String newEmail);
    void addRole(UUID userId, String role);
    void removeRole(UUID userId, String role);
    void updateActivity(UUID userID,boolean active);
}
