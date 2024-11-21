package com.sky.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.HttpMethod;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    private static String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";


    public User wxlogin(UserLoginDTO userLoginDTO) {
        String openid = getOpenid(userLoginDTO);
        if (openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        User user = userMapper.getByOpenid(openid);
        //新用户需要自动注册
        if (user == null){
            User newUser = User.builder().openid(openid).createTime(LocalDateTime.now()).build();
            userMapper.insert(newUser);
        }
        return user;
    }

    private String getOpenid(UserLoginDTO userLoginDTO) {
        //调用微信服务接口，获取openid
        Map<String, String> map = new HashMap<>();
        map.put("grant_type", "authorization_code");
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code", userLoginDTO.getCode());
        String resp = HttpClientUtil.doGet(WX_LOGIN_URL, map);
        JSONObject jsonObj = JSON.parseObject(resp);
        String openid = jsonObj.getString("openid");
        return openid;
    }
}

