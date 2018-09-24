package com.weixin.yixing.interceptor;

import com.weixin.yixing.annotation.LoginRequired;
import com.weixin.yixing.config.RedisUtil;
import com.weixin.yixing.exception.CoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Created by wuchongqian1 on 2017/6/15.
*/
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            LoginRequired annotation = getClassOrMethodAnnotationByClassFirst(method);
            if (null != annotation) {

                if (redisUtil == null) {//解决service为null无法注入问题
                    BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                    redisUtil = (RedisUtil) factory.getBean("redisUtil");
                }
                String token = "";
                if (null != request.getParameter("token")) {
                    token = request.getParameter("token").toString();
                }
                boolean exist = redisUtil.exists(token);
                if (!exist) {
                    throw CoreException.of(CoreException.USER_LOGIN_INVALID);
                }
            }
        }
        return super.preHandle(request, response, handler);
    }

    /**
     * 查看方法是否有登录注解
     * @param method
     * @return
     */
    private LoginRequired getClassOrMethodAnnotationByClassFirst(HandlerMethod method) {
        LoginRequired annotation = method.getBeanType().getAnnotation(LoginRequired.class);
        if (null == annotation) {
            annotation = method.getMethodAnnotation(LoginRequired.class);
        }
        return annotation;
    }

}
