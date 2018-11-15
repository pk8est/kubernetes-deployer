package com.pkest.libs.aliyun;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.pkest.libs.aliyun.model.cs.*;

/**
 * Created by wuzhonggui on 2018/11/15.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public interface HYAliyunClient {
    DefaultAliyunClient getClient();

    void setClient(DefaultAliyunClient client);

    HYAcsResponse<HYDescribeClusterDetailResponse> describeClusterDetail(String clusterId) throws ServerException, ClientException;

    HYAcsResponse<HYDescribeClusterDetailResponse> describeClusterDetail(HYDescribeClusterDetailRequest request) throws ServerException, ClientException;

    HYAcsResponse<HYCreateRepoResponse> createRepo(HYCreateRepoRequest request) throws ServerException, ClientException;

    HYAcsResponse<HYDescribeClusterCertsResponse> describeClusterCerts(HYDescribeClusterCertsRequest request) throws ServerException, ClientException;

    HYAcsResponse<HYCreateClusterResponse> createCluster(HYCreateClusterRequest request) throws ServerException, ClientException;

    HYAcsResponse<HYDeleteClusterResponse> deleteCluster(HYDeleteClusterRequest request) throws ServerException, ClientException;

    HYAcsListResponse<HYDescribeClustersResponse> describeClusters(HYDescribeClustersRequest request) throws ServerException, ClientException;
}
