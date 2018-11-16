package com.pkest.libs.aliyun;

import com.aliyuncs.http.HttpResponse;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuzhonggui on 2018/11/15.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@Data
public class HYAcsListResponse<T> extends HYAcsResponse{

    private List<T> instance = new ArrayList();

    public HYAcsListResponse() {

    }

    public HYAcsListResponse(HttpResponse response){
        super(response);
    }


}
