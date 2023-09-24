package com.example.demo.service;

import com.example.demo.DemoApplication;
import com.example.demo.dto.order_dto.OrderCreateDto;
import com.example.demo.dto.order_dto.OrderGetDto;
import com.example.demo.dto.order_dto.OrderUpdateDto;
import com.example.demo.entity.*;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import static com.example.demo.mapper.DeliveryMapper.DELIVERY_MAPPER;
import static com.example.demo.mapper.GoodMapper.GOOD_MAPPER;
import static com.example.demo.mapper.OrderMapper.ORDER_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final StatusRepository statusRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final DeliveryPointRepository deliveryPointRepository;
    private final GoodRepository goodRepository;
    private final AuthUserRepository authUserRepository;
    private final OrderRepository orderRepository;

    @Override
    public OrderGetDto save(OrderCreateDto dto) {
        try {
            Order order = ORDER_MAPPER.toEntity(dto);

            method1(deliveryPointRepository,
                    authUserRepository,
                    order,
                    paymentTypeRepository,
                    goodRepository,
                    dto);
            Status startStatus;
            try {
                startStatus = statusRepository.findById(DemoApplication.statusId)
                        .orElseThrow(RuntimeException::new);
            }catch (Exception e){
                e.printStackTrace();
                DemoApplication.stop();
                return null;
            }
            order.setStatus(startStatus);
            Order saved = orderRepository.save(order);
            OrderGetDto dto1 = ORDER_MAPPER.toDto(saved);

            method2(dto1,saved);

            return dto1;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    private static void method2(OrderGetDto dto, Order order) {
        dto.setGood(GOOD_MAPPER.toDto(order.getGood()));

        PaymentType paymentType = order.getPaymentType();
        dto.setPaymentType(Map.of(paymentType.getName(),paymentType.isActive()));

        dto.setDeliveryGetDto(DELIVERY_MAPPER.toDto(order.getDeliveryPoint()));
    }

    private static void method1(DeliveryPointRepository deliveryPointRepository,
                                AuthUserRepository authUserRepository,
                                Order order,
                                PaymentTypeRepository paymentTypeRepository,
                                GoodRepository goodRepository,
                                OrderCreateDto dto
                                ) {
        AuthUser authUser = authUserRepository.findById(dto.authUserId).orElseThrow(NotFoundException::new);
        order.setAuthUser(authUser);

        Good good = goodRepository.findById(dto.goodId).orElseThrow(NotFoundException::new);
        order.setGood(good);

        DeliveryPoint deliveryPoint = deliveryPointRepository.findById(dto.deliveryGetDto.id).orElseThrow(NotFoundException::new);
        order.setDeliveryPoint(deliveryPoint);

        PaymentType paymentType =  paymentTypeRepository.findByName(dto.paymentType);
        order.setPaymentType(paymentType);

        order.setTime(LocalDateTime.now());
    }

    @Override
    public OrderGetDto update(OrderUpdateDto dto) {
        try {
            LocalDateTime update = dto.update;

            DeliveryPoint deliveryPoint = DELIVERY_MAPPER.toEntity(dto.deliveryGetDto);

            PaymentType paymentType = paymentTypeRepository.findByName(dto.paymentType);

            UUID id = dto.id;

            orderRepository.updateOrder(update,deliveryPoint,paymentType,id);

            Order order = orderRepository.findById(id).orElseThrow(NotFoundException::new);
            OrderGetDto dto1 = ORDER_MAPPER.toDto(order);

            method2(dto1, order);

            return dto1;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public OrderGetDto updateStatus(UUID id, String statusName) {
        try {
            Status status = statusRepository.findByName(statusName)
                    .orElseThrow(NotFoundException::new);
            orderRepository.updateOrder(LocalDateTime.now(),status,id);
            Order order = orderRepository.findById(id)
                    .orElseThrow(NotFoundException::new);
            OrderGetDto dto = ORDER_MAPPER.toDto(order);
            log.info("{} updated",dto);
            method2(dto,order);
            return dto;
        }catch (Exception e){
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public OrderGetDto get(UUID id) {
        try {
            Order order = orderRepository.findById(id).orElseThrow(NotFoundException::new);
            OrderGetDto dto = ORDER_MAPPER.toDto(order);
            method2(dto, order);
            return dto;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }

    @Override
    public Page<OrderGetDto> orders(UUID userId, Pageable pageable) {
        try {
            Page<Order> result = orderRepository.findAllByUserId(userId,pageable);
            int allByUserId = orderRepository.sizeAllByUserId(userId);
            if (allByUserId <pageable.getPageSize()) {
                result = new PageImpl<>(orderRepository.findAllByUserId(userId),
                        PageRequest.of(0,allByUserId),allByUserId);
            }
            Page<OrderGetDto> dto = ORDER_MAPPER.toDto(result);
            if (dto.isEmpty()) {
                return dto;
            }
            for (int i = 0; i < dto.getContent().size(); i++) {
                method2(dto.getContent().get(i),result.getContent().get(i));
            }
            return dto;
        }catch (Exception e){
            e.printStackTrace();
            Arrays.stream(e.getStackTrace())
                    .forEach(stackTraceElement -> log.warn("{}",stackTraceElement));
            throw new RuntimeException();
        }
    }
}
