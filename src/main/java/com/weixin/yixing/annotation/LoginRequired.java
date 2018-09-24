package com.weixin.yixing.annotation;

import java.lang.annotation.*;

/**
 * 登录注解
 * @Author chengtinghua
 * @Date 2017/6/22 16:17
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginRequired {
}
