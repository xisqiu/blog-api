package com.six.yoblog.controller;

import com.six.yoblog.dao.pojo.SysUser;
import com.six.yoblog.until.UserThreadLocal;
import com.six.yoblog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping
    public Result test(){
        //  SysUser
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}
