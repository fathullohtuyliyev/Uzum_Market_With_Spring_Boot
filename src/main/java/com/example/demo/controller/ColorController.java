package com.example.demo.controller;

import com.example.demo.service.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api.color")
@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','SELLER')")
public class ColorController {
    private final ColorService colorService;
    @PostMapping("/save/{name}")
    public ResponseEntity<Map<Long,String>> save(@PathVariable String name){
        return new ResponseEntity<>(colorService.save(name), HttpStatus.CREATED);
    }
    @PutMapping("/update")
    @CachePut(key = "#id",value = "colors")
    public ResponseEntity<Map<Long, String>> update(@RequestParam String id,
                                                    @RequestParam String name){
        return new ResponseEntity<>(colorService.update
                (Map.of(Long.parseLong(id),name)),HttpStatus.NO_CONTENT);
    }
    @GetMapping("/get/{id}")
    @PreAuthorize("isAuthenticated()")
    @Cacheable(key = "#id",value = "colors")
    public ResponseEntity<Map<Long, String>> get(@PathVariable Long id){
        return ResponseEntity.ok(colorService.get(id));
    }
    @GetMapping("/get")
    public ResponseEntity<Page<Map<Long, String>>> getAll(@RequestParam String page,
                                                          @RequestParam String size){
        try {
            Page<Map<Long, String>> colors = colorService.colors
                    (PageRequest.of(Integer.parseInt(page), Integer.parseInt(size)));
            return ResponseEntity.ok(colors);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
