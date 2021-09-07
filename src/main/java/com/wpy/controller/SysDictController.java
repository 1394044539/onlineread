package com.wpy.controller;


import com.wpy.annotation.SysLogs;
import com.wpy.dto.SysDictDto;
import com.wpy.entity.SysDict;
import com.wpy.entity.SysUser;
import com.wpy.service.SysDictService;
import com.wpy.utils.ResponseResult;
import com.wpy.utils.ShiroUtils;
import com.wpy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 字典数据表 前端控制器
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@RestController
@RequestMapping("/sysDict")
public class SysDictController {

    @Autowired
    private SysDictService sysDictService;

    @GetMapping("/list")
    @SysLogs("获得字典列表")
    public ResponseResult getList(@RequestParam("dictClass")String dictClass,String order){
        return ResponseResult.success(sysDictService.getList(dictClass,order));
    }

    @GetMapping("/novelType")
    @SysLogs("获得主页的小说类型")
    public ResponseResult getNovelType(){
        return ResponseResult.success(sysDictService.getNovelType());
    }

    @PostMapping("/addOrEditDict")
    @SysLogs("添加或修改字典数据")
    public ResponseResult addOrEditDict(HttpServletRequest request, SysDictDto dto){
        SysUser sysUser = ShiroUtils.getSysUser(request);
        if(StringUtils.isEmpty(dto.getId())){
            this.sysDictService.addDict(sysUser,dto);
        }else {
            this.sysDictService.editDict(sysUser,dto);
        }
        return ResponseResult.success();
    }


}
