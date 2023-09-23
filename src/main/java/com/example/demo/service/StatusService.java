package com.example.demo.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface StatusService {
    String save(String name);
    String update(String oldName,String newName);
    Page<String> statuses(Pageable pageable);
}
