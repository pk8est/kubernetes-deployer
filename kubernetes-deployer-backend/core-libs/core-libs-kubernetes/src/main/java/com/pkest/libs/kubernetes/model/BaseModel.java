package com.pkest.libs.kubernetes.model;

import com.pkest.libs.common.util.GsonUtils;

/**
 * Created by wuzhonggui on 2018/11/12.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class BaseModel {

    public String toString(){
        return GsonUtils.getGson().toJson(this);
    }

}
