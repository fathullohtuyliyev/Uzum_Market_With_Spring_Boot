package com.example.demo.service;

import com.example.demo.dto.good_dto.GoodCreateDto;
import com.example.demo.dto.good_dto.GoodCriteria;
import com.example.demo.dto.good_dto.GoodGetDto;
import com.example.demo.dto.good_dto.GoodUpdateDto;
import com.example.demo.entity.Good;
import com.example.demo.entity.Type;
import com.example.demo.exception.NotFoundException;
import com.example.demo.nosql.Comments;
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
            Long typeId = dto.getType_id();
            Type type = typeRepository.findById(typeId).orElseThrow(NotFoundException::new);
            good.setType(type);
            Long colorId = dto.getColor_id();
            if (colorId !=null) {
                colorRepository.findById(colorId).ifPresent(good::setColor);
            }
            Good saved = goodRepository.save(good);
            GoodGetDto dto1 = GOOD_MAPPER.toDto(saved);
            dto1.setColor(Map.of(saved.getColor().getId(),saved.getColor().getName()));
            dto1.setType(Map.of(saved.getType().getId(), saved.getName()));
            Pageable pageable = PageRequest.of(0, 15);
            Page<Comments> commentsPage = commentRepository.findByGoodId(dto1.id, pageable);
            int allSizeByGoodId = commentRepository.findAllSizeByGoodId(dto1.id);
            if (pageable.getPageSize()< allSizeByGoodId) {
                List<Comments> allByGoodId = commentRepository.findAllByGoodId(dto1.id);
                commentsPage = new PageImpl<>(allByGoodId,
                        PageRequest.of(0,allSizeByGoodId),allSizeByGoodId);
            }
            dto1.setComments(commentsPage);
            dto1.setColor(Objects.requireNonNullElse(
                    Map.of(saved.getColor().getId()
                    ,saved.getColor().getName())
                    ,null));
            return dto1;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public GoodGetDto update(GoodUpdateDto dto) {
        return null;
    }

    @Override
    public GoodGetDto get(UUID id) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public Page<GoodGetDto> find(Pageable pageable) {
        return null;
    }

    @Override
    public Page<GoodGetDto> find(Pageable pageable, String name) {
        return null;
    }

    @Override
    public Page<GoodGetDto> find(Pageable pageable, GoodCriteria criteria) {
        return null;
    }
}
