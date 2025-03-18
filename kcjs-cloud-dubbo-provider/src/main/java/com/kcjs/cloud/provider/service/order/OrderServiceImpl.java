package com.kcjs.cloud.provider.service.order;


import com.kcjs.cloud.api.order.OrderService;
import com.kcjs.cloud.mysql.pojo.Order;
import com.kcjs.cloud.mysql.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

@DubboService
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
}