package com.sky.mapper;


import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.Map;

@Mapper
public interface UserMapper {
    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    /**
     * 新建用户
     * @param newUser
     */
    void insert(User newUser);

    /**
     * 根据条件动态统计用户数量
     * @param map
     * @return
     */
    Integer countByMap(Map<String, LocalDateTime> map);
}
