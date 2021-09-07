package com.wpy.service;

import com.wpy.dto.NovelChapterDto;
import com.wpy.entity.Novel;
import com.wpy.entity.NovelChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wpy.entity.ReadFile;
import com.wpy.entity.SysUser;
import com.wpy.pojo.EpubInfoPojo;
import com.wpy.pojo.FileInfoPojo;
import com.wpy.pojo.NovelChapterPojo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 章节表 服务类
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
public interface NovelChapterService extends IService<NovelChapter> {

    /**
     * 解析txt文件生成章节信息
     * @param sysUser 用户
     * @param novelFile 小说文件
     * @param readFile 文件信息
     * @param novel
     */
    FileInfoPojo analysisTxtFile(SysUser sysUser, MultipartFile novelFile, ReadFile readFile, Novel novel);

    /**
     * 获得小说章节信息
     *
     * @param sysUser
     * @param id
     * @return
     */
    List<NovelChapter> getNovelChapter(SysUser sysUser, String id);

    /**
     * 获得小说内容
     *
     * @param sysUser
     * @param novelChapterDto
     * @return
     */
    NovelChapterPojo getNovelChapterContext(SysUser sysUser, NovelChapterDto novelChapterDto);

    /**
     * 解析epub文件生成章节信息
     * @param sysUser 用户
     * @param novelFile 小说文件
     * @param readFile 文件信息
     * @param novel
     */
    EpubInfoPojo analysisEpubFile(SysUser sysUser, MultipartFile novelFile, ReadFile readFile, Novel novel);
}
