package com.pkest.libs.myibatis.config;

import com.pkest.libs.myibatis.CompareBuilder;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by wuzhonggui on 2017/11/2.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public interface HYBaseRepository<T, K> {

    @Lang(HYLanguageDriver.class)
    @Insert({"<script>", "INSERT INTO @{table}(@{insertField}) VALUES (@{insertValue})", "</script>"})
    @Options(useGeneratedKeys=true)
    @AutoKeyProperty()
    K insert(T model);

    @Lang(HYLanguageDriver.class)
    @Update({"<script>", "UPDATE @{table} SET @{updateSet} WHERE @{id}", "</script>"})
    K update(T model);

    @Lang(HYLanguageDriver.class)
    @Select({"<script>", "SELECT @{field} FROM @{table} WHERE @{idField}=#{id} LIMIT 1", "</script>"})
    T find(K id);

    @Lang(HYLanguageDriver.class)
    @Select({"<script>", "SELECT @{field} FROM @{table} WHERE @{where} LIMIT 1", "</script>"})
    T findOne(@Param("where") CompareBuilder compare);

    @Lang(HYLanguageDriver.class)
    @Select({"<script>", "SELECT @{field} FROM @{table} WHERE @{where}", "</script>"})
    List<T> findAll(@Param("where") CompareBuilder compare);


}
