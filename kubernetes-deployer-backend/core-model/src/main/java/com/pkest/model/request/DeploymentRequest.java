package com.pkest.model.request;

import lombok.Data;

import java.util.List;

/**
 * Created by wuzhonggui on 2018/11/21.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@Data
public class DeploymentRequest extends BaseRequestModel{
    private String deployName;
    private long projectId;
    private String clusterId;
    private String namespace = "default";
    private int replicas = 2;

    private List<ContainerRequest> containers;
}
