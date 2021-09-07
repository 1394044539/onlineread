package com.wpy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpy.dto.ReadHistoryDto;
import com.wpy.entity.ReadHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wpy.entity.SysUser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 阅读历史表 服务类
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
public interface ReadHistoryService extends IService<ReadHistory> {

    /**
     * 增加阅读历史
     * @param readHistoryDto
     * @param sysUser
     * @param ip
     */
    void saveHistoryOrMark(ReadHistoryDto readHistoryDto, SysUser sysUser);

    /**
     * 获得阅读历史
     * @param sysUser
     * @param readHistoryDto
     * @return
     */
    Page<ReadHistory> getList(SysUser sysUser, ReadHistoryDto readHistoryDto);

    /**
     * 删除历史记录
     * @param id
     * @param userId
     * @param type
     * @param ip
     * @param deleteType
     * @param sysUser
     */
    void deleteHistory(List<String> id, String userId, Integer type, String ip, Integer deleteType, SysUser sysUser);

    /**
     * 获得阅读历史
     * @param request
     * @param readHistoryDto
     * @return
     */
    ReadHistory getHistory(HttpServletRequest request, ReadHistoryDto readHistoryDto);

    /**
     * 通过ip查找阅读记录
     * @param ip
     * @param novelId
     * @param novelChaperId
     * @return
     */
    ReadHistory getHistoryByIp(String ip, String novelId, String novelChaperId);

    /**
     * 通过用户id查找阅读记录
     * @param id
     * @param novelId
     * @param novelChaperId
     * @return
     */
    ReadHistory getHistoryByUserId(String id, String novelId, String novelChaperId);

    /**
     * 保存书签
     * @param sysUser
     * @param readHistoryDto
     */
    void saveBookMark(SysUser sysUser, ReadHistoryDto readHistoryDto);


}
