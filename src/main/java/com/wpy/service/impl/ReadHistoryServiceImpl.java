package com.wpy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpy.constant.NumConstant;
import com.wpy.constant.SqlConstant;
import com.wpy.dto.ReadHistoryDto;
import com.wpy.entity.NovelChapter;
import com.wpy.entity.ReadHistory;
import com.wpy.entity.SysUser;
import com.wpy.enums.CodeMsgEnums;
import com.wpy.enums.TypeEnums;
import com.wpy.exception.BusinessException;
import com.wpy.mapper.ReadHistoryMapper;
import com.wpy.service.NovelChapterService;
import com.wpy.service.NovelService;
import com.wpy.service.ReadHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wpy.utils.HttpClientUtils;
import com.wpy.utils.MathUtils;
import com.wpy.utils.RequestUtils;
import com.wpy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 阅读历史表 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@Service
public class ReadHistoryServiceImpl extends ServiceImpl<ReadHistoryMapper, ReadHistory> implements ReadHistoryService {

    @Autowired
    private ReadHistoryMapper readHistoryMapper;
    @Autowired
    private NovelChapterService novelChapterService;
    @Autowired
    private NovelService novelService;

    @Override
    public void saveHistoryOrMark(ReadHistoryDto readHistoryDto, SysUser sysUser) {
        //1、判断是历史记录还是书签
        if(TypeEnums.HISTORY_RECORD.getCode().equals(readHistoryDto.getType())){
            this.saveHistory(readHistoryDto,sysUser);
        }else if(TypeEnums.BOOK_MARK.getCode().equals(readHistoryDto.getType())){
            this.saveBookMark(sysUser, readHistoryDto);
        }

    }

    @Override
    public Page<ReadHistory> getList(SysUser sysUser, ReadHistoryDto readHistoryDto) {

        //查看是历史记录还是书签列表
        if(TypeEnums.HISTORY_RECORD.getCode().equals(readHistoryDto.getType())){
            //1、判断是否已经登录
            if(TypeEnums.IS_LOGIN.getCode().equals(readHistoryDto.getUserType())){
                //已登录
                readHistoryDto.setUserId(sysUser.getId());
            }else if(TypeEnums.NOT_LOGIN.getCode().equals(readHistoryDto.getUserType())){
                //未登录，只查五个
                readHistoryDto.setPageSize(5);
            }else if(!TypeEnums.ADMIN_ROLE.getCode().equals(sysUser.getRoleType())){
                return new Page<>();
            }
        }else{
            //书签列表，判断登录人是不是管理员
            if(TypeEnums.ORDINARY_USER.getCode().equals(sysUser.getRoleType())){
                readHistoryDto.setUserId(sysUser.getId());
            }
        }
        Page<ReadHistory> page=this.readHistoryMapper.queryHistoryInfo(readHistoryDto,new Page<>(readHistoryDto.getPageNum(), readHistoryDto.getPageSize()));
        List<ReadHistory> records = page.getRecords();
        for (ReadHistory record : records) {
            record.setPercentage(MathUtils.div(record.getReadPosition(),record.getReadAllPosition(), NumConstant.TWO));
        }
        return page.setRecords(records);

    }

    /**
     * 保存历史记录
     * @param readHistoryDto
     * @param sysUser
     */
    private void saveHistory(ReadHistoryDto readHistoryDto, SysUser sysUser) {
        //1、首先去表中寻找当前小说是否已经在历史记录中
        QueryWrapper<ReadHistory> qw = new QueryWrapper<>();
        qw.eq(SqlConstant.NOVEL_ID,readHistoryDto.getNovelId());
        //这里肯定要找历史记录的
        qw.eq(SqlConstant.TYPE,TypeEnums.HISTORY_RECORD.getCode());
        if(TypeEnums.IS_LOGIN.getCode().equals(readHistoryDto.getUserType())){
            //已登录，查询当前用户或当前ip
            qw.and(wrapper->wrapper.eq(SqlConstant.USER_ID,sysUser.getId()).or().eq(SqlConstant.IP,readHistoryDto.getIp()));
        }else if(TypeEnums.NOT_LOGIN.getCode().equals(readHistoryDto.getUserType())){
            //未登录，只查询当前ip
            qw.eq(SqlConstant.IP,readHistoryDto.getIp());
        }
        ReadHistory history = this.getOne(qw);
        //首先这里肯定是记录历史记录
        //2、判断是更新还是新增
        if(history==null){
            this.addHistory(readHistoryDto,sysUser);
        }else{
            this.updateHistory(readHistoryDto,history,sysUser);
        }
    }

    @Override
    public void deleteHistory(List<String> id, String userId, Integer type, String ip, Integer deleteType, SysUser sysUser) {
        if(TypeEnums.CHOSE_DELETE.getCode().equals(deleteType)){
            //勾选删除
            if(CollectionUtils.isEmpty(id)){
                throw BusinessException.fail(CodeMsgEnums.ID_IS_EMPTY.getMsg());
            }
            this.removeByIds(id);
        }else if(TypeEnums.PERSON_REMOVE.getCode().equals(deleteType)){
            //用户个人清空
            QueryWrapper<ReadHistory> qw = new QueryWrapper<>();
            qw.eq(SqlConstant.TYPE,type);
            if(TypeEnums.HISTORY_RECORD.getCode().equals(type)){
                //若为历史记录，则清掉当前用户和当前ip的所有信息，这个userId需要从当前登录人信息里面拿
                qw.and(wrapper -> wrapper.eq(SqlConstant.USER_ID,sysUser.getId()).or().eq(SqlConstant.IP,ip));
            }else if(TypeEnums.BOOK_MARK.getCode().equals(type)){
                //若为书签记录，则清掉当前用户的信息
                qw.eq(SqlConstant.USER_ID,sysUser.getId());
            }
            this.remove(qw);
        }else if(TypeEnums.ADMIN_CHOSE_DELETE.getCode().equals(deleteType)){
            //管理员根据用户id清空
            if(StringUtils.isEmpty(userId)){
                throw BusinessException.fail(CodeMsgEnums.USER_ID_IS_EMPTY.getMsg());
            }
            QueryWrapper<ReadHistory> qw = new QueryWrapper<>();
            qw.eq(SqlConstant.TYPE,type);
            qw.eq(SqlConstant.USER_ID,userId);
            this.remove(qw);
        }else if(TypeEnums.ADMIN_REMOVEL_ALL.getCode().equals(deleteType)){
            QueryWrapper<ReadHistory> qw = new QueryWrapper<>();
            qw.eq(SqlConstant.TYPE,type);
            this.remove(qw);
        }

    }

    @Override
    public ReadHistory getHistory(HttpServletRequest request, ReadHistoryDto readHistoryDto) {
        SysUser sysUser = RequestUtils.getSysUser(request);
        String ip = HttpClientUtils.getIp(request);
        QueryWrapper<ReadHistory> qw = new QueryWrapper<>();
        qw.eq(SqlConstant.NOVEL_ID,readHistoryDto.getNovelId());
        if(StringUtils.isNotEmpty(readHistoryDto.getNovelChaperId())){
            qw.eq(SqlConstant.LAST_CHAPTER_ID,readHistoryDto.getNovelChaperId());
        }
        qw.eq(SqlConstant.TYPE,TypeEnums.HISTORY_RECORD.getCode());
        if(StringUtils.isEmpty(sysUser.getId())){
            //通过ip去查找
            qw.eq(SqlConstant.IP,ip);
        }else{
            //通过userId去查找
            qw.eq(SqlConstant.USER_ID,sysUser.getId());
        }
        return this.getOne(qw);
    }

    @Override
    public ReadHistory getHistoryByIp(String ip, String novelId, String novelChaperId) {
        QueryWrapper<ReadHistory> qw = new QueryWrapper<>();
        qw.eq(SqlConstant.NOVEL_ID,novelId);
        qw.eq(SqlConstant.LAST_CHAPTER_ID,novelChaperId);
        qw.eq(SqlConstant.IP,ip);
        qw.eq(SqlConstant.TYPE,TypeEnums.HISTORY_RECORD.getCode());
        return this.getOne(qw);
    }

    @Override
    public ReadHistory getHistoryByUserId(String userId, String novelId, String novelChaperId) {
        QueryWrapper<ReadHistory> qw = new QueryWrapper<>();
        qw.eq(SqlConstant.NOVEL_ID,novelId);
        qw.eq(SqlConstant.LAST_CHAPTER_ID,novelChaperId);
        qw.eq(SqlConstant.USER_ID,userId);
        qw.eq(SqlConstant.TYPE,TypeEnums.HISTORY_RECORD.getCode());
        return this.getOne(qw);
    }

    @Override
    public void saveBookMark(SysUser sysUser, ReadHistoryDto readHistoryDto) {
        //判断是修改还是增加
        if(StringUtils.isEmpty(readHistoryDto.getId())){
            //直接增加，不需要判断重复
            this.addHistory(readHistoryDto, sysUser);
        }else{
            //直接修改，只能修改名字
            ReadHistory readHistory=new ReadHistory();
            readHistory.setId(readHistoryDto.getId());
            readHistory.setBookMarkName(readHistoryDto.getMarkName());
            this.updateById(readHistory);
        }
    }

    /**
     * 更新用户阅读历史
     * @param readHistoryDto
     * @param history
     * @param sysUser
     */
    private void updateHistory(ReadHistoryDto readHistoryDto, ReadHistory history, SysUser sysUser) {
        //他有可能之前没有登录，现在登录了
        if(StringUtils.isNotEmpty(sysUser.getId())){
            history.setUserId(sysUser.getId());
        }
        history.setUpdateTime(new Date());
        history.setLastChapterId(readHistoryDto.getNovelChaperId());
        history.setReadPosition(readHistoryDto.getPosition());
        history.setReadAllPosition(readHistoryDto.getAllPosition());
        history.setBookMarkName(readHistoryDto.getMarkName());
        //如果是根据userId找的，他有可能换了ip
        history.setIp(readHistoryDto.getIp());
        this.updateById(history);
    }

    /**
     * 保存历史记录
     * @param readHistoryDto
     * @param sysUser
     */
    private void addHistory(ReadHistoryDto readHistoryDto, SysUser sysUser) {
        ReadHistory readHistory=new ReadHistory();
        readHistory.setId(StringUtils.getUuid());
        readHistory.setCreateTime(new Date());
        readHistory.setNovelId(readHistoryDto.getNovelId());
        readHistory.setIp(readHistoryDto.getIp());
        readHistory.setLastChapterId(readHistoryDto.getNovelChaperId());
        readHistory.setUserType(readHistoryDto.getUserType());
        readHistory.setReadPosition(readHistoryDto.getPosition());
        readHistory.setReadAllPosition(readHistoryDto.getAllPosition());
        readHistory.setUpdateTime(new Date());
        readHistory.setType(readHistoryDto.getType());
        if(StringUtils.isEmpty(readHistoryDto.getMarkName())){
            NovelChapter chapter = novelChapterService.getById(readHistoryDto.getNovelChaperId());
            readHistory.setBookMarkName(chapter.getChapterName()+"_"+System.currentTimeMillis());
        }else {
            readHistory.setBookMarkName(readHistoryDto.getMarkName());
        }
        if(StringUtils.isNotEmpty(sysUser.getId())){
            readHistory.setUserId(sysUser.getId());
        }
        this.save(readHistory);
    }
}
