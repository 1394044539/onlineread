package com.wpy.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

/**
 * jwt工具类
 * @author 13940
 * @date 2020/11/29
 */
@Slf4j
public class JwtUtils {

    @Value("${jwt.expire-time}")
    private static long expireTime;

    /**
     * 创建token
     * @return
     */
    public static String createToken(String id,String accountName,String password,String username){
        try {
            return JWT.create()
                    .withClaim("uid", id)
                    .withClaim("accountName", accountName)
                    .withClaim("username",username)
                    .withExpiresAt(new Date(System.currentTimeMillis() + expireTime))
                    .sign(Algorithm.HMAC256(password));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 校验token是否正确
     * @param token
     * @param accountName
     * @param password
     * @return
     */
    public static boolean verify(String token,String accountName,String password){
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(password))
                    .withClaim("accountName", accountName)
                    .build();
            verifier.verify(token);
            return true;
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 获取token中的信息
     */
    public static JwtToken getJwtInfo(String token){
        DecodedJWT jwt=JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();
        JwtToken jwtToken=new JwtToken();
        jwtToken.setAccountName(claims.get("accountName").asString());
        jwtToken.setUsername(claims.get("username").asString());
        jwtToken.setId(claims.get("uid").asString());
        return jwtToken;
    }
}
