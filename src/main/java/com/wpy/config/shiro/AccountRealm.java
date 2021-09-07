package com.wpy.config.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wpy.config.jwt.JwtToken;
import com.wpy.config.jwt.JwtUtils;
import com.wpy.constant.SqlConstant;
import com.wpy.dto.UserInfoDto;
import com.wpy.entity.SysUser;
import com.wpy.enums.TypeEnums;
import com.wpy.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 账号登录Realm类
 * @author pywang
 * @date 2020/11/27
 */
@Slf4j
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    private SysUserService sysUserService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return new SimpleAuthorizationInfo();
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        //传入的参数，校验账户是否存在
        JwtToken jwtToken=(JwtToken) authenticationToken;
        String accountName = jwtToken.getAccountName();
        SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>()
                .eq(SqlConstant.ACCOUNT_NAME, accountName)
                .or()
                .eq(SqlConstant.PHONE,accountName)
                .last("limit 1"));
        if(user==null){
            log.error("该用户不存在");
            throw new DisabledAccountException("该用户不存在");
        }
        if(TypeEnums.DISABLE_STATUS.getCode().equals(user.getStatus())){
            log.error("该用户被禁用");
            throw new DisabledAccountException("该用户已被禁用");
        }
        //生成token
        String token = JwtUtils.createToken(user.getId(), user.getAccountName(), user.getPassword(),user.getUserName());
        UserInfoDto userInfoDto=new UserInfoDto();
        userInfoDto.setToken(token);
        userInfoDto.setUsername(user.getUserName());
        userInfoDto.setAccountName(user.getAccountName());
        userInfoDto.setRoleType(user.getRoleType());
        return new SimpleAuthenticationInfo(userInfoDto, user.getPassword(),getName());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }
}
