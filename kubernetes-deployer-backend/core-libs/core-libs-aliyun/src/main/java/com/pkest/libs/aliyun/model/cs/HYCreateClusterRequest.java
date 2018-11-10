package com.pkest.libs.aliyun.model.cs;

import com.alibaba.fastjson.annotation.JSONField;
import com.aliyuncs.cs.model.v20151215.CreateClusterRequest;
import lombok.Data;

/**
 * @author 360733598@qq.com
 * @date 2018/11/10 9:22
 */
@Data
public class HYCreateClusterRequest extends CreateClusterRequest {

    @JSONField(label = "form")
    private Boolean disableRollback;
    @JSONField(label = "form")
    private String name;
    @JSONField(label = "form")
    private String clusterType;
    @JSONField(label = "form")
    private Integer timeoutMins;
    @JSONField(label = "form")
    private String regionId;
    @JSONField(label = "form")
    private String zoneid;
    @JSONField(label = "form")
    private String vpcid;
    @JSONField(label = "form")
    private String vswitchid;
    @JSONField(label = "form")
    private String containerCidr;
    @JSONField(label = "form")
    private String serviceCidr;
    @JSONField(label = "form")
    private String masterInstanceChargeType;
    @JSONField(label = "form")
    private String masterPeriodUnit;
    @JSONField(label = "form")
    private String masterPeriod;
    @JSONField(label = "form")
    private String masterAutoRenew;
    @JSONField(label = "form")
    private String masterAutoRenewPeriod;
    @JSONField(label = "form")
    private String masterInstanceType;
    @JSONField(label = "form")
    private String masterSystemDiskCategory;
    @JSONField(label = "form")
    private Integer masterSystemDiskSize;

}
