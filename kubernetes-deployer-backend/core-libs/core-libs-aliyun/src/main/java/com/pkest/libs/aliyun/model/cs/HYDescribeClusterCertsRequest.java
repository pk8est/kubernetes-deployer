package com.pkest.libs.aliyun.model.cs;

import com.aliyuncs.cs.model.v20151215.DescribeClusterCertsRequest;

/**
 * @author 360733598@qq.com
 * @date 2018/11/10 9:10
 */
public class HYDescribeClusterCertsRequest extends DescribeClusterCertsRequest {

    public HYDescribeClusterCertsRequest(){

    }

    public HYDescribeClusterCertsRequest(String clusterId){
        setClusterId(clusterId);
    }
}
