package com.ye.wm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ye.wm.entity.Orders;
import org.springframework.core.annotation.Order;

public interface OrderService extends IService<Orders> {


    void submit(Orders orders);
}
