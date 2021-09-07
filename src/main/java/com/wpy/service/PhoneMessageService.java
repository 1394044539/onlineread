package com.wpy.service;

import com.wpy.dto.ZhenZiResultDto;

/**
 * 短信服务类
 * @author pywang
 * @date 2020/12/6
 */
public interface PhoneMessageService {

    /**
     * 发送短信
     * @param number 手机号
     * @param verifyCode 验证码
     * @return
     */
    ZhenZiResultDto sendMessage(String number, String verifyCode);
}
