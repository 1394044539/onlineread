package com.wpy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wpy.constant.SqlConstant;
import com.wpy.dto.ShareDto;
import com.wpy.entity.*;
import com.wpy.enums.TypeEnums;
import com.wpy.exception.BusinessException;
import com.wpy.mapper.ShareFileMapper;
import com.wpy.mapper.ShareMapper;
import com.wpy.mapper.UserCollectionMapper;
import com.wpy.pojo.NovelTreePojo;
import com.wpy.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wpy.utils.DateUtils;
import com.wpy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wpy
 * @since 2021-03-30
 */
@Service
public class ShareServiceImpl extends ServiceImpl<ShareMapper, Share> implements ShareService {

    @Autowired
    private ShareFileService shareFileService;
    @Autowired
    private ShareFileMapper shareFileMapper;
    @Autowired
    private ShareMapper shareMapper;
    @Autowired
    private NovelService novelService;
    @Autowired
    private UserCollectionService userCollectionService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CatalogService catalogService;

    @Override
    public Share addShare(SysUser sysUser, ShareDto shareDto) {
        Share share=new Share();
        share.setId(StringUtils.getUuid());
        share.setCreateBy(sysUser.getId());
        share.setCreateTime(new Date());
        share.setInvalidTime(shareDto.getInvalidTime());
        share.setShareName(shareDto.getShareName());
        share.setSharePwd(shareDto.getSharePwd());
        share.setSharePath(shareDto.getSharePath()+"?sharePath="+share.getId());
        share.setShareStatus(TypeEnums.SHARE_EFFECTIVE.getCode());

        //判断分享类型
        share.setShareType(this.getShareType(share));

        //判断文件
        if(CollectionUtils.isEmpty(shareDto.getShareFileList())){
            throw BusinessException.fail("分享文件不能为空");
        }
        List<ShareFile> shareFiles= Lists.newArrayList();
        for (ShareFile shareFile : shareDto.getShareFileList()) {
            shareFile.setId(StringUtils.getUuid());
            shareFile.setShareId(share.getId());
            shareFiles.add(shareFile);
        }
        if(!CollectionUtils.isEmpty(shareFiles)){
            this.shareFileService.saveBatch(shareFiles);
        }
        this.save(share);
        return share;
    }

    @Override
    public void editShare(SysUser sysUser, ShareDto shareDto) {
        Share share=this.getById(shareDto.getId());
        share.setInvalidTime(shareDto.getInvalidTime());
        share.setShareName(shareDto.getShareName());
        share.setShareStatus(TypeEnums.SHARE_EFFECTIVE.getCode());

        //判断分享类型
        share.setShareType(this.getShareType(share));

        this.updateById(share);
    }

    @Override
    public Page<Share> getShareList(SysUser sysUser, ShareDto shareDto) {
        QueryWrapper<Share> qw=new QueryWrapper<Share>();
        //1、判断权限
        if(TypeEnums.ADMIN_ROLE.getCode().equals(sysUser.getRoleType())){
            //这是管理员
            if(StringUtils.isNotEmpty(shareDto.getUserId())){
                qw.eq(SqlConstant.CREATE_BY,shareDto.getUserId());
            }
        }else if(TypeEnums.ORDINARY_USER.getCode().equals(sysUser.getRoleType())){
            //这是普通用户
            qw.eq(SqlConstant.CREATE_BY,sysUser.getId());
        }
        if(StringUtils.isNotEmpty(shareDto.getShareName())){
            qw.eq(SqlConstant.SHARE_NAME,shareDto.getShareName());
        }
        if(StringUtils.isNotBlank(shareDto.getShareStatus())){
            qw.eq(SqlConstant.SHARE_STATUS,shareDto.getShareStatus());
        }
        return this.shareMapper.selectPage(new Page<Share>(shareDto.getPageNum(),shareDto.getPageSize()),qw);
    }

    @Override
    public List<NovelTreePojo> shareFileList(SysUser sysUser, ShareDto shareDto) {
        Share share = this.getById(shareDto.getId());
        //通过分享的id去看分享的文件
        List<NovelTreePojo> list = Lists.newArrayList();
        if(StringUtils.isEmpty(shareDto.getParentId())) {
            List<ShareFile> shareFiles = this.shareFileMapper.selectList(new QueryWrapper<ShareFile>()
                    .eq(SqlConstant.SHARE_ID, share.getId()));
            for (ShareFile shareFile : shareFiles) {
                NovelTreePojo novelTreePojo=new NovelTreePojo();
                novelTreePojo.setId(shareFile.getFileId());
                novelTreePojo.setType(shareFile.getFileType());
                if(TypeEnums.NOVEL_TYPE.getCode().equals(shareFile.getFileType())){
                    Novel novel = this.novelService.getById(shareFile.getFileId());
                    if(novel==null){
                        throw BusinessException.fail("分享的文件已被删除");
                    }
                    novelTreePojo.setName(novel.getNovelTitle());
                    novelTreePojo.setNovelId(novel.getId());
                    novelTreePojo.setNovelImg(novel.getNovelImg());
                }else{
                    Catalog catalog = this.catalogService.getById(shareFile.getFileId());
                    novelTreePojo.setName(catalog.getCatalogName());
                }
                list.add(novelTreePojo);
            }
        }else{
            NovelTreePojo novelTreePojo=new NovelTreePojo();
            novelTreePojo.setType(TypeEnums.ALL_TYPE.getCode());
            novelTreePojo.setParentId(shareDto.getParentId());
            list = this.userCollectionService.queryPersonTreeCollect(this.sysUserService.getById(share.getCreateBy()), novelTreePojo);
        }
        return list;
    }

    @Override
    public Integer checkNeedPwd(ShareDto shareDto) {
        Share share = this.getById(shareDto.getId());
        if(share==null){
            //分享不存在
            return -4;
        }
        if(TypeEnums.SHARE_DELETE.getCode().equals(share.getShareStatus())){
            //删除
            return -1;
        }
        if(TypeEnums.SHARE_DISABLE.getCode().equals(share.getShareStatus())){
            //禁用
            return -2;
        }
        if(StringUtils.isNotBlank(share.getInvalidTime())&&DateUtils.getPlusDateTime(1).after(share.getInvalidTime())){
            //若今天+1 大于失效日期，则已过期
            return -3;
        }
        return share.getShareType();
    }

    @Override
    public Integer checkPwd(SysUser sysUser, ShareDto shareDto) {
        Share share = this.getById(shareDto.getId());
        if(TypeEnums.ONLY_PWD.getCode().equals(share.getShareType())
                && !share.getSharePwd().equals(shareDto.getSharePwd())){
            //密码不对
            return 1;
        }
        if(TypeEnums.ASSING_USER.getCode().equals(share.getShareType())
                && !share.getCreateBy().equals(sysUser.getId())){
            //用户不对
            return 2;
        }
        if(TypeEnums.ASSING_PWD_USER.getCode().equals(share.getShareType())){
            if(!share.getCreateBy().equals(sysUser.getId())){
                //用户不对
                return 2;
            }
            if(!share.getSharePwd().equals(shareDto.getSharePwd())){
                //密码不对
                return 1;
            }
        }
        return 0;
    }

    /**
     * 获得类型分享类
     * @param share
     * @return
     */
    private Integer getShareType(Share share) {
        if(StringUtils.isEmpty(share.getSharePwd())&& StringUtils.isEmpty(share.getShareUser())){
            return TypeEnums.ANY_USER.getCode();
        }else if(StringUtils.isNotEmpty(share.getSharePwd())&&StringUtils.isEmpty(share.getShareUser())){
            return TypeEnums.ONLY_PWD.getCode();
        }else if(StringUtils.isEmpty(share.getSharePwd())&&StringUtils.isNotEmpty(share.getShareUser())){
            return TypeEnums.ASSING_USER.getCode();
        }else{
            return TypeEnums.ASSING_PWD_USER.getCode();
        }
    }
}
