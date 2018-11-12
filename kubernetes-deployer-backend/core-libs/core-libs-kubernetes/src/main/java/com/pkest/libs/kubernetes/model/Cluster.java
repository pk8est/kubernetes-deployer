package com.pkest.libs.kubernetes.model;

/**
 * Created by wuzhonggui on 2018/11/12.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class Cluster extends BaseModel{

    private String api;
    private String username;
    private String password;
    private String oauthToken;
    private String tag;
    private String domain;
    private String dns;
    private String etcd;
    private String ownerName;
    private int ownerId;
}
