package com.pkest.libs.myibatis;

import com.google.common.collect.Maps;
import com.pkest.libs.myibatis.config.HYLanguageDriver;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wuzhonggui on 2017/7/25.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class CompareAndPageinationLanguageDriver extends HYLanguageDriver implements LanguageDriver {

    private final Pattern pattern = Pattern.compile("@\\{(\\w+)\\}");
    private static Map<String, String> ids = Maps.newHashMap();

    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {

        String id = mappedStatement.getId();
        MetaObject metaObjectHandler = SystemMetaObject.forObject(boundSql);
        String tableName = "";
        if(!ids.containsKey(id)){
            try {
                String resource = mappedStatement.getResource();
                Class clazz = Class.forName(resource.substring(0, resource.length()-18).replace("/", "."));
                HYTable annotation = (HYTable)clazz.getAnnotation(HYTable.class);

                if(annotation != null){
                    tableName = annotation.tableName();
                }
                ids.put(id, tableName);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            tableName = ids.get(id);
        }

        //System.err.println(id);
        System.err.println("before: " + boundSql.getSql());
        metaObjectHandler.setValue("sql", boundSql.getSql().replace("##TABLE_NAME##", tableName));
        System.err.println("after: " + boundSql.getSql());
        System.err.println("*************************************************");

        return super.createParameterHandler(mappedStatement, parameterObject, boundSql);
    }

    public SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType){
        return super.createSqlSource(configuration, script, parameterType);
    }

    @Override
    //将注解中读入的语句解析并返回一个sqlSource对象
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {

        Matcher matcher = pattern.matcher(script);
        if (matcher.find()) {
            script = matcher.replaceAll(
    "<foreach collection=\"$1\" index=\"__index\" item=\"__item\">" +
                        "<if test=\"__index.isStatement()\">" +
                            "\\${__index.getConnector()} \\${__index.getField()}" +
                        "</if>" +
                        "<if test=\"__index.isList() and (__index.getOperator().getValue() == 'IN' or __index.getOperator().getValue() == 'NOT IN')\">" +
                            "\\${__index.getConnector()} \\${__index.getField()} \\${__index.getOperator().getValue()}" +
                            "(" +
                                "<foreach collection=\"__item\" item=\"__item2\" separator=\",\">" +
                                    "#{__item2}" +
                                "</foreach>" +
                            ")" +
                        "</if>" +
                        "<if test=\"__index.isList() and (__index.getOperator().getValue() == 'BETWEEN' or __index.getOperator().getValue() == 'NOT BETWEEN')\">" +
                            "\\${__index.getConnector()} \\${__index.getField()} \\${__index.getOperator().getValue()}" +
                            "<foreach collection=\"__item\" item=\"__item2\" separator=\"AND\">" +
                                "#{__item2}" +
                            "</foreach>" +
                        "</if>" +
                        "<if test=\"!__index.isStatement() and __index.isSimple()\">" +
                            "\\${__index.getConnector()} \\${__index.getField()} \\${__index.getOperator().getValue()} #{__item}" +
                        "</if>" +
                        "<if test=\"!__index.isStatement() and __index.isMap()\">" +
                            "\\${__index.getConnector()} \\${__index.getField()} \\${__index.getOperator().getValue()}" +
                        "</if>" +
                "</foreach>" +
                " \\${$1.getGroupBy()}" +
                " \\${$1.getHaving()}" +
                " \\${$1.getSortDescribe()}" +
                " \\${$1.getPageDescribe()}"
            );
        }

        if(script.indexOf("<script>") == -1 ){
            script = "<script>" + script + "</script>";
        }

        System.err.println("*****: " + script);
        return super.createSqlSource(configuration, script, parameterType);
    }



}
