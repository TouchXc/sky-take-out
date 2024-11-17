package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
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
    @Autowired
    private DishMapper dishMapper;

    /**
     * 更新套餐信息
     * @param setmealDTO
     */
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        //更新套餐
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);
        //更新套餐关联的菜品--可以先删后加
        Long setmealId = setmealDTO.getId();
        setMealDishMapper.deleteBySetMealId(setmealId);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        setMealDishMapper.insertBatch(setmealDishes);
    }

    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        int pageNum = setmealPageQueryDTO.getPage();
        int pageSize = setmealPageQueryDTO.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    public void chStatus(Integer status, Long id) {
        if (status.equals(StatusConstant.ENABLE)){
          List<Dish>  dishList = dishMapper.getBySetMealId(id);
          if (dishList!=null && dishList.size()>0){
                dishList.forEach(dish -> {
                    if(StatusConstant.DISABLE.equals(dish.getStatus())){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
          }
        }
        Setmeal setmeal = Setmeal.builder().id(id).status(status).build();
        setmealMapper.update(setmeal);
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        ids.forEach(id->{
            Setmeal setmeal = setmealMapper.getById(id);
            //检查套餐是否在售，如果是在售就无法删除
            if(setmeal!=null && setmeal.getStatus() == StatusConstant.ENABLE){
                throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });
        //删除套餐
        ids.forEach(setMealId->{
            setmealMapper.deleteById(setMealId);
            setMealDishMapper.deleteBySetMealId(setMealId);
        });
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
