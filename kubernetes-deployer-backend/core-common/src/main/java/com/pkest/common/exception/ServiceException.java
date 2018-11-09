package com.pkest.common.exception;

/**
 * Created by wuzhonggui on 2018/4/9.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */

import com.pkest.common.core.ResultCode;

/**
 * 服务（业务）异常如“ 账号或密码错误 ”，该异常只做INFO级别的日志记录 @see WebMvcConfigurer
 */
public class ServiceException extends Exception {
    private int code;
    private String extraMessage;

    public ServiceException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code=resultCode.getCode();
    }


    public ServiceException(String message, Throwable cause) {

    }
    public ServiceException(int code,String message,String extraMessage,Throwable cause){
        super(message,cause);
        this.code=code;
        this.extraMessage=extraMessage;
    }



    public ServiceException(ResultCode resultCode, String extraMessage){
        this(resultCode.getCode(),resultCode.getMessage(),extraMessage,null);
    }

    public ServiceException(String extraMessage){
        this(ResultCode.INVALID_PARAM,extraMessage);
    }


    public int getCode() {
        return code;
    }

    public String getExtraMessage() {
        return extraMessage;
    }

}
