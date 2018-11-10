package com.pkest.libs.aliyun.model.cs;

import com.aliyuncs.cs.model.v20151215.DeleteClusterRequest;

/**
 * @author 360733598@qq.com
 * @date 2018/11/10 10:18
 */
public class HYDeleteClusterRequest extends DeleteClusterRequest {

    public HYDeleteClusterRequest() {
    }

    public HYDeleteClusterRequest(String clusterId){
        setClusterId(clusterId);
    }
}
