package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    @Override
    public String save(String name) {
        return null;
    }

    @Override
    public String update(String oldName, String newName) {
        return null;
    }

    @Override
    public String get(String name) {
        return null;
    }

    @Override
    public Page<String> roles(Pageable pageable) {
        return null;
    }
}
