package com.sky.mapper;


import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    List<Long> getSetMealIdsByDishIds(List<Long> dishIds);

    /**
     * 根据套餐id查询套餐关联的菜品信息
     * @param setMealId
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setMealId}")
    List<SetmealDish> getByMealId(Long setMealId);

    /**
     * 批量插入套餐-菜品信息
     * @param setMealDishList
     */
    void insertBatch(List<SetmealDish> setMealDishList);
}
