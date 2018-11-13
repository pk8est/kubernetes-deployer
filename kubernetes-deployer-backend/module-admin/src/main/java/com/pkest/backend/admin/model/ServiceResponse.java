package com.pkest.backend.admin.model;

import com.pkest.backend.admin.enums.ResultCode;

import java.io.Serializable;

/**
 * Created by wuzhonggui on 2018/11/13.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class ServiceResponse<T> implements Serializable {

    private T result;
    private int code;
    private String message = "";

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ServiceResponse(T result, int code, String message) {
        this.result = result;
        this.code = code;
        this.message = message;
    }


    public ServiceResponse(T result, ResultCode resultCode) {
        this(result, resultCode.code, resultCode.message);
    }

    public ServiceResponse(ResultCode resultCode) {
        this(null, resultCode);
    }
}
