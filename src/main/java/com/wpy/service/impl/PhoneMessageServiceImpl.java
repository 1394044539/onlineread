package com.wpy.service.impl;

import com.google.common.collect.Maps;
import com.wpy.config.ZhenZiConfig;
import com.wpy.dto.ZhenZiResultDto;
import com.wpy.enums.ResponeseCode;
import com.wpy.exception.BusinessException;
import com.wpy.service.PhoneMessageService;
import com.wpy.utils.JsonUtils;
import com.zhenzi.sms.ZhenziSmsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 短信服务实现类
 * @author pywang
 * @date 2020/12/6
 */
@Service
@Slf4j
public class PhoneMessageServiceImpl implements PhoneMessageService {

    @Autowired
    ZhenZiConfig zhenZiConfig;

    @Override
    public ZhenZiResultDto sendMessage(String number,String verifyCode) {
        ZhenziSmsClient client = new ZhenziSmsClient(zhenZiConfig.getUrl(), zhenZiConfig.getAppId(), zhenZiConfig.getAppSecret());
        Map<String,Object> params= Maps.newHashMap();
//        params.put("message","验证码为: "+verifyCode+",该验证码有效期为5分钟");
        params.put("templateId","2603");
        String[] templateParams = new String[2];
        templateParams[0]=verifyCode;
        templateParams[1]="5";
        params.put("templateParams",templateParams);
        params.put("number",number);
        String resultStr="";
        try {
            resultStr = client.send(params);
            log.info("发送短信验证码调用成功：{}",resultStr);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new BusinessException(ResponeseCode.PHONE_MESSAGE_SEND_FAIL);
        }
        return JsonUtils.jsonStrToObject(resultStr, ZhenZiResultDto.class);
    }
}
