package com.pkest.libs.dbgeneral;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wuzhonggui on 2017/7/25.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HYTable {
    String tableName() default "";
    String primaryKey() default "id";
    String keyProperty() default "id";
}
