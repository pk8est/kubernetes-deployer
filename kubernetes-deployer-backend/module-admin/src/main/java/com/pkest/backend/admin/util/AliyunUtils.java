package com.pkest.backend.admin.util;

import com.pkest.libs.aliyun.AliyunCsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 360733598@qq.com
 * @date 2018/11/12 22:37
 */
@Component
public class AliyunUtils {

    private AliyunCsClient client;

    @Value("${aliyun.account.accessKey}")
    private String accessKey;

    @Value("${aliyun.account.accessSecret}")
    private String accessSecret;

    private String region = "cn-shenzhen";

    public AliyunCsClient getClient() {
        if (client == null){
            synchronized (this){
                if(client == null){
                    client = new AliyunCsClient(region, accessKey, accessSecret);
                }
            }
        }
        return client;
    }
}
