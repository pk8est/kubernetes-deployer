package com.pkest.backend.admin.entites;

import com.pkest.libs.common.util.GsonUtils;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "user_manager")
public class User {

    @Id
    private Long managerId;
    private String udb;
    private String realname;
    private Integer ctime;
    private Integer lastLogin;
    private Integer status;
    private Integer priority;


    @Override
    public String toString(){
        return GsonUtils.getGson().toJson(this);
    }
}