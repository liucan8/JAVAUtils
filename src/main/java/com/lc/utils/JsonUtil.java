package com.lc.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Created by feihaitao 2017/2/20.
 */
public class JsonUtil {

    private static ObjectMapper mapper;

    /**
     * 获取ObjectMapper实例
     *
     * @param createNew
     *            方式：true，新实例；false,存在的mapper实例
     * @return
     */
    public static synchronized ObjectMapper getMapperInstance(boolean createNew) {
        if (createNew) {
            return new ObjectMapper();
        } else if (mapper == null) {
            mapper = new ObjectMapper();
        }
        return mapper;
    }

    /**
     * 将java对象转换成json字符串
     *
     * @param obj
     *            准备转换的对象
     * @return json字符串
     * @throws Exception
     */
    public static String beanToJson(Object obj) throws Exception {
        try {
            ObjectMapper objectMapper = getMapperInstance(false);
            String json = objectMapper.writeValueAsString(obj);
            return json;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 将java对象转换成json字符串
     *
     * @param obj
     *            准备转换的对象
     * @return json字符串
     * @throws Exception
     */
    public static String beanToJsonNotIncludeNull(Object obj) throws Exception {
        try {
            ObjectMapper objectMapper = getMapperInstance(false);
            String json = objectMapper.writeValueAsString(obj);
            return json;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 将java对象转换成json字符串
     *
     * @param obj
     *            准备转换的对象
     * @param createNew
     *            ObjectMapper实例方式:true，新实例;false,存在的mapper实例
     * @return json字符串
     * @throws Exception
     */
    public static String beanToJson(Object obj, Boolean createNew) throws Exception {
        try {
            ObjectMapper objectMapper = getMapperInstance(createNew);
            String json = objectMapper.writeValueAsString(obj);
            return json;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 将json字符串转换成java对象
     *
     * @param json
     *            准备转换的json字符串
     * @param cls
     *            准备转换的类
     * @return
     * @throws Exception
     */
    public static <T> T jsonToBean(String json, Class<T> cls) throws Exception {
        return jsonToBean(json,cls,true);
    }
    /**
     * 将json字符串转换成java对象
     *
     * @param json
     *            准备转换的json字符串
     * @param cls
     *            准备转换的类
     * @return
     * @throws Exception
     */
    public static <T> T jsonToBean1(String json, Class<T> cls) throws Exception {
        return jsonToBean1(json,cls,true);
    }

    /**
     * 将json字符串转换成java对象
     *
     * @param json
     *            准备转换的json字符串
     * @param cls
     *            准备转换的类
     * @param createNew
     *            ObjectMapper实例方式:true，新实例;false,存在的mapper实例
     * @return
     * @throws Exception
     */
    public static <T> T jsonToBean(String json, Class<T> cls, Boolean createNew) throws Exception {
        try {
            ObjectMapper objectMapper = getMapperInstance(createNew);
            T vo = objectMapper.readValue(json, cls);
            return vo;
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 将json字符串转换成java对象
     *
     * @param json
     *            准备转换的json字符串
     * @param cls
     *            准备转换的类
     * @param createNew
     *            ObjectMapper实例方式:true，新实例;false,存在的mapper实例
     * @return
     * @throws Exception
     */
    public static <T> T jsonToBean1(String json, Class<T> cls, Boolean createNew) {
        try {
            ObjectMapper objectMapper = getMapperInstance(createNew);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            T vo = objectMapper.readValue(json, cls);
            return vo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Map<String,Object> jsonToMap(String json) throws Exception{
        return JSON.parseObject(json,new TypeReference<Map<String,Object>>(){});
    }


}
