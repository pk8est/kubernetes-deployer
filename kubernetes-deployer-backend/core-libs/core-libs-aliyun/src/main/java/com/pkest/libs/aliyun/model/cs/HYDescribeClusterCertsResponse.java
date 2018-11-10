package com.pkest.libs.aliyun.model.cs;

import lombok.Data;

/**
 * @author 360733598@qq.com
 * @date 2018/11/10 9:10
 */
@Data
public class HYDescribeClusterCertsResponse extends HYAliyunResponse {
    private String ca;
    private String cert;
    private String key;
}