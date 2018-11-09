package com.pkest.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuzhonggui on 2018/8/28.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class UrlUtils {

    public static boolean isUrl(String url){
        if(StringUtils.isBlank(url) || url.length() < 8) return false;
        String header = url.trim().substring(0, 8).toLowerCase();
        return header.substring(0, 7).equals("http://") || "https://".equals(header);
    }

    public static String createQueryStringByJSONObject(JSONObject jsonObject){
        return createQueryStringByJSONObject(jsonObject, null);
    }

    public static String createQueryStringByJSONObject(JSONObject jsonObject, String prefix){
        StringBuilder stringBuilder = new StringBuilder();
        if(jsonObject == null){
            return stringBuilder.toString();
        }
        List<String> keys = new ArrayList<String>(jsonObject.keySet());
        for (int i = 0, size = keys.size(); i < size; i++){
            String key = keys.get(i);
            Object value = jsonObject.get(key);
            if(value instanceof JSONObject){
                stringBuilder.append(createQueryStringByJSONObject((JSONObject)value, key));
            }else{
                if(prefix != null){
                    stringBuilder.append(prefix).append("[").append(key).append("]").append("=").append(value);
                }else{
                    stringBuilder.append(key).append("=").append(value);
                }
            }
            if (i != size - 1) {
                stringBuilder.append("&");
            }
        }
        return stringBuilder.toString();
    }


}
