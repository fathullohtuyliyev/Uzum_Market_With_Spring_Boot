package com.example.demo.controller;

import com.example.demo.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api.status")
@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
public class StatusController {
    private final StatusService statusService;
    @PostMapping("/save/{name}")
    public ResponseEntity<String> save(@PathVariable String name){
        return new ResponseEntity<>(statusService.save(name), HttpStatus.CREATED);
    }
    @PutMapping("update")
    public ResponseEntity<String> update(@RequestParam String oldName,
                                         @RequestParam String newName){
        return new ResponseEntity<>(statusService.update(oldName,newName),HttpStatus.NO_CONTENT);
    }
    @GetMapping("/get-all")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN','SELLER')")
    public ResponseEntity<Page<String>> getAll(@RequestParam String page,
                                               @RequestParam String size){
            return ResponseEntity.ok(statusService.statuses
                    (PageRequest.of(Integer.parseInt(page), Integer.parseInt(size))));
    }
}
