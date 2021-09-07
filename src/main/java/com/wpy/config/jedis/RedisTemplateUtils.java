package com.wpy.config.jedis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 13940
 */

@Component
@Slf4j
public class RedisTemplateUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 给一个指定的 key 值附加过期时间
     *
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            return redisTemplate.expire(key, time, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key
     * @return
     */
    public long getTime(String key) {
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return 0;
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 移除指定key 的过期时间
     *
     * @param key
     * @return
     */
    public boolean persist(String key) {
        try {
            return redisTemplate.boundValueOps(key).persist();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 删除指定key
     * @param key
     */
    public void delete(String key){
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 设定对象
     * @param key
     * @param object
     */
    public void setObject(String key,Object object){
        try {
            redisTemplate.opsForValue().set(key,object);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 获取原来的值并且重新赋值
     * @param key
     * @param object
     */
    public void setAndSetObject(String key,Object object){
        try {
            redisTemplate.opsForValue().getAndSet(key,object);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 不存在则新建，存在不改变原有的值
     * @param key
     * @param object
     */
    public void setSetIfAbsent(String key,Object object){
        try {
            redisTemplate.opsForValue().setIfAbsent(key,object);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 设置map
     * @param map
     */
    public void setMap(Map<Object,Object> map){
        try {
            redisTemplate.opsForValue().multiSet(map);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 获得参数
     * @param key
     * @return
     */
    public Object getMap(String key){
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 设定带key的map
     * @param key
     * @param map
     */
    public void setMap(String key, Map<Object,Object> map){
        try {
            redisTemplate.opsForHash().putAll(key,map);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 设置带key的map中的某一个key的值
     * @param key
     * @param mapKey
     * @param value
     */
    public void setMapByKey(String key,Object mapKey,Object value){
        try {
            redisTemplate.opsForHash().put(key,mapKey,value);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 获取指定key下面的Map
     * @param key
     */
    public Map<Object,Object> getAllMap(String key){
        try {
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 获取指定key下面的map的指定key的value
     * @param keyName
     * @param mapKey
     */
    public Object getMapByKey(String keyName,String mapKey){
        try {
            return redisTemplate.opsForHash().get(keyName, mapKey);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }


}
