package com.wpy.controller;


import com.wpy.annotation.SysLogs;
import com.wpy.dto.ShareDto;
import com.wpy.entity.SysUser;
import com.wpy.enums.CodeMsgEnums;
import com.wpy.exception.RequestException;
import com.wpy.service.ShareService;
import com.wpy.utils.RequestUtils;
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
 * @since 2021-03-30
 */
@RestController
@RequestMapping("/share")
public class ShareController {

    @Autowired
    private ShareService shareService;

    @PostMapping("/addOrEdit")
    @SysLogs("创建分享")
    public ResponseResult addOrEditShare(HttpServletRequest request,@RequestBody ShareDto shareDto){
        SysUser sysUser = ShiroUtils.getSysUser(request);
        if(StringUtils.isEmpty(shareDto.getId())){
            //新建
            return ResponseResult.success(shareService.addShare(sysUser,shareDto));
        }else{
            //修改
            shareService.editShare(sysUser,shareDto);
        }
        return ResponseResult.success();
    }

    @GetMapping("/list")
    @SysLogs("分享列表")
    public ResponseResult shareList(HttpServletRequest request,ShareDto shareDto){
        SysUser sysUser = ShiroUtils.getSysUser(request);
        return ResponseResult.success(this.shareService.getShareList(sysUser,shareDto));
    }

    @PostMapping("/checkNeedPwd")
    @SysLogs("校验是否需要需要密码")
    public ResponseResult checkNeedPwd(HttpServletRequest request,@RequestBody ShareDto shareDto){
        if(StringUtils.isEmpty(shareDto.getId())){
            throw RequestException.fail(CodeMsgEnums.ID_IS_EMPTY.getMsg());
        }
        return ResponseResult.success(this.shareService.checkNeedPwd(shareDto));
    }

    @PostMapping("/checkPwd")
    @SysLogs("校验密码和当前登录人是否正确")
    public ResponseResult checkPwd(HttpServletRequest request,@RequestBody ShareDto shareDto){
        if(StringUtils.isEmpty(shareDto.getId())){
            throw RequestException.fail(CodeMsgEnums.ID_IS_EMPTY.getMsg());
        }
        SysUser sysUser = RequestUtils.getSysUser(request);
        return ResponseResult.success(this.shareService.checkPwd(sysUser,shareDto));
    }

    @GetMapping("/fileList")
    @SysLogs("分享后的文件列表")
    public ResponseResult shareFileList(HttpServletRequest request,ShareDto shareDto){
        if(StringUtils.isEmpty(shareDto.getId())){
            throw RequestException.fail(CodeMsgEnums.ID_IS_EMPTY.getMsg());
        }
        SysUser sysUser = ShiroUtils.getSysUser(request);
        return ResponseResult.success(this.shareService.shareFileList(sysUser,shareDto));
    }



}
