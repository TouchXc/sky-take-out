package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;


    @PostMapping
    @ApiOperation("新增菜品")
    public Result<String> save(@RequestBody DishDTO dishDTO) {
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    @ApiOperation("根据id查询菜品")
    @GetMapping("/{id}")
    public Result<DishVO> getDishById(@PathVariable Long id) {
        DishVO dish = dishService.getByIdWithFlavor(id);
        return Result.success(dish);
    }

    @ApiOperation("根据分类id查询菜品")
    @GetMapping("/list")
    public Result<List<Dish>> getDishListByCategoryId(@RequestParam Long categoryId) {
        List<Dish> list = dishService.getListByCategoryId(categoryId);
        return Result.success(list);
    }

    @ApiOperation("菜品分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        PageResult dishList = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(dishList);
    }

    @ApiOperation("修改菜品")
    @PutMapping
    public Result<String> updateDish(@RequestBody DishDTO dishDTO) {
        dishService.update(dishDTO);
        return Result.success();
    }

    @ApiOperation("菜品起售停售")
    @PostMapping("/status/{status}")
    public Result<String> chStatus(@PathVariable String status ,@RequestParam String id) {
        return Result.success();
    }

    @ApiOperation("批量删除菜品")
    @DeleteMapping
    public Result<String> deleteDishByIds(@RequestParam List<Long> ids) {
        dishService.deleteBatch(ids);
        return Result.success();
    }
}
