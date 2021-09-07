package com.wpy.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class JsonUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 将json转化为对象
     * @param jsonStr
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T jsonStrToObject(String jsonStr,Class<T> tClass){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES,true);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            return mapper.readValue(jsonStr,tClass);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    /**
     * 将对象转化为jsonString
     * @return
     */
    public static String ObjectToJsonStr(Object obj){
        if(StringUtils.isBlank(obj)){
            return "";
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(),e);
        }
        return "";
    }

    /**
     * 将jsonString转化为List
     * @param jsonStr
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonStrToList(String jsonStr,Class<T> tClass){
        JavaType javaType=mapper.getTypeFactory().constructParametricType(List.class,tClass);
        try {
            return mapper.readValue(jsonStr,javaType);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    /**
     * 将jsonString转化为map<String,Object>
     */
    public static Map<String,Object> jsonStrToMap(String jsonStr){
        Map<String,Object> map= Maps.newHashMap();
        try {
            map=mapper.readValue(jsonStr, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(),e);
        }
        return map;
    }

    /**
     * 将jsonString转化为List<Map<String,Object>>
     * @param jsonStr
     * @return
     */
    public static List<Map<String,Object>> jsonToListMap(String jsonStr){
        try {
            return mapper.readValue(jsonStr, new TypeReference<List<Map<String, Object>>>() {});
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(),e);
        }
        return Collections.emptyList();
    }
}
