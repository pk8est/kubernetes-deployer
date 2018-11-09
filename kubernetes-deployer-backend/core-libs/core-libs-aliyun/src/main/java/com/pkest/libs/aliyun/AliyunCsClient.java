package com.pkest.libs.aliyun;

import com.aliyuncs.exceptions.ClientException;
import com.pkest.libs.aliyun.model.cs.HYDescribeClusterDetailRequest;
import com.pkest.libs.aliyun.model.cs.HYDescribeClusterDetailResponse;

/**
 * Created by wuzhonggui on 2018/11/9.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class AliyunCsClient extends AliyunClient{

    public AliyunCsClient(String region, String accessKey, String accessSecret){
        super(region, accessKey, accessSecret);
    }

    public HYDescribeClusterDetailResponse describeClusterDetail(String clusterId) throws ClientException {
        return describeClusterDetail(new HYDescribeClusterDetailRequest(clusterId));
    }

    public HYDescribeClusterDetailResponse describeClusterDetail(HYDescribeClusterDetailRequest request) throws ClientException {
        return doAction(request, HYDescribeClusterDetailResponse.class);
    }

}
