package com.wpy.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpy.dto.DynamicDto;
import com.wpy.entity.Dynamic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wpy
 * @since 2021-04-09
 */
public interface DynamicMapper extends BaseMapper<Dynamic> {

    /**
     * 分页查询数据
     * @param dynamicDto
     * @param page
     * @return
     */
    List<Dynamic> selectDynamicPage(@Param("dto") DynamicDto dynamicDto, Page<Dynamic> page);
}
