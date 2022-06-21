package com.ye.wm.dto;

import com.ye.wm.entity.Setmeal;
import com.ye.wm.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
