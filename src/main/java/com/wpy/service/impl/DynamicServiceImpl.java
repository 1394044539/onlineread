package com.wpy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpy.constant.SqlConstant;
import com.wpy.dto.DynamicDto;
import com.wpy.entity.Dynamic;
import com.wpy.entity.Novel;
import com.wpy.entity.SysUser;
import com.wpy.enums.TypeEnums;
import com.wpy.mapper.DynamicMapper;
import com.wpy.service.DynamicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wpy.service.SysUserCacheService;
import com.wpy.service.SysUserService;
import com.wpy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wpy
 * @since 2021-04-09
 */
@Service
public class DynamicServiceImpl extends ServiceImpl<DynamicMapper, Dynamic> implements DynamicService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private DynamicMapper dynamicMapper;
    @Autowired
    private SysUserCacheService sysUserCacheService;

    @Override
    public void addUploadDynamic(SysUser sysUser, List<Novel> uploadNovels) {
        Dynamic dynamic=new Dynamic();
        dynamic.setId(StringUtils.getUuid());
        dynamic.setCreateTime(new Date());
        dynamic.setDynamicTitle("小说上传成功！");
        String msg = uploadNovels.stream().map(Novel::getNovelTitle).collect(Collectors.joining("", "【", "】"));
        dynamic.setDynamicContent("您的小说："+msg+"已上传成功！");
        dynamic.setCreateBy(sysUser.getId());
        dynamic.setIsRead(TypeEnums.NOT_READ.getCode());
        dynamic.setDynamicType(TypeEnums.UPLOAD_DYNAMIC.getCode());
        dynamic.setUserId(sysUser.getId());
        this.save(dynamic);
    }

    @Override
    public void novelAudit(SysUser sysUser, List<Novel> novels) {
        Dynamic dynamic=new Dynamic();
        dynamic.setId(StringUtils.getUuid());
        dynamic.setCreateTime(new Date());
        //因为批量的状态都是一个样，所以判断第一个什么状态即可
        Novel novel = novels.get(0);
        Integer novelStatus = novel.getNovelStatus();
        String uploadUserId = novel.getUploadUserId();
        dynamic.setUserId(uploadUserId);
        dynamic.setCreateBy(sysUser.getId());
        dynamic.setIsRead(TypeEnums.NOT_READ.getCode());
        dynamic.setDynamicType(TypeEnums.AUDIT_DYNAMIC.getCode());
        String novelNames = novels.stream().map(Novel::getNovelTitle).collect(Collectors.joining("", "【", "】"));
        if(TypeEnums.NOVEL_DISABLE.getCode().equals(novelStatus)){
            dynamic.setDynamicTitle("小说被禁用！");
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("您的小说：");
            stringBuilder.append(novelNames);
            stringBuilder.append("被禁用。");
            if(StringUtils.isNotEmpty(novel.getErrorMsg())){
                stringBuilder.append("禁用原因如下：");
                stringBuilder.append(novel.getErrorMsg());
            }else {
                stringBuilder.append("请检查该小说是否有违规内容");
            }
            dynamic.setDynamicContent(stringBuilder.toString());
        }else if(TypeEnums.WAIT_AUDIT.getCode().equals(novelStatus)){
            dynamic.setDynamicTitle("发布成功！");
            dynamic.setDynamicContent("您的小说"+novelNames+"已发布成功，请等待管理员审核！");
        }else if(TypeEnums.SUCCESS_AUDIT.getCode().equals(novelStatus)){
            dynamic.setDynamicTitle("审核成功！");
            dynamic.setDynamicContent("您的小说"+novelNames+"审核完成！您可以在首页上找到您的小说了！感谢您为平台做出贡献！");
        }else if(TypeEnums.FAIL_AUDIT.getCode().equals(novelStatus)){
            dynamic.setDynamicTitle("审核失败！");
            dynamic.setDynamicContent("您的小说"+novelNames+"审核失败！请检查小说中是否有违规内容");
        }else if(TypeEnums.NOT_AUDIT.getCode().equals(novelStatus)){
            dynamic.setDynamicTitle("发布被打回");
            dynamic.setDynamicContent("您的小说"+novelNames+"被管理员打回，请检查是否有违规内容");
        }

        //管理员自己操作自己，不存信息
        if(!TypeEnums.ADMIN_ROLE.getCode().equals(sysUser.getRoleType())||!uploadUserId.equals(sysUser.getId())){
            this.save(dynamic);
        }

    }

    @Override
    public Page<Dynamic> getList(SysUser sysUser, DynamicDto dynamicDto) {
        if(TypeEnums.ORDINARY_USER.getCode().equals(sysUser.getRoleType())){
            dynamicDto.setUserId(sysUser.getId());
        }
        Page<Dynamic> page = new Page<>(dynamicDto.getPageNum(), dynamicDto.getPageSize());
        List<Dynamic> dynamicList = this.dynamicMapper.selectDynamicPage(dynamicDto, page);
        for (Dynamic record : dynamicList) {
            SysUser createUser = sysUserCacheService.getSysUser(record.getCreateBy());
            record.setCreateUsername(createUser.getUserName());
            record.setCreateAccountName(createUser.getAccountName());
        }
        return page.setRecords(dynamicList);
    }

    @Override
    public void alread(SysUser sysUser, DynamicDto dynamicDto) {
        List<String> idList = StringUtils.getIdList(dynamicDto.getId(), dynamicDto.getIds());
        //把这些id全部标记为已读
        UpdateWrapper<Dynamic> uw=new UpdateWrapper<>();
        uw.set(SqlConstant.IS_READ,TypeEnums.ALREADY_READ.getCode());
        if(!idList.isEmpty()){
            uw.in(SqlConstant.ID,idList);
        }
        if(TypeEnums.ADMIN_ROLE.getCode().equals(sysUser.getRoleType())){
            if(dynamicDto.getIsAdmin()==null||!dynamicDto.getIsAdmin()){
                //如果管理员选择是自己
                uw.eq(SqlConstant.USER_ID,sysUser.getId());
            }
        }else {
            uw.eq(SqlConstant.USER_ID,sysUser.getId());
        }

        this.update(uw);
    }

    @Override
    public void deleteDynamic(SysUser sysUser, DynamicDto dynamicDto) {
        List<String> idList = StringUtils.getIdList(dynamicDto.getId(), dynamicDto.getIds());
        //把这些id全部标记为已读
        QueryWrapper<Dynamic> qw=new QueryWrapper<>();
        if(!idList.isEmpty()){
            qw.in(SqlConstant.ID,idList);
        }
        if(TypeEnums.ADMIN_ROLE.getCode().equals(sysUser.getRoleType())){
            if(dynamicDto.getIsAdmin()==null||!dynamicDto.getIsAdmin()){
                //如果管理员选择是自己
                qw.eq(SqlConstant.USER_ID,sysUser.getId());
            }
        }else {
            qw.eq(SqlConstant.USER_ID,sysUser.getId());
        }
        this.remove(qw);
    }
}
