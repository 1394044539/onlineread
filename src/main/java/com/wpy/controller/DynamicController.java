package com.wpy.controller;


import com.wpy.annotation.SysLogs;
import com.wpy.dto.DynamicDto;
import com.wpy.entity.SysUser;
import com.wpy.enums.CodeMsgEnums;
import com.wpy.exception.RequestException;
import com.wpy.service.DynamicService;
import com.wpy.utils.ResponseResult;
import com.wpy.utils.ShiroUtils;
import com.wpy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wpy
 * @since 2021-04-09
 */
@RestController
@RequestMapping("/dynamic")
public class DynamicController {

    @Autowired
    private DynamicService dynamicService;

    @GetMapping("/list")
    @SysLogs("获取动态列表")
    public ResponseResult getList(HttpServletRequest request, DynamicDto dynamicDto){
        SysUser sysUser = ShiroUtils.getSysUser(request);
        return ResponseResult.success(this.dynamicService.getList(sysUser,dynamicDto));
    }

    @GetMapping("/info")
    @SysLogs("获得动态详情")
    public ResponseResult getDynamic(HttpServletRequest request,DynamicDto dynamicDto){
        if(StringUtils.isEmpty(dynamicDto.getId())){
            throw RequestException.fail(CodeMsgEnums.ID_IS_EMPTY.getMsg());
        }
        return ResponseResult.success(this.dynamicService.getById(dynamicDto.getId()));
    }

    @PostMapping("/alread")
    @SysLogs("标记已读")
    public ResponseResult alread(HttpServletRequest request,@RequestBody DynamicDto dynamicDto){
        SysUser sysUser = ShiroUtils.getSysUser(request);
        this.dynamicService.alread(sysUser,dynamicDto);
        return ResponseResult.success();
    }

    @DeleteMapping("/delete")
    @SysLogs("删除动态")
    public ResponseResult deleteDynamic(HttpServletRequest request,DynamicDto dynamicDto){
        SysUser sysUser = ShiroUtils.getSysUser(request);
        this.dynamicService.deleteDynamic(sysUser,dynamicDto);
        return ResponseResult.success();
    }

}
