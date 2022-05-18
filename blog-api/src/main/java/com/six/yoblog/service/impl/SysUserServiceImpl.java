package com.six.yoblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.six.yoblog.dao.mapper.SysUserMapper;
import com.six.yoblog.dao.pojo.SysUser;
import com.six.yoblog.service.LoginService;
import com.six.yoblog.service.SysUserService;
import com.six.yoblog.vo.ErrorCode;
import com.six.yoblog.vo.LoginUserVO;
import com.six.yoblog.vo.Result;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired(required = false)
    private SysUserMapper sysUserMapper;

    @Autowired
    private LoginService loginService;

    @Override
    public SysUser findUserByid(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser==null){
            sysUser = new SysUser();
            sysUser.setNickname("站长亲起");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword,password);
        queryWrapper.select(SysUser::getId,SysUser::getAccount,SysUser::getAvatar,SysUser::getNickname);
        queryWrapper.last("limit 1");
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        return sysUser;
    }

    @Override
    public Result findUserByToken(String token) {
        /**
         * 1. token 合法性校验
         *    是否为空，解析是否成功 redis是否存在
         * 2. 如果校验失败，返回失败
         * 3. 如果校验成功，返回对应的结果LoginUserVO
         */
       SysUser sysUser = loginService.checkToken(token);
       if(sysUser == null){
               return Result.fail(ErrorCode.TOKEN_ERROE.getCode(),ErrorCode.TOKEN_ERROE.getMsg());
       }
        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setAccount(sysUser.getAccount());
        loginUserVO.setAvatar(sysUser.getAvatar());
        loginUserVO.setId(sysUser.getId());
        loginUserVO.setNickname(sysUser.getNickname());
        return Result.success(loginUserVO);
    }
}
