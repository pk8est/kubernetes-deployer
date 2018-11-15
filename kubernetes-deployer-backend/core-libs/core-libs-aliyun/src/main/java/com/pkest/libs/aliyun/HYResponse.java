package com.pkest.libs.aliyun;

import com.aliyuncs.AcsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.transform.UnmarshallerContext;

/**
 * Created by wuzhonggui on 2018/11/14.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class HYResponse extends AcsResponse {

    @Override
    public AcsResponse getInstance(UnmarshallerContext unmarshallerContext) throws ClientException, ServerException {
        return null;
    }
}
