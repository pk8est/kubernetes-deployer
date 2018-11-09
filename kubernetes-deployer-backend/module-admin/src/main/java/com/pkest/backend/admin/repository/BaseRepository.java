package com.pkest.backend.admin.repository;

import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Created by wuzhonggui on 2018/4/9.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> {

    long store(Long vid);

    @SuppressWarnings("unchecked")
    long store2(Number vid);
}
