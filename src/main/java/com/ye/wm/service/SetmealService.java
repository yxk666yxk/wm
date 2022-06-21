package com.ye.wm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ye.wm.dto.SetmealDto;
import com.ye.wm.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，保存套餐关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);


    /**
     * 删除套餐和关联方法
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

}
