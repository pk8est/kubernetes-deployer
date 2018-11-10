package com.pkest.admin.aliyun;

import com.aliyuncs.profile.DefaultProfile;
import com.pkest.backend.admin.AdminApplication;
import com.pkest.libs.aliyun.AliyunCsClient;
import com.pkest.libs.aliyun.model.cs.*;
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
public class AliyunUtilsTest {

    private AliyunCsClient client;

    @Value("${aliyun.account.accessKey}")
    private String accessKey;

    @Value("${aliyun.account.accessSecret}")
    private String accessSecret;

    private String region = "cn-shenzhen";
    private String clusterId = "c7686ee47740a444ba757ff4a6c5e979f";
    private HYAliyunResponse response;
    private static final Logger logger = LoggerFactory.getLogger(AliyunUtilsTest.class);

    @Before
    public void setUp(){
        client = new AliyunCsClient(region, accessKey, accessSecret);
    }

    @After
    public void print(){
        logger.info("{} response: {}", response.getRequestId(), response);
    }

    @Test
    public void DescribeClusterDetailRequest() throws Exception {
        response = client.describeClusterDetail(clusterId);
    }

    @Test
    public void CreateRepoRequest() throws Exception {
        DefaultProfile.addEndpoint("cn-shenzhen", "cn-shenzhen", "cr", "cr.cn-shenzhen.aliyuncs.com");
        HYCreateRepoRequest.Repo repo = new HYCreateRepoRequest.Repo();
        repo.setRepoName("test-001");
        repo.setRepoNamespace("default");
        repo.setRepoType("PRIVATE");
        repo.setSummary("test");
        response = client.createRepo(new HYCreateRepoRequest(repo));
    }

    @Test
    public void DescribeClusterCertsRequest() throws Exception{
        HYDescribeClusterCertsRequest request = new HYDescribeClusterCertsRequest(clusterId);
        response = client.describeClusterCerts(request);
    }

    @Test
    public void CreateClusterRequest() throws Exception{
        HYCreateClusterRequest request = new HYCreateClusterRequest();
        request.setName("test-001");
        request.setClusterType("Kubernetes");
        request.setRegionId("cn-shenzhen");
        request.setZoneid("cn-shenzhen-a");
        response = client.createCluster(request);
    }

    @Test
    public void DeleteClusterRequest() throws Exception{
        HYDeleteClusterRequest request = new HYDeleteClusterRequest("cc49b730207c2460db4eb579cbeb2604e");
        response = client.deleteCluster(request);
        if(response.isSuccess()){
            logger.info("删除成功!");
        }else{
            logger.error("删除失败!");
        }
    }

    @Test
    public void DescribeClustersRequest() throws Exception{
        HYDescribeClustersRequest request = new HYDescribeClustersRequest();
        response = client.describeClusters(request);
    }

}
