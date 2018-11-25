package com.pkest.backend.admin.repository;

import com.pkest.backend.admin.entites.User;
import com.pkest.libs.dbgeneral.BaseRepository;
import com.pkest.libs.dbgeneral.HYTable;
import org.springframework.stereotype.Repository;

@Repository
@HYTable(tableName = "user_manager", keyProperty="manager_id")
public interface UserRepository extends BaseRepository<User> {

    /*@Insert("INSERT INTO ##TABLE_NAME## @INSERT{model}")
    @Lang(InsertLanguageDriver.class)
    @Options(useGeneratedKeys=true, keyProperty="managerId")
    int insert(User model) throws Exception;*/

}
