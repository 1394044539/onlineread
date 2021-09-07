package com.wpy;

import com.wpy.config.jedis.RedisTemplateUtils;
import com.wpy.constant.CacheConstant;
import com.wpy.entity.SysUser;
import com.wpy.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 自定义启动类
 * @author 13940
 */
@Component
@Slf4j
public class CustomApplication implements ApplicationRunner {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplateUtils redisTemplateUtils;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        //查询用户信息
        List<SysUser> sysUsers = sysUserService.getBaseMapper().selectList(null);
        log.info("保存用户信息进入缓存：");
        for (SysUser sysUser : sysUsers) {
            log.info(sysUser.toString());
        }
        //根据id分组
        Map<Object, Object> userMap = sysUsers.stream().collect(
                Collectors.toMap(SysUser::getId, Function.identity(), (key1, key2) -> key2));
        //设置到缓存
        redisTemplateUtils.setMap(CacheConstant.USER_INFO,userMap);
        log.info("保存用户信息成功！");
    }
}
