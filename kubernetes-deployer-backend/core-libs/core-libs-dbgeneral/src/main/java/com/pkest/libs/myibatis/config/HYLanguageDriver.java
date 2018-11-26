package com.pkest.libs.myibatis.config;

import com.pkest.libs.myibatis.HYColumn;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;

import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Created by wuzhonggui on 2018/11/25.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class HYLanguageDriver extends XMLLanguageDriver implements LanguageDriver {

    public static String tablePlaceholder = "@{table}";
    public static String idPlaceholder = "@{id}";
    public static String idFieldPlaceholder = "@{idField}";
    public static String idValuePlaceholder = "@{idValue}";
    public static String fieldsPlaceholder = "@{field}";
    public static String insertFieldPlaceholder = "@{insertField}";
    public static String insertValuePlaceholder = "@{insertValue}";
    public static String updateSetPlaceholder = "@{updateSet}";
    public static String conditionPlaceholder = "@{where}";

    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql){
        KeyGenerator keyGenerator = mappedStatement.getKeyGenerator();
        mappedStatement.getKeyProperties();
        return super.createParameterHandler(mappedStatement, parameterObject, boundSql);
    }

    @Override
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
        Class<?> mapperClass = MybatisMapperRegistry.getCurrentMapper();

        Class<?>[] generics = MybatisReflectUtil.getMapperGenerics(mapperClass);
        if(generics.length < 2){
            return super.createSqlSource(configuration, script, parameterType);
        }
        Class<?> modelClass = generics[0];
        Class<?> idClass = generics[1];

        ResultMap resultMap = getResultMap(configuration.getResultMaps(), modelClass);
        script = setTable(script, mapperClass, modelClass, idClass, resultMap);
        script = setId(script, mapperClass, modelClass, idClass, resultMap);
        script = setIdFeild(script, mapperClass, modelClass, idClass, resultMap);
        script = setIdValue(script, mapperClass, modelClass, idClass, resultMap);
        script = setField(script, mapperClass, modelClass, idClass, resultMap);
        script = setInsertField(script, mapperClass, modelClass, idClass, resultMap);
        script = setInsertValue(script, mapperClass, modelClass, idClass, resultMap);
        script = setUpdateSet(script, mapperClass, modelClass, idClass, resultMap);
        script = setCondition(script, mapperClass, modelClass, idClass, resultMap);

        MybatisMapperRegistry.setCurrentMapperId(getIdField(modelClass));
        System.err.println(script);
        return super.createSqlSource(configuration, script, parameterType);
    }

    private ResultMap getResultMap(Collection<ResultMap> resultMaps, Class<?> modelClass) {
        for (ResultMap resultMap : resultMaps)
            if (modelClass == resultMap.getType() && !resultMap.getId().contains("-"))
                return resultMap;
        return null;
    }

    /**
     * 设置表名
     * 把 ResultMap 的 id 值作为表名，
     * 若 ResultMap 不存在，则把驼峰命名法的 Model 类名转以下划线命名作为表名
     *
     * @param script
     * @param modelClass
     * @param resultMap
     * @return
     */
    private String setTable(String script, Class<?> mapperClass, Class<?> modelClass, Class<?> idClass, ResultMap resultMap) {
        if (script.contains(tablePlaceholder)) {
            String tableName = null;
            Table annotation = modelClass.getAnnotation(Table.class);

            if (resultMap != null) {
                tableName = resultMap.getId().substring(mapperClass.getName().length() + 1);
            }else if(annotation != null && StringUtils.isNotBlank(annotation.name())) {
                tableName = annotation.name();
            }else {
                tableName = toUnderline(modelClass.getSimpleName());
            }
            script = script.replace(tablePlaceholder, tableName);
        }
        return script;
    }

    private String setId(String script, Class<?> mapperClass, Class<?> modelClass, Class<?> idClass, ResultMap resultMap) {
        if (script.contains(idPlaceholder)) {
            script = script.replace(idPlaceholder, "`" + getId(modelClass, resultMap) + "`=" + "#{" + getIdField(modelClass).getName() + "}");
        }
        return script;
    }

    private String setIdFeild(String script, Class<?> mapperClass, Class<?> modelClass, Class<?> idClass, ResultMap resultMap) {
        if (script.contains(idFieldPlaceholder)) {
            script = script.replace(idFieldPlaceholder, "`" + getId(modelClass, resultMap) + "`");
        }
        return script;
    }

    private String setIdValue(String script, Class<?> mapperClass, Class<?> modelClass, Class<?> idClass, ResultMap resultMap) {
        if (script.contains(idValuePlaceholder)) {
            script = script.replace(idValuePlaceholder, "#{" + getIdField(modelClass).getName() + "}");
        }
        return script;
    }

    private String getId(Class<?> modelClass, ResultMap resultMap){
        String idName;
        Field idField = getIdField(modelClass);
        ResultMapping resultMapping = getIdResultMapping(resultMap, null);
        if (resultMapping != null) {
            idName = resultMapping.getColumn();
        }else if(idField != null){
            idName = toUnderline(idField.getName());
        }else {
            idName = "id";
        }
        return idName;
    }

    private Field getIdField(Class<?> modelClass){
        Id id;
        Field[] fields = modelClass.getDeclaredFields();
        for(Field field: fields){
            id = field.getAnnotation(Id.class);
            if(id != null){
                return field;
            }
        }
        return null;
    }

    private String setField(String script, Class<?> mapperClass, Class<?> modelClass, Class<?> idClass, ResultMap resultMap) {
        if (script.contains(fieldsPlaceholder)) {
            StringBuilder fieldsBuilder = new StringBuilder();
            Field[] fields = modelClass.getDeclaredFields();
            for(Field field: fields){
                HYColumn column = field.getAnnotation(HYColumn.class);
                if(column == null || (column.invisible() == false)){
                    fieldsBuilder.append("`").append(column != null && StringUtils.isNotBlank(column.value()) ? column.value() :
                            toUnderline(field.getName())).append("`,");
                }
            }
            String fieldsName = fieldsBuilder.toString();
            if(fieldsName.endsWith(",")) fieldsName = fieldsName.substring(0, fieldsName.length() - 1);
            script = script.replace(fieldsPlaceholder, fieldsName);
        }
        return script;
    }

    private String setInsertField(String script, Class<?> mapperClass, Class<?> modelClass, Class<?> idClass, ResultMap resultMap) {
        if (script.contains(insertFieldPlaceholder)) {
            String fieldName;
            StringBuilder fieldsBuilder = new StringBuilder("<trim prefixOverrides=\",\">");
            Field[] fields = modelClass.getDeclaredFields();
            for(Field field: fields){
                HYColumn column = field.getAnnotation(HYColumn.class);
                if(column == null || (column.invisible() == false && column.insertable())){
                    fieldName = column != null && StringUtils.isNotBlank(column.value()) ? column.value() :
                            toUnderline(field.getName());
                    fieldsBuilder.append("<if test=\""+field.getName()+"!=null\">,`"+fieldName+"`</if>");
                }
            }
            fieldsBuilder.append("</trim>");
            script = script.replace(insertFieldPlaceholder, fieldsBuilder.toString());
        }
        return script;
    }

    private String setInsertValue(String script, Class<?> mapperClass, Class<?> modelClass, Class<?> idClass, ResultMap resultMap) {
        if (script.contains(insertValuePlaceholder)) {
            String fieldName;
            StringBuilder fieldsBuilder = new StringBuilder("<trim prefixOverrides=\",\">");
            Field[] fields = modelClass.getDeclaredFields();
            for(Field field: fields){
                HYColumn column = field.getAnnotation(HYColumn.class);
                if(column == null || (column.invisible() == false && column.insertable())){
                    fieldName = field.getName();
                    fieldsBuilder.append("<if test=\""+fieldName+"!=null\">,#{"+fieldName+"}</if>");
                }
            }
            fieldsBuilder.append("</trim>");
            script = script.replace(insertValuePlaceholder, fieldsBuilder.toString());
        }
        return script;
    }

    private String setUpdateSet(String script, Class<?> mapperClass, Class<?> modelClass, Class<?> idClass, ResultMap resultMap) {
        if (script.contains(updateSetPlaceholder)) {
            String fieldName;
            StringBuilder fieldsBuilder = new StringBuilder("<trim prefixOverrides=\",\">");
            Field[] fields = modelClass.getDeclaredFields();
            for(Field field: fields){
                HYColumn column = field.getAnnotation(HYColumn.class);
                if(column == null || (column.invisible() == false && column.updatable() && column.pk() == false)){
                    fieldName = column != null && StringUtils.isNotBlank(column.value()) ? column.value() :
                            toUnderline(field.getName());
                    fieldsBuilder.append("<if test=\""+field.getName()+"!=null\">,`"+fieldName+"`=#{"+field.getName()+"}</if>");
                }
            }
            fieldsBuilder.append("</trim>");
            script = script.replace(updateSetPlaceholder, fieldsBuilder.toString());
        }
        return script;
    }

    private String setCondition(String script, Class<?> mapperClass, Class<?> modelClass, Class<?> idClass, ResultMap resultMap) {
        if (script.contains(conditionPlaceholder)) {
            script = script.replace(conditionPlaceholder,
                    "<foreach collection=\"where\" index=\"__index\" item=\"__item\">" +
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
                            " \\${where.getGroupBy()}" +
                            " \\${where.getHaving()}" +
                            " \\${where.getSortDescribe()}" +
                            " \\${where.getPageDescribe()}"
            );
        }
        return script;
    }

    private boolean isIdField(ResultMap resultMap, Field field) {
        if (resultMap != null) {
            for (ResultMapping resultMapping : resultMap.getIdResultMappings()) {
                if (resultMapping.getProperty().equals(field.getName()))
                    return true;
            }
        }
        return false;
    }

    private ResultMapping getIdResultMapping(ResultMap resultMap, Field field) {
        if (resultMap != null) {
            if (resultMap.getIdResultMappings().size() > 0)
                return resultMap.getIdResultMappings().get(0);
        }
        return null;
    }

    private ResultMapping getResultMapping(ResultMap resultMap, Field field) {
        if (resultMap != null) {
            for (ResultMapping resultMapping : resultMap.getPropertyResultMappings()) {
                if (resultMapping.getProperty().equals(field.getName()))
                    return resultMapping;
            }
        }
        return null;
    }

    private String getEmptyTesting(Field field) {
        if (String.class == field.getType()) {
            return String.format("%s != null and %s != ''", field.getName(), field.getName());
        } else {
            return String.format("%s != null", field.getName());
        }
    }

    private String getColumnName(ResultMapping resultMapping, Field field) {
        if (resultMapping == null)
            return field.getName();
        return resultMapping.getColumn();
    }

    private String getColumnValue(ResultMapping resultMapping, Field field) {
        if (resultMapping == null || resultMapping.getJdbcType() == null)
            return String.format("#{%s}", field.getName());
        return String.format("#{%s,jdbcType=%s}", field.getName(), resultMapping.getJdbcType());
    }

    private String toUnderline(String str) {
        StringBuilder buf = new StringBuilder();
        buf.append(Character.toLowerCase(str.charAt(0)));
        for (int i = 1; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                buf.append("_" + Character.toLowerCase(c));
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }

}
