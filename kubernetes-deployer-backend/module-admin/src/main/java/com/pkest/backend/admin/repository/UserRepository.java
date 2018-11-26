package com.pkest.backend.admin.repository;

import com.pkest.backend.admin.entites.User;
import com.pkest.libs.myibatis.config.HYBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends HYBaseRepository<User, Long> {

    /*@Insert("INSERT INTO ##TABLE_NAME## @INSERT{model}")
    @Lang(InsertLanguageDriver.class)
    @Options(useGeneratedKeys=true, keyProperty="managerId")
    int insert(User model) throws Exception;*/

}
