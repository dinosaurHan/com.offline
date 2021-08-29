package com.ofl.promotion.aop;

import java.lang.annotation.*;

/**
 * 用户PC操作权限
 * @Author Mr.quan
 * @Date 2021/8/30 0:35
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface OperateAuth {

}
