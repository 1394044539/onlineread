package com.wpy.interceptor;

import com.wpy.config.jwt.JwtToken;
import com.wpy.config.jwt.JwtUtils;
import com.wpy.constant.StrConstant;
import com.wpy.entity.SysUser;
import com.wpy.enums.CodeMsgEnums;
import com.wpy.enums.ResponeseCode;
import com.wpy.exception.RequestException;
import com.wpy.service.SysUserService;
import com.wpy.utils.JsonUtils;
import com.wpy.utils.ResponseResult;
import com.wpy.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * 拦截器
 * @author pywang
 * @date 2020/11/27
 */
@Slf4j
@Controller
@Component
public class Loginlnterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException{
        //跨域问题
        boolean isOpt = this.handleCors(response,request.getMethod());
        if(isOpt){
            return true;
        }

        String token = request.getHeader(StrConstant.AUTHORIZATION);
        if(StringUtils.isEmpty(token)){
            this.notLogin(response);
            return false;
        }
        JwtToken jwtInfo = JwtUtils.getJwtInfo(token);
        if(StringUtils.isNotEmpty(jwtInfo.getAccountName())){
            log.info("拦截器认证已登录");
            return true;
        }
        this.notLogin(response);
        return false;
    }

    /**
     * 解决跨域问题
     * @param response
     * @param method
     * @return
     */
    private boolean handleCors(HttpServletResponse response, String method) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, X-Custom-Header, Authorization,aaa");
        if("OPTIONS".equals(method.toUpperCase().toString())){
            return true;
        }
        return false;
    }

    /**
     * 用户未登录
     * @param response
     * @throws IOException
     */
    private void notLogin(HttpServletResponse response) throws IOException {
        log.info("用户未登录");
        response.setContentType(StrConstant.CONTENT_TYPE);
        response.setCharacterEncoding(StrConstant.DEFAULT_CHARTSET);
        response.setStatus(ResponeseCode.SUCCESS.code);
        OutputStream outputStream = response.getOutputStream();
        String resultStr = JsonUtils.ObjectToJsonStr(ResponseResult.error(CodeMsgEnums.USER_NOT_LOGIN));
        outputStream.write(resultStr.getBytes(StrConstant.DEFAULT_CHARTSET));
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)  {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
