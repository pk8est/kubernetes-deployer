package com.pkest.libs.aliyun.model.cs;

import com.aliyuncs.cs.model.v20151215.DescribeClusterDetailRequest;

/**
 * Created by wuzhonggui on 2018/11/9.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class HYDescribeClusterDetailRequest extends DescribeClusterDetailRequest {

    public HYDescribeClusterDetailRequest(){

    }

    public HYDescribeClusterDetailRequest(String clusterId){
        setClusterId(clusterId);
    }
}
