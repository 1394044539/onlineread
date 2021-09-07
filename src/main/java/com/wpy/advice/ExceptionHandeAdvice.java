package com.wpy.advice;

import com.wpy.exception.BusinessException;
import com.wpy.exception.GlobbalException;
import com.wpy.exception.RequestException;
import com.wpy.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionHandeAdvice {

    @ExceptionHandler(value = RequestException.class)
    public ResponseResult requestExceptionHander(RequestException exception){
        if(exception.getE()!=null){
            log.error(exception.getMessage());
        }
        return ResponseResult.error(exception.getStatus(),exception.getMsg());
    }

    @ExceptionHandler(value = BusinessException.class)
    public ResponseResult requestExceptionHander(BusinessException exception){
        if(exception.getE()!=null){
            log.error(exception.getMessage());
        }
        return ResponseResult.error(exception.getStatus(),exception.getMsg());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseResult methodArgumentNotValidException(MethodArgumentNotValidException exception){
        BindingResult bindingResult = exception.getBindingResult();
        String msg="参数校验错误";
        if(bindingResult.hasErrors()){
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            msg=allErrors.get(0).getDefaultMessage();
        }
        return ResponseResult.error(msg);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseResult requestExceptionHander(RuntimeException exception){
        if(exception!=null){
            log.error(exception.getMessage(),exception);
        }
        return ResponseResult.error();
    }
}
