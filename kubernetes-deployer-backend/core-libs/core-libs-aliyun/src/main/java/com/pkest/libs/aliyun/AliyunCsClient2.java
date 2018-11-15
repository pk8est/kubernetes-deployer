package com.pkest.libs.aliyun;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.pkest.libs.aliyun.exception.AliyunClientException;
import com.pkest.libs.aliyun.model.cs.*;

/**
 * Created by wuzhonggui on 2018/11/9.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class AliyunCsClient2 extends AliyunClient{

    public AliyunCsClient2(String region, String accessKey, String accessSecret){
        super(region, accessKey, accessSecret);
    }

    public AliyunCsClient2(DefaultProfile profile){
        super(profile);
    }

    public HYDescribeClusterDetailResponse describeClusterDetail(String clusterId) throws AliyunClientException {
        return describeClusterDetail(new HYDescribeClusterDetailRequest(clusterId));
    }

    public HYDescribeClusterDetailResponse describeClusterDetail(HYDescribeClusterDetailRequest request) throws AliyunClientException {
        return doAction(request, HYDescribeClusterDetailResponse.class);
    }

    public HYCreateRepoResponse createRepo(HYCreateRepoRequest request) throws AliyunClientException {
        return doAction(request, HYCreateRepoResponse.class);
    }

    public HYDescribeClusterCertsResponse describeClusterCerts(HYDescribeClusterCertsRequest request) throws AliyunClientException{
        return doAction(request, HYDescribeClusterCertsResponse.class);
    }

    public HYCreateClusterResponse createCluster(HYCreateClusterRequest request) throws AliyunClientException{
        return doAction(request, HYCreateClusterResponse.class);
    }

    public HYDeleteClusterResponse deleteCluster(HYDeleteClusterRequest request) throws AliyunClientException{
        return doAction(request, HYDeleteClusterResponse.class);
    }

    public HYAliyunListResponse<HYDescribeClustersResponse> describeClusters(HYDescribeClustersRequest request) throws ClientException{
        return doListAction(request, HYDescribeClustersResponse.class);
    }

}
