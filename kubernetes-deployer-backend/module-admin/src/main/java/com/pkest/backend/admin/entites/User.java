package com.pkest.backend.admin.entites;

import com.pkest.libs.common.util.GsonUtils;
import lombok.Data;

@Data
public class User {

    private Integer managerId;
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