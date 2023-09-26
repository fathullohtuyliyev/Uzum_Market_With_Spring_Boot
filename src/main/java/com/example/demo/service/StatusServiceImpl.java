package com.example.demo.service;

import com.example.demo.entity.Status;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.StatusRepository;
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
public class StatusServiceImpl implements StatusService {
    private final StatusRepository statusRepository;

    @Override
    public String save(String name) {
            Status status = statusRepository.save(Status.builder().name(name.toUpperCase()).build());
            return status.getName();
    }

    @Override
    public String update(String oldName, String newName) {
            statusRepository.updateByName(newName.toUpperCase(), oldName.toUpperCase());
            return statusRepository.findByName(newName.toUpperCase())
                    .orElseThrow(NotFoundException::new).getName();
    }

    @Override
    public Page<String> statuses(Pageable pageable) {
            Page<Status> all = statusRepository.findAll(pageable);
            List<String> list = all.stream()
                    .map(Status::getName)
                    .toList();
            return new PageImpl<>(list,all.getPageable(),list.size());
    }
}
