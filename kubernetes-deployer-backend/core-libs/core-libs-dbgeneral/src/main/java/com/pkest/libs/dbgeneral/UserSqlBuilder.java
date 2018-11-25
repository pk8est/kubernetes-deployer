package com.pkest.libs.dbgeneral;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * Created by wuzhonggui on 2018/11/25.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class UserSqlBuilder {

    public static String selectAll(Map<String, Object> params) {
        return new SQL(){{
            SELECT("*");
            FROM("users");
            WHERE("name like #{value} || '%'");
            ORDER_BY("id");
        }}.toString();
    }
}
