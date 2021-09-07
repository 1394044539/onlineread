package com.wpy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpy.constant.SqlConstant;
import com.wpy.dto.SysNoticeDto;
import com.wpy.entity.SysNotice;
import com.wpy.entity.SysUser;
import com.wpy.enums.CodeMsgEnums;
import com.wpy.enums.ParamEnums;
import com.wpy.enums.TypeEnums;
import com.wpy.exception.BusinessException;
import com.wpy.mapper.SysNoticeMapper;
import com.wpy.service.SysNoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wpy.service.SysUserCacheService;
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
 * @since 2021-03-24
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements SysNoticeService {

    @Autowired
    private SysNoticeMapper sysNoticeMapper;
    @Autowired
    private SysUserCacheService sysUserCacheService;

    @Override
    public void addNotice(SysUser sysUser, SysNotice sysNotice) {
        sysNotice.setId(StringUtils.getUuid());
        sysNotice.setCreateBy(sysUser.getId());
        sysNotice.setCreateTime(new Date());
        this.checkNoticeParam(sysNotice);
        this.save(sysNotice);
    }

    /**
     * 关闭公告
     * @param id
     */
    @Override
    public void closeNotice(String id) {
        UpdateWrapper<SysNotice> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set(SqlConstant.IS_OPEN, false);
        if(StringUtils.isNotEmpty(id)){
            updateWrapper.eq(SqlConstant.ID,id);
        }
        this.update(updateWrapper);
    }

    @Override
    public void openNotice(String id) {
        if(StringUtils.isEmpty(id)){
            throw BusinessException.fail(CodeMsgEnums.ID_IS_EMPTY.getMsg());
        }
        //关闭其他公告
        this.closeNotice(null);
        //打开当前公告
        UpdateWrapper<SysNotice> updateWrapper=new UpdateWrapper<>();
        updateWrapper.set(SqlConstant.IS_OPEN,true);
        updateWrapper.eq(SqlConstant.ID,id);
        this.update(updateWrapper);
    }

    @Override
    public void deleteNotice(List<String> ids) {
        if(!CollectionUtils.isEmpty(ids)){
            this.removeByIds(ids);
        }
    }

    @Override
    public void editNotice(SysUser sysUser, SysNotice sysNotice) {
        sysNotice.setUpdateBy(sysUser.getId());
        sysNotice.setUpdateTime(new Date());
        this.checkNoticeParam(sysNotice);

        this.updateById(sysNotice);
    }

    /**
     * 检查参数
     * @param sysNotice
     */
    private void checkNoticeParam(SysNotice sysNotice) {
        if(StringUtils.isEmpty(sysNotice.getTitle())){
            throw BusinessException.fail(CodeMsgEnums.TITLE_NOT_EMPTY.getMsg());
        }
        if(StringUtils.isEmpty(sysNotice.getContent())){
            throw BusinessException.fail(CodeMsgEnums.CONTENT_NOT_EMPTY.getMsg());
        }
        if(sysNotice.getIsOpen()!=null&&sysNotice.getIsOpen()) {
            //这里要关闭其他的公告
            this.closeNotice(null);
        }
    }

    @Override
    public Page<SysNotice> getList(SysNoticeDto sysNoticeDto) {
        Page<SysNotice> page=new Page<>(sysNoticeDto.getPageNum(),sysNoticeDto.getPageSize());
        QueryWrapper<SysNotice> qw = new QueryWrapper<>();
        if(TypeEnums.MAIN_PAGE_TYPR.getCode().equals(sysNoticeDto.getPageType())){
            //首页的公告
            qw.orderByDesc(SqlConstant.CREATE_TIME);
        }else if(TypeEnums.ADMIN_PAGE_TYPE.getCode().equals(sysNoticeDto.getPageType())){
            //管理员页面，全部展示
            if(StringUtils.isNotEmpty(sysNoticeDto.getTitle())){
                qw.like(SqlConstant.TITLE,sysNoticeDto.getTitle());
            }
            if(StringUtils.isNotBlank(sysNoticeDto.getStartTime())){
                qw.ge(SqlConstant.CREATE_TIME,sysNoticeDto.getStartTime());
            }
            if(StringUtils.isNotBlank(sysNoticeDto.getEndTime())){
                qw.le(SqlConstant.CREATE_TIME,sysNoticeDto.getStartTime());
            }
            if(StringUtils.isNotBlank(sysNoticeDto.getIsOpen())){
                qw.eq(SqlConstant.IS_OPEN,sysNoticeDto.getIsOpen());
            }
            if(ParamEnums.ORDER_ASC.getCode().equals(sysNoticeDto.getOrderType())){
                qw.orderByAsc(SqlConstant.CREATE_TIME);
            }else{
                qw.orderByDesc(SqlConstant.CREATE_TIME);
            }
        }
        Page<SysNotice> sysNoticePage = this.sysNoticeMapper.selectPage(page, qw);
        List<SysNotice> records = sysNoticePage.getRecords();
        for (SysNotice record : records) {
            record.setUpdateBy(sysUserCacheService.getUsername(record.getUpdateBy()));
            record.setCreateBy(sysUserCacheService.getUsername(record.getCreateBy()));
        }
        return sysNoticePage.setRecords(records);
    }
}
