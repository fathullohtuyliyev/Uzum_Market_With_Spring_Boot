package com.example.demo.config;

import com.example.demo.service.CustomUserDetails;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    public final CustomUserDetailsService service;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        System.out.println("request.getRemoteAddr() = " + request.getRemoteAddr());
        String UserAgent = request.getHeader("User-Agent");
        System.out.println("UserAgent = " + UserAgent);
        System.out.println("request.getRemoteUser() = " + request.getRemoteUser());
        if (authorization==null || authorization.isBlank()) {
            filterChain.doFilter(request,response);
            return;
        }
        System.out.println("authorization = " + authorization);
        authorization=authorization.substring(7);
        String email = JwtTokenUtil.getEmail(response, authorization);
        CustomUserDetails userDetails = service.loadUserByUsername(email);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        authorities.forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        WebAuthenticationDetails details = new WebAuthenticationDetails(request);
        System.out.println(details);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);
    }
}
