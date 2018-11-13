package com.pkest.backend.admin.service.impl;

import com.aliyuncs.exceptions.ClientException;
import com.pkest.backend.admin.enums.ResultCode;
import com.pkest.backend.admin.model.ServiceResponse;
import com.pkest.backend.admin.service.ClusterService;
import com.pkest.backend.admin.util.AliyunUtils;
import com.pkest.libs.aliyun.model.cs.HYAliyunListResponse;
import com.pkest.libs.aliyun.model.cs.HYDescribeClustersRequest;
import com.pkest.libs.aliyun.model.cs.HYDescribeClustersResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by wuzhonggui on 2018/11/13.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@Service
public class ClusterServiceImpl implements ClusterService {

    @Resource
    private AliyunUtils aliyunUtils;

    @Override
    public ServiceResponse<HYAliyunListResponse<HYDescribeClustersResponse>> getClusterList() throws ClientException {
        HYAliyunListResponse<HYDescribeClustersResponse>
                response = aliyunUtils.getClient().describeClusters(new HYDescribeClustersRequest());

        ResultCode.throwIfFail(response);
       /* if(!response.isSuccess()){
            return ResultCode.FAIL.wrap(response.getMessage());
        }*/

        return ResultCode.SUCCESS.wrap(response);
    }

    /*public ServiceResponse<HYAliyunListResponse<HYDescribeClustersResponse>> getClusterList(){
        try {
            HYAliyunListResponse<HYDescribeClustersResponse>
                    response = aliyunUtils.getClient().describeClusters(new HYDescribeClustersRequest());
            return ResultCode.SUCCESS.wrap(response);
        } catch (ClientException e) {
            return ResultCode.SERVER_ERROR.wrap(e.getErrMsg());
        }
    }*/
}
