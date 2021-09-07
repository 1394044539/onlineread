package com.wpy.config.jedis;

import com.wpy.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

/**
 * @author 13940
 */
@Component
@Slf4j
public class JedisCache {

    @Autowired
    private JedisPool jedisPool;

    /**
     * key value储存
     * @param key
     * @param value
     */
    public void set(String key,String value){
        try(Jedis jedis=jedisPool.getResource()) {
            if(StringUtils.isNotEmpty(key)){
                jedis.set(key, value);
            }else{
                log.error("REDIS KEY ISEMPTY");
            }
        } catch (Exception e) {
            log.error("REDIS KEY ISEMPTY");
        }
    }

    /**
     * key value expire储存
     * @param key
     * @param value
     * @param expire
     */
    public void set(String key,String value,int expire){
        try(Jedis jedis=jedisPool.getResource()) {
            if(StringUtils.isNotEmpty(key)){
                jedis.set(key, value);
                jedis.expire(key, expire);
            }else{
                log.error("REDIS KEY ISEMPTY");
            }
        } catch (Exception e) {
            log.error("REDIS KEY ISEMPTY");
        }
    }

    /**
     * 根据key获取缓存
     * @param key
     * @return
     */
    public String get(String key){
        try(Jedis jedis=jedisPool.getResource()) {
            if(StringUtils.isNotEmpty(key)){
                return jedis.get(key);
            }else{
                log.error("REDIS KEY ISEMPTY");
            }
        } catch (Exception e) {
            log.error("REDIS KEY ISEMPTY");
        }
        return "";
    }

    /**
     * hst存储
     * @param key
     * @param field
     * @param value
     */
    public void hset(String key,String field,String value){
        try(Jedis jedis=jedisPool.getResource()) {
            if(StringUtils.isNotEmpty(key)){
                jedis.hset(key,field,value);
            }else{
                log.error("REDIS KEY ISEMPTY");
            }
        } catch (Exception e) {
            log.error("REDIS KEY ISEMPTY");
        }
    }

    /**
     *根据key 获取hget方法
     * @param key
     * @return
     */
    public String hget(String key,String feild){
        try(Jedis jedis=jedisPool.getResource()) {
            if(StringUtils.isNotEmpty(key)){
                return jedis.hget(key,feild);
            }else{
                log.error("REDIS KEY ISEMPTY");
            }
        } catch (Exception e) {
            log.error("REDIS KEY ISEMPTY");
        }
        return "";
    }

    /**
     * 设置缓存时间
     * @param key
     * @param expire
     */
    public void setExpire(String key,int expire){
        try(Jedis jedis=jedisPool.getResource()) {
            if(StringUtils.isNotEmpty(key)){
                jedis.expire( key, expire);
            }else{
                log.error("REDIS KEY ISEMPTY");
            }
        } catch (Exception e) {
            log.error("REDIS KEY ISEMPTY");
        }
    }

    /**
     * 根据key前缀批量删除缓存
     * @return
     */
    public long batchDel(String key){
        long result=0;
        try(Jedis jedis=jedisPool.getResource()) {
            if(StringUtils.isNotEmpty(key)){
                Set<String> set=jedis.keys(key+"*");
                Iterator<String> it=set.iterator();
                while(it.hasNext()){
                    result+=jedis.del(it.next());
                }
                return result;
            }else{
                log.error("REDIS KEY ISEMPTY");
            }
        } catch (Exception e) {
            log.error("REDIS KEY ISEMPTY");
        }
        return result;
    }

    /**
     * 删除缓存
     * @param key
     */
    public void delete(String key){
        try(Jedis jedis=jedisPool.getResource()) {
            if(StringUtils.isNotEmpty(key)){
                jedis.del(key);
            }else{
                log.error("REDIS KEY ISEMPTY");
            }
        } catch (Exception e) {
            log.error("REDIS KEY ISEMPTY");
        }
    }

    /**
     * 第一次返回0，第二次以后返回1
     * @param key
     * @param value
     * @return
     */
    public long setNx(String key,String value){
        try(Jedis jedis=jedisPool.getResource()) {
            if(StringUtils.isNotEmpty(key)){
                return jedis.setnx(key,value);
            }else{
                log.error("REDIS KEY ISEMPTY");
            }
        } catch (Exception e) {
            log.error("REDIS KEY ISEMPTY");
        }
        return 0;
    }
}
