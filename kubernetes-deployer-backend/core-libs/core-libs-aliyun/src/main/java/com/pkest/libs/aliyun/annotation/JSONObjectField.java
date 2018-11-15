package com.pkest.libs.aliyun.annotation;

import com.alibaba.fastjson.annotation.JSONField;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wuzhonggui on 2018/11/14.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface JSONObjectField {

    JSONField value() default @JSONField(deserialize = false);
}
