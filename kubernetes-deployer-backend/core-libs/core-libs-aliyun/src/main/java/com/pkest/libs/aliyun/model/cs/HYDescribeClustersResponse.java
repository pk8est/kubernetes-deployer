package com.pkest.libs.aliyun.model.cs;

import com.alibaba.fastjson.annotation.JSONField;
import com.pkest.libs.aliyun.common.EscapeJsonDeserializer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author 360733598@qq.com
 * @date 2018/11/10 10:39
 */
@Getter
@Setter
public class HYDescribeClustersResponse extends HYAliyunResponse{

    @JSONField(name = "vpc_id")
    private String vpcId;
    private String name;
    private String cluster_id;
    private String state;
    private String created;
    private String updated;
    @JSONField(deserializeUsing = EscapeJsonDeserializer.class)
    private MetaData meta_data;
    private UpgradeComponents upgradeComponents;
    private List<Map<String, Object>> outputs;

    public HYDescribeClustersResponse(){

    }

    @Getter
    @Setter
    public static class UpgradeComponents{
        private Kubernetes kubernetes;

        @Getter
        @Setter
        public static class Kubernetes{
            private String component_name;
        }

    }

    @Getter
    @Setter
    public static class MetaData {
        private String DockerVersion;
        private String EtcdVersion;
        private String KubernetesVersion;
        private String MultiAZ;
        private String SubClass;
    }
}
