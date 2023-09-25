package com.example.demo.service;

import com.example.demo.entity.Role;
import com.example.demo.exception.ForbiddenAccessException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.demo.entity.AuthUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthUserRepository authUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            AuthUser authUser = authUserRepository.findByEmailAndActiveTrue(email)
                    .orElseThrow(NotFoundException::new);
            Set<String> collected = authUser.getRoles()
                    .stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());
            if (collected.contains("SUPER_ADMIN")
                    || collected.contains("ADMIN")) {
                LocalTime now = LocalTime.now(ZoneId.of("Asia/Tashkent"));
                if (9>now.getHour() || 18< now.getHour()) {
//                    throw new ForbiddenAccessException();
                }
            }

            adfs
            Set<SimpleGrantedAuthority> authoritySet = collected.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
            return User.builder()
                    .username(authUser.getEmail())
                    .accountLocked(authUser.isActive())
                    .password(Objects.requireNonNullElse(authUser.getTemporaryPassword(),""))
                    .roles(String.join(",", collected))
                    .authorities(authoritySet)
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
