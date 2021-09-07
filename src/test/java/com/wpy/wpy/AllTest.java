package com.wpy.wpy;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpy.config.jedis.RedisTemplateUtils;
import com.wpy.dto.ReadHistoryDto;
import com.wpy.entity.ReadHistory;
import com.wpy.entity.SysUser;
import com.wpy.mapper.ReadHistoryMapper;
import com.wpy.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class AllTest {

    @Autowired
    ReadHistoryMapper readHistoryMapper;
    @Autowired
    RedisTemplateUtils redisTemplateUtils;
    @Autowired
    SysUserService sysUserService;

    @Test
    public void testSE(){
//        BeanUtils.copyProperties();
    }

    @Test
    public void test(){
        List<SysUser> sysUsers = sysUserService.getBaseMapper().selectList(null);
        Map<Object, Object> userMap = sysUsers.stream().collect(
                Collectors.toMap(SysUser::getId, Function.identity(), (key1, key2) -> key2));
        redisTemplateUtils.setMap(userMap);
        redisTemplateUtils.setMap("userMap",userMap);
    }

    @Test
    public void test2(){
        Map<Object, Object> userMap = redisTemplateUtils.getAllMap("userMap");
        System.out.println(userMap);
        Object userMap1 = redisTemplateUtils.getMapByKey("userMap", "9faf00e6-9cb3-402c-bd3a-d8a159f3f7e8");
        System.out.println(userMap1);
        Object map = redisTemplateUtils.getMap("0fa8c54f-4604-4465-9ad6-4b8c97bf4c03");
        System.out.println(map);
    }
}
