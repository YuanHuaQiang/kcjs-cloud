package com.kcjs.cloud.mq.service.seckill.impl;

import com.kcjs.cloud.api.account.AccountService;
import com.kcjs.cloud.api.order.OrderService;
import com.kcjs.cloud.api.storage.StorageService;
import com.kcjs.cloud.mq.service.seckill.SeckillConsumerService;
import com.kcjs.cloud.mysql.pojo.Order;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.random.RandomGenerator;

@Service
public class SeckillConsumerServiceImpl implements SeckillConsumerService {


    @DubboReference
    private StorageService storageService;
    @DubboReference
    private OrderService orderService;
    @DubboReference
    private AccountService accountService;


    /**
     * 处理消息以执行秒杀操作和后续业务流程
     *
     * @param msg 消息字符串，格式为"userId:productId"，用于识别用户和产品
     */
    @Override
    @GlobalTransactional(name = "seckill-global-tx", rollbackFor = Exception.class)
    public void consumer(String msg) {
        String[] split = msg.split(":");
        String userId = split[0];
        String productId = split[1];

        BigDecimal amount = BigDecimal.valueOf(RandomGenerator.getDefault().nextDouble(100, 1000));
        //本应调用其他服务的接口，这里直接调用provider模块
        // 秒杀成功继续业务
        storageService.decrease(Long.valueOf(productId), 1);

        Order order = new Order();
        order.setUserId(Long.valueOf(userId));
        order.setProductId(Long.valueOf(productId));
        order.setCount(1);
        order.setStatus(0);//待支付订单
        order.setAmount(amount);
        order = orderService.saveOrder(order);//若为先创建后支付模式则扣库存和创建订单不需要事物管理


        //不走事务
        accountService.decrease(Long.valueOf(userId), amount);

        order.setStatus(1);//若余额支付成功，则修改订单状态为已支付
        orderService.saveOrder(order);
    }
}
