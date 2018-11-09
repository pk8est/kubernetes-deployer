package com.pkest.libs.aliyun.exception;

import com.aliyuncs.exceptions.ClientException;

/**
 * Created by wuzhonggui on 2018/11/9.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class AliyunClientException extends ClientException {

    public AliyunClientException(String errCode, String errMsg, String requestId) {
        super(errCode, errMsg, requestId);
    }

    public AliyunClientException(String errCode, String errMsg) {
        super(errCode, errMsg);
    }

    public AliyunClientException(String message) {
        super(message);
    }

    public AliyunClientException(Throwable cause) {
        super(cause);
    }
}
