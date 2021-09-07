package com.wpy.config.shiro;

import com.wpy.config.jwt.JwtToken;
import com.wpy.config.jwt.JwtUtils;
import com.wpy.dto.UserInfoDto;
import com.wpy.utils.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * shiro自定义解密规则
 * @author pywang
 * @date
 */
public class CredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //token未加密
        JwtToken jwtToken=(JwtToken)token;
        PrincipalCollection principals = info.getPrincipals();
        UserInfoDto userInfoDto = (UserInfoDto)principals.getPrimaryPrincipal();
        //数据库中取出来已加密的
        Object credentials = getCredentials(info);
        if(StringUtils.isNotEmpty(jwtToken.getPassword())){
            Md5Hash md5Hash=new Md5Hash(jwtToken.getPassword(),userInfoDto.getAccountName(),2);
            if(!credentials.equals(md5Hash.toString())){
                throw new DisabledAccountException("密码错误");
            }
        }else {
            boolean verify = JwtUtils.verify(jwtToken.getToken(), jwtToken.getAccountName(), credentials.toString());
            if(!verify){
                throw new DisabledAccountException("认证失效");
            }
        }
        return true;
    }
}
