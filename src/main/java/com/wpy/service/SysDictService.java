package com.wpy.service;

import com.wpy.dto.SysDictDto;
import com.wpy.entity.Novel;
import com.wpy.entity.SysDict;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wpy.entity.SysUser;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典数据表 服务类
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
public interface SysDictService extends IService<SysDict> {

    /**
     *根据class获得列表
     * @author pywang6
     * @date 2021/3/12 10:28
     *
     * @param dictClass
     * @param order
     * @return java.util.List<com.wpy.entity.SysDict>
     */
    List<SysDict> getList(String dictClass, String order);

    /**
     *根据class和key获得value
     * @author pywang6
     * @date 2021/3/12 10:28
     *
     * @param dictClass
     * @param key
     * @return java.util.List<com.wpy.entity.SysDict>
     */
    String getValue(String key,String dictClass);

    /**
     * 获取主页上的小说表
     * @return
     */
    List<SysDict> getNovelType();

    /**
     * 添加字典
     * @param sysUser
     * @param dto
     */
    void addDict(SysUser sysUser, SysDictDto dto);

    /**
     * 修改字典
     * @param sysUser
     * @param dto
     */
    void editDict(SysUser sysUser, SysDictDto dto);
}
