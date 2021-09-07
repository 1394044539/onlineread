package com.wpy.controller;


import com.wpy.annotation.SysLogs;
import com.wpy.dto.CatalogDto;
import com.wpy.entity.Catalog;
import com.wpy.entity.SysUser;
import com.wpy.enums.CodeMsgEnums;
import com.wpy.exception.RequestException;
import com.wpy.service.CatalogService;
import com.wpy.utils.ResponseResult;
import com.wpy.utils.ShiroUtils;
import com.wpy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 目录表 前端控制器
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@RestController
@RequestMapping("/catalog")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @PostMapping("/create")
    @SysLogs("创建文件夹")
    public ResponseResult createCatalog(HttpServletRequest request,@RequestBody Catalog catalog){
        SysUser sysUser = ShiroUtils.getSysUser(request);
        catalogService.createCatalog(sysUser,catalog);
        return ResponseResult.success();
    }

    @PostMapping("/moveOrCopyCatalog")
    @SysLogs("移动或复制文件夹")
    public ResponseResult moveOrCopyCatalog(HttpServletRequest request,@RequestBody CatalogDto catalogDto){
        if(StringUtils.isEmpty(catalogDto.getChoseId())){
            throw RequestException.fail(CodeMsgEnums.ID_IS_EMPTY.getMsg());
        }
        SysUser sysUser = ShiroUtils.getSysUser(request);
        if(catalogDto.getChoseId().equals(catalogDto.getTargetId())){
            throw RequestException.fail("不允许往自身移动");
        }
        catalogService.moveOrCopyCatalog(sysUser,catalogDto);
        return ResponseResult.success();
    }

    @PostMapping("/moveOrCopyNovel")
    @SysLogs("移动或复制小说")
    public ResponseResult moveOrCopyNovel(HttpServletRequest request, @RequestBody CatalogDto catalogDto){
        if(StringUtils.isEmpty(catalogDto.getChoseId())){
            throw RequestException.fail(CodeMsgEnums.ID_IS_EMPTY.getMsg());
        }
        SysUser sysUser = ShiroUtils.getSysUser(request);
        catalogService.moveOrCopyNovel(sysUser,catalogDto);
        return ResponseResult.success();
    }

    @DeleteMapping("/delete")
    @SysLogs("删除文件夹")
    public ResponseResult deleteCatalog(HttpServletRequest request, CatalogDto catalogDto){
        SysUser sysUser = ShiroUtils.getSysUser(request);
        catalogService.deleteCatalog(sysUser,catalogDto);
        return ResponseResult.success();
    }

    @PostMapping("/update")
    @SysLogs("更新文件夹名称")
    public ResponseResult updateCatalog(HttpServletRequest request, @RequestBody CatalogDto catalogDto){
        if(StringUtils.isEmpty(catalogDto.getChoseId()) || StringUtils.isEmpty(catalogDto.getCatalogName())){
            throw RequestException.fail("id或名称不能为空");
        }
        SysUser sysUser = ShiroUtils.getSysUser(request);
        catalogService.updateCatalog(sysUser,catalogDto);
        return ResponseResult.success();
    }
}
