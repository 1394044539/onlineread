package com.wpy.controller;

import com.wpy.annotation.SysLogs;
import com.wpy.service.SysDictService;
import com.wpy.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 13940
 */
@RestController
@RequestMapping("/rest/sysDict")
public class SysDictRestController {

    @Autowired
    private SysDictService sysDictService;

    @GetMapping("/list")
    @SysLogs("获得字典列表")
    public ResponseResult getList(@RequestParam("dictClass")String dictClass, String order){
        return ResponseResult.success(sysDictService.getList(dictClass,order));
    }

    @GetMapping("/novelType")
    @SysLogs("获得主页的小说类型")
    public ResponseResult getNovelType(){
        return ResponseResult.success(sysDictService.getNovelType());
    }
}
