package com.kcjs.cloud.api.order;


import com.kcjs.cloud.mysql.pojo.Order;

public interface OrderService {

    Order createOrder(Order order) ;
    Order findById(Long id) ;
}