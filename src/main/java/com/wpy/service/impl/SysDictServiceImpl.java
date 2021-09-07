package com.wpy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.wpy.constant.NumConstant;
import com.wpy.constant.SqlConstant;
import com.wpy.dto.SysDictDto;
import com.wpy.entity.Novel;
import com.wpy.entity.SysDict;
import com.wpy.entity.SysUser;
import com.wpy.enums.DictEnums;
import com.wpy.enums.TypeEnums;
import com.wpy.mapper.SysDictMapper;
import com.wpy.service.SysDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wpy.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    @Autowired
    SysDictMapper sysDictMapper;

    @Override
    public List<SysDict> getList(String dictClass, String order) {
        return this.sysDictMapper.selectList(new QueryWrapper<SysDict>().eq(SqlConstant.DICT_CLASS, dictClass));
    }

    @Override
    public String getValue(String key, String dictClass) {
        SysDict sysDict = this.getOne(new QueryWrapper<SysDict>().eq(SqlConstant.DICT_CLASS, dictClass).eq(SqlConstant.DICT_KEY, key));
        return sysDict!=null?sysDict.getDictValue():"";
    }

    @Override
    public List<SysDict> getNovelType() {
        //1、查看管理员是否有设定
        SysDict sysDict = this.sysDictMapper.selectOne(new QueryWrapper<SysDict>().eq(SqlConstant.DICT_CLASS, DictEnums.ADMIN_CHOSE_TYPE.getKey()));
        if(sysDict!=null){
            String dictValue = sysDict.getDictValue();
            List<String> list = StringUtils.commaStrToList(dictValue);
            List<SysDict> dictList= Lists.newArrayList();
            for (String key : list) {
                SysDict novelTypeDict = this.sysDictMapper.selectOne(new QueryWrapper<SysDict>()
                        .eq(SqlConstant.DICT_CLASS,DictEnums.NOVEL_TYPE.getKey())
                        .eq(SqlConstant.DICT_KEY,key));
                dictList.add(novelTypeDict);
            }
            if(!dictList.isEmpty()&&dictList.size()== NumConstant.SIX){
                return dictList;
            }
        }
        List<SysDict> sysDicts = this.sysDictMapper.selectList(new QueryWrapper<SysDict>().eq(SqlConstant.DICT_CLASS, DictEnums.NOVEL_TYPE.getKey()));
        return sysDicts.subList(0,6);
    }

    @Override
    public void addDict(SysUser sysUser, SysDictDto dto) {
        SysDict sysDict = new SysDict();
        BeanUtils.copyProperties(dto,sysDict);
        sysDict.setId(StringUtils.getUuid());
        sysDict.setCreateBy(sysUser.getId());
        sysDict.setCreateTime(new Date());
        this.save(sysDict);
    }

    @Override
    public void editDict(SysUser sysUser, SysDictDto dto) {
        SysDict sysDict = new SysDict();
        BeanUtils.copyProperties(dto,sysDict);
        sysDict.setUpdateBy(sysUser.getId());
        sysDict.setUpdateTime(new Date());
        this.updateById(sysDict);
    }


}
