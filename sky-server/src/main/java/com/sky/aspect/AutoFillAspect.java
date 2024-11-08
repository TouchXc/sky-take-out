package com.sky.aspect;


import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclareAnnotation;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.beans.beancontext.BeanContext;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 实现公共字段自动填充
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        //获取方法签名对象
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        //获取注解对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        //获取操作类型
        OperationType operationType = autoFill.value();

        //拿到mapper方法的参数
        Object[] args = joinPoint.getArgs();
        if (args==null || args.length==0){
            return;
        }
        Object entity = args[0];
        LocalDateTime nowTime = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        if(operationType==OperationType.INSERT){
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setCreateTime.invoke(entity,nowTime);
                setUpdateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,nowTime);
                setCreateUser.invoke(entity,currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }else if (operationType==OperationType.UPDATE){
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setUpdateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,nowTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
