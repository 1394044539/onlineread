package com.wpy.controller;

import com.wpy.annotation.SysLogs;
import com.wpy.config.jedis.JedisCache;
import com.wpy.constant.CacheConstant;
import com.wpy.dto.UserDto;
import com.wpy.entity.SysUser;
import com.wpy.enums.ParamEnums;
import com.wpy.enums.ResponeseCode;
import com.wpy.exception.RequestException;
import com.wpy.service.PhoneMessageService;
import com.wpy.service.SysUserService;
import com.wpy.utils.RequestUtils;
import com.wpy.utils.ResponseResult;
import com.wpy.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 可不登录调用的接口
 * @author 13940
 * @date 2021/4/3
 */

@RestController
@RequestMapping("/rest/sysUser")
@Slf4j
public class SysUserRestController {

    @Autowired
    private JedisCache jedisCache;
    @Autowired
    private PhoneMessageService phoneMessageService;
    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/getVerifyCode")
    @SysLogs("获取验证码")
    public ResponseResult getVerifyCode(@RequestParam("phone")String phone){
        if(StringUtils.isEmpty(phone)){
            throw RequestException.fail(ResponeseCode.PHONE_NOT_EMPTY.getMsg());
        }
        //获取验证码
        String verifyCode = RandomStringUtils.randomNumeric(6);
        jedisCache.set(CacheConstant.VERIFY_CODE+phone,verifyCode,300);
        return ResponseResult.success(phoneMessageService.sendMessage(phone, verifyCode));
    }

    @PutMapping("/reg")
    @SysLogs("用户注册")
    public ResponseResult userReg(@Validated @RequestBody UserDto userDto, HttpServletRequest request){
        SysUser sysUser = RequestUtils.getSysUser(request);
        if(userDto.getCheckType()==null){
            throw RequestException.fail(ResponeseCode.PARAM_ERROR.getMsg());
        }
        //1、手机号注册
        if(ParamEnums.PHONE_LOG_OR_REG.getCode().equals(userDto.getCheckType())){
            if(sysUserService.checkUserExistByPhone(userDto.getPhone())){
                throw new RequestException(ResponeseCode.PHONE_ALREADY_EXISTS);
            }
            sysUserService.phoneReg(userDto,sysUser);
        }
        //2、账号密码注册
        if(ParamEnums.ACCOUNT_LOG_OR_REG.getCode().equals(userDto.getCheckType())){
            if(sysUserService.checkUserExistByAccountName(userDto.getAccountName())){
                throw new RequestException(ResponeseCode.USER_ALREADY_EXISTS);
            }
            sysUserService.regUser(userDto,sysUser);
        }
        //3、邮箱注册
        return ResponseResult.success();
    }
}
