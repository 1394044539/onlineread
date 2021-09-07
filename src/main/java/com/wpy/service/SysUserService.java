package com.wpy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpy.dto.UserDto;
import com.wpy.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * <p>
 * 用户数据表 服务类
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 用户登录
     * @param userDto
     */
    void loginIn(UserDto userDto);

    /**
     * 校验用户是否已存在
     * @param accountName
     */
    boolean checkUserExistByAccountName(String accountName);

    /**
     * 校验手机号是否已存在
     * @param phone
     */
    boolean checkUserExistByPhone(String phone);

    /**
     * 注册用户
     * @param userDto 注册参数
     * @param loginUser 当前登录的用户
     */
    void regUser(UserDto userDto, SysUser loginUser);

    /**
     * 手机号注册
     * @param userDto
     * @param sysUser
     */
    void phoneReg(UserDto userDto, SysUser sysUser);

    /**
     * 校验验证码
     * @param phone
     * @param verifyCode
     * @return
     */
    void checkVerifyCode(String phone,String verifyCode);

    /**
     * 手机号登录
     * @param userDto
     */
    void loginPhone(UserDto userDto);

    /**
     * 用户列表
     * @param dto
     * @param sysUser
     * @return
     */
    Page<SysUser> getUserList(UserDto dto, SysUser sysUser);

    /**
     * 更新用户
     * @param dto
     * @param sysUser
     */
    void updateUser(UserDto dto, SysUser sysUser);

    /**
     * 修改密码
     * @param dto
     * @param sysUser
     */
    void updatePassword(UserDto dto, SysUser sysUser);

    /**
     * 删除用户
     * @param sysUser
     * @param ids
     */
    void deleteUser(SysUser sysUser, List<String> ids);

    /**
     * 根据用户id获取用户信息
     * @param id
     * @return
     */
    SysUser getUserInfoById(String id);

    /**
     * 修改更新Phone
     * @param dto
     * @param sysUser
     */
    void updatePhone(UserDto dto, SysUser sysUser);

    /**
     * 禁用用户
     * @param sysUser
     * @param userDto
     */
    void disableUser(SysUser sysUser, UserDto userDto);

    /**
     * 添加管理员
     * @param sysUser
     * @param dto
     */
    void addAdmin(SysUser sysUser, UserDto dto);

    /**
     * 管理员设置初始密码
     * @param sysUser
     * @param dto
     */
    void initAdminPwd(SysUser sysUser);
}
