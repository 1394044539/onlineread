package com.wpy.config.jwt;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * jwtToken
 * @author pywang
 * @date 2020/11/29
 */
@Data
public class JwtToken implements AuthenticationToken {

    private String token;

    private String username;

    private String accountName;

    private String password;

    private String id;

    public JwtToken(){}

    public JwtToken(String token, String accountName, String password){
        this.token=token;
        this.accountName=accountName;
        this.password=password;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return password;
    }
}
