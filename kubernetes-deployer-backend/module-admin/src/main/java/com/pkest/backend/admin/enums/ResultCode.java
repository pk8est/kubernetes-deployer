package com.pkest.backend.admin.enums;

import com.pkest.backend.admin.model.ServiceResponse;
import com.pkest.libs.aliyun.model.cs.HYAliyunResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by wuzhonggui on 2018/11/13.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public enum ResultCode {
    SUCCESS(0, "SUCCESS"),//成功
    FAIL(-400, "访问失败"),//失败
    UNAUTHORIZED(-401, "签名错误"),//未认证（签名错误）
    NOT_FOUND(-404, "此接口不存在"),//接口不存在
    SERVER_ERROR(-500, "系统繁忙,请稍后再试"),//服务器内部错误
    INVALID_PARAM(-10000, "参数错误"),

    ;
    public final int code;
    public final String message;

    ResultCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public <T> ServiceResponse<T> wrap(T data) {
        return wrap(data, null);
    }

    public <T> ServiceResponse<T> wrap(String msg) {
        return wrap(null, msg);
    }

    public <T> ServiceResponse<T> wrap(T data, String msg) {
        String message = this.message;
        if (!StringUtils.isBlank(msg)) {
            message = message + ":" + msg;
        }
        return new ServiceResponse<>(data, this.code, message);
    }

    public static void throwIfFail(HYAliyunResponse response){
        if(!response.isSuccess()){
            throw new RuntimeException(response.getMessage());
        }
    }

}

