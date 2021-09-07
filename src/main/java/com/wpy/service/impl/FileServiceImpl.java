package com.wpy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wpy.config.FileUploadConfig;
import com.wpy.constant.CharConstant;
import com.wpy.constant.SqlConstant;
import com.wpy.dto.NovelDto;
import com.wpy.entity.Novel;
import com.wpy.entity.ReadFile;
import com.wpy.entity.SysUser;
import com.wpy.enums.ParamEnums;
import com.wpy.enums.TypeEnums;
import com.wpy.exception.BusinessException;
import com.wpy.mapper.FileMapper;
import com.wpy.service.FileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wpy.utils.FileUtils;
import com.wpy.utils.StringUtils;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.nio.ch.IOUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 文件表 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, ReadFile> implements FileService {

    @Autowired
    FileUploadConfig fileUploadConfig;

    @Override
    public ReadFile saveFileToDisk(SysUser sysUser, MultipartFile uploadFile) {
        ReadFile readFile=new ReadFile();
        readFile.setId(StringUtils.getUuid());
        readFile.setCreateBy(sysUser.getAccountName());
        readFile.setCreateTime(new Date());
        readFile.setFileName(uploadFile.getOriginalFilename());
        readFile.setFileSize(uploadFile.getSize());
        readFile.setIsDelete(ParamEnums.NOT_DELETE.getCode());
        readFile.setFileType(StringUtils.getFileType(uploadFile.getOriginalFilename()));
        //1、生成文件存储路径，路径以用户账号，默认用文件md5命名，相同文件不需要再弄一份了
        String md5 = FileUtils.getMd5(uploadFile);
        //查看文件表中是不是有该md5的信息
        ReadFile alreadyFile = this.getOne(new QueryWrapper<ReadFile>().eq(SqlConstant.FILE_MD5, md5));
        if(alreadyFile!=null){
            try {
                if(ParamEnums.TXT.getMsg().equals(readFile.getFileType())){
                    String code=FileUtils.getFileCode(uploadFile.getInputStream());
                    if(StringUtils.isEmpty(code)){
                        alreadyFile.setFileBytes(FileUtils.saveaIns(uploadFile.getInputStream()));
                    }else {
                        InputStreamReader isr = new InputStreamReader(uploadFile.getInputStream(), code);
                        alreadyFile.setFileBytes(IOUtils.toByteArray(isr));
                    }
                }else {
                    alreadyFile.setFileBytes(FileUtils.saveaIns(uploadFile.getInputStream()));
                }

            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
            return alreadyFile;
        }
        //没有才会选择去存一份并且上传
        String savePath=fileUploadConfig.getFileRootPath()+ CharConstant.File_SEPARATOR+sysUser.getAccountName()+CharConstant.File_SEPARATOR+md5;
        File file=new File(savePath);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        try {
            //因为流用过一次就会被关掉，所以通过btye的形式保存在内存中，后续会使用到
            if(ParamEnums.TXT.getMsg().equals(readFile.getFileType())){
                String code=FileUtils.getFileCode(uploadFile.getInputStream());
                if(StringUtils.isEmpty(code)){
                    readFile.setFileBytes(FileUtils.saveaIns(uploadFile.getInputStream()));
                }else {
                    InputStreamReader isr = new InputStreamReader(uploadFile.getInputStream(), code);
                    readFile.setFileBytes(IOUtils.toByteArray(isr));
                }
            }else {
                readFile.setFileBytes(FileUtils.saveaIns(uploadFile.getInputStream()));
            }
            uploadFile.transferTo(file);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw BusinessException.fail("文件保存失败");
        }
        //2、保存到数据库中
        readFile.setFileMd5(md5);
        readFile.setFilePath(savePath);
        //这个也许可以提出来
        this.save(readFile);

        return readFile;
    }

    @Override
    public String saveNovelImgToDisk(MultipartFile imgFile, SysUser sysUser, Novel novel) {
        //默认安装根路径+小说id+文件名的形式保存
        String configPath=fileUploadConfig.getImgRootPath();
        String savePath= fileUploadConfig.getNovelPath()+CharConstant.File_SEPARATOR
                +novel.getId()+CharConstant.File_SEPARATOR
                +imgFile.getOriginalFilename();
        File file=new File(configPath + CharConstant.File_SEPARATOR+savePath);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        try {
            imgFile.transferTo(file);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw BusinessException.fail("图片保存失败");
        }
        return savePath;
    }

    @Override
    public List<String> getFileContextList(String filePath, Long startLine, Long limitLine) {
        return FileUtils.getLineByLimit(filePath, startLine, limitLine);
    }

    @Override
    public List<String> getEpubContextList(String filePath, String chapterHref) {
        List<String> list = Lists.newArrayList();
        Book epubBook = FileUtils.getEpubBookByPath(filePath);
        Resource resource = epubBook.getResources().getByHref(chapterHref);
        byte[] data = null;
        try {
            data = resource.getData();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        if (data != null) {
            String html = new String(data);
            Document parse = Jsoup.parse(html);
            Elements p = parse.getElementsByTag("p");
            for (Element element : p) {
                list.add(element.text());
            }
        }
        return list;
    }

    @Override
    public String savePhotoToDisk(MultipartFile filePhoto, SysUser sysUser) {
        //头像直接保存就行
        String configPath=fileUploadConfig.getImgRootPath();
        String savePath=fileUploadConfig.getPhotoPath()+ CharConstant.File_SEPARATOR
                +sysUser.getAccountName()+CharConstant.File_SEPARATOR
                +filePhoto.getOriginalFilename();
        File file=new File(configPath + CharConstant.File_SEPARATOR + savePath);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        try {
            filePhoto.transferTo(file);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw BusinessException.fail("封面上传失败");
        }
        return savePath;
    }
}
