package com.wpy.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wpy.entity.Conmment;
import com.wpy.mapper.ConmmentMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2020-11-25
 */
@Service
public class ConmmentServiceImpl extends ServiceImpl<ConmmentMapper, Conmment> implements IService<Conmment> {

}
