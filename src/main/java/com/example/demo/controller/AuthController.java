package com.example.demo.controller;

import com.example.demo.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class AuthController {
    private final AuthUserService authUserService;
}
