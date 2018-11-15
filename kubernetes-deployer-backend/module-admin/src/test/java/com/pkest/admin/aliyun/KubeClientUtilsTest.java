package com.pkest.admin.aliyun;

import com.aliyuncs.exceptions.ClientException;
import com.pkest.backend.admin.AdminApplication;
import com.pkest.libs.aliyun.AliyunCsClient;
import com.pkest.libs.aliyun.model.cs.HYDescribeClusterCertsRequest;
import com.pkest.libs.aliyun.model.cs.HYDescribeClusterCertsResponse;
import com.pkest.libs.kubernetes.KubeClient;
import com.pkest.libs.kubernetes.KubeClientImpl;
import com.pkest.libs.kubernetes.exception.K8sDriverException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by wuzhonggui on 2018/11/9.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdminApplication.class)
@WebAppConfiguration
public class KubeClientUtilsTest {

    private AliyunCsClient aliyunCsClient;

    private KubeClient kubeClient;

    @Value("${aliyun.account.accessKey}")
    private String accessKey;

    @Value("${aliyun.account.accessSecret}")
    private String accessSecret;

    private String region = "cn-shenzhen";
    private String clusterId = "c7686ee47740a444ba757ff4a6c5e979f";
    private Object object;
    private static final Logger logger = LoggerFactory.getLogger(KubeClientUtilsTest.class);

    @Before
    public void setUp() throws ClientException, K8sDriverException {
        aliyunCsClient = new AliyunCsClient(region, accessKey, accessSecret);
        HYDescribeClusterCertsResponse response = aliyunCsClient.describeClusterCerts(new HYDescribeClusterCertsRequest(clusterId)).getInstance();
        if(!response.isSuccess()){
            logger.error("fail");
        }
        kubeClient = KubeClientImpl.build(KubeClientImpl.configDefaultBuilder()
                .withMasterUrl("https://47.107.13.156:6443")
                //.withNamespace("test-001")
                .withCaCertData(response.getCa())
                .withClientCertData(response.getCert())
                .withClientKeyData(response.getKey())
                .build());
    }

    @After
    public void print(){
        logger.info("{} ", object);
    }

    @Test
    public void namespace() throws Exception{
        logger.info("{}", kubeClient.namespace());
    }

    @Test
    public void listNamespace() throws Exception{
        object = kubeClient.listNamespace();
    }

    @Test
    public void listNode() throws Exception{
        object = kubeClient.listNode();
    }


}
