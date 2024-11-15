package com.sky.service.impl;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Transactional
    public void update(SetmealDTO setmealDTO) {
        //
    }

    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        return null;
    }

    public void chStatus(Integer status, Long id) {

    }

    @Transactional
    public void deleteBatch(List<Long> ids) {

    }

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Transactional
    public void add(SetmealDTO setmealDTO) {
        //存套餐信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);
        Long setmealId = setmeal.getId();
        //存套餐-菜品信息表
        List<SetmealDish> setMealDishList = setmealDTO.getSetmealDishes();
        setMealDishList.forEach(setMealDish -> {
            setMealDish.setSetmealId(setmealId);
        });
        setMealDishMapper.insertBatch(setMealDishList);
    }

    public SetmealVO getById(Long id) {
        //查套餐信息
        Setmeal setMeal = setmealMapper.getById(id);
        //查套餐对应菜品信息
        List<SetmealDish> setmealDishList = setMealDishMapper.getByMealId(id);
        //拼成SetmealVO返回
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setMeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishList);
        return setmealVO;
    }
}
