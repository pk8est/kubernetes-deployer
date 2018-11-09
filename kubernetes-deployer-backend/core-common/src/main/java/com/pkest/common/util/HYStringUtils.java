package com.pkest.common.util;

/**
 * Created by wuzhonggui on 2018/8/24.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class HYStringUtils {

    public static String truncate(String input, int length){
        return truncate(input, length, "...");
    }

    public static String truncate(String input, int length, String symbol){
        return input.length() > length ? input.substring(0, length) + symbol : input;
    }

}
