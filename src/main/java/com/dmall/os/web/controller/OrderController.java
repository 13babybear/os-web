package com.dmall.os.web.controller;

import com.dmall.os.order.spi.OrderSpi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 下单控制器
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderSpi orderSpi;


    /**
     * 模拟创建订单
     */
    @GetMapping
    public void createOrder() {
        //调用账户进行支付
        pay();

        //调用下单接口
        orderSpi.order();
    }

    /**
     * 模拟支付服务
     */
    private void pay() {
        System.out.println("支付成功！");
    }
}
