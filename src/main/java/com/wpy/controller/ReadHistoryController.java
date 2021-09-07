package com.wpy.controller;


import com.wpy.annotation.SysLogs;
import com.wpy.dto.ReadHistoryDto;
import com.wpy.entity.SysUser;
import com.wpy.enums.CodeMsgEnums;
import com.wpy.enums.TypeEnums;
import com.wpy.exception.RequestException;
import com.wpy.service.ReadHistoryService;
import com.wpy.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 阅读历史表 前端控制器
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@RestController
@RequestMapping("/readHistory")
public class ReadHistoryController {

    @Autowired
    ReadHistoryService readHistoryService;

    @PostMapping("/history")
    @SysLogs("记录阅读记录")
    public ResponseResult addHistory(HttpServletRequest request,@RequestBody ReadHistoryDto readHistoryDto){
        SysUser sysUser = RequestUtils.getSysUser(request);
        String ip = HttpClientUtils.getIp(request);
        readHistoryDto.setIp(ip);
        readHistoryService.saveHistoryOrMark(readHistoryDto,sysUser);
        return ResponseResult.success();
    }

    @GetMapping("/history")
    @SysLogs("查询阅读记录")
    public ResponseResult getHistory(HttpServletRequest request, ReadHistoryDto readHistoryDto){
        return ResponseResult.success(readHistoryService.getHistory(request,readHistoryDto));
    }

    @GetMapping("/list")
    @SysLogs("获取阅读记录列表")
    public ResponseResult getList(HttpServletRequest request, ReadHistoryDto readHistoryDto){
        SysUser sysUser = RequestUtils.getSysUser(request);
        String ip = HttpClientUtils.getIp(request);
        readHistoryDto.setIp(ip);
        return ResponseResult.success(readHistoryService.getList(sysUser,readHistoryDto));
    }

    @DeleteMapping("/history")
    @SysLogs("删除历史记录")
    public ResponseResult deleteHistory(HttpServletRequest request,@RequestParam(value = "ids",required = false) List<String> ids,
                                        @RequestParam(value = "userId",required = false)String userId,
                                        @RequestParam(value = "type",required = false)Integer type,
                                        @RequestParam(value = "deleteType")Integer deleteType ){
        SysUser sysUser = ShiroUtils.getSysUser(request);
        String ip = HttpClientUtils.getIp(request);
        readHistoryService.deleteHistory(ids,userId,type,ip,deleteType,sysUser);
        return ResponseResult.success();
    }
}
