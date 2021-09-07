package com.wpy.wpy;


import com.wpy.entity.SysDict;
import com.wpy.service.SysDictService;
import com.wpy.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class SysDictTest {

    @Autowired
    SysDictService sysDictService;

    @Test
    public void test(){
//        this.savaDict("xuanhuan","玄幻");
//        this.savaDict("wuxia","武侠");
//        this.savaDict("kehuan","科幻");
//        this.savaDict("dushi","都市");
//        this.savaDict("yanqing","言情");
//        this.savaDict("lishi","历史");
//        this.savaDict("linyi","灵异");
//        this.savaDict("tongren","同人");
//        this.savaDict("qinxiaoshuo","轻小说");
        this.savaDict("main_type","");

    }

    private void savaDict(String key, String value) {
        SysDict sysDict=new SysDict();
        sysDict.setId(StringUtils.getUuid());
        sysDict.setCreateBy("admin");
        sysDict.setCreateTime(new Date());
        sysDict.setUpdateBy("admin");
        sysDict.setDictClass("ADMIN_CHOSE_TYPE");
        sysDict.setDictKey(key);
        sysDict.setDictValue(value);
        sysDict.setRemarks("首页要展示的页面分类");
        sysDictService.save(sysDict);
    }

}
