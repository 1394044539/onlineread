package com.wpy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 用户dto类
 * @author pywang
 * @date 2020/11/26
 */
@Data
public class UserDto extends AbstractSplitPageDto{

    /**
     * 账户
     */
    @Pattern(regexp = "^(\\w){6,18}$",message = "用户名必须为字母数字下划线组成的6-18位字符")
    private String accountName;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3456789]\\d{9}$",message = "手机号格式有误")
    private String phone;

    /**
     * 密码
     */
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z]\\S{5,18}$",message = "密码必须是以字母开头的6-18位字符")
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 校验类型
     */
    private Integer checkType;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户角色
     */
    private Integer roleType;

    /**
     * id
     */
    private String id;

    /**
     * 修改密码的方式
     */
    private Integer updatePwdType;

    /**
     * 原密码
     */
    private String oldPassword;

    /**
     * 用户头像
     */
    private MultipartFile filePhoto;

    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
