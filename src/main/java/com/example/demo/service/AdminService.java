package com.example.demo.service;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public interface AdminService {
    void updateEmail(String adminEmail,String oldEmail, String newEmail);
    void updateRole(UUID userId, String role);
    void updateActivity(UUID userID,boolean active);
}
