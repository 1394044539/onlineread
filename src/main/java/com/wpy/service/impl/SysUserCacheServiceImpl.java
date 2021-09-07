package com.wpy.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wpy.config.jedis.RedisTemplateUtils;
import com.wpy.constant.CacheConstant;
import com.wpy.entity.SysUser;
import com.wpy.service.SysUserCacheService;
import com.wpy.service.SysUserService;
import com.wpy.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 13940
 */
@Service
@Slf4j
public class SysUserCacheServiceImpl implements SysUserCacheService {

    @Autowired
    SysUserService sysUserService;
    @Autowired
    RedisTemplateUtils redisTemplateUtils;

    private static ObjectMapper objectMapper=new ObjectMapper();

    /**
     * 初始化用户信息
     */
    @Override
    public void initUserInfo(){
        try {
            //查询用户信息
            List<SysUser> sysUsers = sysUserService.getBaseMapper().selectList(null);
            //根据id分组
            Map<Object, Object> userMap = sysUsers.stream().collect(
                    Collectors.toMap(SysUser::getId, Function.identity(), (key1, key2) -> key2));
            //设置到缓存
            redisTemplateUtils.setMap(CacheConstant.USER_INFO,userMap);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 获得用户信息
     * @param userId
     * @return
     */
    @Override
    public SysUser getSysUser(String userId){
        try {
            Object mapByKey = redisTemplateUtils.getMapByKey(CacheConstant.USER_INFO, userId);
            return objectMapper.convertValue(mapByKey, SysUser.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 获得用户账号
     */
    @Override
    public String getAccountName(String userId){
        SysUser sysUser = this.getSysUser(userId);
        if(StringUtils.isBlank(sysUser)){
            return "";
        }
        return sysUser.getAccountName();
    }

    /**
     * 获得用户名
     * @param userId
     * @return
     */
    @Override
    public String getUsername(String userId){
        SysUser sysUser = this.getSysUser(userId);
        if(StringUtils.isBlank(sysUser)){
            return "";
        }
        return sysUser.getUserName();
    }

    /**
     * 设置用户信息
     * @param sysUser
     */
    @Override
    public void setUserInfo(SysUser sysUser){
        redisTemplateUtils.setMapByKey(CacheConstant.USER_INFO,sysUser.getId(),sysUser);
    }

    @Override
    public void setUserInfo(String userId) {
        SysUser sysUser = sysUserService.getById(userId);
        this.setUserInfo(sysUser);
    }
}
