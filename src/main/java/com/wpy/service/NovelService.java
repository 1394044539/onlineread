package com.wpy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpy.dto.DownloadDto;
import com.wpy.dto.NovelDto;
import com.wpy.dto.NovelUploadDto;
import com.wpy.entity.Novel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wpy.entity.ReadFile;
import com.wpy.entity.SysUser;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 小说数据表 服务类
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
public interface NovelService extends IService<Novel> {

    /**
     * 上传小说
     * @param sysUser
     * @param novelUploadDto
     */
    void uploadNovel(SysUser sysUser, NovelUploadDto novelUploadDto);

    /**
     * 保存txt格式的小说
     * @param sysUser
     * @param novelUploadDto
     * @param file
     * @param novelFile
     * @return
     */
    Novel saveTxtNovel(SysUser sysUser, NovelUploadDto novelUploadDto, ReadFile file, MultipartFile novelFile);

    /**
     * 保存epub格式的小说
     * @param sysUser
     * @param novelUploadDto
     * @param file
     * @param novelFile
     * @return
     */
    Novel saveEpubNovel(SysUser sysUser, NovelUploadDto novelUploadDto, ReadFile file, MultipartFile novelFile);

    /**
     * 获得小说列表
     * @param sysUser
     * @param novelDto
     * @return
     */
    Page<Novel> getNovelList(SysUser sysUser, NovelDto novelDto);

    /**
     * 获得小说信息
     *
     * @param sysUser
     * @param id
     * @param isClick 是否是点击进来的
     * @return
     */
    Novel getNovelInfo(SysUser sysUser, String id, Boolean isClick);

    /**
     * 根据id删除小说
     * @param ids
     */
    void deleteNovel(List<String> ids);

    /**
     * 根据类型获得排名前几的小说
     * @param types
     * @return
     */
    Map<String,List> getHotNovelDataByType(List<String> types);

    /**
     * 更新热度
     * @param id
     */
    void updateNovelHot(String id);

    /**
     * 更新小说
     * @param sysUser
     * @param novelUploadDto
     */
    void updateNovel(SysUser sysUser, NovelUploadDto novelUploadDto);

    /**
     * 下载小说
     * @param request
     * @param response
     * @param downloadDto
     * @param sysUser
     */
    void downloadNovel(HttpServletRequest request, HttpServletResponse response, DownloadDto downloadDto, SysUser sysUser);

    /**
     * 校验小说是否可以访问
     * @param novel
     * @param sysUser
     * @return
     */
    void checkNovelStatus(Novel novel, SysUser sysUser);

    /**
     * 发布小说
     * @param sysUser
     * @param novelDto
     */
    void publicNovel(SysUser sysUser, NovelDto novelDto);

    /**
     * 审核小说
     * @param sysUser
     * @param novelDto
     */
    void auditNovel(SysUser sysUser, NovelDto novelDto);

    /**
     * 取消发布
     * @param sysUser
     * @param novelDto
     */
    void cancelPublic(SysUser sysUser, NovelDto novelDto);

    /**
     * 收藏数+1
     * @param id
     */
    void addCollectionNum(String id);
}
