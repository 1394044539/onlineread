package com.wpy.controller;


import com.wpy.annotation.SysLogs;
import com.wpy.dto.NovelDto;
import com.wpy.dto.NovelUploadDto;
import com.wpy.entity.SysUser;
import com.wpy.service.FileService;
import com.wpy.utils.ResponseResult;
import com.wpy.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 文件表 前端控制器
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@RestController
@RequestMapping("/file")
public class FileController {

}
