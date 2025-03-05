CREATE database if NOT EXISTS `marketing_02` default character set utf8mb4 collate utf8mb4_0900_ai_ci;
use `marketing_02`;

DROP TABLE IF EXISTS `activity_account`;
CREATE TABLE `activity_account`
(
    `id`                  bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`             varchar(32)         NOT NULL COMMENT '用户ID',
    `activity_id`         bigint(12)          NOT NULL COMMENT '活动ID',
    `total_count`         int(8)              NOT NULL COMMENT '总次数',
    `total_count_surplus` int(8)              NOT NULL COMMENT '总次数-剩余',
    `day_count`           int(8)              NOT NULL COMMENT '日次数',
    `day_count_surplus`   int(8)              NOT NULL COMMENT '日次数-剩余',
    `month_count`         int(8)              NOT NULL COMMENT '月次数',
    `month_count_surplus` int(8)              NOT NULL COMMENT '月次数-剩余',
    `create_time`         datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_user_id_activity_id` (`user_id`, `activity_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='抽奖活动账户表';



DROP TABLE IF EXISTS `activity_order_000`;
CREATE TABLE `activity_order_000`
(
    `id`            bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`       varchar(32)         NOT NULL COMMENT '用户ID',
    `activity_id`   bigint(12)          NOT NULL COMMENT '活动ID',
    `activity_name` varchar(64)         NOT NULL COMMENT '活动名称',
    `strategy_id`   bigint(8)           NOT NULL COMMENT '抽奖策略ID',
    `order_id`      varchar(12)         NOT NULL COMMENT '订单ID',
    `order_time`    datetime            NOT NULL COMMENT '下单时间',
    `state`         tinyint(1)          NOT NULL COMMENT '订单状态（0-not_used、1-used、2-expire）',
    `create_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`, `state`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='抽奖活动单';

DROP TABLE IF EXISTS `activity_order_001`;
CREATE TABLE `activity_order_001`
(
    `id`            bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`       varchar(32)         NOT NULL COMMENT '用户ID',
    `activity_id`   bigint(12)          NOT NULL COMMENT '活动ID',
    `activity_name` varchar(64)         NOT NULL COMMENT '活动名称',
    `strategy_id`   bigint(8)           NOT NULL COMMENT '抽奖策略ID',
    `order_id`      varchar(12)         NOT NULL COMMENT '订单ID',
    `order_time`    datetime            NOT NULL COMMENT '下单时间',
    `state`         tinyint(1)          NOT NULL COMMENT '订单状态（0-not_used、1-used、2-expire）',
    `create_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`, `state`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='抽奖活动单';


DROP TABLE IF EXISTS `activity_order_002`;
CREATE TABLE `activity_order_002`
(
    `id`            bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`       varchar(32)         NOT NULL COMMENT '用户ID',
    `activity_id`   bigint(12)          NOT NULL COMMENT '活动ID',
    `activity_name` varchar(64)         NOT NULL COMMENT '活动名称',
    `strategy_id`   bigint(8)           NOT NULL COMMENT '抽奖策略ID',
    `order_id`      varchar(12)         NOT NULL COMMENT '订单ID',
    `order_time`    datetime            NOT NULL COMMENT '下单时间',
    `state`         tinyint(1)          NOT NULL COMMENT '订单状态（0-not_used、1-used、2-expire）',
    `create_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`, `state`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='抽奖活动单';

DROP TABLE IF EXISTS `activity_order_003`;
CREATE TABLE `activity_order_003`
(
    `id`            bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`       varchar(32)         NOT NULL COMMENT '用户ID',
    `activity_id`   bigint(12)          NOT NULL COMMENT '活动ID',
    `activity_name` varchar(64)         NOT NULL COMMENT '活动名称',
    `strategy_id`   bigint(8)           NOT NULL COMMENT '抽奖策略ID',
    `order_id`      varchar(12)         NOT NULL COMMENT '订单ID',
    `order_time`    datetime            NOT NULL COMMENT '下单时间',
    `state`         tinyint(1)          NOT NULL COMMENT '订单状态（0-not_used、1-used、2-expire）',
    `create_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`, `state`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='抽奖活动单';

DROP TABLE IF EXISTS `activity_account_flow_000`;
CREATE TABLE `activity_account_flow_000`
(
    `id`           int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`      varchar(32)      NOT NULL COMMENT '用户ID',
    `activity_id`  bigint(12)       NOT NULL COMMENT '活动ID',
    `total_count`  int(8)           NOT NULL COMMENT '总次数',
    `day_count`    int(8)           NOT NULL COMMENT '日次数',
    `month_count`  int(8)           NOT NULL COMMENT '月次数',
    `flow_id`      varchar(32)      NOT NULL COMMENT '流水ID - 生成的唯一ID',
    `flow_channel` tinyint(1)       NOT NULL DEFAULT '0' COMMENT '流水渠道（0-活动领取、1-购买、2-兑换、3-免费赠送）',
    `biz_id`       varchar(12)      NOT NULL COMMENT '业务ID（外部透传，活动ID、订单ID）',
    `create_time`  datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_flow_id` (`flow_id`),
    UNIQUE KEY `uq_biz_id` (`biz_id`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='抽奖活动账户流水表';

DROP TABLE IF EXISTS `activity_account_flow_001`;
CREATE TABLE `activity_account_flow_001`
(
    `id`           int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`      varchar(32)      NOT NULL COMMENT '用户ID',
    `activity_id`  bigint(12)       NOT NULL COMMENT '活动ID',
    `total_count`  int(8)           NOT NULL COMMENT '总次数',
    `day_count`    int(8)           NOT NULL COMMENT '日次数',
    `month_count`  int(8)           NOT NULL COMMENT '月次数',
    `flow_id`      varchar(32)      NOT NULL COMMENT '流水ID - 生成的唯一ID',
    `flow_channel` tinyint(1)       NOT NULL DEFAULT '0' COMMENT '流水渠道（0-活动领取、1-购买、2-兑换、3-免费赠送）',
    `biz_id`       varchar(12)      NOT NULL COMMENT '业务ID（外部透传，活动ID、订单ID）',
    `create_time`  datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_flow_id` (`flow_id`),
    UNIQUE KEY `uq_biz_id` (`biz_id`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='抽奖活动账户流水表';

DROP TABLE IF EXISTS `activity_account_flow_002`;
CREATE TABLE `activity_account_flow_002`
(
    `id`           int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`      varchar(32)      NOT NULL COMMENT '用户ID',
    `activity_id`  bigint(12)       NOT NULL COMMENT '活动ID',
    `total_count`  int(8)           NOT NULL COMMENT '总次数',
    `day_count`    int(8)           NOT NULL COMMENT '日次数',
    `month_count`  int(8)           NOT NULL COMMENT '月次数',
    `flow_id`      varchar(32)      NOT NULL COMMENT '流水ID - 生成的唯一ID',
    `flow_channel` tinyint(1)       NOT NULL DEFAULT '0' COMMENT '流水渠道（0-活动领取、1-购买、2-兑换、3-免费赠送）',
    `biz_id`       varchar(12)      NOT NULL COMMENT '业务ID（外部透传，活动ID、订单ID）',
    `create_time`  datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_flow_id` (`flow_id`),
    UNIQUE KEY `uq_biz_id` (`biz_id`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='抽奖活动账户流水表';

DROP TABLE IF EXISTS `activity_account_flow_003`;
CREATE TABLE `activity_account_flow_003`
(
    `id`           int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`      varchar(32)      NOT NULL COMMENT '用户ID',
    `activity_id`  bigint(12)       NOT NULL COMMENT '活动ID',
    `total_count`  int(8)           NOT NULL COMMENT '总次数',
    `day_count`    int(8)           NOT NULL COMMENT '日次数',
    `month_count`  int(8)           NOT NULL COMMENT '月次数',
    `flow_id`      varchar(32)      NOT NULL COMMENT '流水ID - 生成的唯一ID',
    `flow_channel` tinyint(1)       NOT NULL DEFAULT '0' COMMENT '流水渠道（0-活动领取、1-购买、2-兑换、3-免费赠送）',
    `biz_id`       varchar(12)      NOT NULL COMMENT '业务ID（外部透传，活动ID、订单ID）',
    `create_time`  datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_flow_id` (`flow_id`),
    UNIQUE KEY `uq_biz_id` (`biz_id`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='抽奖活动账户流水表';