package com.wpy.controller;

import com.wpy.annotation.SysLogs;
import com.wpy.service.NovelService;
import com.wpy.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 13940
 */

@RestController
@RequestMapping("/rest/novel")
@Slf4j
public class NovelRestController {

    @Autowired
    private NovelService novelService;

    @GetMapping("/getHotNovelData")
    @SysLogs("获得不同小说的前几数据")
    public ResponseResult getHotNovelDataByType(@RequestParam(value = "types",required = false) List<String> types){
        return ResponseResult.success(novelService.getHotNovelDataByType(types));
    }
}
