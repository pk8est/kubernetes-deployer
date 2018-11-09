package com.pkest.backend.admin.repository;

import com.pkest.backend.admin.entity.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * Created by wuzhonggui on 2018/4/9.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@Repository
public interface ProjectsRepository extends
        JpaRepository<Projects, Long>,
        JpaSpecificationExecutor<Projects>,
        CrudRepository<Projects, Long>,
        BaseRepository<Projects, Long>,
        Serializable
{

}
