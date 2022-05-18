package com.six.yoblog.controller;

import com.six.yoblog.service.LoginService;
import com.six.yoblog.vo.Result;
import com.six.yoblog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("login")
public class LoginController {

   @Autowired
   private LoginService loginService;

   // 登录 验证用户 访问用户表
    @PostMapping
    public Result login(@RequestBody LoginParam loginParam){
        return loginService.login(loginParam);
    }
}
