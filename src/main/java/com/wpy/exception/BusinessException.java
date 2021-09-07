package com.wpy.exception;

import com.wpy.enums.CodeMsgEnums;
import com.wpy.enums.ResponeseCode;
import com.wpy.utils.ResponseResult;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 业务层异常
 */
@Data
@Builder
public class BusinessException extends RuntimeException implements Serializable {

    private Integer status;
    private String msg;
    private Exception e;

    /**
     * 自定义异常
     * @param responeseCode
     * @param e
     */
    public BusinessException(ResponeseCode responeseCode, Exception e) {
        this.status=responeseCode.code;
        this.msg=responeseCode.msg;
        this.e = e;
    }

    public BusinessException(Integer code,String msg, Exception e) {
        this.status=code;
        this.msg=msg;
        this.e = e;
    }

    public BusinessException(ResponeseCode responeseCode) {
        this.status=responeseCode.code;
        this.msg=responeseCode.msg;
    }

    public static BusinessException fail(String msg){
        return BusinessException.builder()
                .status(ResponeseCode.FAIL.getCode())
                .msg(msg)
                .build();
    }

    public static BusinessException fail(String msg,Exception e){
        return BusinessException.builder()
                .status(ResponeseCode.FAIL.getCode())
                .msg(msg)
                .e(e)
                .build();
    }

    public static BusinessException fail(Integer code,String msg){
        return BusinessException.builder()
                .status(code)
                .msg(msg)
                .build();
    }

    public static BusinessException fail(Integer code,String msg,Exception e){
        return BusinessException.builder()
                .status(code)
                .msg(msg)
                .e(e)
                .build();
    }

    public static BusinessException fail(CodeMsgEnums codeMsgEnums){
        return BusinessException.builder()
                .status(codeMsgEnums.getCode())
                .msg(codeMsgEnums.getMsg())
                .build();
    }

}
