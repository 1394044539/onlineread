package com.wpy.exception;

import com.wpy.enums.ResponeseCode;
import lombok.Builder;
import lombok.Data;

/**
 * @author 13940
 */
@Data
@Builder
public class RequestException extends GlobbalException{
    private Integer status;
    private String msg;
    private Exception e;

    /**
     * 自定义异常
     * @param responeseCode
     * @param e
     */
    public RequestException(ResponeseCode responeseCode, Exception e) {
        this.status=responeseCode.code;
        this.msg=responeseCode.msg;
        this.e = e;
    }

    public RequestException(Integer code,String msg, Exception e) {
        this.status=code;
        this.msg=msg;
        this.e = e;
    }

    public RequestException(ResponeseCode responeseCode) {
        this.status=responeseCode.code;
        this.msg=responeseCode.msg;
    }

    public static RequestException fail(String msg){
        return RequestException.builder()
                .status(ResponeseCode.FAIL.getCode())
                .msg(msg)
                .build();
    }

    public static RequestException fail(String msg,Exception e){
        return RequestException.builder()
                .status(ResponeseCode.FAIL.getCode())
                .msg(msg)
                .e(e)
                .build();
    }

    public static RequestException fail(Integer code,String msg){
        return RequestException.builder()
                .status(code)
                .msg(msg)
                .build();
    }

    public static RequestException fail(Integer code,String msg,Exception e){
        return RequestException.builder()
                .status(code)
                .msg(msg)
                .e(e)
                .build();
    }

}
