package com.example.demo.service;

import com.example.demo.dto.good_dto.GoodCreateDto;
import com.example.demo.dto.good_dto.GoodCriteria;
import com.example.demo.dto.good_dto.GoodGetDto;
import com.example.demo.dto.good_dto.GoodUpdateDto;
import com.example.demo.entity.Color;
import com.example.demo.entity.Product;
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
            Product product = GOOD_MAPPER.toEntity(dto);
            method1(dto, product,typeRepository,colorRepository);
            Product saved = goodRepository.save(product);
            GoodGetDto dto1 = GOOD_MAPPER.toDto(saved);
            method2(dto1, product);
            return dto1;
    }

    private static void method1(GoodCreateDto dto, Product product,
                                TypeRepository typeRepository,
                                ColorRepository colorRepository){
        Long typeId = dto.getType_id();
        Type type = typeRepository.findById(typeId)
                .orElseThrow(NotFoundException::new);
        product.setType(type);
        Long colorId = dto.getColor_id();
        if (colorId !=null) {
            colorRepository.findById(colorId).ifPresent(product::setColor);
        }
    }
    private static void method1(GoodUpdateDto dto, Product product,
                                TypeRepository typeRepository,
                                ColorRepository colorRepository){
        Long typeId = dto.getType_id();
        Type type = typeRepository
                .findById(typeId).orElseThrow(NotFoundException::new);
        product.setType(type);
        Long colorId = dto.getColor_id();
        if (colorId !=null) {
            colorRepository.findById(colorId).ifPresent(product::setColor);
        }
    }
    private static void method2(GoodGetDto dto, Product product){
        dto.setColor(Map.of(product.getColor().getId(), product.getColor().getName()));
        dto.setType(Map.of(product.getType().getId(), product.getName()));
        dto.setColor(Objects.requireNonNullElse(
                Map.of(product.getColor().getId()
                        , product.getColor().getName())
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
            Product product = GOOD_MAPPER.toEntity(dto);
            method1(dto, product,typeRepository,colorRepository);
            goodRepository.updateGood(product.getId(), product.getColor(), product.getName(), product.getType(),
                    product.getCount(), product.getDescription(), product.getImages(),
                    product.getPrice(), product.getOrdersCount(), product.getDiscountPrice(), product.getVideoPath());
            Product updatedProduct = goodRepository.findByIdAndBlockedFalse(product.getId())
                    .orElseThrow(NotFoundException::new);
            GoodGetDto dto1 = GOOD_MAPPER.toDto(updatedProduct);
            method2(dto1, updatedProduct);
            return dto1;
    }

    @Override
    public GoodGetDto get(UUID id) {
            Product product = goodRepository.findById(id).orElseThrow(NotFoundException::new);
            GoodGetDto dto = GOOD_MAPPER.toDto(product);
            method2(dto, product);
            return dto;
    }

    @Override
    public void delete(UUID id) {
            Product product = goodRepository.findById(id)
                    .orElseThrow(NotFoundException::new);
            List<String> images = product.getImages();
            String videoPath = product.getVideoPath();
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
            Page<Product> all = goodRepository.findAllByBlockedFalse(pageable);
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
            Page<Product> result;
            List<String> list = Arrays.stream(split).toList();
            list = list.stream()
                    .map(String::toLowerCase)
                    .toList();
            result = goodRepository.findAllByName(list,doubles,pageable);
            Page<GoodGetDto> dto = GOOD_MAPPER.toDto(result);

            return methodForList(result, dto);
    }
    private static Page<GoodGetDto> methodForList(Page<Product> result, Page<GoodGetDto> dto){
        List<GoodGetDto> contentDto = dto.getContent();
        List<Product> content = result.getContent();
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
            Page<Product> result = goodRepository.findByCriteria(color,
                    criteria.startPrice,criteria.endPrice,type,pageable);
            Page<GoodGetDto> dto = GOOD_MAPPER.toDto(result);
            return methodForList(result, dto);
    }
}
