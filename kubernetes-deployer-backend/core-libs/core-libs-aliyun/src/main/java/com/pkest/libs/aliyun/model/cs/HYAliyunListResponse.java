package com.pkest.libs.aliyun.model.cs;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.http.HttpResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuzhonggui on 2018/11/9.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@Getter
@Setter
public class HYAliyunListResponse<T extends HYAliyunResponse> extends HYAliyunResponse{
    private List<T> list = new ArrayList();

    public HYAliyunListResponse(HttpResponse response) {
        setResponse(response);
    }

    public HYAliyunListResponse(HttpResponse response, Class<T> clazz) {
        setResponse(response);
        setList(JSONObject.parseArray(response.getHttpContent().length == 0 ? "[]" : new String(response.getHttpContent()), clazz));
    }

    public int size(){
        return list.size();
    }
}
