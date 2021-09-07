ALTER TABLE `novel_chapter`
ADD COLUMN `chapter_href` varchar(255) NULL COMMENT 'epub的路径' AFTER `chapter_order`,
ADD COLUMN `parent_id` char(36) NULL COMMENT '父章节id' AFTER `chapter_href`;


--  3/12
ALTER TABLE `sys_dict`
ADD COLUMN `remarks` varchar(512) NULL COMMENT '描述' AFTER `parent_key`;

ALTER TABLE `read_history`
DROP COLUMN `is_delete`,
ADD COLUMN `user_type` tinyint(4) NULL COMMENT '记录途径（0：已登录；1：未登录）' AFTER `last_page`;

ALTER TABLE `read_history`
CHANGE COLUMN `last_page` `last_chapter_id` char(36) NULL DEFAULT NULL COMMENT '小说最后访问的章节id' AFTER `novel_id`;
ALTER TABLE `read_history`
ADD COLUMN `read_position` int(11) NULL COMMENT '阅读位置，页面纵坐标' AFTER `user_type`;

-- 3/19
ALTER TABLE `read_history`
ADD COLUMN `read_all_position` int(11) NULL COMMENT '该章节的全部页面纵坐标' AFTER `read_position`;

ALTER TABLE `read_history`
ADD COLUMN `book_mark_name` varchar(40) NULL COMMENT '书签名字' AFTER `read_all_position`,
ADD COLUMN `type` tinyint(4) NULL COMMENT '类型：0：历史记录；1：书签' AFTER `book_mark_name`;

-- 3/20
INSERT INTO `sys_dict`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `dict_class`, `dict_key`, `dict_value`, `order_num`, `parent_key`, `remarks`) VALUES ('0a7e0749-87fb-4bc8-add3-2c9bd5742e32', 'admin', '2021-03-12 02:57:24', 'admin', NULL, 'NOVEL_TYPE', 'wuxia', '武侠', NULL, NULL, NULL);
INSERT INTO `sys_dict`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `dict_class`, `dict_key`, `dict_value`, `order_num`, `parent_key`, `remarks`) VALUES ('0f4e55ec-a82e-45cb-b2fc-f3ce48a7eb82', 'admin', '2021-03-12 02:57:24', 'admin', NULL, 'NOVEL_TYPE', 'qinxiaoshuo', '轻小说', NULL, NULL, NULL);
INSERT INTO `sys_dict`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `dict_class`, `dict_key`, `dict_value`, `order_num`, `parent_key`, `remarks`) VALUES ('4368974a-5f9d-497b-9a7b-044fb950a241', 'admin', '2021-03-12 02:57:24', 'admin', NULL, 'NOVEL_TYPE', 'lishi', '历史', NULL, NULL, NULL);
INSERT INTO `sys_dict`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `dict_class`, `dict_key`, `dict_value`, `order_num`, `parent_key`, `remarks`) VALUES ('4917eaae-95d2-443f-a32d-2a29d2aed4fd', 'admin', '2021-03-12 02:57:24', 'admin', NULL, 'NOVEL_TYPE', 'tongren', '同人', NULL, NULL, NULL);
INSERT INTO `sys_dict`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `dict_class`, `dict_key`, `dict_value`, `order_num`, `parent_key`, `remarks`) VALUES ('6315820b-82f0-411d-8dad-56a0c5cf1ffd', 'admin', '2021-03-12 02:57:23', 'admin', NULL, 'NOVEL_TYPE', 'xuanhuan', '玄幻', NULL, NULL, NULL);
INSERT INTO `sys_dict`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `dict_class`, `dict_key`, `dict_value`, `order_num`, `parent_key`, `remarks`) VALUES ('77b5a3f0-901c-490a-9d11-9728e33a1d52', 'admin', '2021-03-12 02:57:24', 'admin', NULL, 'NOVEL_TYPE', 'kehuan', '科幻', NULL, NULL, NULL);
INSERT INTO `sys_dict`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `dict_class`, `dict_key`, `dict_value`, `order_num`, `parent_key`, `remarks`) VALUES ('7ba158b3-f7a6-44f8-b32f-1cccdbc72283', 'admin', '2021-03-12 02:57:24', 'admin', NULL, 'NOVEL_TYPE', 'yanqing', '言情', NULL, NULL, NULL);
INSERT INTO `sys_dict`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `dict_class`, `dict_key`, `dict_value`, `order_num`, `parent_key`, `remarks`) VALUES ('7f16d4e5-9e1e-4c21-b467-ea53d6188d3e', 'admin', '2021-03-12 02:57:24', 'admin', NULL, 'NOVEL_TYPE', 'linyi', '灵异', NULL, NULL, NULL);
INSERT INTO `sys_dict`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `dict_class`, `dict_key`, `dict_value`, `order_num`, `parent_key`, `remarks`) VALUES ('c46cccef-f51c-4aac-a46e-72b9ecb59340', 'admin', '2021-03-12 02:57:24', 'admin', NULL, 'NOVEL_TYPE', 'dushi', '都市', NULL, NULL, NULL);

INSERT INTO `sys_dict`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `dict_class`, `dict_key`, `dict_value`, `order_num`, `parent_key`, `remarks`) VALUES ('3be11d49-95f4-4fb1-ac76-5b7310086dfc', 'admin', '2021-03-20 07:41:49', 'admin', NULL, 'ADMIN_CHOSE_TYPE', 'xuanhuan,dushi,wuxia,yanqing,tongren,qinxiaoshuo,linyi,kehuan', 'xuanhuan,dushi,wuxia,yanqing,tongren,qinxiaoshuo,linyi,kehuan', NULL, NULL, '首页要展示的页面分类');

-- 3/21
ALTER TABLE `read_file` MODIFY COLUMN `file_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名' AFTER `update_time`;

-- 3/22
ALTER TABLE `user_collection`
ADD PRIMARY KEY (`id`);

-- 3/24
CREATE TABLE `sys_notice` (
  `id` char(36) NOT NULL COMMENT '主键',
  `title` varchar(255) DEFAULT NULL COMMENT '公告标题',
  `content` text COMMENT '公告内容',
  `is_open` tinyint(4) DEFAULT NULL COMMENT '是否首页打开',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 3/27
ALTER TABLE `novel`
MODIFY COLUMN `create_by` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人' AFTER `id`,
MODIFY COLUMN `update_by` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人' AFTER `create_time`;

-- 3/30
CREATE TABLE `share` (
  `id` char(36) NOT NULL COMMENT '主键id',
  `share_name` varchar(100) DEFAULT NULL COMMENT '分享名',
  `create_by` char(36) DEFAULT NULL COMMENT '分享人id',
  `share_user` varchar(1024) DEFAULT NULL COMMENT '被分享人',
  `share_pwd` varchar(8) DEFAULT NULL COMMENT '密码',
  `share_path` varchar(255) DEFAULT NULL COMMENT '分享连接',
  `invalid_time` datetime DEFAULT NULL COMMENT '失效时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` char(36) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `share_type` tinyint(4) DEFAULT NULL COMMENT '分享类型：0：随意进入;1:纯密码进入;2：指定用户分享;3:指定用户密码分享',
  `share_status` tinyint(4) DEFAULT NULL COMMENT '分享状态: 0:有效;1:删除;2:禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `share_file` (
  `id` char(36) NOT NULL COMMENT '主键id',
  `share_id` char(36) DEFAULT NULL COMMENT '分享id',
  `file_id` char(36) DEFAULT NULL COMMENT '小说或者文件夹id',
  `file_type` tinyint(4) DEFAULT NULL COMMENT '分享的文件类型:0:文件夹;1:小说',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `chat` (
  `id` char(36) NOT NULL COMMENT '主键id',
  `message` text COMMENT '消息内容',
  `send_acconut_name` varchar(255) DEFAULT NULL COMMENT '发送人',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `novel_id` char(36) DEFAULT NULL COMMENT '小说id',
  `chapter_id` char(36) DEFAULT NULL COMMENT '章节id',
  `msg_type` tinyint(4) DEFAULT NULL COMMENT '0,"通知消息";1,"聊天消息";2,"用户自己";3,"在线人数"',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `read_history`
MODIFY COLUMN `user_id` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '创建人' AFTER `id`;

ALTER TABLE `novel`
ADD COLUMN `novel_collection` int(11) NULL COMMENT '收藏数量' AFTER `novel_comment`,
ADD COLUMN `novel_share` int(11) NULL COMMENT '分享数量' AFTER `novel_collection`,
ADD COLUMN `novel_status` tinyint(4) NULL COMMENT '审核状态:0:未审核；1:待审核;2:审核完成;3：审核失败，4：被禁用' AFTER `upload_user_id`;

ALTER TABLE `novel`
MODIFY COLUMN `novel_collection` int(11) NULL DEFAULT 0 COMMENT '收藏数量' AFTER `novel_comment`,
MODIFY COLUMN `novel_share` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '分享数量' AFTER `novel_collection`;

ALTER TABLE `novel`
ADD COLUMN `error_msg` varchar(255) NULL COMMENT '审核失败或禁用原因' AFTER `novel_status`;

-- 2021/4/9 动态表
CREATE TABLE `dynamic` (
  `id` char(36) NOT NULL COMMENT '主键id',
  `dynamic_title` varchar(255) DEFAULT NULL COMMENT '动态标题',
  `dynamic_content` varchar(1024) DEFAULT NULL COMMENT '动态内容',
  `user_id` char(36) DEFAULT NULL COMMENT '用户id',
  `create_by` char(36) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `is_read` tinyint(4) DEFAULT NULL COMMENT '是否已阅读:0:未阅读，1：已阅读',
  `dynamic_type` tinyint(4) DEFAULT NULL COMMENT '动态类型：0：小说审核；1：用户审核',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 2021/4/17
ALTER TABLE `sys_user`
ADD COLUMN `photo` varchar(128) NULL COMMENT '头像' AFTER `phone`;

-- 2021/4/18
ALTER TABLE `sys_user`
DROP COLUMN `user_status`,
ADD COLUMN `status` tinyint(4) NULL COMMENT '用户状态(0:正常;1:注销;2:禁用)' AFTER `role_type`;

-- 2021/5/7 增加日志参数表
CREATE TABLE `sys_dict_param` (
  `id` char(36) NOT NULL COMMENT '主键id',
  `sys_dict_id` char(36) DEFAULT NULL COMMENT '字典id',
  `param_key` varchar(255) DEFAULT NULL COMMENT '参数key',
  `param_value` varchar(255) DEFAULT NULL COMMENT '参数值',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `update_by` char(36) DEFAULT NULL COMMENT '操作人',
  `update_time` datetime DEFAULT NULL COMMENT '操作时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2021/05/20 设置管理员初始面
INSERT INTO `sys_dict`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `dict_class`, `dict_key`, `dict_value`, `order_num`, `parent_key`, `remarks`) VALUES ('c46cccef-f51c-4aac-a46e-72b9ecb59345', 'admin', '2021-05-20 02:57:24', 'admin', NULL, 'ADMIN_INIT_PWD', 'adminInitPwd', 'admin9962', NULL, NULL, '管理员初始密码');
