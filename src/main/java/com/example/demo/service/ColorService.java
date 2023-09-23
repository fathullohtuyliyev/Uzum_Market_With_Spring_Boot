package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public interface ColorService {
    Map<Long,String> save(String name);
    Map<Long,String> update(Map<Long,String> colorDto);
    Map<Long,String> get(Long id);
    Page<Map<Long,String>> colors(Pageable pageable);
}
