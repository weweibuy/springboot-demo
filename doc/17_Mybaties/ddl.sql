CREATE TABLE `demo_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `full_name` varchar(20) NOT NULL COMMENT '姓名',
  `user_name` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(200) NOT NULL COMMENT '密码',
  `phone_no` varchar(150) NOT NULL DEFAULT '' COMMENT '手机号',
  `id_no` varchar(200) NOT NULL COMMENT '身份证号',
  `is_delete` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_name` (`user_name`(10))
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
