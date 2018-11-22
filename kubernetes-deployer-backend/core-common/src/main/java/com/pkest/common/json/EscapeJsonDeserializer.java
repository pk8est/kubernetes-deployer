package com.pkest.common.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;

/**
 * Created by wuzhonggui on 2018/11/15.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class EscapeJsonDeserializer implements ObjectDeserializer{
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        return JSONObject.parseObject((String) parser.parse(String.class), type);
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
