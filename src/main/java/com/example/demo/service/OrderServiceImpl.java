package com.example.demo.service;

import com.example.demo.DemoApplication;
import com.example.demo.dto.good_dto.GoodGetDto;
import com.example.demo.dto.order_dto.OrderCreateDto;
import com.example.demo.dto.order_dto.OrderGetDto;
import com.example.demo.dto.order_dto.OrderUpdateDto;
import com.example.demo.entity.*;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.DoubleStream;

import static com.example.demo.mapper.DeliveryMapper.DELIVERY_MAPPER;
import static com.example.demo.mapper.GoodMapper.GOOD_MAPPER;
import static com.example.demo.mapper.OrderMapper.ORDER_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final PromoCodeRepository promoCodeRepository;
    private final StatusRepository statusRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final DeliveryPointRepository deliveryPointRepository;
    private final GoodRepository goodRepository;
    private final AuthUserRepository authUserRepository;
    private final OrderRepository orderRepository;

    @Override
    public OrderGetDto save(OrderCreateDto dto) {
            Order order = ORDER_MAPPER.toEntity(dto);

            method1(deliveryPointRepository,
                    authUserRepository,
                    order,
                    paymentTypeRepository,
                    goodRepository,
                    dto);
            Status startStatus;
            try {
                startStatus = statusRepository.findByName("ORDER IS PREPARING")
                        .orElseThrow(RuntimeException::new);
            }catch (Exception e){
                e.printStackTrace();
                DemoApplication.stop();
                return null;
            }
            order.setStatus(startStatus);
            List<PromoCode> promoCodes = order.getProducts()
                    .stream()
                    .flatMap(good -> good.getPromoCodes().stream())
                    .toList();
            double sum = order.getProducts()
                    .stream()
                    .mapToDouble(value -> value.getPrice() - value.getDiscountPrice())
                    .sum();
            order.setPrice(sum);
            String promoCode = order.getPromoCode();
            List<String> list = promoCodes
                    .stream()
                    .map(PromoCode::getName)
                    .toList();

            if (!promoCodes.isEmpty() && promoCode !=null
                && !promoCode.isBlank() && list.contains(promoCode)) {
                PromoCode founded = promoCodeRepository.findByName(promoCode)
                        .orElseThrow(NotFoundException::new);
                Double discount = founded.getDiscount() * (order.getPrice() / 100);
                double summed = DoubleStream.of(order.getPrice(), discount).sum();
                order.setPrice(summed);
            }
            Order saved = orderRepository.save(order);
            OrderGetDto dto1 = ORDER_MAPPER.toDto(saved);

            method2(dto1,saved);

            return dto1;
    }

    private static void method2(OrderGetDto dto, Order order) {
        List<GoodGetDto> list = order.getProducts()
                .stream()
                .map(GOOD_MAPPER::toDto)
                .toList();
        dto.setGoods(list);

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

        List<Product> products = goodRepository.findAllById(dto.goodsId);
        order.setProducts(products);

        DeliveryPoint deliveryPoint = deliveryPointRepository.findById(dto.deliveryGetDto.id).orElseThrow(NotFoundException::new);
        order.setDeliveryPoint(deliveryPoint);

        PaymentType paymentType =  paymentTypeRepository.findByName(dto.paymentType);
        order.setPaymentType(paymentType);

        order.setTime(LocalDateTime.now());
    }

    @Override
    public OrderGetDto update(OrderUpdateDto dto) {
            LocalDateTime update = dto.update;

            DeliveryPoint deliveryPoint = DELIVERY_MAPPER.toEntity(dto.deliveryGetDto);

            PaymentType paymentType = paymentTypeRepository.findByName(dto.paymentType);

            UUID id = dto.id;

            orderRepository.updateOrder(update,deliveryPoint,paymentType,id);

            Order order = orderRepository.findById(id).orElseThrow(NotFoundException::new);
            OrderGetDto dto1 = ORDER_MAPPER.toDto(order);

            method2(dto1, order);

            return dto1;
    }

    @Override
    public OrderGetDto updateStatus(UUID id, String statusName) {
            Status status = statusRepository.findByName(statusName)
                    .orElseThrow(NotFoundException::new);
            orderRepository.updateOrder(LocalDateTime.now(),status,id);
            Order order = orderRepository.findById(id)
                    .orElseThrow(NotFoundException::new);
            OrderGetDto dto = ORDER_MAPPER.toDto(order);
            log.info("{} updated",dto);
            method2(dto,order);
            return dto;
    }

    @Override
    public OrderGetDto get(UUID id) {
            Order order = orderRepository.findById(id).orElseThrow(NotFoundException::new);
            OrderGetDto dto = ORDER_MAPPER.toDto(order);
            method2(dto, order);
            return dto;
    }

    @Override
    public Page<OrderGetDto> orders(UUID userId, Pageable pageable) {
            Page<Order> result = orderRepository.findAllByUserId(userId,pageable);
            Page<OrderGetDto> dto = ORDER_MAPPER.toDto(result);
            if (dto.isEmpty()) {
                return dto;
            }
            for (int i = 0; i < dto.getContent().size(); i++) {
                method2(dto.getContent().get(i),result.getContent().get(i));
            }
            return dto;
    }
}
