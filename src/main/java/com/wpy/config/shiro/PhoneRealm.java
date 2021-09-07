package com.wpy.config.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wpy.config.jwt.JwtUtils;
import com.wpy.constant.SqlConstant;
import com.wpy.dto.UserInfoDto;
import com.wpy.entity.SysUser;
import com.wpy.enums.TypeEnums;
import com.wpy.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 手机号登录realm类
 * @author pywang
 * @date 2020/12/6
 */
@Slf4j
public class PhoneRealm extends AuthorizingRealm {

    @Autowired
    SysUserService sysUserService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //传入的参数，校验账户是否存在
        PhoneTokenDto phoneTokenDto=(PhoneTokenDto) authenticationToken;
        String phone = phoneTokenDto.getPhone();
        SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>()
                .eq(SqlConstant.PHONE, phone));
        if(user==null){
            log.error("该手机号不存在");
            throw new DisabledAccountException("手机号不存在");
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
        return token instanceof PhoneTokenDto;
    }
}
