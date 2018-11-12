package com.pkest.libs.aliyun.model.cs;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 360733598@qq.com
 * @date 2018/11/10 10:39
 */
@Getter
@Setter
public class HYDescribeClustersResponse extends HYAliyunResponse{

    @JSONField(name = "vpc_id")
    private String vpcId;

}
