package com.ye.wm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ye.wm.common.CustomException;
import com.ye.wm.entity.Category;
import com.ye.wm.entity.Dish;
import com.ye.wm.entity.Setmeal;
import com.ye.wm.mapper.CategoryMapper;
import com.ye.wm.service.CategoryService;
import com.ye.wm.service.DishService;
import com.ye.wm.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    /**
     * 根据id删除分类，删除前判断是否有关联
     * @param ids
     */

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long ids) {

        LambdaQueryWrapper<Dish> dQueryWrapper = new LambdaQueryWrapper<>();
        dQueryWrapper.eq(Dish::getCategoryId,ids);
        int count1 = dishService.count(dQueryWrapper);
        if (count1 > 0){

            throw new CustomException("当前分类关联了菜品，无法删除");

        }

        LambdaQueryWrapper<Setmeal> sQueryWrapper = new LambdaQueryWrapper<>();
        sQueryWrapper.eq(Setmeal::getCategoryId,ids);
        int count2 = setmealService.count(sQueryWrapper);
        if (count2 >0 ){

            throw new CustomException("当前分类关联了套餐，无法删除");

        }
        super.removeById(ids);
    }
}
