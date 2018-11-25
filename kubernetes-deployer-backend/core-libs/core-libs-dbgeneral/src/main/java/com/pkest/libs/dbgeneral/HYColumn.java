package com.pkest.libs.dbgeneral;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wuzhonggui on 2018/5/21.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HYColumn {
    String value() default "";
    boolean nullable() default true;
    boolean insertable() default true;
    boolean updatable() default true;
    boolean invisible() default false;
    boolean pk() default false;
}

