package com.pkest.backend.admin.service;

import com.aliyuncs.exceptions.ClientException;
import com.pkest.backend.admin.model.ServiceResponse;
import com.pkest.libs.aliyun.exception.AliyunClientException;
import com.pkest.libs.aliyun.model.cs.HYAliyunListResponse;
import com.pkest.libs.aliyun.model.cs.HYDescribeClustersResponse;
import io.fabric8.kubernetes.api.model.apps.Deployment;

/**
 * Created by wuzhonggui on 2018/11/13.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public interface ClusterService {
    ServiceResponse<HYAliyunListResponse<HYDescribeClustersResponse>> getClusterList() throws ClientException;

    void createDeployment(Deployment deployment) throws Exception;
}
