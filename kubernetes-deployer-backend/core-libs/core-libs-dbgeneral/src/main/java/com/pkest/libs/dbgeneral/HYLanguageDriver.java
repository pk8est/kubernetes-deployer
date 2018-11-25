package com.pkest.libs.dbgeneral;

import com.google.common.collect.Maps;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;

import java.util.Map;

/**
 * Created by wuzhonggui on 2018/11/25.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class HYLanguageDriver  extends XMLLanguageDriver {

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
}
