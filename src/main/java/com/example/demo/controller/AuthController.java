package com.example.demo.controller;

import com.example.demo.dto.auth_user_dto.AuthUserCreateDto;
import com.example.demo.dto.auth_user_dto.AuthUserGetDto;
import com.example.demo.dto.auth_user_dto.AuthUserUpdateDto;
import com.example.demo.service.AuthUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api.auth")
@PreAuthorize("isAuthenticated()")
public class AuthController {
    private final AuthUserService authUserService;
    @PostMapping("/register")
    public void register(@RequestBody AuthUserCreateDto dto, HttpServletResponse response){
        authUserService.save(dto, response);
    }
    @PutMapping("/activation")
    public void activation(@RequestParam String code, HttpServletRequest request){
        authUserService.activate(code, request);
    }

    @PostMapping("/login-1")
    public void checkAndSentPasswordToEmail(@RequestParam String email, HttpServletResponse response){
        authUserService.checkAndSendPasswordToEmail(email,response);
    }
    @GetMapping("/login-2")
    public ResponseEntity<AuthUserGetDto> login(@RequestParam String password,
                                                HttpServletRequest request,
                                                HttpServletResponse response){
        AuthUserGetDto dto = authUserService.login(password, request, response);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response){
        authUserService.logout(request, response);
    }
    @PutMapping("/update")
    public ResponseEntity<AuthUserGetDto> update(@RequestBody AuthUserUpdateDto dto){
        AuthUserGetDto update = authUserService.update(dto);
        return new ResponseEntity<>(update, HttpStatus.NO_CONTENT);
    }
    @GetMapping("/get-self-data")
    public ResponseEntity<AuthUserGetDto> get(@RequestParam String id, HttpServletRequest request){
        AuthUserGetDto getDto = authUserService.get(UUID.fromString(id), request);
        return ResponseEntity.ok(getDto);
    }
    @GetMapping("/get-all-users-data")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Page<AuthUserGetDto>> getUsers(@RequestParam String page,
                                                         @RequestParam String size){
        Page<AuthUserGetDto> users = authUserService
                .users(PageRequest.of(Integer.parseInt(page), Integer.parseInt(size)));
        return ResponseEntity.ok(users);
    }
    @PutMapping("/online")
    public void online(@RequestParam String id){
        authUserService.online(UUID.fromString(id));
    }
    @PutMapping("/offline")
    public void offline(@RequestParam String id){
        authUserService.offline(UUID.fromString(id));
    }
    @GetMapping("/exist-email/{email}")
    public ResponseEntity<Boolean> existEmail(@PathVariable String email){
        return ResponseEntity.ok(authUserService.existEmail(email));
    }
    @GetMapping("/exist-phone/{phone}")
    public ResponseEntity<Boolean> existPhone(@PathVariable String phone){
        return ResponseEntity.ok(authUserService.existPhone(phone));
    }
}
