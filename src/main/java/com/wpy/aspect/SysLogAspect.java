package com.wpy.aspect;

import com.wpy.annotation.SysLogs;
import com.wpy.entity.SysLog;
import com.wpy.entity.SysUser;
import com.wpy.exception.GlobbalException;
import com.wpy.service.SysLogService;
import com.wpy.utils.HttpClientUtils;
import com.wpy.utils.RequestUtils;
import com.wpy.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

/**
 * @author 13940
 */
@Aspect
@Component
public class SysLogAspect {

    private static final Logger log= LoggerFactory.getLogger(SysLogAspect.class);

    @Autowired
    private SysLogService sysLogService;

    @Pointcut("@annotation(com.wpy.annotation.SysLogs)")
    public void logPointCut(){
        if(log.isDebugEnabled()){
            log.info("logPointCut");
        }
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable{
        Object proceed = point.proceed();
        saveSysLog(point);
        return proceed;
    }

    protected void saveSysLog(ProceedingJoinPoint point) {
        MethodSignature signature=(MethodSignature) point.getSignature();
        Method method=signature.getMethod();
        SysLog sysLog=new SysLog();
        SysLogs sysLogs=method.getAnnotation(SysLogs.class);
        HttpServletRequest request= HttpClientUtils.getHttpServletRequest();
        SysUser sysUser = RequestUtils.getSysUser(request);
        String ip = HttpClientUtils.getIp(request);
        //名字
        String className=point.getTarget().getClass().getName();
        String methodName=point.getSignature().getName();
        //参数
        Object[] args = point.getArgs();
        String s = Arrays.toString(args);
        String param=s.length()<500?s:"参数过大，不予记录";
        log.info("请求接口：{}",className);
        log.info("方法名：{}",methodName+"("+param+")");
        log.info("请求方式：{}",request.getMethod());
        log.info("请求ip：{}",ip);
        log.info("请求人：{}",sysUser.getUserName());
        sysLog.setId(StringUtils.getUuid());
        sysLog.setCreateTime(new Date());
        sysLog.setIp(ip);
        sysLog.setMethod(request.getMethod());
        sysLog.setParam(param);
        sysLog.setPath(className+"."+methodName+"()");
        sysLog.setOperatorUserName(sysUser.getUserName());
        sysLog.setOperatorAccountName(sysUser.getAccountName());
        sysLog.setLogDescribe(sysLogs.value());
        sysLogService.save(sysLog);
    }

}
