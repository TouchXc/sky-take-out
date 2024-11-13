package com.sky.service.impl;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetMealServiceImpl implements SetMealService {

    @Transactional
    public void update(SetmealDTO setmealDTO) {

    }

    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        return null;
    }

    public void chStatus(Integer status, Long id) {

    }

    @Transactional
    public void deleteBatch(List<Long> ids) {

    }

    @Transactional
    public void add(SetmealDTO setmealDTO) {

    }

    public SetmealVO getById(Long id) {
        return null;
    }
}
