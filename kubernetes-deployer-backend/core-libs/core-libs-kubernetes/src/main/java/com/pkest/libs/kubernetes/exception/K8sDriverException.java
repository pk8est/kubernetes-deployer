package com.pkest.libs.kubernetes.exception;

/**
 * @author 360733598@qq.com
 * @date 2018/11/6 22:45
 */
public class K8sDriverException extends Exception {
    public K8sDriverException() {
    }

    public K8sDriverException(String message) {
        super(message);
    }
}
