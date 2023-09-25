package com.example.demo.service;

import com.example.demo.entity.Color;
import com.example.demo.exception.BadParamException;
import com.example.demo.exception.ForbiddenAccessException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.ColorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {
    private final ColorRepository colorRepository;

    @Override
    public Map<Long, String> save(String name) {
        try {
            Color color = colorRepository.save(Color.builder().name(name).build());
            log.info("{} {} saved",color.getId(),color.getName());
            return Map.of(color.getId(),color.getName());
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
    public Map<Long, String> update(Map<Long, String> colorDto) {
        try {
            Long id = colorDto.keySet().stream().findFirst().orElseThrow();
            String name = colorDto.get(id);
            Color saved = colorRepository.save(Color.builder().id(id).name(name).build());
            log.info("{} updated to {}",saved.getId(),saved.getName());
            return Map.of(saved.getId(),saved.getName());
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
    public Map<Long, String> get(Long id) {
        try {
            Color color = colorRepository.findById(id).orElseThrow(NotFoundException::new);
            log.info("{} gave",color);
            return Map.of(color.getId(),color.getName());
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
    public Page<Map<Long, String>> colors(Pageable pageable) {
        try {
            Page<Color> all = colorRepository.findAll(pageable);
            int allSize = colorRepository.findAllSize();
            if (allSize <all.getContent().size()) {
                List<Color> colors = colorRepository.findAll();
                all=new PageImpl<>(colors, PageRequest.of(0,allSize),allSize);
            }
            List<Map<Long, String>> list = all.getContent().stream()
                    .map(color -> Map.of(color.getId(), color.getName()))
                    .toList();
            return new PageImpl<>(list,pageable,list.size());
        }catch (NotFoundException | ForbiddenAccessException | BadParamException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }
}
