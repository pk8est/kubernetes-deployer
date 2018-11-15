package com.pkest.libs.aliyun;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.Labels;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.aliyuncs.AcsError;
import com.aliyuncs.AcsRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ErrorType;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.reader.Reader;
import com.aliyuncs.reader.ReaderFactory;
import com.aliyuncs.transform.UnmarshallerContext;
import com.pkest.libs.aliyun.exception.AliyunClientException;
import com.pkest.libs.common.util.GsonUtils;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by wuzhonggui on 2018/11/9.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class DefaultAliyunClient{

    private IClientProfile clientProfile = null;
    private DefaultAcsClient client;
    private static SerializeConfig serializeConfig;
    private static final Logger logger = LoggerFactory.getLogger(DefaultAliyunClient.class);

    static {
        serializeConfig = new SerializeConfig();
        serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
    }

    public IClientProfile getClientProfile() {
        return clientProfile;
    }

    public void setClientProfile(IClientProfile clientProfile) {
        this.clientProfile = clientProfile;
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
        DefaultAliyunClient.serializeConfig = serializeConfig;
    }

    public DefaultAliyunClient(){}

    public DefaultAliyunClient(String region, String accessKey, String accessSecret){
        this(DefaultProfile.getProfile(region, accessKey, accessSecret));
    }

    public DefaultAliyunClient(IClientProfile clientProfile){
        this.clientProfile = clientProfile;
        this.client = new DefaultAcsClient(clientProfile);
    }

    public HttpResponse doAction(AcsRequest request) throws ServerException, ClientException {
        request.setAcceptFormat(FormatType.JSON);
        if (!MethodType.GET.equals(request.getMethod())) {
            String requestBody = JSONObject.toJSONString(request, serializeConfig, Labels.includes("form"));
            request.setHttpContent(requestBody.getBytes(), request.getEncoding(), FormatType.JSON);
        }
        HttpResponse baseResponse = getClient().doAction(request);
        logger.info("{} {} {}", request.getMethod(), baseResponse.getStatus(), baseResponse.getUrl());
        if (logger.isDebugEnabled()) {
            logger.debug("Content: {}", new String(baseResponse.getHttpContent()));
        }
        if(!baseResponse.isSuccess()) {
            AcsError error = this.readError(baseResponse, baseResponse.getHttpContentType());
            if(500 <= baseResponse.getStatus()) {
                logger.error("[{}]ErrorType:{}, ErrCode:{}, ErrMsg: {}",
                        error.getRequestId(), ErrorType.Server, error.getErrorCode(), error.getErrorMessage());
                throw new ServerException(error.getErrorCode(), error.getErrorMessage(), error.getRequestId());
            } else {
                logger.error("[{}]ErrorType:{}, ErrCode:{}, ErrMsg: {}",
                        error.getRequestId(), ErrorType.Client, error.getErrorCode(), error.getErrorMessage());
                throw new ClientException(error.getErrorCode(), error.getErrorMessage(), error.getRequestId());
            }
        }
        return baseResponse;
    }

    public <T>HYAcsResponse<T> getAcsResponse(AcsRequest request) throws ServerException, ClientException {
        return getAcsResponse(request, request.getResponseClass());
    }

    public <T>HYAcsResponse<T> getAcsResponse(AcsRequest request, Class<T> clazz) throws ServerException, ClientException {
        return parseAcsResponse(CommonUtils.buildType(clazz), doAction(request));
    }

    public <T>HYAcsResponse<T> getAcsResponse(AcsRequest request, Type type) throws ServerException, ClientException {
        return parseAcsResponse(type, doAction(request));
    }

    public <T>HYAcsResponse<T> getAcsResponse(AcsRequest request, TypeReference typeReference) throws ServerException, ClientException {
        return parseAcsResponse(typeReference.getType(), doAction(request));
    }

    private <T>HYAcsResponse<T> parseAcsResponse(Type type, HttpResponse httpResponse) throws ClientException {
        checkFormatType(httpResponse.getHttpContentType());
        try {
            HYAcsResponse<T> response = new HYAcsResponse(httpResponse);
            response.setInstance((T)parseResponseContent(httpResponse, type));
            return response;
        }catch (Exception e){
            throw new ClientException("SDK.ParseResponseContent", e.getMessage());
        }
    }

    public <T>HYAcsListResponse<T> getAcsResponseList(AcsRequest request, Class<T> clazz) throws ServerException, ClientException {
        return parseAcsResponseList(CommonUtils.buildType(List.class, clazz), doAction(request));
    }

    private <T>HYAcsListResponse<T> parseAcsResponseList(Type type, HttpResponse httpResponse) throws ClientException {
        checkFormatType(httpResponse.getHttpContentType());
        try {
            HYAcsListResponse response = new HYAcsListResponse(httpResponse);
            response.setInstance((List<T>)parseResponseContent(httpResponse, type));
            return response;
        }catch (Exception e){
            throw new ClientException("SDK.ParseResponseContent", e.getMessage());
        }
    }

    public void checkFormatType(FormatType formatType) throws ClientException {
        if(FormatType.XML.equals(formatType)){
            throw new ClientException("SDK.UnsupportedFormat", "XML format is not supported for the time being!");
        }
    }

    public Object parseResponseContent(HttpResponse httpResponse, Type type) throws ClientException {
        return JSONObject.parseObject(getResponseContent(httpResponse), type);
    }

    private AcsError readError(HttpResponse httpResponse, FormatType format) throws ClientException {
        AcsError error = new AcsError();
        Reader reader = ReaderFactory.createInstance(format);
        UnmarshallerContext context = new UnmarshallerContext();
        String stringContent = getResponseContent(httpResponse);
        Map<String, String> map = new CaseInsensitiveMap(reader.read(stringContent, "Error"));
        context.setResponseMap(map);
        return error.getInstance(context);
    }



    private String getResponseContent(HttpResponse httpResponse) throws ClientException {
        String stringContent = null;

        try {
            if(null == httpResponse.getEncoding()) {
                stringContent = new String(httpResponse.getHttpContent());
            } else {
                stringContent = new String(httpResponse.getHttpContent(), httpResponse.getEncoding());
            }

            return stringContent;
        } catch (UnsupportedEncodingException var4) {
            throw new ClientException("SDK.UnsupportedEncoding", "Can not parse response due to un supported encoding.");
        }
    }



}
