package com.wpy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpy.config.jedis.JedisCache;
import com.wpy.config.jedis.RedisTemplateUtils;
import com.wpy.config.jwt.JwtToken;
import com.wpy.config.shiro.PhoneTokenDto;
import com.wpy.constant.CacheConstant;
import com.wpy.constant.SqlConstant;
import com.wpy.dto.UserDto;
import com.wpy.entity.SysDict;
import com.wpy.entity.SysUser;
import com.wpy.enums.CodeMsgEnums;
import com.wpy.enums.DictEnums;
import com.wpy.enums.ResponeseCode;
import com.wpy.enums.TypeEnums;
import com.wpy.exception.BusinessException;
import com.wpy.exception.RequestException;
import com.wpy.mapper.SysUserMapper;
import com.wpy.service.FileService;
import com.wpy.service.SysDictService;
import com.wpy.service.SysUserCacheService;
import com.wpy.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wpy.utils.DateUtils;
import com.wpy.utils.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户数据表 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private JedisCache jedisCache;
    @Autowired
    private SysUserCacheService sysUserCacheService;
    @Autowired
    private FileService fileService;
    @Autowired
    private SysDictService sysDictService;

    @Override
    public void loginIn(UserDto userDto) {
        if(StringUtils.isEmpty(userDto.getAccountName())||StringUtils.isEmpty(userDto.getPassword())){
            throw new BusinessException(ResponeseCode.USER_PASSWORD_ERROR);
        }
        Subject subject = SecurityUtils.getSubject();
        JwtToken jwtToken = new JwtToken();
        jwtToken.setAccountName(userDto.getAccountName());
        jwtToken.setPassword(userDto.getPassword());
        try {
            subject.login(jwtToken);
        } catch (DisabledAccountException e) {
            throw new BusinessException(ResponeseCode.USER_PASSWORD_ERROR.getCode(), e.getMessage(),e);
        } catch (Exception e){
            throw new BusinessException(ResponeseCode.USER_LOGIN_FAIL);
        }
        if (!subject.isAuthenticated()) {
            log.error("用户验证失败");
            throw new BusinessException(ResponeseCode.USER_LOGIN_FAIL);
        }
    }

    @Override
    public boolean checkUserExistByAccountName(String accountName) {
        if(StringUtils.isEmpty(accountName)){
            throw BusinessException.fail(ResponeseCode.ACCOUNT_NAME_NOT_EMPTY.getMsg());
        }
        int count = sysUserMapper.selectCount(new QueryWrapper<SysUser>().eq(SqlConstant.ACCOUNT_NAME, accountName));
        if(count>0){
            return true;
        }
        return false;
    }

    @Override
    public void regUser(UserDto userDto, SysUser loginUser) {
        SysUser sysUser=new SysUser();
        sysUser.setId(StringUtils.getUuid());
        //有可能是管理员创建的账号，这里取一下登录的用户
        if(TypeEnums.ADMIN_ROLE.getCode().equals(loginUser.getRoleType())){
            sysUser.setCreateBy(loginUser.getAccountName());
            sysUser.setRoleType(StringUtils.isNotBlank(userDto.getRoleType())?userDto.getRoleType():TypeEnums.ORDINARY_USER.getCode());
        }else{
            sysUser.setCreateBy(userDto.getAccountName());
            sysUser.setRoleType(TypeEnums.ORDINARY_USER.getCode());
        }
        sysUser.setCreateTime(new Date());
        sysUser.setAccountName(userDto.getAccountName());
        sysUser.setUserName(userDto.getAccountName());
        sysUser.setPassword(userDto.getPassword());
        sysUser.setPhone(userDto.getPhone());
        //加密
        sysUser.setPassword(StringUtils.md5Encryption(sysUser.getPassword(),sysUser.getAccountName()));
        sysUser.setStatus(TypeEnums.NORMAL_STATUS.getCode());
        sysUserMapper.insert(sysUser);
    }

    @Override
    public void phoneReg(UserDto userDto, SysUser sysUser) {
        if(StringUtils.isEmpty(userDto.getVerifyCode())){
            throw BusinessException.fail(ResponeseCode.VERIFY_CODE_NOT_EMPTY.getMsg());
        }
        //检查验证码
        this.checkVerifyCode(userDto.getPhone(),userDto.getVerifyCode());
        //进行注册
        this.regUser(userDto,sysUser);
    }

    @Override
    public void checkVerifyCode(String phone, String verifyCode) {
        String cacheVerifyCode = jedisCache.get(CacheConstant.VERIFY_CODE + phone);
        if(StringUtils.isEmpty(cacheVerifyCode)){
            throw BusinessException.fail(ResponeseCode.VERIFY_CODE_IS_INVALID.getMsg());
        }
        if(!cacheVerifyCode.equals(verifyCode)){
            throw BusinessException.fail(ResponeseCode.VERIFY_CODE_ERROR.getMsg());
        }
        //校验成功就删掉
        jedisCache.delete(CacheConstant.VERIFY_CODE + phone);
    }

    @Override
    public void loginPhone(UserDto userDto) {
        if(StringUtils.isEmpty(userDto.getPhone())){
            throw BusinessException.fail(ResponeseCode.PHONE_NOT_EMPTY.getMsg());
        }
        //校验验证码
        this.checkVerifyCode(userDto.getPhone(),userDto.getVerifyCode());
        //登录
        Subject subject = SecurityUtils.getSubject();
        PhoneTokenDto phoneTokenDto=new PhoneTokenDto();
        phoneTokenDto.setPhone(userDto.getPhone());
        try {
            subject.login(phoneTokenDto);
        } catch (DisabledAccountException e) {
            throw new BusinessException(ResponeseCode.PHONE_NOT_EXISTS.getCode(), e.getMessage(),e);
        } catch (Exception e){
            throw new BusinessException(ResponeseCode.USER_LOGIN_FAIL);
        }
        if (!subject.isAuthenticated()) {
            log.error("用户验证失败");
            throw new BusinessException(ResponeseCode.USER_LOGIN_FAIL);
        }
    }

    @Override
    public Page<SysUser> getUserList(UserDto dto, SysUser sysUser) {
        if(TypeEnums.ADMIN_ROLE.getCode().equals(sysUser.getRoleType())){
            QueryWrapper<SysUser> qw=new QueryWrapper();
            if(StringUtils.isNotEmpty(dto.getAccountName())){
                qw.like(SqlConstant.ACCOUNT_NAME,dto.getAccountName());
            }
            if(StringUtils.isNotEmpty(dto.getUserName())){
                qw.like(SqlConstant.USER_NAME,dto.getUserName());
            }
            if(StringUtils.isNotBlank(dto.getRoleType())){
                qw.eq(SqlConstant.ROLE_TYPE,dto.getRoleType());
            }
            if(StringUtils.isNotBlank(dto.getStartTime())){
                qw.ge(SqlConstant.CREATE_TIME,dto.getStartTime());
            }
            if(StringUtils.isNotBlank(dto.getEndTime())){
                qw.lt(SqlConstant.CREATE_TIME, DateUtils.addDay(dto.getEndTime()));
            }
            if(StringUtils.isNotBlank(dto.getStatus())){
                qw.eq(SqlConstant.STATUS,dto.getStatus());
            }
            qw.orderBy(true,dto.getAsc(),SqlConstant.CREATE_TIME);
            Page<SysUser> sysUserPage = this.sysUserMapper.selectPage(new Page<SysUser>(dto.getPageNum(), dto.getPageSize()), qw);
            return sysUserPage;
        }
        return new Page<>();
    }

    @Override
    public void updateUser(UserDto dto, SysUser sysUser) {
        SysUser newUser=new SysUser();
        BeanUtils.copyProperties(dto,newUser);
        //判断是不是上传了头像
        if(dto.getFilePhoto()!=null){
            newUser.setPhoto(fileService.savePhotoToDisk(dto.getFilePhoto(),sysUser));
        }
        if(StringUtils.isEmpty(dto.getId())){
            //为空取当前登录人的id
            newUser.setId(sysUser.getId());
        }else if(!TypeEnums.ADMIN_ROLE.getCode().equals(sysUser.getRoleType()) && !dto.getId().equals(sysUser.getId())){
            //如果用户不是管理员，且不等于当前登录人id，则报错
            throw BusinessException.fail("无权限");
        }else {
            //传递了userid，且是管理员
            newUser.setId(dto.getId());
        }
        this.updateById(newUser);
        //结束之后修改缓存
        sysUserCacheService.setUserInfo(newUser.getId());
    }

    @Override
    public void updatePassword(UserDto dto, SysUser sysUser) {
        SysUser user=new SysUser();
        //判断类型
        if(TypeEnums.ADMIN_ROLE.getCode().equals(sysUser.getRoleType())){
            //管理员角色
            if(StringUtils.isNotEmpty(dto.getId())){
                //不为空说明修改某用户id，直接改，不需要原密码
                user = this.getById(dto.getId());
            }else {
                //修改自己的密码，判断原密码是否正确
                String oldPwd=StringUtils.md5Encryption(dto.getOldPassword(),sysUser.getAccountName());
                if(!oldPwd.equals(sysUser.getPassword())){
                    throw BusinessException.fail(CodeMsgEnums.OLD_PWD_ERROE);
                }
                user=sysUser;
            }
        }else {
            //非管理员
            if(TypeEnums.FIND_PWD_BY_PWD.getCode().equals(dto.getUpdatePwdType())){
                //通过密码修改
                String oldPwd=StringUtils.md5Encryption(dto.getOldPassword(),sysUser.getAccountName());
                if(!oldPwd.equals(sysUser.getPassword())){
                    throw BusinessException.fail(CodeMsgEnums.OLD_PWD_ERROE);
                }
                user=sysUser;
            }else if(TypeEnums.FIND_PWD_BY_PHONE.getCode().equals(dto.getUpdatePwdType())){
                String verifyCode = jedisCache.get(CacheConstant.VERIFY_CODE + dto.getPhone());
                if(!dto.getVerifyCode().equals(verifyCode)){
                    //验证码错误
                    throw BusinessException.fail(CodeMsgEnums.VERIFY_CODE_ERROR.getMsg());
                }
                user = this.getOne(new QueryWrapper<SysUser>().eq(SqlConstant.PHONE, dto.getPhone()));
            }
        }
        user.setPassword(StringUtils.md5Encryption(dto.getPassword(),user.getAccountName()));
        this.updateById(user);
        //结束之后修改缓存
        sysUserCacheService.setUserInfo(user.getId());
    }

    @Override
    public void deleteUser(SysUser sysUser, List<String> ids) {
        this.removeByIds(ids);
    }

    @Override
    public SysUser getUserInfoById(String id) {
        SysUser user = this.getById(id);
        if(user==null){
            throw BusinessException.fail(ResponeseCode.USER_NOT_EXISTS.getMsg());
        }
        return user;
    }

    @Override
    public void updatePhone(UserDto dto, SysUser sysUser) {
        //获取新手机号的代码
        String code = jedisCache.get(CacheConstant.VERIFY_CODE + dto.getPhone());
        if(StringUtils.isEmpty(code)||!code.equals(dto.getVerifyCode())){
            throw BusinessException.fail(CodeMsgEnums.VERIFY_CODE_ERROR);
        }
        UpdateWrapper<SysUser> uw=new UpdateWrapper<>();
        uw.set(SqlConstant.PHONE,dto.getPhone());
        uw.eq(SqlConstant.ID,sysUser.getId());
        this.update(uw);
        //结束之后修改缓存
        sysUserCacheService.setUserInfo(sysUser.getId());
    }

    @Override
    public void disableUser(SysUser sysUser, UserDto userDto) {
        if(!TypeEnums.ADMIN_ROLE.getCode().equals(sysUser.getRoleType())){
            throw BusinessException.fail("权限不足");
        }
        UpdateWrapper<SysUser> uw=new UpdateWrapper<>();
        uw.set(SqlConstant.STATUS,userDto.getStatus());
        uw.eq(SqlConstant.ID,userDto.getId());
        this.update(uw);
    }

    @Override
    public void addAdmin(SysUser sysUser, UserDto dto) {
        if(!TypeEnums.ADMIN_ROLE.getCode().equals(sysUser.getRoleType())){
            throw BusinessException.fail("权限不足");
        }
        if(this.checkUserExistByAccountName(dto.getAccountName())){
            throw new RequestException(ResponeseCode.USER_ALREADY_EXISTS);
        }
        SysUser newUser=new SysUser();
        newUser.setId(StringUtils.getUuid());
        newUser.setAccountName(dto.getAccountName());
        newUser.setPassword(StringUtils.md5Encryption(dto.getPassword(),newUser.getAccountName()));
        newUser.setRoleType(TypeEnums.ADMIN_ROLE.getCode());
        newUser.setUserName(StringUtils.isEmpty(dto.getUserName())?"管理员":dto.getUserName());
        newUser.setCreateBy(sysUser.getAccountName());
        newUser.setCreateTime(new Date());
        newUser.setStatus(TypeEnums.NORMAL_STATUS.getCode());
        this.save(newUser);
    }

    @Override
    public void initAdminPwd(SysUser sysUser) {
        if(!TypeEnums.ADMIN_ROLE.getCode().equals(sysUser.getRoleType())){
            throw BusinessException.fail("权限不足");
        }
        SysDict dict = this.sysDictService.getOne(
                new QueryWrapper<SysDict>().eq(SqlConstant.DICT_CLASS, DictEnums.ADMIN_INIT_PWD.getKey()));
        if(dict==null){
            throw BusinessException.fail("字典默认密码不存在，请联系管理员");
        }
        sysUser.setPassword(StringUtils.md5Encryption(dict.getDictValue(),sysUser.getAccountName()));
        this.updateById(sysUser);
    }

    @Override
    public boolean checkUserExistByPhone(String phone) {
        if(StringUtils.isEmpty(phone)){
            throw BusinessException.fail(ResponeseCode.PHONE_NOT_EMPTY.getMsg());
        }
        int count = sysUserMapper.selectCount(new QueryWrapper<SysUser>().eq(SqlConstant.PHONE, phone));
        if(count>0){
            return true;
        }
        return false;
    }
}
