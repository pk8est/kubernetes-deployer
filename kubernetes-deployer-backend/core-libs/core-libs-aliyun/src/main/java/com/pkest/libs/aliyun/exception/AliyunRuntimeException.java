package com.pkest.libs.aliyun.exception;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ErrorType;
import com.pkest.libs.aliyun.model.cs.HYAliyunResponse;

/**
 * Created by wuzhonggui on 2018/11/14.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class AliyunRuntimeException extends RuntimeException{

    private String requestId;
    private String errCode;
    private String errMsg;
    private ErrorType errorType;

    public AliyunRuntimeException(HYAliyunResponse response) {
        this(response.getCode(), response.getMessage(), response.getRequestId());
    }

    public AliyunRuntimeException(ClientException e) {
        this(e.getErrCode(), e.getErrMsg(), e.getRequestId());
    }

    public AliyunRuntimeException(String errCode, String errMsg, String requestId) {
        this(errCode, errMsg);
        this.requestId = requestId;
    }

    public AliyunRuntimeException(String errCode, String errMsg) {
        super(errCode + " : " + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.setErrorType(ErrorType.Client);
    }

    public AliyunRuntimeException(String message) {
        super(message);
    }

    public AliyunRuntimeException(Throwable cause) {
        super(cause);
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public ErrorType getErrorType() {
        return this.errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    public String getMessage() {
        return super.getMessage() + (null == this.getRequestId()?"":"\r\nRequestId : " + this.getRequestId());
    }

}
