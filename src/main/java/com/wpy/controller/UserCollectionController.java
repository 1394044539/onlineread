package com.wpy.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpy.annotation.SysLogs;
import com.wpy.config.jedis.RedisTemplateUtils;
import com.wpy.dto.CollectionDto;
import com.wpy.dto.NovelUploadDto;
import com.wpy.entity.SysUser;
import com.wpy.enums.CodeMsgEnums;
import com.wpy.enums.TypeEnums;
import com.wpy.exception.RequestException;
import com.wpy.pojo.NovelTreePojo;
import com.wpy.service.SysUserCacheService;
import com.wpy.service.UserCollectionService;
import com.wpy.utils.RequestUtils;
import com.wpy.utils.ResponseResult;
import com.wpy.utils.ShiroUtils;
import com.wpy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 收藏表 前端控制器
 * </p>
 *
 * @author wpy
 * @since 2020-11-26
 */
@RestController
@RequestMapping("/userCollection")
public class UserCollectionController {

    @Autowired
    UserCollectionService userCollectionService;
    @Autowired
    SysUserCacheService sysUserCacheService;

    @GetMapping("/list")
    @SysLogs("获取收藏列表")
    public ResponseResult list(HttpServletRequest request, CollectionDto collectionDto){
        SysUser sysUser = ShiroUtils.getSysUser(request);
        return ResponseResult.success(userCollectionService.queryPersonCollect(sysUser,collectionDto));
    }

    @GetMapping("/treeList")
    @SysLogs("获得带文件的列表")
    public ResponseResult treeList(HttpServletRequest request, NovelTreePojo novelTreePojo){
        SysUser sysUser=ShiroUtils.getSysUser(request);
        if(TypeEnums.ADMIN_ROLE.getCode().equals(sysUser.getRoleType())&&StringUtils.isNotEmpty(novelTreePojo.getUserId())){
            sysUser = sysUserCacheService.getSysUser(novelTreePojo.getUserId());
        }
        return ResponseResult.success(userCollectionService.queryPersonTreeCollect(sysUser,novelTreePojo));
    }

    @PutMapping("/collection")
    @SysLogs("收藏小说")
    public ResponseResult addCollection(HttpServletRequest request,@RequestBody CollectionDto collectionDto){
        if(StringUtils.isEmpty(collectionDto.getNovelId())){
            throw RequestException.fail(CodeMsgEnums.ID_IS_EMPTY.getMsg());
        }
        SysUser sysUser = ShiroUtils.getSysUser(request);
        return ResponseResult.success(userCollectionService.addCollection(sysUser,collectionDto.getNovelId()));
    }

    @DeleteMapping("/collection")
    @SysLogs("取消收藏小说")
    public ResponseResult cancelCollection(HttpServletRequest request,CollectionDto collectionDto){
        SysUser sysUser = ShiroUtils.getSysUser(request);
        if(StringUtils.isNotEmpty(collectionDto.getNovelId())){
            userCollectionService.cancelCollection(sysUser,collectionDto.getNovelId());
        }else if(StringUtils.isNotEmpty(collectionDto.getId())){
            userCollectionService.removeById(collectionDto.getId());
        }else if(!CollectionUtils.isEmpty(collectionDto.getIds())){
            userCollectionService.removeByIds(collectionDto.getIds());
        }else{
            return ResponseResult.error(CodeMsgEnums.ID_IS_EMPTY.getMsg());
        }
        return ResponseResult.success();
    }

    @GetMapping("/collection")
    @SysLogs("获取小说收藏信息")
    public ResponseResult getCollection(HttpServletRequest request,CollectionDto collectionDto){
        SysUser sysUser = RequestUtils.getSysUser(request);
        return ResponseResult.success(this.userCollectionService.getCollection(sysUser,collectionDto));
    }

}
