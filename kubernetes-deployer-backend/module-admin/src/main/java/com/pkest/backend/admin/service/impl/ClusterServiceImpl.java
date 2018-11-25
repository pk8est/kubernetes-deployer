package com.pkest.backend.admin.service.impl;

import com.aliyuncs.exceptions.ClientException;
import com.pkest.backend.admin.enums.ResultCode;
import com.pkest.backend.admin.model.ServiceResponse;
import com.pkest.backend.admin.service.ClusterService;
import com.pkest.backend.admin.util.AliyunUtils;
import com.pkest.libs.aliyun.HYAcsListResponse;
import com.pkest.libs.aliyun.model.cs.HYDescribeClusterCertsRequest;
import com.pkest.libs.aliyun.model.cs.HYDescribeClusterCertsResponse;
import com.pkest.libs.aliyun.model.cs.HYDescribeClustersRequest;
import com.pkest.libs.aliyun.model.cs.HYDescribeClustersResponse;
import com.pkest.libs.kubernetes.KubeClient;
import com.pkest.libs.kubernetes.KubeClientImpl;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by wuzhonggui on 2018/11/13.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@Service
public class ClusterServiceImpl implements ClusterService {

    @Resource
    private AliyunUtils aliyunUtils;

    @Override
    public ServiceResponse getClusterList() throws ClientException {
        HYAcsListResponse<HYDescribeClustersResponse>
                response = aliyunUtils.getClient().describeClusters(new HYDescribeClustersRequest());
        //ResultCode.throwIfFail(response);
        //return ResultCode.SUCCESS.wrap(response);
        System.err.println(response.getInstance());
        return ResultCode.SUCCESS.wrap(response.getInstance());

    }

    @Override
    public void createDeployment(Deployment deployment) throws Exception {
        String clusterId = "c7686ee47740a444ba757ff4a6c5e979f";
        ObjectMeta objectMeta = new ObjectMeta();
        objectMeta.setName("test-" + System.currentTimeMillis());
        deployment.setMetadata(objectMeta);
        HYDescribeClusterCertsResponse response = aliyunUtils.getClient().describeClusterCerts(new HYDescribeClusterCertsRequest(clusterId)).getInstance();
        KubeClient kubeClient = KubeClientImpl.build(KubeClientImpl.configDefaultBuilder()
                .withMasterUrl("https://47.107.13.156:6443")
                .withNamespace("test-001")
                .withCaCertData(response.getCa())
                .withClientCertData(response.getCert())
                .withClientKeyData(response.getKey())
                .withLoggingInterval(0)
                .build());

        kubeClient.createDeployment(deployment);
    }
}
