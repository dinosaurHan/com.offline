package com.ofl.promotion.aop;

import java.lang.annotation.*;

/**
 * @VerifyToken：该注解为验证jwt token注解，会自动验证token的有效性。在需要的验证的方法（一般在controller的方法上）上加上此注解即可（配合条件：前端再head中传递userId和token两个字段）。
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface VerifyToken {

}
