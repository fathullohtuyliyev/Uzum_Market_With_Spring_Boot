package com.example.demo.service;

import com.example.demo.entity.Role;
import com.example.demo.exception.BadParamException;
import com.example.demo.exception.ForbiddenAccessException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public String save(String name) {
        try {
            if (roleRepository.existsRoleByName(name.toUpperCase())) {
                throw new BadParamException();
            }
            Role saved = roleRepository.save(Role.builder().name(name.toUpperCase()).build());
            return saved.getName();
        }catch (NotFoundException | ForbiddenAccessException | BadParamException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public String update(String oldName, String newName) {
        try {
            if (!roleRepository.existsRoleByName(oldName.toUpperCase())) {
                throw new BadParamException();
            }
            roleRepository.updateRole(newName.toUpperCase(), oldName.toUpperCase());
            return roleRepository.findByName(newName.toUpperCase()).orElseThrow(NotFoundException::new).getName();
        }catch (NotFoundException | ForbiddenAccessException | BadParamException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public Page<String> roles(Pageable pageable) {
        try {
            Page<Role> all = roleRepository.findAll(pageable);
            int size = roleRepository.allSize();
            if (size <pageable.getPageSize()) {
                all = new PageImpl<>(roleRepository.findAll(), PageRequest.of(0,size),size);
            }
            List<String> list = all.stream()
                    .map(Role::getName)
                    .toList();
            return new PageImpl<>(list,all.getPageable(),list.size());
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }
}
