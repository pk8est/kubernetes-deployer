package com.pkest.libs.aliyun.model.cs;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.http.FormatType;
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

    public HYAliyunListResponse(){

    }

    public HYAliyunListResponse(HttpResponse response) {
        setResponse(response);
    }

    public  HYAliyunListResponse(HttpResponse response, Class<T> clazz) {
        setResponse(response);
        if(response.isSuccess() && FormatType.JSON.equals(response.getHttpContentType())){
           // ParameterizedTypeImpl classType = new ParameterizedTypeImpl(new Type[]{clazz}, null, clazz);
          //  ParameterizedTypeImpl type = new ParameterizedTypeImpl(new Type[]{classType}, null, List.class);

            //setList((List<T>)JSON.parseObject(new String(response.getHttpContent()), type));
        //setList((List<T>)JSON.parseObject(new String(response.getHttpContent()), new TypeReference<List<HYDescribeClustersResponse>>(){}));
        //setList((List<T>) GsonUtils.getGson().fromJson(new String(response.getHttpContent()), new TypeToken<List<T>>(){}.getType()));
        setList(JSONObject.parseArray(response.getHttpContent().length == 0 ? "[]" : new String(response.getHttpContent()), clazz));
        }
    }

    public int size(){
        return list.size();
    }
}
