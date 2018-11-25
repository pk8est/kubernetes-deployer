package com.pkest.common.util;

import org.apache.commons.lang3.StringUtils;

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

    /**
     * 将字符串转换为unicode
     *
     * @param string 字符串
     * @return unicode
     */
    public static String str2Unicode(String string) {

        StringBuilder unicode = new StringBuilder();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u").append(Integer.toHexString(c));
        }

        return unicode.toString();
    }

    /**
     * 将unicode转换为字符串
     *
     * @param unicode unicode编码
     * @return 字符串
     */
    public static String unicode2Str(String unicode) {

        StringBuilder string = new StringBuilder();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {

            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);

            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }

    /**
     * 驼峰格式字符串转换为下划线格式字符串
     *
     * @param param
     * @return
     */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        char UNDERLINE = '_';
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线格式字符串转换为驼峰格式字符串
     *
     * @param param
     * @return
     */
    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        char UNDERLINE = '_';
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String formatDuration(String duration) {
        String formatDuration = "00:00";
        try {
            if (StringUtils.isNotEmpty(duration)) {
                int sec = (int) Double.parseDouble(duration);
                int min = sec / 60;
                sec = sec % 60;
                formatDuration = (min < 10 ? "0" : "") + min + ":" + (sec < 10 ? "0" : "") + sec;
            }
        } catch (Exception e) {
            formatDuration = "00:00";
        }
        return formatDuration;
    }

}
