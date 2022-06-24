package com.ye.wm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ye.wm.common.R;
import com.ye.wm.dto.DishDto;
import com.ye.wm.entity.Category;
import com.ye.wm.entity.Dish;
import com.ye.wm.entity.DishFlavor;
import com.ye.wm.service.CategoryService;
import com.ye.wm.service.DishFlavorService;
import com.ye.wm.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *  菜品管理
 */

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 增加菜品
     * @param dishDto
     * @return
     */

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page ,int pageSize,String name){


        //分页构造器
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDto = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //根据更新时间降序排序
        queryWrapper.orderByAsc(Dish::getSort);

        //调用方法查询
        dishService.page(pageInfo,queryWrapper);

        BeanUtils.copyProperties(pageInfo,dishDto,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list  = records.stream().map((item)->{
            DishDto dDto = new DishDto();

            BeanUtils.copyProperties(item,dDto);

            Long categoryId = item.getCategoryId();
            Category byId = categoryService.getById(categoryId);

            if (byId != null){
                String byIdName = byId.getName();
                dDto.setCategoryName(byIdName);
            }

            return dDto;
        }).collect(Collectors.toList());


        dishDto.setRecords(list);

        //返回查新信息
        return R.success(dishDto);

    }

    /**
     * 查询菜品基本信息和口味信息
     * @param id
     * @return
     */

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){


        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);

    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);

        //Set keys = redisTemplate.keys("dish_*");
        String keys = "dish_"+dishDto.getCategoryId()+"_1";

        redisTemplate.delete(keys);

        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){

        List<DishDto> dishDto  = null;

                String key = "dish_"+dish.getCategoryId()+"_"+dish.getStatus();

        dishDto = (List<DishDto>) redisTemplate.opsForValue().get(key);

        if (dishDto != null){
            return R.success(dishDto);
        }


        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getUpdateTime);
        List<Dish> dishList = dishService.list(queryWrapper);


        dishDto  = dishList.stream().map((item)->{
            DishDto dDto = new DishDto();

            BeanUtils.copyProperties(item,dDto);

            Long categoryId = item.getCategoryId();
            Category byId = categoryService.getById(categoryId);

            if (byId != null){
                String byIdName = byId.getName();
                dDto.setCategoryName(byIdName);
            }

            Long id = item.getId();

            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper();

            lambdaQueryWrapper.eq(DishFlavor::getDishId,id);

            List<DishFlavor> dlist = dishFlavorService.list(lambdaQueryWrapper);

            dDto.setFlavors(dlist);

            return dDto;
        }).collect(Collectors.toList());

        redisTemplate.opsForValue().set(key,dishDto,60, TimeUnit.MINUTES);

        return R.success(dishDto);
    }


}
