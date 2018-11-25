package com.pkest.libs.dbgeneral;

import com.google.common.base.CaseFormat;
import com.pkest.common.util.HYStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wuzhonggui on 2017/7/25.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class UpdateLanguageDriver extends XMLLanguageDriver implements LanguageDriver {

    private final Pattern pattern = Pattern.compile("@UPDATE\\{(\\w+)\\}");

    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        return super.createParameterHandler(mappedStatement, parameterObject, boundSql);
    }

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
                StringBuilder first = new StringBuilder("<trim prefix=\"set\" prefixOverrides=\",\">");
                for(Field field: fields){
                    HYColumn column = field.getAnnotation(HYColumn.class);
                    if(column == null || (column.invisible() == false && column.updatable() == true && column.pk() == false)){
                        String fieldName = HYStringUtils.underlineToCamel(field.getName());
                        String tableFieldName = column != null && StringUtils.isNotBlank(column.value()) ? column.value() :
                                CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
                        first.append("<if test=\""+fieldName+"!=null\">,`"+tableFieldName+"`=#{"+fieldName+"}</if>");
                    }
                }
                first.append("</trim>");
                script = matcher.replaceAll(first.toString());
            }
        }

        if(script.indexOf("<script>") == -1 ){
            script = "<script>" + script + "</script>";
        }
        return super.createSqlSource(configuration, script, parameterType);
    }

}
