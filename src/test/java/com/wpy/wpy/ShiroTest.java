package com.wpy.wpy;

import com.auth0.jwt.JWT;
import com.wpy.config.jwt.JwtToken;
import com.wpy.config.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ShiroTest {

    @Test
    public void test(){
        String wpy = JwtUtils.createToken("123", "wpy", "123","123");
        System.out.println(wpy);
//        eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIxMjMiLCJhY2NvdW50TmFtZSI6IndweSIsImV4cCI6MTYwNzAwODg0M30.bXbu-uFj6r7vX4iSGGHix4suVdDcH6PcvdHhiOVBosY
        boolean wpy1 = JwtUtils.verify(wpy, "wpy", "123");
        System.out.println(wpy1);
        JwtToken jwtInfo = JwtUtils.getJwtInfo(wpy);
        System.out.println(jwtInfo);


    }
}
