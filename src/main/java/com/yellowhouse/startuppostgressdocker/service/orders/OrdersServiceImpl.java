package com.yellowhouse.startuppostgressdocker.service.orders;

import com.yellowhouse.startuppostgressdocker.controller.ResourceNotFoundException;
import com.yellowhouse.startuppostgressdocker.model.clothes.Clothes;
import com.yellowhouse.startuppostgressdocker.model.orders.Order;
import com.yellowhouse.startuppostgressdocker.repository.orders.OrdersRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.util.ReflectionUtil;
import org.aspectj.weaver.ast.Or;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
@Service
public class OrdersServiceImpl implements OrdersService{

    @Autowired
    public OrdersRepository ordersRepository;

    @Override
    public void createOrder(Order order) {
        ordersRepository.save(order);
        log.info("Создана запись о заказе ");
    }

    @Override
    public List<Order> readAllOrders() {
        List<Order> orderList = ordersRepository.findAll();
        log.info("Возвращены все заказы ");
        return new ArrayList<>(orderList);
    }

    @Override
    public Order readOrderById(UUID orderId) {
        Order order = ordersRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order not found " + orderId));
        log.info("Получена вещь " + orderId);
        return order;
    }

    @Override
    public boolean deleteOrderById(UUID orderId) {
        boolean flag = false;
        try {
            ordersRepository.deleteById(orderId);
        } catch (Exception e) {
            log.info("Заказ не найден");
            throw new ResourceNotFoundException("Order not found" + orderId);
        }

        log.info("Удален заказ " + orderId);
        return flag = true;
    }

    @Override
    public void update(Order order, UUID orderId) {
        try {
            ordersRepository.findById(orderId);
            ordersRepository.save(order);
        } catch (Exception e) {
            log.info("Вещь не найдена");
            throw new ResourceNotFoundException("Clothes not found" + orderId);
        }
    }

    @Override
    public Order patch(UUID orderId, Map<Object, Object> fields) {
        Optional<Order> order = ordersRepository.findById(orderId);
        if (order.isPresent()){
            fields.forEach((key,value) ->{
                Field field = ReflectionUtils.findField(Order.class, key.toString());
                field.setAccessible(true);
                if (field.getType().equals(UUID.class)){
                    ReflectionUtils.setField(field,order.get(),UUID.fromString(value.toString()));
                } else if (field.getType().equals(int.class)) {
                    ReflectionUtils.setField(field,order.get(),Integer.valueOf(value.toString()));
                }
            });
            Order updatedOrder = ordersRepository.save(order.get());
            return updatedOrder;
        } else {
            throw new ResourceNotFoundException("Запись с заказом по заданному id не найдена");
        }
    }

}
