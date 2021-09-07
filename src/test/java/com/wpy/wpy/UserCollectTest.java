package com.wpy.wpy;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpy.dto.CollectionDto;
import com.wpy.entity.UserCollection;
import com.wpy.mapper.UserCollectionMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.regex.Pattern;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class UserCollectTest {

    @Autowired
    UserCollectionMapper userCollectionMapper;

    private static final Pattern txtChapterPattern=Pattern.compile("[\\u3000|\\u0020|\\u00A0]*第[0-9零一二三四五六七八九十百千万亿]+[章节卷集部篇回][\\u3000|\\u0020|\\u00A0]*\\S*");
    private static final Pattern txtChapterPattern2=Pattern.compile("\\s*第[0-9零一二三四五六七八九十百千万亿]+[章节卷集部篇回]\\s+[\\s\\S]*");
    private static final Pattern txtChapterPattern3=Pattern.compile("第[0-9零一二三四五六七八九十百千万亿]+[章节卷集部篇回]");
    private static final Pattern txtChapterPattern4=Pattern.compile("\\s*第[0-9零一二三四五六七八九十百千万亿]+[章节卷集部篇回]\\s*世界大变");

    @Test
    public void getList(){
        String test="第1章 世界大变";
        boolean matches = txtChapterPattern.matcher(test).matches();
        boolean matches2 = txtChapterPattern2.matcher(test).matches();
        boolean matches3 = txtChapterPattern3.matcher(test).matches();
        boolean matches4 = txtChapterPattern4.matcher(test).matches();
        System.out.println(matches);
    }
}
