package com.wpy.controller;


import com.wpy.annotation.SysLogs;
import com.wpy.dto.DownloadDto;
import com.wpy.dto.NovelDto;
import com.wpy.dto.NovelUploadDto;
import com.wpy.entity.Novel;
import com.wpy.entity.SysUser;
import com.wpy.enums.CodeMsgEnums;
import com.wpy.exception.RequestException;
import com.wpy.service.NovelService;
import com.wpy.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * <p>
 * 小说数据表 前端控制器
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@RestController
@RequestMapping("/novel")
@Slf4j
public class NovelController {

    @Autowired
    private NovelService novelService;

    @PostMapping("/upload")
    @SysLogs("小说上传")
    public ResponseResult uploadNovel(HttpServletRequest request,NovelUploadDto novelUploadDto){
        try{
            if(novelUploadDto.getNovelFiles().length==0){
                throw RequestException.fail("文件不能为空");
            }
            SysUser sysUser = ShiroUtils.getSysUser(request);
            novelService.uploadNovel(sysUser,novelUploadDto);
            return ResponseResult.success();
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return ResponseResult.error();
        }
    }

    @PostMapping("/updateNovel")
    @SysLogs("小说修改")
    public ResponseResult updateNovel(HttpServletRequest request,NovelUploadDto novelUploadDto){
        if(StringUtils.isEmpty(novelUploadDto.getId())){
            throw RequestException.fail(CodeMsgEnums.ID_IS_EMPTY.getMsg());
        }
        try{
            SysUser sysUser = ShiroUtils.getSysUser(request);
            novelService.updateNovel(sysUser,novelUploadDto);
            return ResponseResult.success();
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return ResponseResult.error();
        }
    }

    @GetMapping("/list")
    @SysLogs("小说列表")
    public ResponseResult getNovelList(HttpServletRequest request,NovelDto novelDto){
        SysUser sysUser = RequestUtils.getSysUser(request);
        return ResponseResult.success(novelService.getNovelList(sysUser,novelDto));
    }

    @GetMapping("/novelInfo")
    @SysLogs("获取小说信息")
    public ResponseResult getNovelInfo(HttpServletRequest request,@RequestParam("id")String id,@RequestParam("isClick")Boolean isClick){
        if(StringUtils.isEmpty(id)){
            throw RequestException.fail(CodeMsgEnums.ID_IS_EMPTY.getMsg());
        }
        SysUser sysUser = RequestUtils.getSysUser(request);
        return ResponseResult.success(novelService.getNovelInfo(sysUser,id,isClick));
    }

    @DeleteMapping("/deleteNovel")
    @SysLogs("删除小说")
    public ResponseResult deleteNovel(@RequestParam("ids")List<String> ids){
        if(CollectionUtils.isEmpty(ids)){
            throw RequestException.fail(CodeMsgEnums.ID_IS_EMPTY.getMsg());
        }
        novelService.deleteNovel(ids);
        return ResponseResult.success();
    }

    @GetMapping("/download")
    @SysLogs("下载小说")
    public void downloadNovel(HttpServletRequest request, HttpServletResponse response, @RequestParam("params") String params){
        try {getSysUser
            SysUser sysUser = ShiroUtils.getSysUser(request);
            String param = URLDecoder.decode(params, "UTF-8");
            DownloadDto downloadDto = JsonUtils.jsonStrToObject(param, DownloadDto.class);
            this.novelService.downloadNovel(request,response,downloadDto,sysUser);
        } catch (UnsupportedEncodingException e) {
            throw RequestException.fail("参数错误");
        }
    }

    @PostMapping("/public")
    @SysLogs("发布小说")
    public ResponseResult publicNovel(HttpServletRequest request,@RequestBody NovelDto novelDto){
        if(StringUtils.isEmpty(novelDto.getId())&&CollectionUtils.isEmpty(novelDto.getIds())){
            throw RequestException.fail(CodeMsgEnums.ID_IS_EMPTY.getMsg());
        }
        SysUser sysUser = ShiroUtils.getSysUser(request);
        this.novelService.publicNovel(sysUser,novelDto);
        return ResponseResult.success();
    }

    @PostMapping("/cancelPublic")
    @SysLogs("取消发布")
    public ResponseResult cancelPublic(HttpServletRequest request,@RequestBody NovelDto novelDto){
        if(StringUtils.isEmpty(novelDto.getId())&&CollectionUtils.isEmpty(novelDto.getIds())){
            throw RequestException.fail(CodeMsgEnums.ID_IS_EMPTY.getMsg());
        }
        SysUser sysUser = ShiroUtils.getSysUser(request);
        this.novelService.cancelPublic(sysUser,novelDto);
        return ResponseResult.success();
    }

    @PostMapping("/audit")
    @SysLogs("审核小说")
    public ResponseResult auditNovel(HttpServletRequest request,@RequestBody NovelDto novelDto){
        if(StringUtils.isEmpty(novelDto.getId())&&CollectionUtils.isEmpty(novelDto.getIds())){
            throw RequestException.fail(CodeMsgEnums.ID_IS_EMPTY.getMsg());
        }
        SysUser sysUser = ShiroUtils.getSysUser(request);
        this.novelService.auditNovel(sysUser,novelDto);
        return ResponseResult.success();
    }
}
