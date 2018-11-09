package com.pkest.libs.aliyun;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.Labels;
import com.aliyuncs.AcsRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.pkest.libs.aliyun.exception.AliyunClientException;
import com.pkest.libs.aliyun.model.cs.HYAliyunResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wuzhonggui on 2018/11/9.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class AliyunClient{

    private DefaultProfile profile;
    private DefaultAcsClient client;
    private static final Logger logger = LoggerFactory.getLogger(AliyunClient.class);

    public DefaultProfile getProfile() {
        return profile;
    }

    public void setProfile(DefaultProfile profile) {
        this.profile = profile;
    }

    public DefaultAcsClient getClient() throws AliyunClientException{
        if(client == null){
            throw new AliyunClientException("client undefined!");
        }
        return client;
    }

    public void setClient(DefaultAcsClient client) {
        this.client = client;
    }

    public AliyunClient(){

    }

    public AliyunClient(String region, String accessKey, String accessSecret){
        profile = DefaultProfile.getProfile(
                region,
                accessKey,
                accessSecret);
        client = new DefaultAcsClient(profile);
    }

    public <T extends HYAliyunResponse> T doAction(AcsRequest request, Class<T> clazz) throws ClientException{
        HttpResponse response = null;
        try {
            if(!"GET".equals(request.getMethod())){
                String requestBody = JSONObject.toJSONString(request, Labels.includes("form"));
                if(!"{}".equals(requestBody)){
                    request.setHttpContent(requestBody.getBytes(),"UTF-8", FormatType.JSON);
                }
            }
            response = getClient().doAction(request);
            String content = new String(response.getHttpContent());
            logger.info("{} {}", request.getMethod(), response.getUrl());
            logger.debug("Status: {} content: {}", response.getStatus(), content);
            HYAliyunResponse hyAliyunResponse = JSONObject.parseObject(content, clazz);
            hyAliyunResponse.setResponse(response);
            return  (T)hyAliyunResponse;
        } catch (ClientException e) {
            if(response != null) {
                logger.error("{} {}", request.getMethod(), response.getUrl());
                logger.error("Code: {} content: {}", response.getStatus(), new String(response.getHttpContent()));
            }
            logger.error("{} {}", e.getClass().getName(), e.getMessage());
            throw e;
        }
    }


}
