package com.wpy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.wpy.config.FileUploadConfig;
import com.wpy.constant.CharConstant;
import com.wpy.constant.NumConstant;
import com.wpy.constant.SqlConstant;
import com.wpy.dto.NovelChapterDto;
import com.wpy.entity.Novel;
import com.wpy.entity.NovelChapter;
import com.wpy.entity.ReadFile;
import com.wpy.entity.SysUser;
import com.wpy.enums.CodeMsgEnums;
import com.wpy.enums.ParamEnums;
import com.wpy.enums.TypeEnums;
import com.wpy.exception.BusinessException;
import com.wpy.mapper.NovelChapterMapper;
import com.wpy.pojo.EpubInfoPojo;
import com.wpy.pojo.FileInfoPojo;
import com.wpy.pojo.NovelChapterPojo;
import com.wpy.service.FileService;
import com.wpy.service.NovelChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wpy.service.NovelService;
import com.wpy.utils.DateUtils;
import com.wpy.utils.FileUtils;
import com.wpy.utils.StringUtils;
import nl.siegmann.epublib.domain.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <p>
 * 章节表 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@Service
public class NovelChapterServiceImpl extends ServiceImpl<NovelChapterMapper, NovelChapter> implements NovelChapterService {


    @Autowired
    NovelChapterMapper chapterMapper;
    @Autowired
    FileService fileService;
    @Autowired
    NovelService novelService;
    @Autowired
    FileUploadConfig fileUploadConfig;

    /**
     * 第一种正则规则：空格或没有空格开头，匹配到章节，中间可能有空格，可能没有，然后匹配到章节名称
     */
    private static final Pattern txtChapterPattern1=Pattern.compile("[\\u3000|\\u0020|\\u00A0]*第[0-9零一二三四五六七八九十百千万亿]+[章节卷集部篇回][\\u3000|\\u0020|\\u00A0]*\\S*");

    @Override
    public FileInfoPojo analysisTxtFile(SysUser sysUser, MultipartFile novelFile, ReadFile readFile, Novel novel) {
        //1、获得文件的一些数据
        FileInfoPojo fileInfoByFile = FileUtils.getFileInfoByInput(new ByteArrayInputStream(readFile.getFileBytes()), null, null);
        //2、真正的开始解析
        List<NovelChapter> novelChapters= Lists.newArrayList();

        List<String> allList = fileInfoByFile.getAllList();
        int chapterOrder=0;
        //第一次循环，正则取到章节基本信息
        for (int i=0;i<allList.size();i++) {
            //如果正则匹配上，则说明这是章节名称
            String chapterText = allList.get(i);
            if(txtChapterPattern1.matcher(chapterText).matches()){
                NovelChapter novelChapter=new NovelChapter(StringUtils.getUuid(),sysUser.getAccountName(),new Date());
                //设置小说id
                novelChapter.setNovelId(novel.getId());
                //设置章节名
                novelChapter.setChapterName(chapterText);
                //设置章节行数
                novelChapter.setChapterLine((long)(i+1));
                //设置排序
                novelChapter.setChapterOrder(chapterOrder);
                chapterOrder++;
                novelChapters.add(novelChapter);
            }else if(i==NumConstant.ZERO){
                //如果是第一次，且没有匹配到数据，默认为序章
                NovelChapter novelChapter=new NovelChapter(StringUtils.getUuid(),sysUser.getAccountName(),new Date());
                //设置小说id
                novelChapter.setNovelId(novel.getId());
                //设置章节名
                novelChapter.setChapterName("序章");
                //设置章节行数
                novelChapter.setChapterLine(0L);
                //设置排序
                novelChapter.setChapterOrder(chapterOrder);
                chapterOrder++;
                novelChapters.add(novelChapter);
            }
        }
        //说明未解析到任何章节名数据，则整本为一个章节，章节名为小说名
        if(CollectionUtils.isEmpty(novelChapters)){
            NovelChapter novelChapter=new NovelChapter(StringUtils.getUuid(),sysUser.getAccountName(),new Date());
            novelChapter.setNovelId(novel.getId());
            novelChapter.setChapterOrder(NumConstant.ZERO);
            novelChapter.setChapterTotal((long)allList.size());
            novelChapter.setChapterLine(NumConstant.ZERO.longValue());
            novelChapter.setChapterName(novel.getNovelTitle());
            novelChapters.add(novelChapter);
        }else{
            //再循环一次，把每一章的行数的记录下来
            for (int i=0;i<novelChapters.size();i++) {
                NovelChapter novelChapter = novelChapters.get(i);
                if(i+1>=novelChapters.size()){
                    novelChapter.setChapterTotal(allList.size()-novelChapter.getChapterLine());
                }else{
                    NovelChapter nextNovelChapter = novelChapters.get(i+1);
                    novelChapter.setChapterTotal(nextNovelChapter.getChapterLine()-novelChapter.getChapterLine());
                }
            }
        }
        //批量插入进数据库
        if(!CollectionUtils.isEmpty(novelChapters)){
            this.saveBatch(novelChapters);
        }

        return fileInfoByFile;
    }

    @Override
    public List<NovelChapter> getNovelChapter(SysUser sysUser, String id) {
        return this.chapterMapper.selectList(new QueryWrapper<NovelChapter>()
                .eq(SqlConstant.NOVEL_ID, id).orderByAsc(SqlConstant.CHAPTER_ORDER));
    }

    @Override
    public NovelChapterPojo getNovelChapterContext(SysUser sysUser, NovelChapterDto novelChapterDto) {
        //1、根据章节id去捞数据，章节名和小说名可以直接从库里面取出来
        NovelChapterPojo chapterContext = chapterMapper.getChapterContext(novelChapterDto);
        Novel novel = novelService.getById(chapterContext.getNovelId());
        //校验小说是否可以访问
        novelService.checkNovelStatus(novel,sysUser);
        //2、查询上一章和下一章的id
        QueryWrapper<NovelChapter> qw = new QueryWrapper<>();
        qw.eq(SqlConstant.NOVEL_ID, chapterContext.getNovelId());
        qw.and(wrapper -> wrapper.eq(SqlConstant.CHAPTER_ORDER, chapterContext.getChapterOrder() - 1)
                .or().eq(SqlConstant.CHAPTER_ORDER, chapterContext.getChapterOrder() + 1));
        List<NovelChapter> novelChapters = this.chapterMapper.selectList(qw);
        for (NovelChapter novelChapter : novelChapters) {
            if(novelChapter.getChapterOrder().equals(chapterContext.getChapterOrder() - 1)){
                chapterContext.setLastChapterId(novelChapter.getId());
            }
            if(novelChapter.getChapterOrder().equals(chapterContext.getChapterOrder() + 1)){
                chapterContext.setNextChapterId(novelChapter.getId());
            }
        }
        //3、根据查出来的行数，去文件中捞数据
        //这里要判断文件类型
        ReadFile file = fileService.getById(novel.getFileId());
        //1)txt
        if(ParamEnums.TXT.getMsg().equals(file.getFileType())){
            List<String> list=Lists.newArrayList();
            try{
                File novelFile = new File(chapterContext.getFilePath());
                InputStream is = new FileInputStream(novelFile);
                String code=FileUtils.getFileCode(is);
                if(novelChapters.isEmpty()){
                    if(StringUtils.isEmpty(code)){
                        list=FileUtils.getAllList(chapterContext.getFilePath());
                    }else {
                        InputStreamReader isr = new InputStreamReader(is, code);
                        byte[] fileBytes = IOUtils.toByteArray(isr);
                        list=FileUtils.getAllList(new ByteArrayInputStream(fileBytes));
                    }
                }else {
                    if(StringUtils.isEmpty(code)){
                        list=fileService.getFileContextList(chapterContext.getFilePath(),chapterContext.getChapterLine(),chapterContext.getChapterTotal()-1);
                    }else {
                        InputStreamReader isr = new InputStreamReader(is, code);
                        byte[] fileBytes = IOUtils.toByteArray(isr);
                        list = FileUtils.getLineByLimit(new ByteArrayInputStream(fileBytes),chapterContext.getChapterLine(),chapterContext.getChapterTotal()-1);
                    }
                }
            }catch (Exception e){
                log.error(e.getMessage());
            }
            List<String> heanderList=Lists.newArrayList();
            //去空格处理
            for (String s : list) {
                heanderList.add(StringUtils.removeBlank(s));
            }
            chapterContext.setList(heanderList);

        }
        if(ParamEnums.EPUB.getMsg().equals(file.getFileType())){
            chapterContext.setList(fileService.getEpubContextList(chapterContext.getFilePath(),chapterContext.getChapterHref()));
        }
        return chapterContext;
    }

    @Override
    public EpubInfoPojo analysisEpubFile(SysUser sysUser, MultipartFile novelFile, ReadFile readFile, Novel novel) {
        EpubInfoPojo epubInfoPojo=new EpubInfoPojo();
        //1、获得文件信息
        Book epubBook = FileUtils.getEpubBookByIn(new ByteArrayInputStream(readFile.getFileBytes()));

        //2、解析头信息信息，然后把完整的novel属性返回回去
        Novel headNovel=this.getNovelBaseInfoByMeta(epubBook.getMetadata(),novel);
        //保存封面
        if(StringUtils.isEmpty(novel.getNovelImg())){
            Resource coverImage = epubBook.getCoverImage();
            if(coverImage!=null){
                try {
                    byte[] data = coverImage.getData();
                    MediaType mediaType = coverImage.getMediaType();
                    String imgName=novel.getId()+mediaType.getDefaultExtension();
                    FileUtils.byteToFile(data,fileUploadConfig.getImgRootPath()+CharConstant.File_SEPARATOR+fileUploadConfig.getNovelPath()+CharConstant.File_SEPARATOR+novel.getId(),imgName);
                    headNovel.setNovelImg(fileUploadConfig.getNovelPath()+CharConstant.File_SEPARATOR+novel.getId()+CharConstant.File_SEPARATOR +imgName);
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }

        //3、解析章节信息
        TableOfContents tableOfContents = epubBook.getTableOfContents();
        List<TOCReference> tocReferences = tableOfContents.getTocReferences();
        List<NovelChapter> list=Lists.newArrayList();
        long wordNum=0;
        long novelLine=0;
        for(int i=0;i<tocReferences.size();i++){
            TOCReference tocReference = tocReferences.get(i);
            Resource resource = tocReference.getResource();
            if(StringUtils.isBlank(resource)){
                continue;
            }
            NovelChapter novelChapter=new NovelChapter(StringUtils.getUuid(),sysUser.getAccountName(),new Date());
            novelChapter.setNovelId(novel.getId());
            novelChapter.setChapterHref(resource.getHref());
            novelChapter.setChapterName(StringUtils.isNotEmpty(resource.getTitle())?resource.getTitle():tocReference.getTitle());
            novelChapter.setChapterOrder(i);
            byte[] data=null;
            try {
                data = resource.getData();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
            long chapterLine=0;
            if(data!=null){
                String context = StringUtils.removeHtml(new String(data));
                while (context.contains("\n")){
                    context=StringUtils.replaceFirst(context, "\n", "");
                    chapterLine++;
                    novelLine++;
                }
                context=StringUtils.replaceAll(context, "\r", "");
                wordNum += context.length();
            }
            novelChapter.setChapterTotal(chapterLine);
            list.add(novelChapter);
        }
        //保存章节信息
        if(!CollectionUtils.isEmpty(list)){
            this.saveBatch(list);
        }
        headNovel.setNovelWordNum(wordNum);
        headNovel.setNovelTotal(novelLine);
        epubInfoPojo.setNovel(headNovel);
        return epubInfoPojo;
    }

    /**
     *获得头部信息
     * @author pywang6
     * @date 2021/3/11 14:07
     *
     * @param metadata
     * @param novel
     * @return com.wpy.entity.Novel
     */
    private Novel getNovelBaseInfoByMeta(Metadata metadata, Novel novel) {
        //获得作者信息
        if(StringUtils.isEmpty(novel.getNovelAuthor())){
            String authorStr="";
            List<Author> authors = metadata.getAuthors();
            for (Author author : authors) {
                String a=author.getFirstname();
                if(StringUtils.isNotEmpty(a)){
                    a+="·" + author.getLastname();
                }else{
                    a=author.getLastname();
                }
                if(StringUtils.isEmpty(authorStr)){
                    authorStr=a;
                }else{
                    authorStr+=";"+a;
                }
            }
            novel.setNovelAuthor(authorStr);
        }
        //获得发布时间
        if(StringUtils.isBlank(novel.getNovelPublicDate())){
            List<nl.siegmann.epublib.domain.Date> dates = metadata.getDates();
            if(!dates.isEmpty()){
                nl.siegmann.epublib.domain.Date date = dates.get(0);
                if(date!=null){
                    novel.setNovelPublicDate(DateUtils.getAllFormatDate(date.getValue()));
                }
            }
        }
        //获得描述信息
        if(StringUtils.isEmpty(novel.getNovelIntroduce())){
            String introduce="";
            List<String> descriptions = metadata.getDescriptions();
            for (String description : descriptions) {
                description = StringUtils.removeHtml(description);
                if(StringUtils.isEmpty(introduce)){
                    introduce=description;
                }else{
                    introduce+=";"+description;
                }
            }
            introduce = introduce.replace("\n","");
            introduce = introduce.replace("\t","");
            introduce = introduce.replace("\r","");
            introduce = introduce.replace(" ","");
            novel.setNovelIntroduce(introduce);
        }
        //获得标题信息
        if(StringUtils.isEmpty(novel.getNovelTitle())){
            if(StringUtils.isNotEmpty(metadata.getFirstTitle())){
                novel.setNovelTitle(metadata.getFirstTitle());
            }else if(!metadata.getTitles().isEmpty()){
                novel.setNovelTitle(metadata.getTitles().get(0));
            }
        }
        return novel;
    }
}
