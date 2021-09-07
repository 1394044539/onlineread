package com.wpy.wpy;

import com.wpy.entity.SysLog;
import com.wpy.service.SysLogService;
import com.wpy.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class SyslogTest {

    @Autowired
    private SysLogService sysLogService;

    @Test
    public void test(){
        SysLog sysLog=new SysLog();
        sysLog.setId(StringUtils.getUuid());
        sysLog.setOperatorUserName("");
        sysLog.setOperatorAccountName("");
        sysLog.setIp("0:0:0:0:0:0:0:1");
        sysLog.setCreateTime(new Date());
        sysLog.setMethod("GET");
        sysLog.setLogDescribe("测试");
        sysLog.setParam("[SysUser(id=1, createBy=null, createTime=null, updateBy=null, updateTime=null, accountName=null, userName=null, identityCard=null, trueName=null, phone=null, email=null, password=null)]");
        sysLogService.save(sysLog);
    }
}
