package com.six.yoblog.handler;

import com.alibaba.fastjson.JSON;
import com.six.yoblog.dao.pojo.SysUser;
import com.six.yoblog.service.LoginService;
import com.six.yoblog.until.UserThreadLocal;
import com.six.yoblog.vo.ErrorCode;
import com.six.yoblog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器实现
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 执行controller方法（handle)之前进行换行
         * 1. 需要判断请求的接口路径 是否为HandleMethod(controller方法)
         * 2. 判断token是否为空 如果为空 未登录 进行登录验证
         * 3. 如果token不为空，登录验证 logService checkToken 验证
         * 4. 如果认证成功 放行即可
         */
        if (!(handler instanceof HandlerMethod)){
            // handle 可能是 RequestResourceHandle 资源 springboot程序 访问静态资源 默认去classpath下的static 目录去查询
            return true;
        }
        String token = request.getHeader("Authorization");
        // 打印日志
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

        if (StringUtils.isBlank(token)){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //是登录状态，放行
        //我希望在controller中 直接获取用户的信息 怎么获取?
        UserThreadLocal.put(sysUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 如果不删除Threard中的信息，会造成内存泄漏
        UserThreadLocal.remove();
    }


}
