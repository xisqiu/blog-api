package com.six.yoblog.service;

import com.six.yoblog.dao.pojo.SysUser;
import com.six.yoblog.vo.Result;
import com.six.yoblog.vo.params.LoginParam;

public interface LoginService {

    /**
     * 登录功能
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    /**
     * 验证token
     * @param token
     * @return
     */
    SysUser checkToken(String token);

    Result logout(String token);
}
