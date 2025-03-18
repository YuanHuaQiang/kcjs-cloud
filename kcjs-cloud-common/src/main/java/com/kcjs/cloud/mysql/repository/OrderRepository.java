package com.kcjs.cloud.mysql.repository;


import com.kcjs.cloud.mysql.pojo.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}