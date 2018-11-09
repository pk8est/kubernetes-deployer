package com.pkest.admin.aliyun;

import com.aliyuncs.profile.DefaultProfile;
import com.pkest.backend.admin.AdminApplication;
import com.pkest.libs.aliyun.AliyunCsClient;
import com.pkest.libs.aliyun.model.cs.HYAliyunResponse;
import com.pkest.libs.aliyun.model.cs.HYCreateRepoRequest;
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
    private String cluster = "c7686ee47740a444ba757ff4a6c5e979f";
    private HYAliyunResponse response;
    private static final Logger logger = LoggerFactory.getLogger(AliyunUtilsTest.class);

    @Before
    public void setUp(){
        client = new AliyunCsClient(region, accessKey, accessSecret);
    }

    @After
    public void print(){
        /*String vpc_id = response.get("vpc_id", "");
        int data_disk_size = response.get("data_disk_size", 100);
        logger.info("vpc_id: {}", vpc_id);
        logger.info("data_disk_size: {}", data_disk_size);
        logger.info("upgrade_components.Kubernetes: {}", response.get("upgrade_components.Kubernetes"));*/
        logger.info("response: {}", response);
    }

    @Test
    public void DescribeClusterDetailRequest() throws Exception {
        response = client.describeClusterDetail(cluster);
    }

    @Test
    public void CreateRepoRequest() throws Exception {
        DefaultProfile.addEndpoint("cn-shenzhen", "cn-shenzhen", "cr", "cr.cn-shenzhen.aliyuncs.com");
        HYCreateRepoRequest.Repo repo = new HYCreateRepoRequest.Repo();
        repo.setRepoName("test-001");
        repo.setRepoNamespace("default");
        repo.setRepoType("PRIVATE");
        repo.setSummary("test");
        response = client.createRepoRequest(new HYCreateRepoRequest(repo));
    }

}
