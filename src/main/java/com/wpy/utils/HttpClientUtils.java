package com.wpy.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class HttpClientUtils {

    private static String unknowStr= "unknown";

    /**
     * 获取访问的id
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request){
        String ip = "";
        try{
            ip = request.getHeader("x forwarded for");
            if (StringUtils.isEmpty(ip) || unknowStr. equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy -Client IP");
            }
            if (StringUtils.isEmpty(ip) || ip. length() == 0 || unknowStr.equalsIgnoreCase(ip)){
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || unknowStr. equalsIgnoreCase(ip)){
                ip = request.getHeader("HTTP_ CLIENT IP");
            }
            if (StringUtils.isEmpty(ip) || unknowStr. equalsIgnoreCase(ip)){
                ip = request.getHeader("HTTP_ X FORWARDED FOR");
            }
            if (StringUtils.isEmpty(ip)|| unknowStr.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        }catch (Exception e){
            log.error(" IPUtils ERROR",e) ;
            return ip ;
        }
        return ip;
    }

    /**
     * 获取request
     * @return
     */
    public static HttpServletRequest getHttpServletRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
