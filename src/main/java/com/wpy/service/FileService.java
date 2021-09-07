package com.wpy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wpy.entity.Novel;
import com.wpy.entity.ReadFile;
import com.wpy.entity.SysUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 文件表 服务类
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
public interface FileService extends IService<ReadFile> {

    /**
     * 将文件保存到服务器
     *
     * @param sysUser
     * @param novelFile
     * @return
     */
    ReadFile saveFileToDisk(SysUser sysUser, MultipartFile novelFile);

    /**
     * 上传小说封面
     * @param imgFile
     * @param sysUser
     * @param novel
     * @return
     */
    String saveNovelImgToDisk(MultipartFile imgFile, SysUser sysUser, Novel novel);

    /**
     * 获取文件的内容
     * @param filePath
     * @param startLine
     * @param limitLine
     * @return
     */
    List<String> getFileContextList(String filePath, Long startLine, Long limitLine);

    /**
     *获得epub的内容
     * @author pywang6
     * @date 2021/3/11 15:16
     *
     * @param filePath
     * @param chapterHref
     * @return java.util.List<java.lang.String>
     */
    List<String> getEpubContextList(String filePath, String chapterHref);

    /**
     * 保存用户封面
     * @param filePhoto
     * @param sysUser
     * @return
     */
    String savePhotoToDisk(MultipartFile filePhoto, SysUser sysUser);
}
