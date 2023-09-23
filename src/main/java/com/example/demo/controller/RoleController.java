package com.example.demo.controller;

import com.example.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api.role")
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
public class RoleController {
    private final RoleService roleService;
    @PostMapping("/save/{name}")
    public ResponseEntity<String> save(@PathVariable String name){
        return new ResponseEntity<>(roleService.save(name), HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestParam String oldName,
                                         @RequestParam String newName){
        return new ResponseEntity<>(roleService.update(oldName,newName),
                HttpStatus.NO_CONTENT);
    }
    @GetMapping("/get-roles")
    public ResponseEntity<Page<String>> getRoles(@RequestParam String page,
                                                 @RequestParam String size){
        return ResponseEntity.ok(roleService.roles
                (PageRequest.of(Integer.parseInt(page),Integer.parseInt(size))));
    }
}
