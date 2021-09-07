package com.wpy.exception;

import com.wpy.enums.ResponeseCode;

/**
 * 全局异常类
 */
public class GlobbalException extends RuntimeException{

    /**
     * 状态码
     */
    private Integer code;
    /**
     * 消息
     */
    private String msg;
    /**
     * 异常信息
     */
    private Exception e;

    public GlobbalException(){
        this.code=ResponeseCode.FAIL.code;
        this.msg=ResponeseCode.FAIL.msg;
    }

    public GlobbalException(int code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public GlobbalException(int code){
        this.code=code;
        this.msg=ResponeseCode.FAIL.msg;
    }

    public GlobbalException(String msg){
        this.code=ResponeseCode.FAIL.code;
        this.msg=msg;
    }
}
