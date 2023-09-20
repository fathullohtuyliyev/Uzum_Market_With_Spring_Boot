package com.example.demo.config;

import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
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
        if (authorization==null || authorization.isBlank()) {
            filterChain.doFilter(request,response);
            return;
        }
        System.out.println("authorization = " + authorization);
        authorization=authorization.substring(7);
        if (!JwtTokenUtil.isValid(response,authorization)) {
            filterChain.doFilter(request,response);
            return;
        }
        String email = JwtTokenUtil.getEmail(response, authorization);
        UserDetails userDetails = service.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email,null,userDetails.getAuthorities());
        WebAuthenticationDetails details = new WebAuthenticationDetails(request);
        System.out.println(details);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);
    }
}
