package com.pkest.libs.aliyun.model.cs;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.aliyuncs.http.HttpResponse;
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

    @JSONField(serialize = false)
    private transient HttpResponse response;
    @JSONField(serialize = false)
    private transient JSONObject httpContentObject;
    @JSONField(serialize = false)
    private String code;
    @JSONField(serialize = false)
    private String message;
    @JSONField(serialize = false)
    private String requestId;

    @JSONField(serialize = false)
    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }

    @JSONField(serialize = false)
    public String getHttpContent(){
        if(response != null){
            return new String(response.getHttpContent());
        }
        return "";
    }

    @JSONField(serialize = false)
    public Boolean isSuccess(){
        if(response != null) {
            return response.isSuccess();
        }
        return false;
    }

    @JSONField(serialize = false)
    public Integer getStatus(){
        if(response != null) {
            return response.getStatus();
        }
        return 0;
    }

    @JSONField(serialize = false)
    public JSONObject getObject(){
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
        return HYJSONObject.get(getObject(), key, defaultValue);
    }

    public String toString(){
        return HYJSONObject.toJSONString(this);
    }

}
