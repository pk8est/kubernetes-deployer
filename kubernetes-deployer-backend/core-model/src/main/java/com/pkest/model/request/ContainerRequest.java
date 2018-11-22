package com.pkest.model.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Map;

/**
 * Created by wuzhonggui on 2018/11/21.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@Data
public class ContainerRequest extends BaseRequestModel{

    private String imageName;
    @JSONField(name = "image")
    private Map<String, String> images;
    private String version;
    private boolean always = true;

}
