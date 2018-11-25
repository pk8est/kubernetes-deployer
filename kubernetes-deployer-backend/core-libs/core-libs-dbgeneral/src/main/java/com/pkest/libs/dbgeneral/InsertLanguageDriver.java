package com.pkest.libs.dbgeneral;

import com.google.common.base.CaseFormat;
import com.pkest.common.util.HYStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wuzhonggui on 2017/7/25.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class InsertLanguageDriver extends HYLanguageDriver implements LanguageDriver {

    private final Pattern pattern = Pattern.compile("@INSERT\\{(\\w+)\\}");

    public SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType){
        return super.createSqlSource(configuration, script, parameterType);
    }

    @Override
    //将注解中读入的语句解析并返回一个sqlSource对象
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
        configuration.setMapUnderscoreToCamelCase(true);
        Matcher matcher = pattern.matcher(script);
        if (matcher.find()) {
            String group = matcher.group(1);
            if(!parameterType.equals(MapperMethod.ParamMap.class)){
                Field[] fields = parameterType.getDeclaredFields();
                StringBuilder first = new StringBuilder("<trim prefixOverrides=\",\">");
                StringBuilder second = new StringBuilder("<trim prefixOverrides=\",\">");
                String start = script.substring(0, matcher.start());
                int firstEnd = matcher.end();
                for(Field field: fields){
                    HYColumn column = field.getAnnotation(HYColumn.class);
                    if(column == null || (column.invisible() == false && column.insertable() == true)){
                        //String fieldName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, field.getName());
                        String fieldName = HYStringUtils.underlineToCamel(field.getName());
                        String tableFieldName = column != null && StringUtils.isNotBlank(column.value()) ? column.value() :
                                CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
                        first.append("<if test=\""+fieldName+"!=null\">,`"+tableFieldName+"`</if>");
                        second.append("<if test=\""+fieldName+"!=null\">,#{"+fieldName+"}</if>");
                    }
                }
                first.append("</trim>");
                second.append("</trim>");
                if (matcher.find()) {
                    script = start + first + script.substring(firstEnd, matcher.start()) + second + script.substring(matcher.end());
                } else {
                    script = start + "(" + first + ")VALUES(" + second + ")" + script.substring(firstEnd);
                }
            }
        }

        if(script.indexOf("<script>") == -1 ){
            script = "<script>" + script + "</script>";
        }
        System.err.println(script);
        return super.createSqlSource(configuration, script, parameterType);
    }

}
