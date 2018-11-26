package com.pkest.libs.myibatis;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by wuzhonggui on 2017/11/2.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public interface BaseRepository<T, K> {

    String TABLE_NAME = "";

    List<T> select(@Param("c") CompareBuilder compare);
    Long count(@Param("compare") CompareBuilder c);

    @Lang(CompareAndPageinationLanguageDriver.class)
    @Select("SELECT * FROM ##TABLE_NAME## WHERE @{compare}")
    List<T> selectAll(@Param("compare") CompareBuilder compare);


    @SelectProvider(type = UserSqlBuilder.class, method = "selectAll")
    List<T> selectAll2(@Param("compare") CompareBuilder compare);

    @Insert("INSERT INTO ##TABLE_NAME## @INSERT{model}")
    @Lang(InsertLanguageDriver.class)
    @Options(useGeneratedKeys=true, keyProperty="id")
    int insert(T model) throws Exception;
}
