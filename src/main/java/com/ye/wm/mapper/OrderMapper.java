package com.ye.wm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ye.wm.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.core.annotation.Order;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
