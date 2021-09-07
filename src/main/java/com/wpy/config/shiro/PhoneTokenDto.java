package com.wpy.config.shiro;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * 手机号登录认证dto
 * @author pywang
 * @date 2020/11/29
 */
@Data
public class PhoneTokenDto implements AuthenticationToken {

    /**
     * 手机号
     */
    private String phone;

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
