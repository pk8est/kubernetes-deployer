package com.pkest.libs.aliyun;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.Labels;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.aliyuncs.AcsRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.pkest.libs.aliyun.exception.AliyunClientException;
import com.pkest.libs.aliyun.model.cs.HYAliyunListResponse;
import com.pkest.libs.aliyun.model.cs.HYAliyunResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * Created by wuzhonggui on 2018/11/9.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class AliyunClient{

    private DefaultProfile profile;
    private DefaultAcsClient client;
    private static SerializeConfig serializeConfig;
    private static final Logger logger = LoggerFactory.getLogger(AliyunClient.class);

    static {
        serializeConfig = new SerializeConfig();
        serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
    }

    public DefaultProfile getProfile() {
        return profile;
    }

    public void setProfile(DefaultProfile profile) {
        this.profile = profile;
    }

    public DefaultAcsClient getClient() throws AliyunClientException{
        if(client == null){
            throw new AliyunClientException("Client.Undefined", "Aliyun client undefined!");
        }
        return client;
    }

    public void setClient(DefaultAcsClient client) {
        this.client = client;
    }

    public static SerializeConfig getSerializeConfig() {
        return serializeConfig;
    }

    public static void setSerializeConfig(SerializeConfig serializeConfig) {
        AliyunClient.serializeConfig = serializeConfig;
    }

    public AliyunClient(){}

    public AliyunClient(String region, String accessKey, String accessSecret){
        this(DefaultProfile.getProfile(region, accessKey, accessSecret));
    }

    public AliyunClient(DefaultProfile profile){
        this.profile = profile;
        this.client = new DefaultAcsClient(profile);
    }

    public HttpResponse doAction(AcsRequest request) throws AliyunClientException{
        try {
            request.setAcceptFormat(FormatType.JSON);
            if(!"GET".equals(request.getMethod())){
                String requestBody = JSONObject.toJSONString(request, serializeConfig, Labels.includes("form"));
                if(!"{}".equals(requestBody)){
                    request.setHttpContent(requestBody.getBytes(),"UTF-8", FormatType.JSON);
                    logger.debug("RequestBody: {}", requestBody);
                }
            }

            HttpResponse response = getClient().doAction(request);
            logger.info("{} {} {}", request.getMethod(), response.getStatus(), response.getUrl());
            if(logger.isDebugEnabled()){
                logger.debug("Content: {}", new String(response.getHttpContent()));
            }
            return response;
        } catch (ClientException e) {
            logger.error("[{}]ErrCode:{}, ErrorType:{} ErrMsg: {}",
                    e.getRequestId(), e.getErrCode(), e.getErrorType(), e.getErrMsg());
            throw new AliyunClientException(e.getErrCode(), e.getErrMsg(), e.getRequestId());
        }
    }

    public String getResponseContent(HttpResponse httpResponse) throws AliyunClientException {
        String stringContent = null;

        try {
            if(null == httpResponse.getEncoding()) {
                stringContent = new String(httpResponse.getHttpContent());
            } else {
                stringContent = new String(httpResponse.getHttpContent(), httpResponse.getEncoding());
            }

            return stringContent;
        } catch (UnsupportedEncodingException var4) {
            throw new AliyunClientException("SDK.UnsupportedEncoding", "Can not parse response due to un supported encoding.");
        }
    }

    public <T extends HYAliyunResponse> T doAction(AcsRequest request, Class<T> clazz) throws AliyunClientException{
        HttpResponse response = doAction(request);
        HYAliyunResponse hyAliyunResponse = JSONObject.parseObject(getResponseContent(response), clazz);
        hyAliyunResponse.setResponse(response);
        return  (T)hyAliyunResponse;
    }

    public <T extends HYAliyunResponse> HYAliyunListResponse<T> doListAction(AcsRequest request, Class<T> clazz) throws AliyunClientException{
        HttpResponse response = doAction(request);
        if(response.isSuccess()){
            return new HYAliyunListResponse(doAction(request), clazz);
        }
        HYAliyunListResponse hyAliyunListResponse = JSONObject.parseObject(getResponseContent(response), HYAliyunListResponse.class);
        hyAliyunListResponse.setResponse(response);
        return hyAliyunListResponse;
    }

}
