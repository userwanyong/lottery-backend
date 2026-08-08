-- ============================================================
-- 营销抽奖系统 - 轻量版单库建表脚本
-- 合并 marketing + marketing_01/02 所有表为单库 marketing_lite（避免与老版本 marketing 库冲突）：
--   1. 全局表（原 marketing 库）原样保留，含业务初始化数据
--   2. 分库分表（原 marketing_01/02 的 _000~_003）合并为单表（去后缀）
--   3. 只分库表（activity_account 系列、credit_account、task）合并为单表
--   4. 新增 sys_config 动态配置表（替代 Zookeeper DCC）
-- 用法：新建空库后执行本脚本即可，无需分库分表
-- ============================================================

CREATE DATABASE IF NOT EXISTS `marketing_lite` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `marketing_lite`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 全局表（原 marketing 库）
-- ----------------------------

DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity`
(
    `id`              bigint unsigned NOT NULL COMMENT '雪花ID',
    `strategy_id`     bigint          NOT NULL COMMENT '抽奖策略ID',
    `activity_name`   varchar(64)     NOT NULL COMMENT '活动名称',
    `activity_desc`   varchar(128)    NOT NULL COMMENT '活动描述',
    `begin_date_time` datetime        NOT NULL COMMENT '开始时间',
    `end_date_time`   datetime        NOT NULL COMMENT '结束时间',
    `state`           varchar(8)      NOT NULL DEFAULT 'create' COMMENT '活动状态 create-创建 open-开启 close-关闭',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_begin_date_time` (`begin_date_time`),
    KEY `idx_end_date_time` (`end_date_time`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='活动表';

INSERT INTO `activity` VALUES (1989345533881966594, 1989339401985679361, 'ai-agent项目', 'ai-agent项目', '2025-11-14 22:51:43', '2027-11-14 00:00:00', 'open', '2025-11-14 22:51:49', '2025-11-14 22:51:49');

DROP TABLE IF EXISTS `activity_count`;
CREATE TABLE `activity_count`
(
    `id`          bigint unsigned NOT NULL COMMENT '雪花ID',
    `total_count` int             NOT NULL COMMENT '总次数',
    `day_count`   int             NOT NULL COMMENT '日次数',
    `month_count` int             NOT NULL COMMENT '月次数',
    `create_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='活动次数配置表';

INSERT INTO `activity_count` VALUES
(1934610051247448065, 10, 10, 10, '2025-06-16 21:52:34', '2025-06-16 21:52:34'),
(1947276092532346881, 20, 20, 20, '2025-07-21 20:42:54', '2025-07-21 20:42:54'),
(1947297667382018050, 100, 100, 100, '2025-07-21 22:08:37', '2025-07-21 22:08:37'),
(1989346519992197121, 1, 1, 1, '2025-11-14 22:55:45', '2025-11-14 22:55:45');

DROP TABLE IF EXISTS `activity_sku`;
CREATE TABLE `activity_sku`
(
    `id`                  bigint unsigned NOT NULL COMMENT '雪花ID',
    `activity_id`         bigint          NOT NULL COMMENT '活动ID',
    `activity_count_id`   bigint          NOT NULL COMMENT '活动个人参与次数ID',
    `stock_count`         int             NOT NULL COMMENT '商品库存',
    `stock_count_surplus` int             NOT NULL COMMENT '剩余库存',
    `product_amount`      decimal(10, 2)  NOT NULL COMMENT '兑换所需积分',
    `create_time`         datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_activity_id_activity_count_id` (`activity_id`, `activity_count_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='活动sku表';

INSERT INTO `activity_sku` VALUES
(1989346292388290562, 1989345533881966594, 1934610051247448065, 1000000, 999998, 100.00, '2025-11-14 22:54:50', '2025-11-22 15:15:15'),
(1989346736015630338, 1989345533881966594, 1989346519992197121, 1000000, 999985, 15.00, '2025-11-14 22:56:36', '2025-11-23 18:30:30'),
(1989347216963887106, 1989345533881966594, 1947276092532346881, 1000000, 999999, 175.00, '2025-11-14 22:58:31', '2025-11-22 15:15:15'),
(1989347310463311874, 1989345533881966594, 1947297667382018050, 1000000, 1000000, 666.66, '2025-11-14 22:58:53', '2025-11-14 22:59:27');

DROP TABLE IF EXISTS `award`;
CREATE TABLE `award`
(
    `id`           bigint unsigned NOT NULL COMMENT '雪花ID',
    `award_key`    varchar(32)     NOT NULL COMMENT '奖品对接标识（每一个都是一个对应的发奖策略）',
    `award_config` varchar(32)     NOT NULL COMMENT '奖品配置信息',
    `award_desc`   varchar(128)    NOT NULL COMMENT '奖品内容描述',
    `image`        varchar(500)    NOT NULL COMMENT '图片',
    `create_time`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='奖品表';

INSERT INTO `award` VALUES
(1934859819538202696, 'user_credit_blacklist', '1', '黑名单积分(可通用)', 'http://115.190.198.152:9001/xybjz/fba43c6c-b432-4145-89df-317b9d9f6059.png', '2025-02-17 13:16:03', '2025-11-22 15:09:07'),
(1989336435136356353, 'user_ai', '1', 'AI调用次数+1', 'http://115.190.198.152:9001/xybjz/b5266b4c-3db2-4f3b-a12c-5a6a792d99d5.png', '2025-11-14 22:15:40', '2025-11-22 15:08:58'),
(1989336702498070529, 'user_credit_random', '10,20', '随机积分(可通用)', 'http://115.190.198.152:9001/xybjz/c2c590a3-80df-4373-be44-d7296e8c6192.png', '2025-11-14 22:16:44', '2025-11-22 15:09:15'),
(1989336788581965826, 'user_ai', '5', 'AI调用次数+5', 'http://115.190.198.152:9001/xybjz/b6db5d8c-8490-4709-b8a5-b8e5c9d0056f.png', '2025-11-14 22:17:04', '2025-11-22 15:09:21'),
(1989336928877240321, 'user_cj', '1', '抽奖次数+1', 'http://115.190.198.152:9001/xybjz/610ed4a9-c73b-4d2e-9fec-4307189e97cd.png', '2025-11-14 22:17:38', '2025-11-22 15:09:28'),
(1989336991691137026, 'user_cj', '5', '抽奖次数+5', 'http://115.190.198.152:9001/xybjz/2d9d19f9-ffb4-49bc-9937-4daa4a2ec919.png', '2025-11-14 22:17:53', '2025-11-22 15:09:33'),
(1989337068971188225, 'user_thanks', '1', '谢谢参与(可通用)', 'http://115.190.198.152:9001/xybjz/b54c13c4-e71f-4477-b9e9-3e2d47d9d2f0.png', '2025-11-14 22:18:11', '2025-11-22 15:09:39'),
(1989337375050522625, 'vip', 'z', 'VIP周卡', 'http://115.190.198.152:9001/xybjz/09242948-cecc-4526-b045-4330d780e5f9.png', '2025-11-14 22:19:24', '2025-11-23 18:39:28'),
(1989337465119006721, 'vip', 'y', 'VIP月卡', 'http://115.190.198.152:9001/xybjz/c63eaf86-0f40-4f2d-bc75-c8116ee3a6bc.png', '2025-11-14 22:19:46', '2025-11-23 18:39:22');

DROP TABLE IF EXISTS `behavior_rebate`;
CREATE TABLE `behavior_rebate`
(
    `id`            bigint unsigned NOT NULL COMMENT '雪花ID',
    `activity_id`   bigint          NOT NULL COMMENT '活动ID',
    `behavior_type` varchar(16)     NOT NULL COMMENT '行为类型（sign 签到、openai_pay 支付、activity_gift 活动赠送）',
    `rebate_desc`   varchar(128)    NOT NULL COMMENT '返利描述',
    `rebate_type`   varchar(16)     NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
    `rebate_config` varchar(32)     NOT NULL COMMENT '返利配置',
    `state`         varchar(12)     NOT NULL COMMENT '状态（open 开启、close 关闭）',
    `create_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_behavior_type` (`behavior_type`),
    KEY `idx_activity_id_behavior_type` (`activity_id`, `behavior_type`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='返利活动配置表';

INSERT INTO `behavior_rebate` VALUES
(1989346009608314882, 1989345533881966594, 'activity_gift', '活动赠送1次抽奖次数', 'gift', '1', 'open', '2025-11-14 22:53:43', '2025-11-14 22:53:43'),
(1989346867989405698, 1989345533881966594, 'sign', '签到返抽奖次数', 'sku', '1989346736015630338', 'open', '2025-11-14 22:57:08', '2025-11-14 22:57:08'),
(1989347020871786497, 1989345533881966594, 'sign', '签到返积分', 'integral', '5', 'open', '2025-11-14 22:57:44', '2025-11-14 22:57:44');

DROP TABLE IF EXISTS `rule`;
CREATE TABLE `rule`
(
    `id`          bigint unsigned NOT NULL COMMENT '雪花ID',
    `rule_model`  varchar(60)     NOT NULL COMMENT '规则模型',
    `rule_value`  varchar(256)    NOT NULL COMMENT '规则比值',
    `rule_desc`   varchar(512)    NOT NULL COMMENT '规则描述',
    `create_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `rule_pk` (`rule_model`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='策略规则表';

INSERT INTO `rule` VALUES
(1989338390122422274, 'ai-agent-rule_weight', '4000:1989337375050522625 5000:1989337465119006721,1989337375050522625', '到达4000幸运值，必中奖品1989337375050522625\n到达5000幸运值，必中奖品1989337375050522625或1989337465119006721', '2025-11-14 22:23:26', '2025-11-14 22:51:19'),
(1989339056215646209, 'ai-agent-rule_blacklist', '1947133372497252353:user01,user02', '1947133372497252353:user01,user02', '2025-11-14 22:26:05', '2025-11-14 22:26:05');

DROP TABLE IF EXISTS `rule_tree`;
CREATE TABLE `rule_tree`
(
    `id`                 bigint unsigned NOT NULL COMMENT '雪花ID',
    `tree_name`          varchar(64)     NOT NULL COMMENT '规则树名称',
    `tree_desc`          varchar(128)             DEFAULT NULL COMMENT '规则树描述',
    `tree_node_rule_key` varchar(32)     NOT NULL COMMENT '规则树根入口规则',
    `create_time`        datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`        datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='规则树表';

INSERT INTO `rule_tree` VALUES
(1947994741845037057, '默认规则', '默认规则，无次数规则', 'rule_stock', '2025-07-23 20:18:33', '2025-07-23 20:33:46'),
(1989339772170756097, 'ai-agent_tree无规则', 'ai-agent_tree无规则', 'rule_stock', '2025-11-14 22:28:56', '2025-11-14 22:28:56'),
(1989339871722561537, 'ai-agent_tree抽奖2次后解锁', 'ai-agent_tree抽奖2次后解锁', 'rule_lock', '2025-11-14 22:29:20', '2025-11-14 22:29:20'),
(1989339936365174786, 'ai-agent_tree抽奖3次后解锁', 'ai-agent_tree抽奖3次后解锁', 'rule_lock', '2025-11-14 22:29:35', '2025-11-14 22:29:35'),
(1989339970464866305, 'ai-agent_tree抽奖5次后解锁', 'ai-agent_tree抽奖5次后解锁', 'rule_lock', '2025-11-14 22:29:43', '2025-11-14 22:29:43');

DROP TABLE IF EXISTS `rule_tree_node`;
CREATE TABLE `rule_tree_node`
(
    `id`           bigint unsigned NOT NULL COMMENT '雪花ID',
    `rule_tree_id` bigint          NOT NULL COMMENT '规则树ID',
    `rule_name`    varchar(32)     NOT NULL COMMENT '规则名',
    `rule_desc`    varchar(64)     NOT NULL COMMENT '规则描述',
    `rule_value`   varchar(128)             DEFAULT NULL COMMENT '规则的值',
    `create_time`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_rule_tree_id` (`rule_tree_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='规则节点表';

INSERT INTO `rule_tree_node` VALUES
(1947995190753005569, 1947994741845037057, 'rule_stock', '默认节点', NULL, '2025-07-23 20:20:21', '2025-07-23 20:20:21'),
(1947995290489360385, 1947994741845037057, 'rule_luck_award', '默认节点', NULL, '2025-07-23 20:20:43', '2025-07-23 20:20:43'),
(1989340434652684290, 1989339970464866305, 'rule_lock', '抽奖5次解锁', '5', '2025-11-14 22:31:34', '2025-11-14 22:31:34'),
(1989340505272180738, 1989339936365174786, 'rule_lock', '抽奖3次解锁', '3', '2025-11-14 22:31:51', '2025-11-14 22:31:51'),
(1989340554844659714, 1989339871722561537, 'rule_lock', '抽奖2次解锁', '2', '2025-11-14 22:32:02', '2025-11-14 22:32:02'),
(1989341633128914945, 1989339970464866305, 'rule_stock', '库存扣减', NULL, '2025-11-14 22:36:19', '2025-11-14 22:36:19'),
(1989341715043672065, 1989339970464866305, 'rule_luck_award', '幸运奖', NULL, '2025-11-14 22:36:39', '2025-11-14 22:36:39'),
(1989341818257104898, 1989339936365174786, 'rule_stock', '库存扣减', NULL, '2025-11-14 22:37:04', '2025-11-14 22:37:04'),
(1989341855586410497, 1989339936365174786, 'rule_luck_award', '幸运奖', NULL, '2025-11-14 22:37:13', '2025-11-14 22:37:13'),
(1989341899018428418, 1989339871722561537, 'rule_stock', '库存扣减', NULL, '2025-11-14 22:37:23', '2025-11-14 22:37:23'),
(1989341949245218817, 1989339871722561537, 'rule_luck_award', '幸运奖', NULL, '2025-11-14 22:37:35', '2025-11-14 22:37:35'),
(1990305628043874306, 1989339772170756097, 'rule_stock', 'ai-agent_tree无规则节点', NULL, '2025-11-17 14:26:52', '2025-11-17 14:26:52'),
(1990305705537835010, 1989339772170756097, 'rule_luck_award', 'ai-agent_tree无规则节点', NULL, '2025-11-17 14:27:10', '2025-11-17 14:27:10');

DROP TABLE IF EXISTS `rule_tree_node_line`;
CREATE TABLE `rule_tree_node_line`
(
    `id`               bigint unsigned NOT NULL COMMENT '雪花ID',
    `rule_tree_id`     bigint          NOT NULL COMMENT '规则树ID',
    `rule_node_from`   varchar(32)     NOT NULL COMMENT 'From',
    `rule_node_to`     varchar(32)     NOT NULL COMMENT 'To',
    `rule_limit_type`  varchar(8)      NOT NULL COMMENT '限定类型；1:=;2:>;3:<;4:>=;5<=;6:enum[枚举范围];',
    `rule_limit_value` varchar(32)     NOT NULL COMMENT '限定值（到下个节点）',
    `create_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_rule_tree_id` (`rule_tree_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='规则节点走向表';

INSERT INTO `rule_tree_node_line` VALUES
(1947995350061060097, 1947994741845037057, 'rule_stock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER', '2025-07-23 20:20:59', '2025-07-23 20:20:59'),
(1989342193643118594, 1989339970464866305, 'rule_lock', 'rule_stock', 'EQUAL', 'ALLOW', '2025-11-14 22:38:33', '2025-11-14 22:38:33'),
(1989342296189657089, 1989339970464866305, 'rule_stock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER', '2025-11-14 22:38:58', '2025-11-14 22:38:58'),
(1989342371628408833, 1989339970464866305, 'rule_lock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER', '2025-11-14 22:39:16', '2025-11-14 22:39:16'),
(1989342412799696897, 1989339936365174786, 'rule_lock', 'rule_stock', 'EQUAL', 'ALLOW', '2025-11-14 22:39:25', '2025-11-14 22:39:25'),
(1989342463370420225, 1989339936365174786, 'rule_stock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER', '2025-11-14 22:39:37', '2025-11-14 22:39:37'),
(1989342545830436865, 1989339936365174786, 'rule_lock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER', '2025-11-14 22:39:57', '2025-11-14 22:39:57'),
(1989342723039780865, 1989339871722561537, 'rule_lock', 'rule_stock', 'EQUAL', 'ALLOW', '2025-11-14 22:40:40', '2025-11-14 22:40:40'),
(1989342789632745474, 1989339871722561537, 'rule_stock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER', '2025-11-14 22:40:55', '2025-11-14 22:40:55'),
(1989342830640455681, 1989339871722561537, 'rule_lock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER', '2025-11-14 22:41:05', '2025-11-14 22:41:05'),
(1990305804993171457, 1989339772170756097, 'rule_stock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER', '2025-11-17 14:27:34', '2025-11-17 14:27:34');

DROP TABLE IF EXISTS `strategy`;
CREATE TABLE `strategy`
(
    `id`            bigint unsigned NOT NULL COMMENT '雪花ID',
    `strategy_desc` varchar(128)    NOT NULL COMMENT '抽奖策略描述',
    `rule_models`   varchar(256)             DEFAULT NULL COMMENT '规则模型',
    `create_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='策略表';

INSERT INTO `strategy` VALUES
(1947996138992865281, '默认策略', '', '2025-07-23 20:24:05', '2025-07-24 14:54:26'),
(1989339401985679361, 'ai-agent项目抽奖策略', 'ai-agent-rule_blacklist,ai-agent-rule_weight', '2025-11-14 22:27:28', '2025-11-14 22:27:28');

DROP TABLE IF EXISTS `activity_award`;
CREATE TABLE `activity_award`
(
    `id`                  bigint unsigned NOT NULL COMMENT '雪花ID',
    `activity_id`         bigint          NOT NULL COMMENT '活动ID',
    `award_id`            bigint          NOT NULL COMMENT '奖品ID',
    `award_title`         varchar(128)    NOT NULL COMMENT '奖品标题',
    `award_subtitle`      varchar(128)             DEFAULT NULL COMMENT '奖品副标题',
    `award_count`         int             NOT NULL DEFAULT '0' COMMENT '奖品库存总量',
    `award_count_surplus` int             NOT NULL DEFAULT '0' COMMENT '奖品库存剩余',
    `award_rate`          decimal(6, 4)   NOT NULL COMMENT '奖品中奖概率',
    `rule_tree_id`        bigint          NOT NULL COMMENT '奖品规则树ID',
    `sort`                int             NOT NULL DEFAULT '0' COMMENT '排序',
    `create_time`         datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_activity_id_award_id` (`activity_id`, `award_id`),
    KEY `idx_activity_id_sort` (`activity_id`, `sort`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='活动奖品表';

INSERT INTO `activity_award` VALUES
(1989343319025213441, 1989345533881966594, 1989336435136356353, 'AI调用次数+1', '', 1000000, 999281, 0.2000, 1989339772170756097, 2, '2025-11-14 22:43:01', '2025-11-23 18:34:19'),
(1989343506909061121, 1989345533881966594, 1989337068971188225, '谢谢参与', NULL, 1000000, 998932, 0.3000, 1989339772170756097, 1, '2025-11-14 22:43:46', '2025-11-23 18:34:24'),
(1989343674081435649, 1989345533881966594, 1989336928877240321, '抽奖次数+1', '', 1000000, 999500, 0.1500, 1989339772170756097, 3, '2025-11-14 22:44:26', '2025-11-21 23:33:21'),
(1989343816616468481, 1989345533881966594, 1989336702498070529, '随机积分', '', 1000000, 999676, 0.1000, 1989339772170756097, 4, '2025-11-14 22:45:00', '2025-11-22 15:14:40'),
(1989344008988221441, 1989345533881966594, 1989336991691137026, '抽奖次数+5', NULL, 1000000, 999626, 0.1000, 1989339772170756097, 5, '2025-11-14 22:45:46', '2025-11-21 23:33:07'),
(1989344181713854466, 1989345533881966594, 1989336788581965826, 'AI调用次数+5', ' ', 100000, 99583, 0.1200, 1989339871722561537, 6, '2025-11-14 22:46:27', '2025-11-21 23:32:16'),
(1989344419094683650, 1989345533881966594, 1989337375050522625, 'VIP周卡', '抽奖 3 次后解锁', 100000, 99919, 0.0200, 1989339936365174786, 7, '2025-11-14 22:47:24', '2025-11-21 21:28:42'),
(1989344519481155586, 1989345533881966594, 1989337465119006721, 'VIP月卡', '抽奖 5 次后解锁', 100000, 99965, 0.0100, 1989339970464866305, 8, '2025-11-14 22:47:48', '2025-11-21 19:07:52');


-- ----------------------------
-- 原 marketing_01/02 分库表，合并为单表（去 _000~_003 后缀）
-- ----------------------------

DROP TABLE IF EXISTS `activity_account`;
CREATE TABLE `activity_account`
(
    `id`                  bigint unsigned NOT NULL COMMENT '雪花ID',
    `user_id`             varchar(32)     NOT NULL COMMENT '用户ID',
    `activity_id`         bigint          NOT NULL COMMENT '活动ID',
    `total_count`         int             NOT NULL COMMENT '总次数',
    `total_count_surplus` int             NOT NULL COMMENT '总次数-剩余',
    `day_count`           int             NOT NULL COMMENT '日次数',
    `day_count_surplus`   int             NOT NULL COMMENT '日次数-剩余',
    `month_count`         int             NOT NULL COMMENT '月次数',
    `month_count_surplus` int             NOT NULL COMMENT '月次数-剩余',
    `create_time`         datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_user_id_activity_id` (`user_id`, `activity_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='抽奖账户表';

DROP TABLE IF EXISTS `activity_account_day`;
CREATE TABLE `activity_account_day`
(
    `id`                bigint unsigned NOT NULL COMMENT '雪花ID',
    `user_id`           varchar(32)     NOT NULL COMMENT '用户ID',
    `activity_id`       bigint          NOT NULL COMMENT '活动ID',
    `day`               varchar(10)     NOT NULL COMMENT '日期（yyyy-mm-dd）',
    `day_count`         int             NOT NULL COMMENT '日次数',
    `day_count_surplus` int             NOT NULL COMMENT '日次数-剩余',
    `create_time`       datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_user_id_activity_id_day` (`user_id`, `activity_id`, `day`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='抽奖账户表-日次数';

DROP TABLE IF EXISTS `activity_account_month`;
CREATE TABLE `activity_account_month`
(
    `id`                  bigint unsigned NOT NULL COMMENT '雪花ID',
    `user_id`             varchar(32)     NOT NULL COMMENT '用户ID',
    `activity_id`         bigint          NOT NULL COMMENT '活动ID',
    `month`               varchar(7)      NOT NULL COMMENT '月（yyyy-mm）',
    `month_count`         int             NOT NULL COMMENT '月次数',
    `month_count_surplus` int             NOT NULL COMMENT '月次数-剩余',
    `create_time`         datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_user_id_activity_id_month` (`user_id`, `activity_id`, `month`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='抽奖账户表-月次数';

DROP TABLE IF EXISTS `activity_record`;
CREATE TABLE `activity_record`
(
    `id`              bigint unsigned NOT NULL COMMENT '雪花ID',
    `user_id`         varchar(32)     NOT NULL COMMENT '用户ID',
    `activity_id`     bigint          NOT NULL COMMENT '活动ID',
    `sku`             bigint                   DEFAULT NULL COMMENT '商品sku',
    `activity_name`   varchar(64)              DEFAULT NULL COMMENT '活动名称',
    `total_count`     int             NOT NULL COMMENT '总次数',
    `day_count`       int             NOT NULL COMMENT '日次数',
    `month_count`     int             NOT NULL COMMENT '月次数',
    `pay_amount`      decimal(10, 2)           DEFAULT NULL COMMENT '支付积分',
    `state`           varchar(10)     NOT NULL DEFAULT 'complete' COMMENT '订单状态（complete-完成 wait_pay-等待支付）',
    `out_business_no` varchar(150)    NOT NULL COMMENT '保证幂等，不会重复消费',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`, `state`),
    KEY `idx_user_id_activity_id_sku_state` (`user_id`, `activity_id`, `sku`, `state`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户抽奖额度记录表';

DROP TABLE IF EXISTS `credit_account`;
CREATE TABLE `credit_account`
(
    `id`               bigint unsigned NOT NULL COMMENT '雪花ID',
    `activity_id`      bigint          NOT NULL COMMENT '活动ID',
    `user_id`          varchar(32)     NOT NULL COMMENT '用户ID',
    `total_amount`     decimal(10, 2)  NOT NULL COMMENT '总积分',
    `available_amount` decimal(10, 2)  NOT NULL COMMENT '可用积分',
    `account_status`   varchar(8)      NOT NULL COMMENT '账户状态【open - 可用，close - 冻结】',
    `create_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='积分账户表';

DROP TABLE IF EXISTS `credit_record`;
CREATE TABLE `credit_record`
(
    `id`              bigint unsigned NOT NULL COMMENT '雪花ID',
    `user_id`         varchar(32)     NOT NULL COMMENT '用户ID',
    `activity_id`     bigint          NOT NULL COMMENT '活动ID',
    `trade_name`      varchar(32)     NOT NULL COMMENT '交易名称',
    `trade_type`      varchar(8)      NOT NULL DEFAULT 'forward' COMMENT '交易类型；forward-正向、reverse-逆向',
    `trade_amount`    decimal(10, 2)  NOT NULL COMMENT '交易金额',
    `out_business_no` varchar(150)    NOT NULL COMMENT '业务仿重ID',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户积分记录表';

DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`
(
    `id`          int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `topic`       varchar(32)  NOT NULL COMMENT '消息主题',
    `activity_id` bigint       NOT NULL COMMENT '活动ID',
    `user_id`     varchar(32)  NOT NULL COMMENT '用户ID',
    `message_id`  varchar(16)  NOT NULL COMMENT '消息编号',
    `message`     varchar(512) NOT NULL COMMENT '消息主体',
    `state`       varchar(16)  NOT NULL DEFAULT 'create' COMMENT '任务状态；create-创建、completed-完成、fail-失败',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_message_id` (`message_id`),
    KEY `idx_state` (`state`),
    KEY `idx_create_time` (`update_time`),
    KEY `idx_state_update_time` (`state`, `update_time`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='任务表，消息补偿';

DROP TABLE IF EXISTS `user_award_record`;
CREATE TABLE `user_award_record`
(
    `id`            bigint unsigned NOT NULL COMMENT '雪花ID',
    `user_id`       varchar(32)     NOT NULL COMMENT '用户ID',
    `activity_id`   bigint          NOT NULL COMMENT '活动ID',
    `strategy_id`   bigint          NOT NULL COMMENT '抽奖策略ID',
    `user_order_id` bigint          NOT NULL COMMENT '抽奖单ID【作为幂等使用】',
    `award_id`      bigint          NOT NULL COMMENT '奖品ID',
    `award_title`   varchar(128)    NOT NULL COMMENT '奖品标题（名称）',
    `award_time`    datetime        NOT NULL COMMENT '中奖时间',
    `award_state`   varchar(16)     NOT NULL DEFAULT 'create' COMMENT '奖品状态；create-创建、complete-发奖完成、fail-发奖失败',
    `create_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`user_order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_award_id` (`strategy_id`),
    KEY `idx_user_id_activity_id_award_time` (`user_id`, `activity_id`, `award_time`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户中奖记录表';

DROP TABLE IF EXISTS `user_behavior_rebate_order`;
CREATE TABLE `user_behavior_rebate_order`
(
    `id`              bigint unsigned NOT NULL COMMENT '雪花ID',
    `activity_id`     bigint          NOT NULL COMMENT '活动ID',
    `user_id`         varchar(32)     NOT NULL COMMENT '用户ID',
    `behavior_type`   varchar(16)     NOT NULL COMMENT '行为类型（sign 签到、openai_pay 支付）',
    `rebate_desc`     varchar(128)    NOT NULL COMMENT '返利描述',
    `rebate_type`     varchar(16)     NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
    `rebate_config`   varchar(32)     NOT NULL COMMENT '返利配置【sku值，积分值】',
    `out_business_no` varchar(100)    NOT NULL COMMENT '业务仿重ID',
    `biz_id`          varchar(150)    NOT NULL COMMENT '业务ID - 拼接的唯一值',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_biz_id` (`biz_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_user_id_out_business_no` (`user_id`, `out_business_no`),
    KEY `idx_user_id_activity_id_out_business_no` (`user_id`, `activity_id`, `out_business_no`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='返利订单表';

DROP TABLE IF EXISTS `user_order`;
CREATE TABLE `user_order`
(
    `id`            bigint unsigned NOT NULL COMMENT '雪花ID',
    `user_id`       varchar(32)     NOT NULL COMMENT '用户ID',
    `activity_id`   bigint          NOT NULL COMMENT '活动ID',
    `activity_name` varchar(64)     NOT NULL COMMENT '活动名称',
    `strategy_id`   bigint          NOT NULL COMMENT '抽奖策略ID',
    `order_state`   varchar(16)     NOT NULL DEFAULT 'create' COMMENT '订单状态；create-创建、used-已使用、cancel-已作废',
    `create_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='抽奖订单表';


-- ----------------------------
-- 动态配置表（新增，替代 Zookeeper DCC 配置中心）
-- ----------------------------

DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`
(
    `config_key`   varchar(64)  NOT NULL COMMENT '配置键',
    `config_value` varchar(256)          DEFAULT NULL COMMENT '配置值',
    `remark`       varchar(256)          DEFAULT NULL COMMENT '备注',
    `update_time`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`config_key`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='系统动态配置表（替代 Zookeeper DCC）';

-- 预置两个动态开关（与 @DCCValue 默认值一致）
INSERT INTO `sys_config` (`config_key`, `config_value`, `remark`) VALUES
('degradeSwitch', 'close', '抽奖降级开关：close-不降级(默认)，open-降级'),
('rateLimiterSwitch', 'open', '限流总开关：open-开启限流(默认)，close-关闭限流');

SET FOREIGN_KEY_CHECKS = 1;
