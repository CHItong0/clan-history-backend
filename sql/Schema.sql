-- =====================================================
-- 族谱历史管理系统 数据库脚本
-- 数据库: clan_history
-- =====================================================

CREATE DATABASE IF NOT EXISTS clan_history DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE clan_history;

-- =====================================================
-- 1. 系统用户表 (sys_user)
-- =====================================================
CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` VARCHAR(100) NOT NULL COMMENT '用户名',
  `password` VARCHAR(200) NOT NULL COMMENT '密码(BCrypt加密)',
  `real_name` VARCHAR(100) DEFAULT NULL COMMENT '真实姓名',
  `email` VARCHAR(200) DEFAULT NULL COMMENT '邮箱',
  `phone` VARCHAR(50) DEFAULT NULL COMMENT '手机号',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1=正常, 0=禁用',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0=未删除, 1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- =====================================================
-- 2. 系统角色表 (sys_role)
-- =====================================================
CREATE TABLE IF NOT EXISTS `sys_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_name` VARCHAR(100) NOT NULL COMMENT '角色名称',
  `role_key` VARCHAR(100) NOT NULL COMMENT '角色标识',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1=正常, 0=禁用',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0=未删除, 1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- =====================================================
-- 3. 用户角色关联表 (sys_user_role)
-- =====================================================
CREATE TABLE IF NOT EXISTS `sys_user_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- =====================================================
-- 4. 系统权限表 (sys_permission)
-- =====================================================
CREATE TABLE IF NOT EXISTS `sys_permission` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `permission_name` VARCHAR(100) NOT NULL COMMENT '权限名称',
  `permission_key` VARCHAR(100) NOT NULL COMMENT '权限标识',
  `resource_type` VARCHAR(50) DEFAULT NULL COMMENT '资源类型: menu, button, api',
  `parent_id` BIGINT DEFAULT NULL COMMENT '父权限ID',
  `path` VARCHAR(200) DEFAULT NULL COMMENT '路径',
  `icon` VARCHAR(100) DEFAULT NULL COMMENT '图标',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_key` (`permission_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统权限表';

-- =====================================================
-- 5. 角色权限关联表 (sys_role_permission)
-- =====================================================
CREATE TABLE IF NOT EXISTS `sys_role_permission` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `permission_id` BIGINT NOT NULL COMMENT '权限ID',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- =====================================================
-- 6. 家族表 (family)
-- =====================================================
CREATE TABLE IF NOT EXISTS `family` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `family_name` VARCHAR(200) NOT NULL COMMENT '家族名称',
  `family_code` VARCHAR(100) DEFAULT NULL COMMENT '家族编码',
  `description` TEXT DEFAULT NULL COMMENT '家族描述',
  `region` VARCHAR(200) DEFAULT NULL COMMENT '所属地区',
  `founded_year` INT DEFAULT NULL COMMENT '创立年份',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1=正常',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_family_code` (`family_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家族表';

-- =====================================================
-- 7. 人物表 (person) - 核心族谱数据
-- =====================================================
CREATE TABLE IF NOT EXISTS `person` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `family_id` BIGINT NOT NULL COMMENT '家族ID',
  `name` VARCHAR(100) NOT NULL COMMENT '姓名',
  `gender` TINYINT DEFAULT NULL COMMENT '性别: 1=男, 2=女',
  `birth_year` INT DEFAULT NULL COMMENT '出生年份',
  `death_year` INT DEFAULT NULL COMMENT '去世年份',
  `generation` INT NOT NULL COMMENT '代数(1=最早的祖先)',
  `birthplace` VARCHAR(200) DEFAULT NULL COMMENT '出生地',
  `deathplace` VARCHAR(200) DEFAULT NULL COMMENT '去世地',
  `biography` TEXT DEFAULT NULL COMMENT '生平介绍',
  `father_id` BIGINT DEFAULT NULL COMMENT '父亲ID',
  `mother_id` BIGINT DEFAULT NULL COMMENT '母亲ID',
  `spouse_id` BIGINT DEFAULT NULL COMMENT '配偶ID',
  `portrait` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
  `is_celebrity` TINYINT NOT NULL DEFAULT 0 COMMENT '是否名人: 0=否, 1=是',
  `display_order` INT NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1=正常',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_family_id` (`family_id`),
  KEY `idx_generation` (`generation`),
  KEY `idx_father_id` (`father_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人物表';

-- =====================================================
-- 8. 名人表 (celebrity)
-- =====================================================
CREATE TABLE IF NOT EXISTS `celebrity` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `person_id` BIGINT DEFAULT NULL COMMENT '关联人物ID',
  `family_id` BIGINT DEFAULT NULL COMMENT '家族ID',
  `name` VARCHAR(200) DEFAULT NULL COMMENT '姓名',
  `title` VARCHAR(200) DEFAULT NULL COMMENT '称谓/头衔',
  `era` VARCHAR(100) DEFAULT NULL COMMENT '时代/年代',
  `brief` TEXT DEFAULT NULL COMMENT '简介',
  `biography` TEXT DEFAULT NULL COMMENT '详细生平(换行分隔段落)',
  `achievements` TEXT DEFAULT NULL COMMENT '主要成就',
  `honor` TEXT DEFAULT NULL COMMENT '荣誉',
  `portrait` VARCHAR(500) DEFAULT NULL COMMENT '肖像URL',
  `display_order` INT NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_person_id` (`person_id`),
  KEY `idx_family_id` (`family_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='名人表';

-- =====================================================
-- 9. 迁徙路线表 (migration_route)
-- =====================================================
CREATE TABLE IF NOT EXISTS `migration_route` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `family_id` BIGINT NOT NULL COMMENT '家族ID',
  `route_name` VARCHAR(200) NOT NULL COMMENT '路线名称',
  `start_year` INT DEFAULT NULL COMMENT '开始年份',
  `end_year` INT DEFAULT NULL COMMENT '结束年份',
  `description` TEXT DEFAULT NULL COMMENT '路线描述',
  `total_distance` DECIMAL(10,2) DEFAULT NULL COMMENT '总距离(公里)',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_family_id` (`family_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='迁徙路线表';

-- =====================================================
-- 10. 迁徙地点表 (migration_point)
-- =====================================================
CREATE TABLE IF NOT EXISTS `migration_point` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `route_id` BIGINT NOT NULL COMMENT '路线ID',
  `location_name` VARCHAR(200) NOT NULL COMMENT '地点名称',
  `province` VARCHAR(100) DEFAULT NULL COMMENT '省份',
  `city` VARCHAR(100) DEFAULT NULL COMMENT '城市',
  `district` VARCHAR(100) DEFAULT NULL COMMENT '区县',
  `longitude` DECIMAL(10,8) DEFAULT NULL COMMENT '经度',
  `latitude` DECIMAL(10,8) DEFAULT NULL COMMENT '纬度',
  `arrival_year` INT DEFAULT NULL COMMENT '到达年份',
  `departure_year` INT DEFAULT NULL COMMENT '离开年份',
  `residence_duration` INT DEFAULT NULL COMMENT '居住时长(年)',
  `event_year` INT DEFAULT NULL COMMENT '事件年份',
  `event_description` TEXT DEFAULT NULL COMMENT '事件描述',
  `point_type` VARCHAR(50) DEFAULT NULL COMMENT '地点类型: origin, stop, destination',
  `display_order` INT NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `hierarchy_level` INT DEFAULT NULL COMMENT '层级(from V1 migration)',
  `hierarchy_path` VARCHAR(500) DEFAULT NULL COMMENT '路径(from V1 migration)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_route_id` (`route_id`),
  KEY `idx_longitude_latitude` (`longitude`, `latitude`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='迁徙地点表';

-- =====================================================
-- 11. 历史事件表 (historical_event)
-- =====================================================
CREATE TABLE IF NOT EXISTS `historical_event` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `family_id` BIGINT DEFAULT NULL COMMENT '家族ID',
  `person_id` BIGINT DEFAULT NULL COMMENT '关联人物ID',
  `event_name` VARCHAR(200) NOT NULL COMMENT '事件名称',
  `event_year` INT DEFAULT NULL COMMENT '事件年份',
  `event_type` VARCHAR(100) DEFAULT NULL COMMENT '事件类型',
  `description` TEXT DEFAULT NULL COMMENT '事件描述',
  `location` VARCHAR(200) DEFAULT NULL COMMENT '事件地点',
  `significance` VARCHAR(100) DEFAULT NULL COMMENT '意义/影响',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_family_id` (`family_id`),
  KEY `idx_person_id` (`person_id`),
  KEY `idx_event_year` (`event_year`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='历史事件表';

-- =====================================================
-- 12. 审批请求表 (approval_request)
-- =====================================================
CREATE TABLE IF NOT EXISTS `approval_request` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `request_type` VARCHAR(100) NOT NULL COMMENT '请求类型: person_add, person_update, celebrity_add等',
  `entity_type` VARCHAR(100) NOT NULL COMMENT '实体类型: person, celebrity, migration等',
  `entity_id` BIGINT DEFAULT NULL COMMENT '关联实体ID',
  `applicant_id` BIGINT NOT NULL COMMENT '申请人ID',
  `approver_id` BIGINT DEFAULT NULL COMMENT '审批人ID',
  `status` VARCHAR(50) NOT NULL DEFAULT 'pending' COMMENT '状态: pending=待审批, approved=已批准, rejected=已拒绝',
  `request_data` JSON DEFAULT NULL COMMENT '申请数据(JSON)',
  `approval_comment` TEXT DEFAULT NULL COMMENT '审批意见',
  `approved_at` DATETIME DEFAULT NULL COMMENT '审批时间',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_applicant_id` (`applicant_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批请求表';

-- =====================================================
-- 13. 人物生物信息表 (biography)
-- =====================================================
CREATE TABLE IF NOT EXISTS `biography` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `person_id` BIGINT NOT NULL COMMENT '人物ID',
  `chapter_title` VARCHAR(200) DEFAULT NULL COMMENT '章节标题',
  `chapter_order` INT NOT NULL DEFAULT 0 COMMENT '章节顺序',
  `content` TEXT DEFAULT NULL COMMENT '内容',
  `source` VARCHAR(500) DEFAULT NULL COMMENT '史料来源',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_person_id` (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人物生物信息表';

-- =====================================================
-- 初始化数据
-- =====================================================

-- 插入超级管理员角色
INSERT INTO `sys_role` (`role_name`, `role_key`, `description`, `status`, `deleted`) VALUES
('超级管理员', 'SUPER_ADMIN', '系统超级管理员，拥有所有权限', 1, 0),
('普通用户', 'USER', '普通用户', 1, 0);

-- 插入默认用户 (密码: admin123)
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `email`, `status`, `deleted`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '系统管理员', 'admin@clanhistory.com', 1, 0);

-- 关联管理员和角色
INSERT INTO `sys_user_role` (`user_id`, `role_id`, `deleted`) VALUES
(1, 1, 0);

-- 插入默认家族
INSERT INTO `family` (`family_name`, `family_code`, `description`, `region`, `founded_year`, `status`, `deleted`) VALUES
('测试家族', 'TEST_FAMILY', '用于测试的默认家族', '中国', 1900, 1, 0);

-- =====================================================
-- V1 Migration: 添加迁徙地点层级字段
-- =====================================================
-- 此脚本已在 migration/V1__add_hierarchy_fields_to_migration_point.sql 中定义
