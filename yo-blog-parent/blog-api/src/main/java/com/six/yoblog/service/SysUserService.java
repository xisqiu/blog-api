package com.six.yoblog.service;

import com.six.yoblog.dao.pojo.SysUser;
import com.six.yoblog.vo.Result;

public interface SysUserService {

    SysUser findUserByid(Long id);

    /**
     * 查找 用户信息
     * @param account
     * @param password
     * @return
     */
    SysUser findUser(String account, String password);

    /**
     * 根据token查询用户信息
     * @param token
     * @return
     */
    Result findUserByToken(String token);

    /**
     * 添加用户信息
     * @param sysUser
     */
    void save(SysUser sysUser);

    SysUser findUserByAccount(String account);
}
