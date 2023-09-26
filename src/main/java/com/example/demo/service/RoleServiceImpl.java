package com.example.demo.service;

import com.example.demo.entity.Role;
import com.example.demo.exception.BadParamException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public String save(String name) {
            if (roleRepository.existsRoleByName(name.toUpperCase())) {
                throw new BadParamException();
            }
            Role saved = roleRepository.save(Role.builder().name(name.toUpperCase()).build());
            return saved.getName();
    }

    @Override
    public String update(String oldName, String newName) {
            if (!roleRepository.existsRoleByName(oldName.toUpperCase())) {
                throw new BadParamException();
            }
            Role role = roleRepository.findByName(oldName)
                    .orElseThrow(NotFoundException::new);
            role.setName(newName);
            return roleRepository.save(role).getName();
    }

    @Override
    public Page<String> roles(Pageable pageable) {
            Page<Role> all = roleRepository.findAll(pageable);
            List<String> list = all.stream()
                    .map(Role::getName)
                    .toList();
            return new PageImpl<>(list,all.getPageable(),list.size());
    }
}
