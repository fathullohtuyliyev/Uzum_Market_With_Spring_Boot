package com.example.demo.service;

import com.example.demo.entity.Status;
import com.example.demo.repository.StatusRepository;
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
public class StatusServiceImpl implements StatusService {
    private final StatusRepository statusRepository;

    @Override
    public String save(String name) {
        try {
            Status status = statusRepository.save(Status.builder().name(name).build());
            return status.getName();
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
            statusRepository.updateByName(newName, oldName);
            return statusRepository.findByName(newName).getName();
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public String get(String name) {
        try {
            return statusRepository.findByName(name).getName();
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
            Page<Status> all = statusRepository.findAll(pageable);
            int sizeRoles = statusRepository.size();
            if (sizeRoles < pageable.getPageSize()) {
                all = new PageImpl<>(statusRepository.findAll(), PageRequest.of(0,sizeRoles),sizeRoles);
            }
            List<String> list = all.stream()
                    .map(Status::getName)
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
