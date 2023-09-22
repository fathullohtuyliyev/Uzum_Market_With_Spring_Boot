package com.example.demo.service;

import com.example.demo.exception.ForbiddenAccessException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.demo.entity.AuthUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthUserRepository authUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            AuthUser authUser = authUserRepository.findByEmailAndActiveTrue(email)
                    .orElseThrow(NotFoundException::new);
            if (authUser.getRoles().contains("SUPER_ADMIN")
                    || authUser.getRoles().contains("ADMIN")) {
                LocalTime now = LocalTime.now(ZoneId.of("Asia/Tashkent"));
                if (9>now.getHour() || 18< now.getHour()) {
                    throw new ForbiddenAccessException();
                }
            }
            return User.builder()
                    .username(authUser.getEmail())
                    .accountLocked(authUser.isActive())
                    .password(authUser.getTemporaryPassword())
                    .roles(String.join(",", authUser.getRoles()))
                    .credentialsExpired(false)
                    .disabled(false)
                    .build();
            }catch (Exception e){
                e.printStackTrace();
            log.info("{}", Arrays.toString(e.getStackTrace()));
                throw new RuntimeException();
            }
        }
}
