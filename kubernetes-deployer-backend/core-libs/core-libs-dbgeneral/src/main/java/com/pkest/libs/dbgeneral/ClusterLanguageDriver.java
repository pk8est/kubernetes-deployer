package com.pkest.libs.dbgeneral;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wuzhonggui on 2018/4/19.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class ClusterLanguageDriver extends CompareAndPageinationLanguageDriver {

    private final String pattern = "##CLUSTER##";

    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {

        int start, end;
        Sort sortable = null;
        Pageable pageable = null;
        Sort.Order sort = null;
        StringBuilder sqlBuilder = new StringBuilder();
        String replaceSql = boundSql.getSql();
        String lastSql = "";
        Boolean count = false;
        String originalSql = boundSql.getSql();
        List<String> tableCluster = Lists.newArrayList();
        List<String> sortList = Lists.newArrayList();
        List<String> sqlCluster = Lists.newArrayList();
        MetaObject boundSqlMetaObject = SystemMetaObject.forObject(boundSql);

        if((start = originalSql.indexOf("[CLUSTER]")) != -1 && (end = originalSql.indexOf("[/CLUSTER]")) != -1){
            sqlBuilder.append(originalSql.substring(0, start));
            replaceSql = originalSql.substring(start + 9, end);
            lastSql = originalSql.substring(end + 10);
        }

        if(parameterObject != null && parameterObject instanceof Map){
            for(Map.Entry<String, Object> entry: ((Map<String, Object>)parameterObject).entrySet()){
                if(entry.getValue() instanceof TableCluster){
                    tableCluster = getTableCluster((TableCluster)entry.getValue());
                }else if(entry.getValue() instanceof Pageable){
                    pageable = (Pageable)entry.getValue();
                }else if(entry.getValue() instanceof Sort){
                    sortable = (Sort)entry.getValue();
                }else if(entry.getValue() instanceof TableClusterCount){
                    count = true;
                }
            }
        }else if(parameterObject instanceof TableCluster){
            tableCluster = getTableCluster((TableCluster)parameterObject);
        }
        if(tableCluster.size() == 1){
            sqlCluster.add(replaceSql.replace(pattern, tableCluster.get(0)));
        }else if(tableCluster.size() > 1){
            List<ParameterMapping> parameterMappings = Lists.newArrayList();
            List<ParameterMapping> originalParameterMappings = boundSql.getParameterMappings();
            for(int n = 0; n < tableCluster.size(); ++n) {
                sqlCluster.add("(" + replaceSql.replace(pattern, tableCluster.get(n)) + ")");
                if(originalParameterMappings != null) {
                    for(int i = 0; i < originalParameterMappings.size(); ++i) {
                        parameterMappings.add(originalParameterMappings.get(i));
                    }
                }
            }
            if(parameterMappings.size() > 0){
                boundSqlMetaObject.setValue("parameterMappings", parameterMappings);
            }
        }else{
            sqlCluster.add(replaceSql.replace(pattern, ""));
        }
        if(sqlCluster.size() > 0){
            sqlBuilder.append(Joiner.on(" UNION ALL ").skipNulls().join(sqlCluster));
        }


        sqlBuilder.append(lastSql);

        sortable = sortable != null ? sortable : (pageable != null ? pageable.getSort() : sortable);
        if(sortable != null){
            Iterator<Sort.Order> iterator = sortable.iterator();
            while (iterator.hasNext()){
                sort = iterator.next();
                sortList.add(sort.getProperty() + " " + sort.getDirection());
            }
            if(sortList.size() > 0 && !count){
                sqlBuilder.append(" ORDER BY " + Joiner.on(",").skipNulls().join(sortList));
            }
        }

        if(count){
            sqlBuilder.insert(0, "SELECT count(0) FROM (").append(") HUYA_COUNT_TABLE");
        }else if(pageable != null){
            sqlBuilder.append(" LIMIT " + pageable.getOffset() + "," + pageable.getPageSize());
        }

        if(StringUtils.isNotBlank(sqlBuilder)){
            boundSqlMetaObject.setValue("sql", sqlBuilder.toString());
        }

        return new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
    }

    private List<String> getTableCluster(TableCluster tableCluster){
        List<String> data = Lists.newArrayList();
        for(String table: tableCluster.getList()){
            if(!data.contains(table)){
                data.add(table);
            }
        }
        return data;
    }
}
