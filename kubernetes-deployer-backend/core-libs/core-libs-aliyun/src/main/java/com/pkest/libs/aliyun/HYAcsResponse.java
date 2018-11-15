package com.pkest.libs.aliyun;

import com.aliyuncs.http.HttpResponse;
import lombok.Data;

/**
 * Created by wuzhonggui on 2018/11/15.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@Data
public class HYAcsResponse<T> {

    private T instance;
    private String requestId;
    private transient HttpResponse response;

    public HYAcsResponse() {
    }

    public HYAcsResponse(HttpResponse response){
        setResponse(response);
        setRequestId(response.getHeaderValue("x-acs-request-id"));
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


}
