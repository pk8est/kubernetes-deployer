package com.pkest.libs.myibatis;

import com.pkest.libs.myibatis.config.MybatisMapperRegistry;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

/**
 * Created by wuzhonggui on 2018/11/25.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class HYMybatisConfiguration extends Configuration {

    protected final MapperRegistry mapperRegistry = new MybatisMapperRegistry(this);

    public HYMybatisConfiguration() {
        super();
    }

    public HYMybatisConfiguration(Environment environment) {
        super(environment);
    }

    @Override
    public MapperRegistry getMapperRegistry() {
        return mapperRegistry;
    }

    @Override
    public void addMappers(String packageName, Class<?> superType) {
        mapperRegistry.addMappers(packageName, superType);
    }

    @Override
    public void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    @Override
    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    @Override
    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }
}
