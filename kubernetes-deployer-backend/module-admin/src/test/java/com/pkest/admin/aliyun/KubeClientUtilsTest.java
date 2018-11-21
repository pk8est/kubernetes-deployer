package com.pkest.admin.aliyun;

import com.google.common.collect.ImmutableMap;
import com.pkest.backend.admin.AdminApplication;
import com.pkest.libs.aliyun.AliyunCsClient;
import com.pkest.libs.aliyun.model.cs.HYDescribeClusterCertsRequest;
import com.pkest.libs.aliyun.model.cs.HYDescribeClusterCertsResponse;
import com.pkest.libs.kubernetes.KubeClient;
import com.pkest.libs.kubernetes.KubeClientImpl;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpecBuilder;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;

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

    @Rule
    public KubernetesServer server = new KubernetesServer();

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
    public void setUp() throws Exception {
        aliyunCsClient = new AliyunCsClient(region, accessKey, accessSecret);
        HYDescribeClusterCertsResponse response = aliyunCsClient.describeClusterCerts(new HYDescribeClusterCertsRequest(clusterId)).getInstance();
        if(!response.isSuccess()){
            logger.error("fail");
        }
        kubeClient = KubeClientImpl.build(KubeClientImpl.configDefaultBuilder()
                .withMasterUrl("https://47.107.13.156:6443")
                .withNamespace("test-001")
                .withCaCertData(response.getCa())
                .withClientCertData(response.getCert())
                .withClientKeyData(response.getKey())
                .withLoggingInterval(0)
                .build());
    }

    @After
    public void print(){
        System.err.println(object);
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

    @Test
    public void listNodeTags() throws Exception{
        Map<String, String> selector = new HashMap<>();
        selector.put("test", "USER_LABEL_VALUE");
        object = kubeClient.listNode(selector);
    }

    @Test
    public void listJob() throws Exception{
        object = kubeClient.listJob();
    }

    @Test
    public void createJob() throws Exception{
        Job job = new Job();
        object = kubeClient.createJob(job);
    }

    @Test
    public void createDeployment() throws Exception{
        Deployment deployment = new DeploymentBuilder()
                .withNewMetadata()
                .withName("deployment1")
                .withLabels(ImmutableMap.<String, String>of("test", "test000"))
                .withNamespace("test-001")
                .endMetadata()
                .build();
        Map<String, String> annotations = new HashMap<>();
        annotations.put("test", "USER_LABEL_VALUE");
        Map<String, String> podLabels = ImmutableMap.<String, String>of("test", "test000");

        DeploymentSpec deploymentSpec = new DeploymentSpecBuilder().withNewStrategy()
                .withNewRollingUpdate().withMaxSurge(new IntOrString(0)).withMaxUnavailable(new IntOrString(1))
                .endRollingUpdate().endStrategy()
                .withNewSelector().withMatchLabels(ImmutableMap.<String, String>of("test", "test000")).endSelector()
                .withNewTemplate().withNewMetadata().withLabels(podLabels)
                .withAnnotations(annotations).withDeletionGracePeriodSeconds(0L).endMetadata()
                .endTemplate().build();
        deployment.setSpec(deploymentSpec);

       /* new DeploymentSpecBuilder().buildTemplate();
        DeploymentSpec deploymentSpec = new DeploymentSpec();
        PodTemplateSpec templateSpec = new PodTemplateSpec();
        deploymentSpec.setTemplate(templateSpec);
        Deployment deployment = new DeploymentBuilder().withNewMetadata()
                .withName("deployment1")
                .withNamespace("test-001")
                .addToLabels("testKey", "testValue")
                .endMetadata()
                .withNewSpec()
                .endSpec()
                .build();*/
        object = kubeClient.createDeployment(deployment);
    }

}
