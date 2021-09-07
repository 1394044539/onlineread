package com.wpy.controller;


import com.wpy.annotation.SysLogs;
import com.wpy.dto.SysNoticeDto;
import com.wpy.entity.SysNotice;
import com.wpy.entity.SysUser;
import com.wpy.enums.CodeMsgEnums;
import com.wpy.exception.RequestException;
import com.wpy.service.SysNoticeService;
import com.wpy.utils.RequestUtils;
import com.wpy.utils.ResponseResult;
import com.wpy.utils.ShiroUtils;
import com.wpy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wpy
 * @since 2021-03-24
 */
@RestController
@RequestMapping("/sysNotice")
public class SysNoticeController {

    @Autowired
    private SysNoticeService sysNoticeService;

    @PostMapping("/notice")
    @SysLogs("新增或修改公告")
    public ResponseResult addOrEditNotice(HttpServletRequest request, @RequestBody SysNotice sysNotice){
        SysUser sysUser = ShiroUtils.getSysUser(request);
        if(StringUtils.isEmpty(sysNotice.getId())){
            //为空是新增
            sysNoticeService.addNotice(sysUser,sysNotice);
        }else {
            //不为空是修改
            sysNoticeService.editNotice(sysUser,sysNotice);
        }
        return ResponseResult.success();
    }

    @GetMapping("/list")
    @SysLogs("公告列表")
    public ResponseResult getList(HttpServletRequest request,SysNoticeDto sysNoticeDto){
        SysUser sysUser = RequestUtils.getSysUser(request);
        return ResponseResult.success(this.sysNoticeService.getList(sysNoticeDto));
    }

    @DeleteMapping("/notice")
    @SysLogs("删除公告")
    public ResponseResult deleteNotice(@RequestParam("ids") List<String> ids){
        this.sysNoticeService.deleteNotice(ids);
        return ResponseResult.success();
    }

    @PostMapping("/openOrClose")
    @SysLogs("打开公告")
    public ResponseResult openOrClose(@RequestBody SysNoticeDto sysNoticeDto){
        if(StringUtils.isEmpty(sysNoticeDto.getId())){
            throw RequestException.fail(CodeMsgEnums.ID_IS_EMPTY.getMsg());
        }
        if(StringUtils.isBlank(sysNoticeDto.getIsOpen())){
            throw RequestException.fail("状态不能为空");
        }
        if(sysNoticeDto.getIsOpen()){
            sysNoticeService.openNotice(sysNoticeDto.getId());
        }else{
            sysNoticeService.closeNotice(sysNoticeDto.getId());
        }
        return ResponseResult.success();
    }
}
