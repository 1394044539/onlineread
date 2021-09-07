package com.wpy.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wpy.config.jwt.JwtToken;
import com.wpy.config.jwt.JwtUtils;
import com.wpy.constant.SqlConstant;
import com.wpy.entity.SysUser;
import com.wpy.enums.ResponeseCode;
import com.wpy.exception.RequestException;
import com.wpy.mapper.SysUserMapper;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 13940
 * @date 2021/3/2
 */
public class ShiroUtils {

    public static SysUser getSysUser(HttpServletRequest request){
        String authorization=request.getHeader("authorization");
        if(authorization==null){
            authorization=request.getHeader("Authorization");
            if(authorization==null){
                authorization=request.getParameter("authorization");
                if(authorization==null){
                    authorization=request.getParameter("Authorization");
                }
            }
        }
        if(StringUtils.isEmpty(authorization)){
            throw RequestException.fail(ResponeseCode.NOT_AUTHORIZATION.getMsg());
        }
        JwtToken jwtInfo = JwtUtils.getJwtInfo(authorization);
        SysUser sysUser = SpringUtils.getBean(SysUserMapper.class).selectOne(new QueryWrapper<SysUser>()
                .eq(SqlConstant.ACCOUNT_NAME, jwtInfo.getAccountName()));
        if(sysUser==null){
            throw RequestException.fail(ResponeseCode.USER_NOT_EXISTS.getMsg());
        }
        return sysUser;
    }
}
