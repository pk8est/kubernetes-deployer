package com.pkest.libs.aliyun;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.IClientProfile;
import com.pkest.libs.aliyun.model.cs.*;

/**
 * Created by wuzhonggui on 2018/11/9.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class AliyunCsClient implements HYAliyunClient{

    private DefaultAliyunClient client;

    @Override
    public DefaultAliyunClient getClient() {
        return client;
    }

    @Override
    public void setClient(DefaultAliyunClient client) {
        this.client = client;
    }

    public AliyunCsClient(){
    }

    public AliyunCsClient(String region, String accessKey, String accessSecret){
        setClient(new DefaultAliyunClient(region, accessKey, accessSecret));
    }

    public AliyunCsClient(IClientProfile clientProfile){
        setClient(new DefaultAliyunClient(clientProfile));
    }

    @Override
    public HYAcsResponse<HYDescribeClusterDetailResponse> describeClusterDetail(String clusterId) throws ServerException, ClientException {
        return describeClusterDetail(new HYDescribeClusterDetailRequest(clusterId));
    }

    @Override
    public HYAcsResponse<HYDescribeClusterDetailResponse> describeClusterDetail(HYDescribeClusterDetailRequest request) throws ServerException, ClientException {
        return getClient().getAcsResponse(request, HYDescribeClusterDetailResponse.class);
    }

    @Override
    public HYAcsResponse<HYCreateRepoResponse> createRepo(HYCreateRepoRequest request) throws ServerException, ClientException {
        return getClient().getAcsResponse(request, HYCreateRepoResponse.class);
    }

    @Override
    public HYAcsResponse<HYDescribeClusterCertsResponse> describeClusterCerts(HYDescribeClusterCertsRequest request) throws ServerException, ClientException{
        return getClient().getAcsResponse(request, HYDescribeClusterCertsResponse.class);
    }

    @Override
    public HYAcsResponse<HYCreateClusterResponse> createCluster(HYCreateClusterRequest request) throws ServerException, ClientException{
        return getClient().getAcsResponse(request, HYCreateClusterResponse.class);
    }

    @Override
    public HYAcsResponse<HYDeleteClusterResponse> deleteCluster(HYDeleteClusterRequest request) throws ServerException, ClientException{
        return getClient().getAcsResponse(request, HYDeleteClusterResponse.class);
    }

    @Override
    public HYAcsListResponse<HYDescribeClustersResponse> describeClusters(HYDescribeClustersRequest request) throws ServerException, ClientException{
        return getClient().getAcsResponseList(request, HYDescribeClustersResponse.class);
    }

}
