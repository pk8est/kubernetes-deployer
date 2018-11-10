package com.pkest.libs.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by wuzhonggui on 2018/8/28.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class HYJSONObject {

    public static String toJSONString(Object object){
        if(object == null) return "{}";
        return JSONObject.toJSONString(object);
    }

    public static <T> T parseObject(String content, Class<T> clazz) {
        return parseObject(content, clazz, null);
    }

    public static <T> T parseObject(String content, T defaultValue) {
        return parseObject(content, defaultValue, true);
    }

    public static <T> T parseObject(String content, T defaultValue, boolean quiet) {
        return parseObject(content, null, defaultValue, quiet);
    }

    public static <T> T parseObject(String content, Class<T> clazz, boolean quiet) {
        return parseObject(content, clazz, null, quiet);
    }

    public static <T> T parseObject(String content, Class<T> clazz, T defaultValue) {
        return parseObject(content, clazz, defaultValue, true);
    }

    public static <T> T parseObject(String content, Class<T> clazz, T defaultValue, boolean quiet) {
        try {
            if(clazz == null && defaultValue != null){
                clazz = (Class<T>) defaultValue.getClass();
            }
            return JSONObject.parseObject(content, clazz);
        }catch (RuntimeException e){
            if(quiet) return defaultValue;
            throw e;
        }
    }

    public static JSONObject parseObject(String content) {
        return parseObject(content, false);
    }

    public static JSONObject parseObject(String content, boolean quiet) {
        try {
            if(StringUtils.isBlank(content)){
                throw new NullPointerException(content);
            }
            return JSONObject.parseObject(content);
        }catch (RuntimeException e){
            if(quiet) return new JSONObject();
            throw e;
        }
    }

    public static<T> T get(String content, String key) {
        return get(content, key, null, true);
    }

    public static<T> T get(String content, String key, boolean quiet) {
        return get(content, key, null, quiet);
    }

    public static<T> T get(String content, String key, T defaultValue) {
        return get(content, key, defaultValue, null, true);
    }

    public static<T> T get(String content, String key, T defaultValue, boolean quiet) {
        return get(content, key, defaultValue, null, quiet);
    }

    public static<T> T get(String content, String key, Class<T> clazz) {
        return get(content, key, null, clazz, true);
    }

    public static<T> T get(String content, String key, Class<T> clazz, boolean quiet) {
        return get(content, key, null, clazz, quiet);
    }

    public static<T> T get(String content, String key, T defaultValue, Class<T> clazz, boolean quiet) {
        return get(JSONObject.parseObject(content), key, defaultValue, clazz, quiet);
    }

    public static<T> T get(JSONObject jsonObject, String key) {
        return get(jsonObject, key, null, true);
    }

    public static<T> T get(JSONObject jsonObject, String key, boolean quiet) {
        return get(jsonObject, key, null, quiet);
    }

    public static<T> T get(JSONObject jsonObject, String key, T defaultValue) {
        return get(jsonObject, key, defaultValue, null, true);
    }

    public static<T> T get(JSONObject jsonObject, String key, T defaultValue, boolean quiet) {
        return get(jsonObject, key, defaultValue, null, quiet);
    }

    public static<T> T get(JSONObject jsonObject, String key, Class<T> clazz) {
        return get(jsonObject, key, null, clazz, true);
    }

    public static<T> T get(JSONObject jsonObject, String key, Class<T> clazz, boolean quiet) {
        return get(jsonObject, key, null, clazz, quiet);
    }

    public static<T> T get(JSONObject jsonObject, String key, T defaultValue, Class<T> clazz, boolean quiet) {
        if(clazz == null && defaultValue != null){
            clazz = (Class<T>) defaultValue.getClass();
        }
        try {
            int pos = key.indexOf(".");
            if(pos == -1){
                T object = clazz == null ? (T)jsonObject.get(key) : jsonObject.getObject(key, clazz);
                return (defaultValue != null ? (object == null ? defaultValue : object) : object);
            }else{
                String firstKey = key.substring(0, pos);
                if(jsonObject.containsKey(firstKey)){
                    return get(jsonObject.getJSONObject(firstKey), key.substring(pos + 1), defaultValue, clazz, quiet);
                }else{
                    throw new RuntimeException(key + " not found in " + jsonObject);
                }
            }
        }catch (RuntimeException e){
            if(quiet) return defaultValue;
            throw e;
        }
    }
}
