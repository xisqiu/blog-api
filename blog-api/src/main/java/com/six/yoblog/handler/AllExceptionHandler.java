package com.six.yoblog.handler;

import com.six.yoblog.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

// 对加了@controller的方法进行拦截处理  AOP的实现
@ControllerAdvice
public class AllExceptionHandler {

    // 进行异常处理 处理Exception.class 的异常
    @ExceptionHandler(Exception.class)
    //返回json 数据
    @ResponseBody
    public Result doException(Exception ex){
        ex.printStackTrace();
        return Result.fail(-999,"系统异常");
    }
}
