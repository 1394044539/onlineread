package com.wpy.service.impl;

import com.wpy.entity.Chat;
import com.wpy.mapper.ChatMapper;
import com.wpy.service.ChatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wpy
 * @since 2021-04-03
 */
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements ChatService {

}
