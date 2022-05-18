package com.six.yoblog.controller;

import com.six.yoblog.service.LoginService;
import com.six.yoblog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("logout")
public class LogoutController {

    @Autowired
    private LoginService loginService;

    @GetMapping
    public Result loginout(@RequestHeader("Authorization") String token){
        return loginService.logout(token);
    }
}
