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
import org.apache.commons.lang3.StringUtils;
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
    private static SerializeConfig serializeConfig;

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
            throw new AliyunClientException("client undefined!");
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

    public AliyunClient(){

    }

    public AliyunClient(String region, String accessKey, String accessSecret){
        profile = DefaultProfile.getProfile(
                region,
                accessKey,
                accessSecret);
        client = new DefaultAcsClient(profile);
    }

    public void loggerDebug(String s, Object... o){
        if(logger.isDebugEnabled()) logger.debug(s, o);
    }

    public void loggerInfo(String s, Object... o){
        if(logger.isInfoEnabled()) logger.info(s, o);
    }

    public void loggerError(String s, Object... o){
        if(logger.isErrorEnabled()) logger.error(s, o);
    }

    public HttpResponse doAction(AcsRequest request) throws ClientException{
        try {
            if(!"GET".equals(request.getMethod())){
                String requestBody = JSONObject.toJSONString(request, serializeConfig, Labels.includes("form"));
                if(!"{}".equals(requestBody)){
                    request.setHttpContent(requestBody.getBytes(),"UTF-8", FormatType.JSON);
                    loggerDebug("RequestBody: {}", requestBody);
                }
            }
            HttpResponse response = getClient().doAction(request);
            loggerInfo("{} {} {}", request.getMethod(), response.getUrl(), response.getStatus());
            if(logger.isDebugEnabled()){
                loggerDebug("content: {}", new String(response.getHttpContent()));
            }
            return response;
        } catch (ClientException e) {
            loggerError("[{}] ErrCode:{}, ErrorType:{} ErrMsg: {}",
                    e.getRequestId(), e.getClass().getName(), e.getErrCode(), e.getErrorType(), e.getErrMsg());
            throw e;
        }
    }

    public <T extends HYAliyunResponse> T doAction(AcsRequest request, Class<T> clazz) throws ClientException{
        HttpResponse response = doAction(request);
        String content = response.getHttpContent().length == 0 ? "{}" : new String(response.getHttpContent());
        HYAliyunResponse hyAliyunResponse = JSONObject.parseObject(StringUtils.isBlank(content) ? "{}" : content, clazz);
        hyAliyunResponse.setResponse(response);
        return  (T)hyAliyunResponse;
    }

    public <T extends HYAliyunResponse> HYAliyunListResponse<T> doListAction(AcsRequest request, Class<T> clazz) throws ClientException{
        HYAliyunListResponse response = new HYAliyunListResponse(doAction(request), clazz);
        return response;
    }

}
