package com.ye.wm.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ye.wm.entity.ShoppingCart;
import com.ye.wm.mapper.ShoppingCartMapper;
import com.ye.wm.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
