package com.pkest.libs.aliyun.model.cs;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.http.HttpResponse;
import com.pkest.libs.common.util.GsonUtils;
import com.pkest.libs.common.util.HYJSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by wuzhonggui on 2018/11/9.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@Getter
@Setter
public class HYAliyunResponse{

    private transient HttpResponse response;
    private transient JSONObject httpContentObject;
    private String code;
    private String message;
    private String requestId;

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }

    public String getHttpContent(){
        if(response != null){
            return new String(response.getHttpContent());
        }
        return "";
    }

    public Boolean isSuccess(){
        if(response != null) {
            return response.isSuccess();
        }
        return false;
    }

    public Integer getStatus(){
        if(response != null) {
            return response.getStatus();
        }
        return 0;
    }

    public JSONObject getJSONObject(){
        if(httpContentObject == null){
            if(response != null){
                httpContentObject = JSONObject.parseObject(new String(getHttpContent()));
            }else {
                httpContentObject = new JSONObject();
            }
        }
        return httpContentObject;
    }

    public <T> T get(String key) {
        return get(key, null);
    }

    public <T> T get(String key, T defaultValue) {
        return HYJSONObject.get(getJSONObject(), key, defaultValue);
    }

    public String toString(){
        return GsonUtils.getGson().toJson(this);
    }
}
