package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;


public interface SetMealService {
    void update(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void chStatus(Integer status, Long id);

    void deleteBatch(List<Long> ids);

    void add(SetmealDTO setmealDTO);

    SetmealVO getById(Long id);
}
