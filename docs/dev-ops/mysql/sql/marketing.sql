-- MySQL dump 10.13  Distrib 8.0.35, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: marketing
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity`
(
    `id`              bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `activity_id`     bigint          NOT NULL COMMENT '活动ID',
    `activity_name`   varchar(64)     NOT NULL COMMENT '活动名称',
    `activity_desc`   varchar(128)    NOT NULL COMMENT '活动描述',
    `begin_date_time` datetime        NOT NULL COMMENT '开始时间',
    `end_date_time`   datetime        NOT NULL COMMENT '结束时间',
    `strategy_id`     bigint          NOT NULL COMMENT '抽奖策略ID',
    `state`           varchar(8)      NOT NULL DEFAULT 'create' COMMENT '活动状态 create-创建 open-开启 close-关闭',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_activity_id` (`activity_id`),
    KEY `idx_begin_date_time` (`begin_date_time`),
    KEY `idx_end_date_time` (`end_date_time`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='活动表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity`
    DISABLE KEYS */;
INSERT INTO `activity`
VALUES (2, 100301, '测试活动test', '测试活动test', '2025-03-05 22:17:16', '2029-06-05 22:17:18', 200001, 'open',
        '2025-03-05 22:17:20', '2025-06-08 16:22:51');
/*!40000 ALTER TABLE `activity`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_count`
--

DROP TABLE IF EXISTS `activity_count`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_count`
(
    `id`                bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `activity_count_id` bigint          NOT NULL COMMENT '活动次数id',
    `total_count`       int             NOT NULL COMMENT '总次数',
    `day_count`         int             NOT NULL COMMENT '日次数',
    `month_count`       int             NOT NULL COMMENT '月次数',
    `create_time`       datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_activity_count_id` (`activity_count_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='活动次数配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_count`
--

LOCK TABLES `activity_count` WRITE;
/*!40000 ALTER TABLE `activity_count`
    DISABLE KEYS */;
INSERT INTO `activity_count`
VALUES (1, 11101, 1, 1, 1, '2025-03-05 22:19:07', '2025-05-09 13:06:09'),
       (2, 11102, 5, 5, 5, '2025-05-07 23:34:04', '2025-05-09 13:06:09');
/*!40000 ALTER TABLE `activity_count`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_sku`
--

DROP TABLE IF EXISTS `activity_sku`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_sku`
(
    `id`                  int unsigned   NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `sku`                 bigint         NOT NULL COMMENT '商品sku - 把每一个组合当做一个商品',
    `activity_id`         bigint         NOT NULL COMMENT '活动ID',
    `activity_count_id`   bigint         NOT NULL COMMENT '活动个人参与次数ID',
    `stock_count`         int            NOT NULL COMMENT '商品库存',
    `stock_count_surplus` int            NOT NULL COMMENT '剩余库存',
    `product_amount`      decimal(10, 2) NOT NULL COMMENT '兑换所需积分',
    `create_time`         datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_sku` (`sku`),
    KEY `idx_activity_id_activity_count_id` (`activity_id`, `activity_count_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='活动sku表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_sku`
--

LOCK TABLES `activity_sku` WRITE;
/*!40000 ALTER TABLE `activity_sku`
    DISABLE KEYS */;
INSERT INTO `activity_sku`
VALUES (1, 9011, 100301, 11101, 20000, 199898, 1.99, '2025-03-11 22:12:56', '2025-06-09 13:21:51'),
       (2, 9012, 100301, 11102, 20000, 199549, 5.99, '2025-05-07 23:33:24', '2025-05-09 23:35:20');
/*!40000 ALTER TABLE `activity_sku`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `award`
--

DROP TABLE IF EXISTS `award`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `award`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `award_id`     bigint          NOT NULL COMMENT '抽奖奖品ID（内部流转使用）',
    `award_key`    varchar(32)     NOT NULL COMMENT '奖品对接标识（每一个都是一个对应的发奖策略）',
    `award_config` varchar(32)     NOT NULL COMMENT '奖品配置信息',
    `award_desc`   varchar(128)    NOT NULL COMMENT '奖品内容描述',
    `create_time`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='奖品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `award`
--

LOCK TABLES `award` WRITE;
/*!40000 ALTER TABLE `award`
    DISABLE KEYS */;
INSERT INTO `award`
VALUES (1, 101, 'user_credit_random', '1,100', '随机积分', '2025-02-17 13:16:03', '2025-05-07 23:38:03'),
       (2, 102, 'openai_use_count', '5', 'OpenAI 增加使用次数', '2025-02-17 13:16:03', '2025-02-17 13:16:03'),
       (3, 103, 'openai_use_count', '10', 'OpenAI 增加使用次数', '2025-02-17 13:16:03', '2025-02-17 13:16:03'),
       (4, 104, 'openai_use_count', '20', 'OpenAI 增加使用次数', '2025-02-17 13:16:03', '2025-02-17 13:16:03'),
       (5, 105, 'openai_model', 'gpt-4', 'OpenAI 增加模型', '2025-02-17 13:16:03', '2025-02-17 13:16:03'),
       (6, 106, 'openai_model', 'dall-e-2', 'OpenAI 增加模型', '2025-02-17 13:16:03', '2025-02-17 13:16:03'),
       (7, 107, 'openai_model', 'dall-e-3', 'OpenAI 增加模型', '2025-02-17 13:16:03', '2025-02-17 13:16:03'),
       (8, 108, 'openai_use_count', '100', 'OpenAI 增加使用次数', '2025-02-17 13:16:03', '2025-02-17 13:16:03'),
       (9, 109, 'openai_model', 'gpt-4,dall-e-2,dall-e-3', 'OpenAI 增加模型', '2025-02-17 13:16:03',
        '2025-02-17 13:16:03'),
       (10, 100, 'user_credit_blacklist', '1', '黑名单积分', '2025-02-17 13:16:03', '2025-02-17 13:16:03');
/*!40000 ALTER TABLE `award`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `behavior_rebate`
--

DROP TABLE IF EXISTS `behavior_rebate`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `behavior_rebate`
(
    `id`            int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `behavior_type` varchar(16)  NOT NULL COMMENT '行为类型（sign 签到、openai_pay 支付）',
    `rebate_desc`   varchar(128) NOT NULL COMMENT '返利描述',
    `rebate_type`   varchar(16)  NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
    `rebate_config` varchar(32)  NOT NULL COMMENT '返利配置',
    `state`         varchar(12)  NOT NULL COMMENT '状态（open 开启、close 关闭）',
    `create_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_behavior_type` (`behavior_type`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='返利活动配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `behavior_rebate`
--

LOCK TABLES `behavior_rebate` WRITE;
/*!40000 ALTER TABLE `behavior_rebate`
    DISABLE KEYS */;
INSERT INTO `behavior_rebate`
VALUES (1, 'sign', '签到返利-抽奖额度', 'sku', '9011', 'open', '2025-03-28 18:42:59', '2025-03-28 18:42:59'),
       (2, 'sign', '签到返利-积分', 'integral', '10', 'open', '2025-03-28 18:42:59', '2025-03-28 18:42:59');
/*!40000 ALTER TABLE `behavior_rebate`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rule`
--

DROP TABLE IF EXISTS `rule`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rule`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `strategy_id` bigint          NOT NULL COMMENT '抽奖策略ID',
    `award_id`    bigint                   DEFAULT NULL COMMENT '抽奖奖品ID（规则类型为策略，则不需要奖品ID）',
    `rule_type`   tinyint(1)      NOT NULL DEFAULT '0' COMMENT '规则类型（1-策略规则、2-奖品规则）',
    `rule_model`  varchar(16)     NOT NULL COMMENT '规则模型（rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品)）',
    `rule_value`  varchar(256)    NOT NULL COMMENT '规则比值',
    `rule_desc`   varchar(128)    NOT NULL COMMENT '规则描述',
    `create_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_strategy_id_award_id` (`strategy_id`, `award_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 18
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='规则表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule`
--

LOCK TABLES `rule` WRITE;
/*!40000 ALTER TABLE `rule`
    DISABLE KEYS */;
INSERT INTO `rule`
VALUES (1, 200001, 101, 2, 'rule_random', '1,1000', '随机积分策略', '2025-02-17 13:16:03', '2025-02-17 13:16:03'),
       (2, 200001, 107, 2, 'rule_lock', '1', '抽奖1次后解锁', '2025-02-17 13:16:03', '2025-02-17 13:16:03'),
       (3, 200001, 108, 2, 'rule_lock', '2', '抽奖2次后解锁', '2025-02-17 13:16:03', '2025-02-17 13:16:03'),
       (4, 200001, 109, 2, 'rule_lock', '6', '抽奖6次后解锁', '2025-02-17 13:16:03', '2025-02-17 13:16:03'),
       (5, 200001, 107, 2, 'rule_luck_award', '1,100', '兜底奖品100以内随机积分', '2025-02-17 13:16:03',
        '2025-02-17 13:16:03'),
       (6, 200001, 108, 2, 'rule_luck_award', '1,100', '兜底奖品100以内随机积分', '2025-02-17 13:16:03',
        '2025-02-17 13:16:03'),
       (7, 200001, 101, 2, 'rule_luck_award', '1,10', '兜底奖品10以内随机积分', '2025-02-17 13:16:03',
        '2025-02-17 13:16:03'),
       (8, 200001, 102, 2, 'rule_luck_award', '1,20', '兜底奖品20以内随机积分', '2025-02-17 13:16:03',
        '2025-02-17 13:16:03'),
       (9, 200001, 103, 2, 'rule_luck_award', '1,30', '兜底奖品30以内随机积分', '2025-02-17 13:16:03',
        '2025-02-17 13:16:03'),
       (10, 200001, 104, 2, 'rule_luck_award', '1,40', '兜底奖品40以内随机积分', '2025-02-17 13:16:03',
        '2025-02-17 13:16:03'),
       (11, 200001, 105, 2, 'rule_luck_award', '1,50', '兜底奖品50以内随机积分', '2025-02-17 13:16:03',
        '2025-02-17 13:16:03'),
       (12, 200001, 106, 2, 'rule_luck_award', '1,60', '兜底奖品60以内随机积分', '2025-02-17 13:16:03',
        '2025-02-17 13:16:03'),
       (13, 200001, NULL, 1, 'rule_weight',
        '4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108', '消耗6000分，必中奖范围',
        '2025-02-17 13:16:03', '2025-02-17 13:16:03'),
       (14, 200001, NULL, 1, 'rule_blacklist', '101:user001,user002,user003', '黑名单抽奖，积分兜底',
        '2025-02-17 13:16:03', '2025-02-17 13:16:03'),
       (15, 200001, 107, 2, 'rule_lock', '1', '抽奖1次后解锁', '2025-02-17 13:16:03', '2025-02-17 13:16:03'),
       (16, 200001, 108, 2, 'rule_lock', '2', '抽奖2次后解锁', '2025-02-17 13:16:03', '2025-02-17 13:16:03'),
       (17, 200001, 109, 2, 'rule_lock', '6', '抽奖6次后解锁', '2025-02-17 13:16:03', '2025-02-17 13:16:03');
/*!40000 ALTER TABLE `rule`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rule_tree`
--

DROP TABLE IF EXISTS `rule_tree`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rule_tree`
(
    `id`                 bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `tree_id`            varchar(32)     NOT NULL COMMENT '规则树ID',
    `tree_name`          varchar(64)     NOT NULL COMMENT '规则树名称',
    `tree_desc`          varchar(128)             DEFAULT NULL COMMENT '规则树描述',
    `tree_node_rule_key` varchar(32)     NOT NULL COMMENT '规则树根入口规则',
    `create_time`        datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`        datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_tree_id` (`tree_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='规则树表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule_tree`
--

LOCK TABLES `rule_tree` WRITE;
/*!40000 ALTER TABLE `rule_tree`
    DISABLE KEYS */;
INSERT INTO `rule_tree`
VALUES (1, 'tree_lock_1', '抽奖规则树', '抽奖中、抽奖后规则树', 'rule_lock', '2025-02-17 13:16:03',
        '2025-02-20 23:25:44'),
       (2, 'tree_lock_2', '抽奖规则树', '抽奖中、抽奖后规则树', 'rule_lock', '2025-02-17 13:16:03',
        '2025-02-20 23:25:44'),
       (3, 'tree_luck_award', '抽奖规则树', '规则树-兜底奖', 'rule_stock', '2025-02-20 23:27:13',
        '2025-02-20 23:27:13'),
       (4, 'tree_lock_3', '抽奖规则树', '抽奖中、抽奖后规则树', 'rule_lock', '2025-03-19 20:51:14',
        '2025-03-19 20:51:14');
/*!40000 ALTER TABLE `rule_tree`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rule_tree_node`
--

DROP TABLE IF EXISTS `rule_tree_node`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rule_tree_node`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `tree_id`     varchar(32)     NOT NULL COMMENT '规则树ID',
    `rule_name`   varchar(32)     NOT NULL COMMENT '规则名',
    `rule_desc`   varchar(64)     NOT NULL COMMENT '规则描述',
    `rule_value`  varchar(128)             DEFAULT NULL COMMENT '规则的值',
    `create_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 12
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='规则节点表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule_tree_node`
--

LOCK TABLES `rule_tree_node` WRITE;
/*!40000 ALTER TABLE `rule_tree_node`
    DISABLE KEYS */;
INSERT INTO `rule_tree_node`
VALUES (1, 'tree_lock_1', 'rule_lock', '限定用户已完成1次抽奖后解锁', '1', '2025-02-17 13:16:03',
        '2025-02-20 23:27:52'),
       (2, 'tree_lock_1', 'rule_luck_award', '兜底奖品随机积分', '101:1,100', '2025-02-17 13:16:03',
        '2025-02-20 23:27:52'),
       (3, 'tree_lock_1', 'rule_stock', '库存扣减规则', NULL, '2025-02-17 13:16:03', '2025-02-20 23:27:52'),
       (4, 'tree_lock_2', 'rule_lock', '限定用户已完成2次抽奖后解锁', '2', '2025-02-17 13:16:03',
        '2025-02-20 23:27:52'),
       (5, 'tree_lock_2', 'rule_luck_award', '兜底奖品随机积分', '101:1,100', '2025-02-17 13:16:03',
        '2025-02-20 23:27:52'),
       (6, 'tree_lock_2', 'rule_stock', '库存扣减规则', NULL, '2025-02-17 13:16:03', '2025-02-20 23:27:52'),
       (7, 'tree_luck_award', 'rule_stock', '库存扣减规则', NULL, '2025-02-20 23:29:16', '2025-02-20 23:29:16'),
       (8, 'tree_luck_award', 'rule_luck_award', '兜底奖品随机积分', '101:1,100', '2025-02-20 23:29:43',
        '2025-02-20 23:29:43'),
       (9, 'tree_lock_3', 'rule_lock', '限定用户已完成3次抽奖后解锁', '3', '2025-03-19 20:52:07',
        '2025-03-19 20:52:07'),
       (10, 'tree_lock_3', 'rule_luck_award', '兜底奖品随机积分', '101:1,100', '2025-03-19 20:52:07',
        '2025-03-19 20:52:07'),
       (11, 'tree_lock_3', 'rule_stock', '库存扣减规则', NULL, '2025-03-19 20:52:07', '2025-03-19 20:52:07');
/*!40000 ALTER TABLE `rule_tree_node`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rule_tree_node_line`
--

DROP TABLE IF EXISTS `rule_tree_node_line`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rule_tree_node_line`
(
    `id`               bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `tree_id`          varchar(32)     NOT NULL COMMENT '规则树ID',
    `rule_node_from`   varchar(32)     NOT NULL COMMENT 'From',
    `rule_node_to`     varchar(32)     NOT NULL COMMENT 'To',
    `rule_limit_type`  varchar(8)      NOT NULL COMMENT '限定类型；1:=;2:>;3:<;4:>=;5<=;6:enum[枚举范围];',
    `rule_limit_value` varchar(32)     NOT NULL COMMENT '限定值（到下个节点）',
    `create_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='规则节点走向表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule_tree_node_line`
--

LOCK TABLES `rule_tree_node_line` WRITE;
/*!40000 ALTER TABLE `rule_tree_node_line`
    DISABLE KEYS */;
INSERT INTO `rule_tree_node_line`
VALUES (1, 'tree_lock_1', 'rule_lock', 'rule_stock', 'EQUAL', 'ALLOW', '2025-02-17 13:16:03', '2025-02-20 23:30:37'),
       (2, 'tree_lock_1', 'rule_lock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER', '2025-02-17 13:16:03',
        '2025-02-20 23:30:37'),
       (3, 'tree_lock_1', 'rule_stock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER', '2025-02-17 13:16:03',
        '2025-02-20 23:30:37'),
       (4, 'tree_lock_2', 'rule_lock', 'rule_stock', 'EQUAL', 'ALLOW', '2025-02-17 13:16:03', '2025-02-20 23:30:37'),
       (5, 'tree_lock_2', 'rule_lock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER', '2025-02-17 13:16:03',
        '2025-02-20 23:30:37'),
       (6, 'tree_lock_2', 'rule_stock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER', '2025-02-17 13:16:03',
        '2025-02-20 23:30:37'),
       (7, 'rule_luck_award', 'rule_stock', 'rule_luck_award', 'EQUAL', 'ALLOW', '2025-02-20 23:31:42',
        '2025-02-20 23:31:42'),
       (8, 'tree_lock_3', 'rule_lock', 'rule_stock', 'EQUAL', 'ALLOW', '2025-02-17 13:16:03', '2025-02-20 23:30:37'),
       (9, 'tree_lock_3', 'rule_lock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER', '2025-02-17 13:16:03',
        '2025-02-20 23:30:37'),
       (10, 'tree_lock_3', 'rule_stock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER', '2025-02-17 13:16:03',
        '2025-02-20 23:30:37');
/*!40000 ALTER TABLE `rule_tree_node_line`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `strategy`
--

DROP TABLE IF EXISTS `strategy`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `strategy`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `strategy_id`   bigint          NOT NULL COMMENT '抽奖策略ID',
    `strategy_desc` varchar(128)    NOT NULL COMMENT '抽奖策略描述',
    `rule_models`   varchar(256)             DEFAULT NULL COMMENT '规则模型',
    `create_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_strategy_id` (`strategy_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='策略表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `strategy`
--

LOCK TABLES `strategy` WRITE;
/*!40000 ALTER TABLE `strategy`
    DISABLE KEYS */;
INSERT INTO `strategy`
VALUES (5, 200001, '抽奖策略-规则树', 'rule_blacklist,rule_weight', '2025-02-20 23:17:19', '2025-02-20 23:17:19');
/*!40000 ALTER TABLE `strategy`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `strategy_award`
--

DROP TABLE IF EXISTS `strategy_award`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `strategy_award`
(
    `id`                  bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `strategy_id`         bigint          NOT NULL COMMENT '抽奖策略ID',
    `award_id`            bigint          NOT NULL COMMENT '抽奖奖品ID',
    `award_title`         varchar(128)    NOT NULL COMMENT '抽奖奖品标题',
    `award_subtitle`      varchar(128)             DEFAULT NULL COMMENT '抽奖奖品副标题',
    `award_count`         int             NOT NULL DEFAULT '0' COMMENT '奖品库存总量',
    `award_count_surplus` int             NOT NULL DEFAULT '0' COMMENT '奖品库存剩余',
    `award_rate`          decimal(6, 4)   NOT NULL COMMENT '奖品中奖概率',
    `rule_models`         varchar(256)             DEFAULT NULL COMMENT '规则模型（rule配置的模型同步到此表，便于使用）',
    `sort`                int             NOT NULL DEFAULT '0' COMMENT '排序',
    `create_time`         datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_strategy_id_award_id` (`strategy_id`, `award_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 26
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='策略奖品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `strategy_award`
--

LOCK TABLES `strategy_award` WRITE;
/*!40000 ALTER TABLE `strategy_award`
    DISABLE KEYS */;
INSERT INTO `strategy_award`
VALUES (18, 200001, 101, '随机积分', NULL, 100000000, 100000000, 0.5000, 'tree_luck_award', 1, '2025-02-20 23:20:00',
        '2025-06-09 13:59:10'),
       (19, 200001, 102, 'OpenAI会员卡', NULL, 100000000, 100000000, 0.0500, 'tree_luck_award', 2,
        '2025-02-20 23:24:23', '2025-06-09 13:59:10'),
       (20, 200001, 103, '支付优惠券', NULL, 100000000, 100000000, 0.1000, 'tree_luck_award', 3, '2025-02-20 23:24:23',
        '2025-06-09 13:59:10'),
       (21, 200001, 104, '小米台灯', NULL, 100000000, 100000000, 0.0001, 'tree_luck_award', 4, '2025-02-20 23:24:23',
        '2025-06-08 19:08:08'),
       (22, 200001, 105, '小米su7周体验', '抽奖3次后解锁', 100000000, 100000000, 0.1000, 'tree_lock_3', 5,
        '2025-02-20 23:24:23', '2025-06-09 13:59:10'),
       (23, 200001, 106, '轻奢办公椅', '抽奖2次后解锁', 100000000, 100000000, 0.0500, 'tree_lock_2', 6,
        '2025-02-20 23:24:23', '2025-06-09 13:59:10'),
       (24, 200001, 107, '小霸王游戏机', '抽奖1次后解锁', 100000000, 100000000, 0.0500, 'tree_lock_1', 7,
        '2025-02-20 23:24:23', '2025-06-09 13:59:10'),
       (25, 200001, 108, '暴走玩偶', '', 100000000, 100000000, 0.1499, 'tree_luck_award', 8, '2025-02-20 23:24:23',
        '2025-06-09 13:59:10');
/*!40000 ALTER TABLE `strategy_award`
    ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2025-06-09 13:59:19
