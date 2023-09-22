package com.example.demo.service;

import com.example.demo.dto.good_dto.GoodCreateDto;
import com.example.demo.dto.good_dto.GoodCriteria;
import com.example.demo.dto.good_dto.GoodGetDto;
import com.example.demo.dto.good_dto.GoodUpdateDto;
import com.example.demo.entity.Good;
import com.example.demo.entity.Type;
import com.example.demo.exception.NotFoundException;
import com.example.demo.nosql.Comment;
import com.example.demo.repository.ColorRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.GoodRepository;
import com.example.demo.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.demo.mapper.GoodMapper.GOOD_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodServiceImpl implements GoodService {
    private final CommentRepository commentRepository;
    private final ColorRepository colorRepository;
    private final TypeRepository typeRepository;
    private final GoodRepository goodRepository;

    @Override
    public GoodGetDto save(GoodCreateDto dto) {
        try {
            Good good = GOOD_MAPPER.toEntity(dto);
            method1(dto,good,typeRepository,colorRepository);
            Good saved = goodRepository.save(good);
            GoodGetDto dto1 = GOOD_MAPPER.toDto(saved);
            method2(dto1,good,commentRepository);
            return dto1;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    private static void method1(GoodCreateDto dto,Good good,
                                TypeRepository typeRepository,
                                ColorRepository colorRepository){
        Long typeId = dto.getType_id();
        Type type = typeRepository.findById(typeId).orElseThrow(NotFoundException::new);
        good.setType(type);
        Long colorId = dto.getColor_id();
        if (colorId !=null) {
            colorRepository.findById(colorId).ifPresent(good::setColor);
        }
    }
    private static void method2(GoodGetDto dto,Good good,CommentRepository commentRepository){
        dto.setColor(Map.of(good.getColor().getId(),good.getColor().getName()));
        dto.setType(Map.of(good.getType().getId(), good.getName()));
        Pageable pageable = PageRequest.of(0, 15);
        Page<Comment> commentsPage = commentRepository.findByGoodId(dto.id, pageable);
        int allSizeByGoodId = commentRepository.findAllSizeByGoodId(dto.id);
        if (pageable.getPageSize()< allSizeByGoodId) {
            List<Comment> allByGoodId = commentRepository.findAllByGoodId(dto.id);
            commentsPage = new PageImpl<>(allByGoodId,
                    PageRequest.of(0,allSizeByGoodId),allSizeByGoodId);
        }
        dto.setComments(commentsPage);
        dto.setColor(Objects.requireNonNullElse(
                Map.of(good.getColor().getId()
                        ,good.getColor().getName())
                ,null));
    }

    @Override
    public GoodGetDto update(GoodUpdateDto dto) {
        try {
            Good good = GOOD_MAPPER.toEntity(dto);
            method1(dto,good,typeRepository,colorRepository);
            goodRepository.updateGood(good.getId(),good.getColor(),good.getName(),good.getType(),
                    good.getCount(),good.getCommentsId(),good.getDescription(),good.getImagesId(),
                    good.getPrice(),good.getOrdersCount(),good.getDiscountPrice());
            Good updatedGood = goodRepository.findByIdAndBlockedFalse(good.getId())
                    .orElseThrow(NotFoundException::new);
            GoodGetDto dto1 = GOOD_MAPPER.toDto(updatedGood);
            method2(dto1,updatedGood,commentRepository);
            return dto1;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public GoodGetDto get(UUID id) {
        try {
            Good good = goodRepository.findById(id).orElseThrow(NotFoundException::new);
            GoodGetDto dto = GOOD_MAPPER.toDto(good);
            method2(dto,good,commentRepository);
            return dto;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public void delete(UUID id) {
        try {
            goodRepository.updateGoodBlockedById(true,id);
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public Page<GoodGetDto> find(Pageable pageable) {
        try {
            Page<Good> all = goodRepository.findAllByBlockedFalse(pageable);
            Page<GoodGetDto> dto = GOOD_MAPPER.toDto(all);
            return methodForList(all, dto, commentRepository);
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public Page<GoodGetDto> find(Pageable pageable, String name) {
        try {
            String[] split = name.split("\\s+");
            List<Double> doubles = Arrays.stream(split)
                    .filter(s -> s.matches("[0-9]"))
                    .map(Double::valueOf)
                    .toList();
            Page<Good> result;
            List<String> list = Arrays.stream(split).toList();
            list = list.stream()
                    .map(String::toLowerCase)
                    .toList();
            if (doubles.isEmpty()) {
                result = goodRepository.findAllByName(list,pageable);
            }else {
                result = goodRepository.findAllByName(list,doubles,pageable);
            }
            if (pageable.getPageSize()>result.getContent().size()) {
                result = new PageImpl<>(result.getContent(),PageRequest.of(0,result.getContent().size()),result.getContent().size());
            }
            Page<GoodGetDto> dto = GOOD_MAPPER.toDto(result);

            return methodForList(result, dto, commentRepository);
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }
    private static Page<GoodGetDto> methodForList(Page<Good> result,Page<GoodGetDto> dto,CommentRepository commentRepository){
        List<GoodGetDto> contentDto = dto.getContent();
        List<Good> content = result.getContent();
        if (!contentDto.isEmpty()) {
            for (int i = 0; i < content.size(); i++) {
                method2(contentDto.get(i), content.get(i), commentRepository);
            }
        }
        return new PageImpl<>(contentDto,dto.getPageable(),contentDto.size());
    }

    @Override
    public Page<GoodGetDto> find(Pageable pageable, GoodCriteria criteria) {
        try {
            Page<Good> result = goodRepository.findByCriteria(criteria.color_id,
                    criteria.startPrice,criteria.endPrice,criteria.type_id,pageable);
            int size = result.getContent().size();
            if (pageable.getPageSize()> size) {
                result = new PageImpl<>(result.getContent(),PageRequest.of(0,size),size);
            }
            Page<GoodGetDto> dto = GOOD_MAPPER.toDto(result);
            return methodForList(result, dto, commentRepository);
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }
}
