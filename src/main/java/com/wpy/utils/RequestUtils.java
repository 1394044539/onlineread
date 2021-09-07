package com.wpy.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wpy.config.jwt.JwtToken;
import com.wpy.config.jwt.JwtUtils;
import com.wpy.constant.SqlConstant;
import com.wpy.constant.StrConstant;
import com.wpy.entity.SysUser;
import com.wpy.mapper.SysUserMapper;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求工具类
 * @author pywang
 * @date 2020/11/27
 */

public class RequestUtils {

    /**
     * 获取当前登录用户信息
     * @param request
     * @return
     */
    public static SysUser getSysUser(HttpServletRequest request){
        String token = request.getHeader(StrConstant.AUTHORIZATION);
        if(StringUtils.isEmpty(token)){
            return new SysUser();
        }
        JwtToken jwtInfo = JwtUtils.getJwtInfo(token);
        SysUser sysUser = SpringUtils.getBean(SysUserMapper.class).selectOne(new QueryWrapper<SysUser>()
                .eq(SqlConstant.ACCOUNT_NAME, jwtInfo.getAccountName()));
        return sysUser;
    }
}
