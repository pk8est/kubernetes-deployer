package com.pkest.libs.aliyun;

import com.aliyuncs.RpcAcsRequest;
import com.aliyuncs.http.MethodType;

/**
 * Created by wuzhonggui on 2018/11/14.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class HYRequest extends RpcAcsRequest<HYResponse> {

    public HYRequest() {
        super("CS", "2015-12-15", "DescribeClusters", "CS");
        /* public DescribeClustersRequest() {
            super("CS", "2015-12-15", "DescribeClusters");

        }
        this.setUriPattern("/clusters");*/
        this.setMethod(MethodType.GET);
    }

    @Override
    public Class<HYResponse> getResponseClass() {
        return HYResponse.class;
    }
}
