package com.example.demo.service;

import com.example.demo.dto.good_dto.GoodCreateDto;
import com.example.demo.dto.good_dto.GoodCriteria;
import com.example.demo.dto.good_dto.GoodGetDto;
import com.example.demo.dto.good_dto.GoodUpdateDto;
import com.example.demo.entity.Color;
import com.example.demo.entity.Good;
import com.example.demo.entity.Type;
import com.example.demo.exception.BadParamException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.ColorRepository;
import com.example.demo.repository.GoodRepository;
import com.example.demo.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.demo.mapper.GoodMapper.GOOD_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodServiceImpl implements GoodService {
    private final ColorRepository colorRepository;
    private final TypeRepository typeRepository;
    private final GoodRepository goodRepository;
    private final MultimediaService multimediaService;

    @Override
    public GoodGetDto save(GoodCreateDto dto) {
            dto.images.forEach(s -> {
                if (!multimediaService.isExist(s)) {
                    throw new BadParamException();
                }
            });
            if (dto.videoPath!=null && !multimediaService.isExist(dto.videoPath)) {
                throw new BadParamException();
            }
            Good good = GOOD_MAPPER.toEntity(dto);
            method1(dto,good,typeRepository,colorRepository);
            Good saved = goodRepository.save(good);
            GoodGetDto dto1 = GOOD_MAPPER.toDto(saved);
            method2(dto1,good);
            return dto1;
    }

    private static void method1(GoodCreateDto dto,Good good,
                                TypeRepository typeRepository,
                                ColorRepository colorRepository){
        Long typeId = dto.getType_id();
        Type type = typeRepository.findById(typeId)
                .orElseThrow(NotFoundException::new);
        good.setType(type);
        Long colorId = dto.getColor_id();
        if (colorId !=null) {
            colorRepository.findById(colorId).ifPresent(good::setColor);
        }
    }
    private static void method1(GoodUpdateDto dto,Good good,
                                TypeRepository typeRepository,
                                ColorRepository colorRepository){
        Long typeId = dto.getType_id();
        Type type = typeRepository
                .findById(typeId).orElseThrow(NotFoundException::new);
        good.setType(type);
        Long colorId = dto.getColor_id();
        if (colorId !=null) {
            colorRepository.findById(colorId).ifPresent(good::setColor);
        }
    }
    private static void method2(GoodGetDto dto,Good good){
        dto.setColor(Map.of(good.getColor().getId(),good.getColor().getName()));
        dto.setType(Map.of(good.getType().getId(), good.getName()));
        dto.setColor(Objects.requireNonNullElse(
                Map.of(good.getColor().getId()
                        ,good.getColor().getName())
                ,null));
    }

    @Override
    public GoodGetDto update(GoodUpdateDto dto) {
            dto.images.forEach(s -> {
                if (!multimediaService.isExist(s)) {
                    throw new BadParamException();
                }
            });
            if (dto.videoPath!=null && !multimediaService.isExist(dto.videoPath)) {
                throw new BadParamException();
            }
            Good good = GOOD_MAPPER.toEntity(dto);
            method1(dto,good,typeRepository,colorRepository);
            goodRepository.updateGood(good.getId(),good.getColor(),good.getName(),good.getType(),
                    good.getCount(),good.getDescription(),good.getImages(),
                    good.getPrice(),good.getOrdersCount(),good.getDiscountPrice(),good.getVideoPath());
            Good updatedGood = goodRepository.findByIdAndBlockedFalse(good.getId())
                    .orElseThrow(NotFoundException::new);
            GoodGetDto dto1 = GOOD_MAPPER.toDto(updatedGood);
            method2(dto1,updatedGood);
            return dto1;
    }

    @Override
    public GoodGetDto get(UUID id) {
            Good good = goodRepository.findById(id).orElseThrow(NotFoundException::new);
            GoodGetDto dto = GOOD_MAPPER.toDto(good);
            method2(dto,good);
            return dto;
    }

    @Override
    public void delete(UUID id) {
            Good good = goodRepository.findById(id)
                    .orElseThrow(NotFoundException::new);
            List<String> images = good.getImages();
            String videoPath = good.getVideoPath();
            if (images!=null) {
                images.forEach(s -> multimediaService.delete(videoPath));
            }
            if (videoPath!=null) {
                multimediaService.delete(videoPath);
            }
            goodRepository.updateGoodBlockedById(true,id);
    }

    @Override
    public Page<GoodGetDto> find(Pageable pageable) {
            Page<Good> all = goodRepository.findAllByBlockedFalse(pageable);
            Page<GoodGetDto> dto = GOOD_MAPPER.toDto(all);
            return methodForList(all, dto);
    }

    @Override
    public Page<GoodGetDto> find(Pageable pageable, String name) {
            String[] split = name.split("[+]");
            List<Double> doubles = Arrays.stream(split)
                    .filter(s -> s.matches("[0-9]"))
                    .map(Double::valueOf)
                    .toList();
            Page<Good> result;
            List<String> list = Arrays.stream(split).toList();
            list = list.stream()
                    .map(String::toLowerCase)
                    .toList();
            result = goodRepository.findAllByName(list,doubles,pageable);
            Page<GoodGetDto> dto = GOOD_MAPPER.toDto(result);

            return methodForList(result, dto);
    }
    private static Page<GoodGetDto> methodForList(Page<Good> result,Page<GoodGetDto> dto){
        List<GoodGetDto> contentDto = dto.getContent();
        List<Good> content = result.getContent();
        if (!contentDto.isEmpty()) {
            for (int i = 0; i < content.size(); i++) {
                method2(contentDto.get(i), content.get(i));
            }
        }
        return new PageImpl<>(contentDto,dto.getPageable(),contentDto.size());
    }

    @Override
    public Page<GoodGetDto> find(Pageable pageable, GoodCriteria criteria) {
            Type type = typeRepository.findById(criteria.type_id)
                    .orElseThrow(NotFoundException::new);
            Color color = colorRepository.findById(criteria.color_id)
                    .orElseThrow(NotFoundException::new);
            Page<Good> result = goodRepository.findByCriteria(color,
                    criteria.startPrice,criteria.endPrice,type,pageable);
            Page<GoodGetDto> dto = GOOD_MAPPER.toDto(result);
            return methodForList(result, dto);
    }
}
