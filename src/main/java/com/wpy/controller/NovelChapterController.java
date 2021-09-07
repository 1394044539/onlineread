package com.wpy.controller;


import com.wpy.annotation.SysLogs;
import com.wpy.dto.NovelChapterDto;
import com.wpy.entity.SysUser;
import com.wpy.enums.CodeMsgEnums;
import com.wpy.exception.RequestException;
import com.wpy.service.NovelChapterService;
import com.wpy.utils.RequestUtils;
import com.wpy.utils.ResponseResult;
import com.wpy.utils.ShiroUtils;
import com.wpy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 章节表 前端控制器
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@RestController
@RequestMapping("/novelChapter")
public class NovelChapterController {

    @Autowired
    private NovelChapterService novelChapterService;

    @GetMapping("/list")
    @SysLogs("获取小说章节列表")
    public ResponseResult getNovelChapter(HttpServletRequest request,@RequestParam("id")String id){
        if(StringUtils.isEmpty(id)){
            throw RequestException.fail(CodeMsgEnums.ID_IS_EMPTY.getMsg());
        }
        SysUser sysUser = RequestUtils.getSysUser(request);
        return ResponseResult.success(novelChapterService.getNovelChapter(sysUser,id));
    }

    @GetMapping("/content")
    @SysLogs("获取小说内容")
    public ResponseResult getNovelContext(HttpServletRequest request,NovelChapterDto novelChapterDto){
        SysUser sysUser = RequestUtils.getSysUser(request);
        return ResponseResult.success(novelChapterService.getNovelChapterContext(sysUser,novelChapterDto));
    }

}
