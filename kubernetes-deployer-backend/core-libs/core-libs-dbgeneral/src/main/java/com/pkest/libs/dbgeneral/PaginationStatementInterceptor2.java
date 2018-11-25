package com.pkest.libs.dbgeneral;

import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.hibernate.dialect.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by wuzhonggui on 2018/11/25.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class }) })
public class PaginationStatementInterceptor2 implements Interceptor {

    private final static Logger logger = LoggerFactory.getLogger(PaginationStatementInterceptor2.class);

    private Dialect dialect;

    static int MAPPED_STATEMENT_INDEX = 0;
    static int PARAMETER_INDEX = 1;
    static int ROWBOUNDS_INDEX = 2;
    static int RESULT_HANDLER_INDEX = 3;

    public Object intercept(Invocation invocation) throws Throwable {
        final Object[] queryArgs = invocation.getArgs();
        Object parameter = queryArgs[PARAMETER_INDEX];
        Pageable pageRequest = findPageableObject(parameter);
        if (pageRequest != null) {
            final MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
            if (parameter instanceof ParamMap) {
                ParamMap<?> paramMap = (ParamMap<?>) parameter;
                if (paramMap.size() == 4) {
                    parameter = ((ParamMap<?>) parameter).get("param1");
                    queryArgs[PARAMETER_INDEX] = parameter;
                }
            }
            final BoundSql boundSql = ms.getBoundSql(parameter);
            String sql = boundSql.getSql().trim().replaceAll(";$", "");
            int total = this.queryTotal(sql, ms, boundSql);
            if (pageRequest.getSort() != null) {
                Iterator<Sort.Order> it = pageRequest.getSort().iterator();
                if (it.hasNext()) {
                    sql = "select o.* from ( " + sql + " ) o order by ";
                }
                for (int i = 0; it.hasNext(); i++) {
                    if (i > 0) {
                        sql += " , ";
                    }
                    Sort.Order order = it.next();
                    sql += order.getProperty() + " " + order.getDirection().name();
                }
            }

            String limitSql = dialect.getLimitString(sql, pageRequest.getOffset(), pageRequest.getPageSize());
            queryArgs[ROWBOUNDS_INDEX] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
            queryArgs[MAPPED_STATEMENT_INDEX] = copyFromNewSql(ms, boundSql, limitSql);

            Object ret = invocation.proceed();
            @SuppressWarnings("unchecked")
            Page<Object> page = new PageImpl<Object>((List<Object>) ret, pageRequest, total);
            List<Page<?>> ls = new ArrayList<Page<?>>(1);
            ls.add(page);
            return ls;
        }
        return invocation.proceed();
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
        String dialectClass = properties.getProperty("dialectClass");
        try {
            setDialect((Dialect) Class.forName(dialectClass).newInstance());
        } catch (Exception e) {
            throw new RuntimeException("cannot create dialect instance by dialectClass:" + dialectClass, e);
        }
    }

    public Dialect getDialect() {
        return dialect;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }


    private Pageable findPageableObject(Object params) {

        if (params == null) {
            return null;
        }

        // 单个参数 表现为参数对象
        if (Pageable.class.isAssignableFrom(params.getClass())) {
            return (Pageable) params;
        }

        // 多个参数 表现为 ParamMap
        else if (params instanceof ParamMap) {
            ParamMap<?> paramMap = (ParamMap<?>) params;
            for (Map.Entry<String, ?> entry : paramMap.entrySet()) {
                Object paramValue = entry.getValue();
                if (paramValue != null && Pageable.class.isAssignableFrom(paramValue.getClass())) {
                    return (Pageable) paramValue;
                }
            }
        }

        return null;
    }

    private int queryTotal(String sql, MappedStatement mappedStatement, BoundSql boundSql) throws SQLException {
        Connection connection = null;
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {

            connection = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();

            //"select count(1) as cnt from (" + sql + ")  c";
            //String countSql = this.dialect.getCountString(sql);
            String countSql = "";

            countStmt = connection.prepareStatement(countSql);
            BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql,
                    boundSql.getParameterMappings(), boundSql.getParameterObject());

            setParameters(countStmt, mappedStatement, countBoundSql, boundSql.getParameterObject());

            rs = countStmt.executeQuery();
            int totalCount = 0;
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }

            return totalCount;
        } catch (SQLException e) {
            logger.error("查询总记录数出错", e);
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("exception happens when doing: ResultSet.close()", e);
                }
            }

            if (countStmt != null) {
                try {
                    countStmt.close();
                } catch (SQLException e) {
                    logger.error("exception happens when doing: PreparedStatement.close()", e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("exception happens when doing: Connection.close()", e);
                }
            }
        }
    }

    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
                               Object parameterObject) throws SQLException {
        ParameterHandler parameterHandler = /*
											 * mappedStatement.getLang().
											 * createParameterHandler(
											 * mappedStatement, parameterObject,
											 * boundSql);
											 */new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
        parameterHandler.setParameters(ps);
    }

    private MappedStatement copyFromNewSql(MappedStatement ms, BoundSql boundSql, String sql) {
        BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, sql);
        return copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
    }

    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(),
                boundSql.getParameterObject());
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    // see: MapperBuilderAssistant
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuffer keyProperties = new StringBuffer();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }

        // setStatementTimeout()
        builder.timeout(ms.getTimeout());

        // setStatementResultMap()
        builder.parameterMap(ms.getParameterMap());

        // setStatementResultMap()
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());

        // setStatementCache()
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

}
