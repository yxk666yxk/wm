package com.ye.wm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ye.wm.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long ids);
}
