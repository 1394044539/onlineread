package com.wpy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wpy.constant.CharConstant;
import com.wpy.constant.NumConstant;
import com.wpy.constant.SqlConstant;
import com.wpy.dto.DownloadDto;
import com.wpy.dto.NovelDto;
import com.wpy.dto.NovelUploadDto;
import com.wpy.entity.*;
import com.wpy.enums.CodeMsgEnums;
import com.wpy.enums.DictEnums;
import com.wpy.enums.ParamEnums;
import com.wpy.enums.TypeEnums;
import com.wpy.exception.BusinessException;
import com.wpy.mapper.CatalogMapper;
import com.wpy.mapper.NovelMapper;
import com.wpy.mapper.SysDictMapper;
import com.wpy.mapper.UserCollectionMapper;
import com.wpy.pojo.EpubInfoPojo;
import com.wpy.pojo.FileInfoPojo;
import com.wpy.pojo.UserCollectFileInfoPojo;
import com.wpy.service.*;
import com.wpy.utils.ShiroUtils;
import com.wpy.utils.StringUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

/**
 * <p>
 * 小说数据表 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@Service
public class NovelServiceImpl extends ServiceImpl<NovelMapper, Novel> implements NovelService {

    @Autowired
    FileService fileService;
    @Autowired
    UserCollectionService userCollectionService;
    @Autowired
    UserCollectionMapper userCollectionMapper;
    @Autowired
    NovelChapterService novelChapterService;
    @Autowired
    NovelMapper novelMapper;
    @Autowired
    SysDictService sysDictService;
    @Autowired
    SysDictMapper sysDictMapper;
    @Autowired
    private ReadHistoryService readHistoryService;
    @Autowired
    private CatalogService catalogService;
    @Autowired
    private CatalogMapper catalogMapper;
    @Autowired
    private DynamicService dynamicService;

    @Override
    public void uploadNovel(SysUser sysUser, NovelUploadDto novelUploadDto) {
        List<Novel> uploadNovels=Lists.newArrayList();
        for (MultipartFile novelFile : novelUploadDto.getNovelFiles()) {
            //1、先将文件上传到本地服务器中
            ReadFile file = fileService.saveFileToDisk(sysUser,novelFile);
            //2、解析文件生成小说，这里会把小说的相关参数设置好，不涉及业务参数
            Novel novel=null;
            if(ParamEnums.TXT.getMsg().equalsIgnoreCase(file.getFileType())){
                //解析txt文件
                novel =this.saveTxtNovel(sysUser,novelUploadDto,file,novelFile);
            }else{
                //解析epub文件
                novel=this.saveEpubNovel(sysUser,novelUploadDto,file,novelFile);
            }
            //3、判断是否需要收藏
            if(novelUploadDto.getIsCollect()!=null&&novelUploadDto.getIsCollect()){
                userCollectionService.saveCollection(sysUser,novel,TypeEnums.USER_CHANNEL.getCode());
                novel.setNovelCollection(NumConstant.ONE);
            }
            //3、设置业务参数
            if(TypeEnums.ADMIN_ROLE.getCode().equals(sysUser.getRoleType())){
                //管理员上传直接设为公开
                novel.setNovelUploadType(TypeEnums.PUBLIC_NOVEL.getCode());
                novel.setNovelStatus(TypeEnums.SUCCESS_AUDIT.getCode());
            }else{
                //用户上传则先设为私有，再看需不需要发布审核
                novel.setNovelUploadType(TypeEnums.PRIVATE_NOVEL.getCode());
                if(novelUploadDto.getIsPublic()!=null&&novelUploadDto.getIsPublic()){
                    novel.setNovelStatus(TypeEnums.WAIT_AUDIT.getCode());
                }else{
                    novel.setNovelStatus(TypeEnums.NOT_AUDIT.getCode());
                }
            }
            this.save(novel);
            uploadNovels.add(novel);
        }

        //结束后加入动态
        dynamicService.addUploadDynamic(sysUser,uploadNovels);

    }

    @Override
    public Novel saveTxtNovel(SysUser sysUser, NovelUploadDto novelUploadDto, ReadFile file, MultipartFile novelFile) {
        //1、设置基本属性
        Novel novel = this.setNovelParam(sysUser,file,novelUploadDto);
        //3、解析文件生成小说章节数
        FileInfoPojo fileInfoPojo = novelChapterService.analysisTxtFile(sysUser, novelFile, file, novel);
        novel.setNovelTotal(fileInfoPojo.getTotal());
        novel.setNovelWordNum(fileInfoPojo.getWordNum());
        if(StringUtils.isEmpty(novel.getNovelTitle())){
            novel.setNovelTitle(StringUtils.getFileName(file.getFileName()));
        }
        return novel;
    }

    @Override
    public Novel saveEpubNovel(SysUser sysUser, NovelUploadDto novelUploadDto, ReadFile file, MultipartFile novelFile) {
        //1、设置基本
        Novel novel = this.setNovelParam(sysUser,file,novelUploadDto);
        //3、解析文件生成小说章节数
        EpubInfoPojo epubInfoPojo = novelChapterService.analysisEpubFile(sysUser, novelFile, file, novel);
        Novel saveNovel = epubInfoPojo.getNovel();
        if(StringUtils.isEmpty(saveNovel.getNovelTitle())){
            saveNovel.setNovelTitle(StringUtils.getFileName(file.getFileName()));
        }
        //2、解析epub文件。并存放到章节表中
        return saveNovel;
    }

    /**
     * 设置小说参数
     * @author pywang6
     * @date 2021/3/11 10:18
     *
     * @param sysUser
     * @param file
     * @param novelUploadDto
     * @return com.wpy.entity.Novel
     */
    private Novel setNovelParam(SysUser sysUser, ReadFile file, NovelUploadDto novelUploadDto) {
        //1、设置通用参数
        Novel novel = this.getBaseParam(sysUser,file.getId());
        novel.setNovelAuthor(novelUploadDto.getNovelAuthor());
        novel.setNovelIntroduce(novelUploadDto.getNovelIntroduce());
        novel.setNovelPublicDate(novelUploadDto.getNovelPublicDate());
        novel.setNovelTitle(novelUploadDto.getNovelTitle());
        novel.setUploadUserId(sysUser.getId());
        novel.setNovelType(StringUtils.listToCommaStr(novelUploadDto.getNovelTypes()));
        //3、上传小说图片
        if(novelUploadDto.getImgFile()!=null){
            novel.setNovelImg(fileService.saveNovelImgToDisk(novelUploadDto.getImgFile(), sysUser, novel));
        }
        return novel;
    }

    @Override
    public Page<Novel> getNovelList(SysUser sysUser, NovelDto novelDto) {
        //判断搜索条件
        boolean isAdmin=false;
        if(TypeEnums.PERSON_PAGE_TYPE.getCode().equals(novelDto.getPageType())
                &&StringUtils.isNotEmpty(sysUser.getId())){
            //个人类型，只根据当前用户id来查
            novelDto.setUploadUserId(sysUser.getId());
        }else if(TypeEnums.ADMIN_PAGE_TYPE.getCode().equals(novelDto.getPageType())
                &&TypeEnums.ADMIN_ROLE.getCode().equals(sysUser.getRoleType())){
            //管理员页面，判断是不是管理员，是管理员就不需要查管理员有没有收藏了
            isAdmin=true;
        }
        if(StringUtils.isNotEmpty(novelDto.getNovelType())){
            List<String> list=novelDto.getNovelTypes();
            if(CollectionUtils.isEmpty(list)){
                list=Lists.newArrayList();
            }
            list.add(novelDto.getNovelType());
            novelDto.setNovelTypes(list);
        }
        Page<Novel> page = new Page<>(novelDto.getPageNum(),novelDto.getPageSize());
        List<Novel> novelList=this.novelMapper.selectNovelList(novelDto,page);
        //查询数据字典的所有小说类型
        List<SysDict> sysDicts = sysDictMapper.selectList(new QueryWrapper<SysDict>()
                .eq(SqlConstant.DICT_CLASS, DictEnums.NOVEL_TYPE.getKey()));
        Map<String, SysDict> sysDictMap = sysDicts.stream().collect(Collectors.toMap(SysDict::getDictKey, x -> x));
        for (Novel record : novelList) {
            //1、查询是否已经收藏
            if(!isAdmin){
                Integer integer = userCollectionMapper.selectCount(new QueryWrapper<UserCollection>()
                        .eq(SqlConstant.NOVEL_ID, record.getId()).eq(SqlConstant.USER_ID, sysUser.getId()));
                if(integer>0){
                    record.setIsCollection(TypeEnums.IS_COLLECTION.getCode());
                }else{
                    record.setIsCollection(TypeEnums.NOT_COLLECTION.getCode());
                }
            }
            //2、查询小说类型
            if(StringUtils.isNotEmpty(record.getNovelType())){
                List<String> list = StringUtils.commaStrToList(record.getNovelType());
                List<SysDict> typeList=Lists.newArrayList();
                for (String key : list) {
                    SysDict sysDict = sysDictMap.get(key);
                    typeList.add(sysDict);
                }
                List<String> values = list.stream().map(x -> sysDictMap.get(x).getDictValue()).collect(Collectors.toList());
                record.setNovelType(StringUtils.listToCommaStr(values));
                record.setNovelTypeDict(sysDicts);
                record.setNovelTypes(typeList);
            }
        }

        return page.setRecords(novelList);
    }

    @Override
    public Novel getNovelInfo(SysUser sysUser, String id, Boolean isClick) {
        Novel novel = this.getById(id);

        this.checkNovelStatus(novel,sysUser);

        String novelType = novel.getNovelType();
        if(StringUtils.isNotEmpty(novelType)){
            //查询小说类型
            List<SysDict> sysDicts = sysDictMapper.selectList(new QueryWrapper<SysDict>()
                    .eq(SqlConstant.DICT_CLASS, DictEnums.NOVEL_TYPE.getKey())
                    .in(SqlConstant.DICT_KEY, StringUtils.commaStrToList(novelType)));
            List<String> collect = sysDicts.stream().map(SysDict::getDictValue).collect(Collectors.toList());
            novel.setNovelType(StringUtils.listToCommaStr(collect));
            novel.setNovelTypeDict(sysDicts);
        }
        //如果是用户点进来的话，立马更新点击数和热度
        if(isClick){
            this.updateNovelHot(novel.getId());
        }

        List<NovelChapter> novelChapter = this.novelChapterService.getNovelChapter(sysUser, id);
        novel.setChapterList(novelChapter);
        return novel;
    }

    @Override
    public void deleteNovel(List<String> ids) {
        //1、删除小说表中的数据
        this.removeByIds(ids);
        //2、删除章节表中的数据
        novelChapterService.remove(new QueryWrapper<NovelChapter>().in(SqlConstant.NOVEL_ID,ids));
    }

    @Override
    public Map<String, List> getHotNovelDataByType(List<String> types) {
        Map<String,List> map= Maps.newHashMap();
        List<SysDict> novelTypelist= Lists.newArrayList();
        if(CollectionUtils.isEmpty(types)){
            //如果为空则取最新数据
            novelTypelist = sysDictService.getNovelType();
        }else{
            //如果不为空，则取前端传来的值
            novelTypelist = sysDictService.getBaseMapper().selectList(new QueryWrapper<SysDict>()
                    .eq(SqlConstant.DICT_CLASS, DictEnums.NOVEL_TYPE.getKey()).in(SqlConstant.DICT_KEY, types));
        }
        //先把字典放进去
        map.put("typeDict",novelTypelist);
        for (SysDict sysDict : novelTypelist) {
            String sql="find_in_set('"+sysDict.getDictKey()+"',novel_type)";
            QueryWrapper<Novel> qw = new QueryWrapper<>();
            qw.eq(SqlConstant.NOVEL_UPLOAD_TYPE, TypeEnums.PUBLIC_NOVEL.getCode());
            qw.apply(sql);
            qw.orderByDesc(SqlConstant.NOVEL_HOT);
            Page<Novel> novelPage = this.novelMapper.selectPage(new Page<Novel>(1, 6), qw);
            map.put(sysDict.getDictKey(),novelPage.getRecords());
        }
        return map;
    }

    @Override
    public void updateNovelHot(String id) {
        Novel novel = this.getById(id);
        novel.setNovelClick(novel.getNovelClick()+1);
        novel.setNovelHot(novel.getNovelHot()+1);
        this.updateById(novel);
    }

    @Override
    public void updateNovel(SysUser sysUser, NovelUploadDto novelUploadDto) {
        //1、找到原数据，修改一些简单的数据
        Novel oldNovel = this.getById(novelUploadDto.getId());
        oldNovel.setNovelAuthor(novelUploadDto.getNovelAuthor());
        oldNovel.setNovelIntroduce(novelUploadDto.getNovelIntroduce());
        oldNovel.setNovelPublicDate(novelUploadDto.getNovelPublicDate());
        oldNovel.setNovelTitle(novelUploadDto.getNovelTitle());
        oldNovel.setNovelType(StringUtils.listToCommaStr(novelUploadDto.getNovelTypes()));
        oldNovel.setUpdateTime(new Date());
        oldNovel.setUpdateBy(sysUser.getId());
        MultipartFile[] novelFiles = novelUploadDto.getNovelFiles();
        /**
         * 解析txt的时候不管图片，解析epub的时候，看原来有没有图片，如果原来有图片，他就不帮你生成，如果没有，才会生成
         * 这里是修改，只看epub，有几种情况：
         * 1、原来没有，这次他没上传，这里就按照epub来生成，√
         * 2、原来没有，这次他上传了，用新的，√
         * 3、原来有，这次他没上传，用原来的，√
         * 4、原来有，这次他上传了，用新的，√
         */
        if(novelUploadDto.getImgFile()!=null){
            oldNovel.setNovelImg(fileService.saveNovelImgToDisk(novelUploadDto.getImgFile(), sysUser, oldNovel));
        }
        if(novelFiles!=null&&novelFiles.length!=0){
            //1、把原来的章节信息，以及相关的历史记录、书签等删除
            novelChapterService.remove(new QueryWrapper<NovelChapter>().eq(SqlConstant.NOVEL_ID,oldNovel.getId()));
            readHistoryService.remove(new QueryWrapper<ReadHistory>().eq(SqlConstant.NOVEL_ID,oldNovel.getId()));

            //1、先将文件上传到本地服务器中
            ReadFile file = fileService.saveFileToDisk(sysUser,novelFiles[0]);
            oldNovel.setFileId(file.getId());
            //2、解析新的文件内容
            if(ParamEnums.TXT.getMsg().equalsIgnoreCase(file.getFileType())){
                //解析txt文件
                oldNovel = this.saveTxtNovelUpdate(sysUser, oldNovel, file, novelFiles[0]);
            }else{
                //解析epub文件
                oldNovel = this.saveEpubNovelUpdate(sysUser, oldNovel, file, novelFiles[0]);
            }
        }
        this.updateById(oldNovel);
    }

    @Override
    public void downloadNovel(HttpServletRequest request, HttpServletResponse response, DownloadDto downloadDto, SysUser sysUser) {
        //1、设置参数
        if(NumConstant.ONE.equals(downloadDto.getList().size())){
            //单小说或文件夹下载
            this.downloadOneNovelOrCatalog(request,response,downloadDto.getList().get(0),downloadDto.getDownloadType());
        }else{
            //多小说或多文件夹下载
        }
    }

    @Override
    public void checkNovelStatus(Novel novel, SysUser sysUser) {
        if(novel==null){
            throw BusinessException.fail(CodeMsgEnums.NOVEL_NOT_EXIST.getMsg());
        }
        //如果是私人，且 当前登录人不等于上传人且不等于管理员，就报错
        if(TypeEnums.PRIVATE_NOVEL.getCode().equals(novel.getNovelUploadType())
                &&!novel.getUploadUserId().equals(sysUser.getId())
                &&!TypeEnums.ADMIN_ROLE.getCode().equals(sysUser.getRoleType())){
            throw BusinessException.fail(CodeMsgEnums.NOT_NOVEL_PERMISSION.getMsg());
        }
        //如果被禁用，且不是管理员，就报错
        if(TypeEnums.NOVEL_DISABLE.getCode().equals(novel.getNovelStatus())
                &&!TypeEnums.ADMIN_ROLE.getCode().equals(sysUser.getRoleType())){
            throw BusinessException.fail(CodeMsgEnums.NOVEL_IS_DISABLE.getMsg());
        }
    }

    @Override
    public void publicNovel(SysUser sysUser, NovelDto novelDto) {
        List<String> idList = StringUtils.getIdList(novelDto.getId(), novelDto.getIds());
        List<Novel> novels = this.novelMapper.selectBatchIds(idList);
        for (Novel novel : novels) {
            if(novel==null){
                throw BusinessException.fail(CodeMsgEnums.NOVEL_NOT_EXIST.getMsg());
            }
            if(TypeEnums.ADMIN_ROLE.getCode().equals(sysUser.getRoleType())){
                //如果是管理员操作，直接不需要审核了，设为公开
                novel.setNovelStatus(TypeEnums.SUCCESS_AUDIT.getCode());
                novel.setNovelUploadType(TypeEnums.PUBLIC_NOVEL.getCode());
            }else {
                //普通人发布需要经过审核，只有未审核和审核失败的状态才再能进行审核
                if(!TypeEnums.NOT_AUDIT.getCode().equals(novel.getNovelStatus())
                        &&!TypeEnums.FAIL_AUDIT.getCode().equals(novel.getNovelStatus())){
                    throw BusinessException.fail(CodeMsgEnums.NOVEL_STATUS_ERROE.getMsg());
                }
                //待审核
                novel.setNovelStatus(TypeEnums.WAIT_AUDIT.getCode());
            }
        }
        if(!novels.isEmpty()){
            this.updateBatchById(novels);
            dynamicService.novelAudit(sysUser,novels);
        }
    }

    @Override
    public void auditNovel(SysUser sysUser, NovelDto novelDto) {
        //管理员可以在这里直接操作
        if(!TypeEnums.ADMIN_ROLE.getCode().equals(sysUser.getRoleType())){
            //判断是不是管理员，不是就无权限
            throw BusinessException.fail(CodeMsgEnums.NOT_NOVEL_PERMISSION.getMsg());
        }
        List<String> ids=StringUtils.getIdList(novelDto.getId(),novelDto.getIds());
        List<Novel> novels = this.novelMapper.selectBatchIds(ids);
        for (Novel novel : novels) {
            if(novel==null){
                throw BusinessException.fail(CodeMsgEnums.NOVEL_NOT_EXIST.getMsg());
            }
            //判断审核结果
            novel.setNovelStatus(novelDto.getNovelStatus());
            //审核成功审核完成则改为共有
            if(TypeEnums.SUCCESS_AUDIT.getCode().equals(novelDto.getNovelStatus())){
                novel.setNovelUploadType(TypeEnums.PUBLIC_NOVEL.getCode());
            }else {
                //审核失败就是失败，禁用就是禁用，其他状态都改为私有
                novel.setNovelUploadType(TypeEnums.PRIVATE_NOVEL.getCode());
                novel.setErrorMsg(novelDto.getErrorMsg());
            }
            novel.setUpdateBy(sysUser.getId());
            novel.setUpdateTime(new Date());
        }
        this.updateBatchById(novels);
        //动态
        dynamicService.novelAudit(sysUser,novels);
    }

    @Override
    public void cancelPublic(SysUser sysUser, NovelDto novelDto) {
        //用户有两种状态，一个是已经发布取消，一个是待审核取消
        List<String> idList = StringUtils.getIdList(novelDto.getId(), novelDto.getIds());
        UpdateWrapper<Novel> uw=new UpdateWrapper<Novel>();
        uw.set(SqlConstant.NOVEL_UPLOAD_TYPE,TypeEnums.PRIVATE_NOVEL.getCode());
        uw.set(SqlConstant.NOVEL_STATUS,TypeEnums.NOT_AUDIT.getCode());
        uw.set(SqlConstant.UPDATE_TIME,new Date());
        uw.set(SqlConstant.UPDATE_BY,sysUser.getId());
        uw.in(SqlConstant.ID,idList);
        this.update(uw);

        List<Novel> novels = this.novelMapper.selectBatchIds(idList);
        //增加动态
        dynamicService.novelAudit(sysUser,novels);
    }

    @Override
    public void addCollectionNum(String id) {
        UpdateWrapper<Novel> uw=new UpdateWrapper<>();
        uw.setSql("novel_collection = novel_collection + 1");
        uw.eq(SqlConstant.NOVEL_ID,id);
        this.update(uw);
    }

    /**
     * 下载单小说或文件夹
     * @param request
     * @param response
     * @param downloadDto
     * @param downloadType
     */
    private void downloadOneNovelOrCatalog(HttpServletRequest request, HttpServletResponse response, DownloadDto downloadDto, Integer downloadType) {
        if(TypeEnums.NOVEL_TYPE.getCode().equals(downloadDto.getType())){
            //小说
            Novel novel = this.novelMapper.selectById(downloadDto.getId());
            ReadFile readFile = this.fileService.getById(novel.getFileId());
            this.downloadOneNovel(novel,readFile,request,response);
        }else if(TypeEnums.CATALOG_TYPE.getCode().equals(downloadDto.getType())){
            //文件夹
            Catalog catalog = this.catalogService.getById(downloadDto.getId());
            if(catalog==null&&StringUtils.isEmpty(downloadDto.getId())){
                catalog=new Catalog();
                catalog.setCatalogName("全部小说");
                catalog.setId(null);
            }else if(catalog==null){
                throw BusinessException.fail("文件夹不存在");
            }
            this.downloadOneCatalog(catalog,request,response);
        }
    }

    /**
     * 下载文件夹下的内容
     * @param catalog
     * @param request
     * @param response
     */
    private void downloadOneCatalog(Catalog catalog, HttpServletRequest request, HttpServletResponse response) {
        SysUser sysUser = ShiroUtils.getSysUser(request);
        String fileName=catalog.getCatalogName()+CharConstant.SEPARATOR+"zip";
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setContentType("multipart/form-data");
        try {
            request.setCharacterEncoding("utf-8");
            //让前端能拿到文件名
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Access-Control-Expose-Headers", "filename");
            response.setHeader("Content-Disposition", "attachment;fileName=\"" + new String(fileName.getBytes("utf-8"), ISO_8859_1) + "\"");
            response.setHeader("filename", URLEncoder.encode(fileName,"UTF8"));
            // 解析断点续传相关信息
            response.setHeader("Accept-Ranges", "bytes");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        //1、压缩文件
        try (
            ZipOutputStream zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
            DataOutputStream os = new DataOutputStream(zipos)
        ){
            //设置压缩方法
            zipos.setMethod(ZipOutputStream.DEFLATED);
            List<String> renameList=Lists.newArrayList();
            //递归下载
            this.recursionCatalog(catalog,zipos,os,catalog.getCatalogName(),sysUser,renameList);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    /**
     * 递归下载
     * @param catalog
     * @param zipos
     * @param os
     * @param catalogName
     * @param sysUser
     * @param renameList
     * @throws Exception
     */
    private void recursionCatalog(Catalog catalog, ZipOutputStream zipos, DataOutputStream os, String catalogName, SysUser sysUser, List<String> renameList) throws Exception{
        //2、取文件内容
        List<UserCollectFileInfoPojo> list=
                this.userCollectionMapper.selectFileInfoByCatalogId(catalog.getId(),sysUser.getId());
        for (UserCollectFileInfoPojo pojo : list) {
            String pathName=catalogName+CharConstant.File_SEPARATOR+
                    pojo.getNovelTitle()+CharConstant.SEPARATOR+pojo.getFileType();
            //校验是否重名了
            if(renameList.contains(pathName)){
                //随机生成六位字符加数字
                pathName=catalogName+CharConstant.File_SEPARATOR+
                        pojo.getNovelTitle()+ CharConstant.UNDERLINE+RandomStringUtils.randomAlphanumeric(6)+
                        CharConstant.SEPARATOR+pojo.getFileType();
            }
            renameList.add(pathName);
            ZipEntry zipEntry=new ZipEntry(pathName);
            zipos.putNextEntry(zipEntry);
            //这里才开始获取流
            InputStream inputStream=new FileInputStream(new File(pojo.getFilePath()));
            int b = 0;
            byte[] buffer = new byte[1024];
            while ((b = inputStream.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        }
        //2、读取文件夹
        QueryWrapper<Catalog> qw = new QueryWrapper<>();
        qw.eq(SqlConstant.USER_ID,sysUser.getId());
        if(StringUtils.isEmpty(catalog.getId())){
            qw.isNull(SqlConstant.PARENT_ID);
        }else {
            qw.eq(SqlConstant.PARENT_ID, catalog.getId());
        }
        List<Catalog> catalogs = this.catalogMapper.selectList(qw);
        for (Catalog child : catalogs) {
            //先把这个文件夹打包
//            ZipEntry zipEntry=new ZipEntry(catalogName+CharConstant.File_SEPARATOR+child.getCatalogName()+CharConstant.File_SEPARATOR);
//            zipos.putNextEntry(zipEntry);
            this.recursionCatalog(child,zipos,os,
                    catalogName+CharConstant.File_SEPARATOR+child.getCatalogName(), sysUser, renameList);
        }
    }

    /**
     * 下载单小说
     * @param novel
     * @param readFile
     * @param request
     * @param response
     */
    private void downloadOneNovel(Novel novel, ReadFile readFile, HttpServletRequest request, HttpServletResponse response) {
        File file=new File(readFile.getFilePath());
        String fileName=novel.getNovelTitle()+ CharConstant.SEPARATOR+readFile.getFileType();
        // 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setContentType("multipart/form-data");
        try {
            request.setCharacterEncoding("utf-8");
            //让前端能拿到文件名
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Access-Control-Expose-Headers", "filename");
            response.setHeader("Content-Disposition", "attachment;fileName=\"" + new String(fileName.getBytes("utf-8"), ISO_8859_1) + "\"");
            response.setHeader("filename", URLEncoder.encode(fileName,"UTF8"));
            // 解析断点续传相关信息
            response.setHeader("Accept-Ranges", "bytes");
            response.addHeader("Content-Length", "" + file.length());
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        try (
                InputStream inputStream=new FileInputStream(file);
                OutputStream out=response.getOutputStream();
        ){
            int b = 0;
            byte[] buffer = new byte[1024];
            while ((b = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, b);
            }
            out.flush();
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    /**
     * 更新epub小说
     * @param sysUser
     * @param novel
     * @param file
     * @param novelFile
     * @return
     */
    private Novel saveEpubNovelUpdate(SysUser sysUser, Novel novel, ReadFile file, MultipartFile novelFile) {
        //解析文件生成小说章节数
        EpubInfoPojo epubInfoPojo = novelChapterService.analysisEpubFile(sysUser, novelFile, file, novel);
        Novel saveNovel = epubInfoPojo.getNovel();
        if(StringUtils.isEmpty(saveNovel.getNovelTitle())){
            saveNovel.setNovelTitle(StringUtils.getFileName(file.getFileName()));
        }
        return saveNovel;
    }

    /**
     * 更新txt小说
     * @param sysUser
     * @param novel
     * @param file
     * @param novelFile
     * @return
     */
    private Novel saveTxtNovelUpdate(SysUser sysUser, Novel novel, ReadFile file, MultipartFile novelFile) {
        //1、设置基本属性
        //3、解析文件生成小说章节数
        FileInfoPojo fileInfoPojo = novelChapterService.analysisTxtFile(sysUser, novelFile, file, novel);
        //如果更新完没有名字，则把新文件的名字给他
        if(StringUtils.isEmpty(novel.getNovelTitle())){
            novel.setNovelTitle(StringUtils.getFileName(file.getFileName()));
        }
        novel.setNovelTotal(fileInfoPojo.getTotal());
        novel.setNovelWordNum(fileInfoPojo.getWordNum());
        return novel;
    }

    private Novel getBaseParam(SysUser sysUser, String fileId) {
        Novel novel = new Novel();
        novel.setId(StringUtils.getUuid());
        novel.setCreateBy(sysUser.getAccountName());
        novel.setUploadUserId(sysUser.getId());
        novel.setCreateTime(new Date());
        novel.setFileId(fileId);
        novel.setUpdateBy(sysUser.getAccountName());
        novel.setUpdateTime(new Date());
        return novel;
    }
}
