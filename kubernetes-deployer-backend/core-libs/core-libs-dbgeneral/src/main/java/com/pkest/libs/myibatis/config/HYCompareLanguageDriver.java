package com.pkest.libs.myibatis.config;

import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

/**
 * Created by wuzhonggui on 2018/11/26.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class HYCompareLanguageDriver extends HYLanguageDriver implements LanguageDriver {

    @Override
    //将注解中读入的语句解析并返回一个sqlSource对象
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
        return this.createSqlSource(configuration, script, parameterType);
    }

}
