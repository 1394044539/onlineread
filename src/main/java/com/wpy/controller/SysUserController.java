package com.wpy.controller;


import com.wpy.annotation.SysLogs;
import com.wpy.config.jedis.JedisCache;
import com.wpy.constant.CacheConstant;
import com.wpy.constant.StrConstant;
import com.wpy.dto.UserDto;
import com.wpy.dto.UserInfoDto;
import com.wpy.dto.ZhenZiResultDto;
import com.wpy.entity.SysLog;
import com.wpy.entity.SysUser;
import com.wpy.enums.CodeMsgEnums;
import com.wpy.enums.ParamEnums;
import com.wpy.enums.ResponeseCode;
import com.wpy.exception.RequestException;
import com.wpy.service.FileService;
import com.wpy.service.PhoneMessageService;
import com.wpy.service.SysUserService;
import com.wpy.utils.RequestUtils;
import com.wpy.utils.ResponseResult;
import com.wpy.utils.ShiroUtils;
import com.wpy.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 用户数据表 前端控制器
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@RestController
@RequestMapping("/sysUser")
@Slf4j
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private JedisCache jedisCache;
    @Autowired
    private PhoneMessageService phoneMessageService;
    @Autowired
    private FileService fileService;

    @PostMapping("/login")
    @SysLogs("用户登录")
    public ResponseResult userLogin(@RequestBody UserDto userDto){
        if(userDto.getCheckType()==null){
            throw RequestException.fail(ResponeseCode.PARAM_ERROR.getMsg());
        }
        //1、账号密码登录
        if(ParamEnums.ACCOUNT_LOG_OR_REG.getCode().equals(userDto.getCheckType())){
            sysUserService.loginIn(userDto);
        }
        //2、手机号登录
        if(ParamEnums.PHONE_LOG_OR_REG.getCode().equals(userDto.getCheckType())){
            sysUserService.loginPhone(userDto);
        }
        return ResponseResult.success(SecurityUtils.getSubject().getPrincipal());
    }

    @PostMapping("/logout")
    @SysLogs("用户退出登录")
    public ResponseResult userLogout(){
        SecurityUtils.getSubject().logout();
        return ResponseResult.success("退出登录成功");
    }

    @PostMapping("/checkAccount")
    @SysLogs("校验账号是否已存在")
    public ResponseResult checkAccount(@RequestBody UserDto dto){
        //有就返回true
        return ResponseResult.success(sysUserService.checkUserExistByAccountName(dto.getAccountName()));
    }

    @PostMapping("/checkPhone")
    @SysLogs("校验账号是否已存在")
    public ResponseResult checkPhone(@RequestBody UserDto dto){
        //有就返回true
        return ResponseResult.success(sysUserService.checkUserExistByPhone(dto.getPhone()));
    }

    @GetMapping("/list")
    @SysLogs("用户列表")
    public ResponseResult getList(HttpServletRequest request,UserDto dto){
        SysUser sysUser = ShiroUtils.getSysUser(request);
        return ResponseResult.success(sysUserService.getUserList(dto,sysUser));
    }

    @PostMapping("/user")
    @SysLogs("修改用户")
    public ResponseResult updateUser(HttpServletRequest request,UserDto dto){
        SysUser sysUser = ShiroUtils.getSysUser(request);
        sysUserService.updateUser(dto,sysUser);
        return ResponseResult.success();
    }

    @PostMapping("/password")
    @SysLogs("修改密码")
    public ResponseResult updatePassword(HttpServletRequest request,@RequestBody UserDto dto){
        SysUser sysUser=RequestUtils.getSysUser(request);
        sysUserService.updatePassword(dto,sysUser);
        return ResponseResult.success();
    }

    @PostMapping("/updatePhone")
    @SysLogs("修改手机号")
    public ResponseResult updatePhone(HttpServletRequest request,@RequestBody UserDto dto){
        if(StringUtils.isEmpty(dto.getVerifyCode())){
            throw RequestException.fail("验证码不能为空");
        }
        if(StringUtils.isEmpty(dto.getPhone())){
            throw RequestException.fail("手机号不能为空");
        }
        SysUser sysUser=RequestUtils.getSysUser(request);
        sysUserService.updatePhone(dto,sysUser);
        return ResponseResult.success();
    }

    @DeleteMapping("/user")
    @SysLogs("删除用户")
    public ResponseResult deleteUser(HttpServletRequest request,@RequestParam("ids") List<String> ids){
        SysUser sysUser=ShiroUtils.getSysUser(request);
        sysUserService.deleteUser(sysUser,ids);
        return ResponseResult.success();
    }

    @PostMapping("/disable")
    @SysLogs("禁用用户")
    public ResponseResult disableUser(HttpServletRequest request,@RequestBody UserDto userDto){
        if(StringUtils.isEmpty(userDto.getId())){
            throw RequestException.fail(CodeMsgEnums.ID_IS_EMPTY.getMsg());
        }
        SysUser sysUser = ShiroUtils.getSysUser(request);
        this.sysUserService.disableUser(sysUser,userDto);
        return ResponseResult.success();
    }

    @GetMapping("/user")
    @SysLogs("获取用户信息")
    public ResponseResult getUserInfo(HttpServletRequest request,@RequestParam(value = "id",required = false)String id){
        SysUser sysUser = ShiroUtils.getSysUser(request);
        if(StringUtils.isEmpty(id)){
            sysUser.setPhone(StringUtils.phoneEncryption(sysUser.getPhone()));
            sysUser.setEmail(StringUtils.emailEncryption(sysUser.getEmail()));
            return ResponseResult.success(sysUser);
        }
        return ResponseResult.success(this.sysUserService.getUserInfoById(id));
    }

    @GetMapping("/getVerifyCode")
    @SysLogs("获取验证码")
    public ResponseResult getVerifyCode(HttpServletRequest request){
        SysUser sysUser = ShiroUtils.getSysUser(request);
        //获取验证码
        String verifyCode = RandomStringUtils.randomNumeric(6);
        jedisCache.set(CacheConstant.VERIFY_CODE+sysUser.getPhone(),verifyCode,300);
        return ResponseResult.success(phoneMessageService.sendMessage(sysUser.getPhone(), verifyCode));
    }

    @GetMapping("/checkVerifyCode")
    @SysLogs("校验验证码")
    public ResponseResult checkVerifyCode(HttpServletRequest request,@RequestParam("verifyCode")String verifyCode){
        SysUser sysUser = ShiroUtils.getSysUser(request);
        String code = jedisCache.get(CacheConstant.VERIFY_CODE + sysUser.getPhone());
        return ResponseResult.success(verifyCode.equals(code));
    }

    @PostMapping("/addAdmin")
    @SysLogs("添加管理员")
    public ResponseResult addAdmin(HttpServletRequest request,@RequestBody UserDto dto){
        SysUser sysUser = ShiroUtils.getSysUser(request);
        this.sysUserService.addAdmin(sysUser,dto);
        return ResponseResult.success();
    }

    @PostMapping("/initAdminPwd")
    @SysLogs("管理员设为初始面")
    public ResponseResult initAdminPwd(HttpServletRequest request){
        SysUser sysUser = ShiroUtils.getSysUser(request);
        this.sysUserService.initAdminPwd(sysUser);
        return ResponseResult.success();
    }

}
