package com.ye.wm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ye.wm.dto.DishDto;
import com.ye.wm.entity.Dish;

public interface DishService extends IService<Dish> {

    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
