package com.pkest.model.request;

import lombok.Data;

/**
 * Created by wuzhonggui on 2018/11/21.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@Data
public class ContainerRequest extends BaseRequestModel{

    private String imageName;
    private String version;
    private boolean always = true;

}
