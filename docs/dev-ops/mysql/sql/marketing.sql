CREATE DATABASE /*!32312 IF NOT EXISTS */ `marketing` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;
USE `marketing`;
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
) ENGINE = InnoDB
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
VALUES (100301, 200001, '测试活动', '测试活动test', '2025-06-15 21:17:36', '2025-09-05 21:17:35', 'open',
        '2025-03-05 22:17:20', '2025-06-16 20:43:54'),
       (1934223919414280193, 200001, '活动v1', '活动v1', '2025-06-15 20:15:46', '2025-09-07 00:00:08', 'open',
        '2025-06-15 20:18:13', '2025-06-15 22:03:55'),
       (1947194890802733058, 1947130468034007042, '一起来抽奖', '惊喜大奖等你拿呀', '2025-07-21 15:20:05',
        '2026-07-21 15:20:07', 'open', '2025-07-21 15:20:13', '2025-07-23 13:15:16'),
       (1948000932272812034, 1947996138992865281, '无限制抽奖', '加油呀', '2025-07-23 20:42:59', '2025-07-31 00:00:00',
        'open', '2025-07-23 20:43:10', '2025-07-23 20:43:10');
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
    `id`          bigint unsigned NOT NULL COMMENT '雪花ID',
    `total_count` int             NOT NULL COMMENT '总次数',
    `day_count`   int             NOT NULL COMMENT '日次数',
    `month_count` int             NOT NULL COMMENT '月次数',
    `create_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
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
VALUES (11101, 1, 1, 1, '2025-03-05 22:19:07', '2025-06-15 16:31:02'),
       (11102, 5, 5, 5, '2025-05-07 23:34:04', '2025-06-15 16:31:02'),
       (1934610051247448065, 10, 10, 10, '2025-06-16 21:52:34', '2025-06-16 21:52:34'),
       (1947276092532346881, 20, 20, 20, '2025-07-21 20:42:54', '2025-07-21 20:42:54'),
       (1947297667382018050, 100, 100, 100, '2025-07-21 22:08:37', '2025-07-21 22:08:37');
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
) ENGINE = InnoDB
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
VALUES (9011, 100301, 11101, 2000000, 199569, 1.99, '2025-03-11 22:12:56', '2025-07-24 15:46:20'),
       (9012, 100301, 11102, 2000000, 199394, 5.99, '2025-05-07 23:33:24', '2025-07-24 14:24:25'),
       (9013, 1934223919414280193, 11101, 1000000, 99982, 2.99, '2025-06-15 22:31:56', '2025-07-09 23:44:44'),
       (1934859819538202625, 1934223919414280193, 1934610051247448065, 1000, 990, 25.00, '2025-06-17 14:25:03',
        '2025-07-24 14:42:15'),
       (1947276284354646017, 1947194890802733058, 1947276092532346881, 100000, 99999, 79.99, '2025-07-21 20:43:39',
        '2025-07-22 15:01:55'),
       (1947277048632332290, 1947194890802733058, 11101, 100000, 99982, 5.99, '2025-07-21 20:46:42',
        '2025-07-24 14:49:25'),
       (1947294317592739842, 1947194890802733058, 1934610051247448065, 100000, 99999, 45.99, '2025-07-21 21:55:18',
        '2025-07-22 14:37:00'),
       (1947294503257800705, 1947194890802733058, 11102, 100000, 99991, 24.99, '2025-07-21 21:56:03',
        '2025-07-23 16:42:25'),
       (1947297747681968129, 1947194890802733058, 1947297667382018050, 100000, 100000, 299.99, '2025-07-21 22:08:57',
        '2025-07-21 22:09:44');
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
    `id`           bigint unsigned NOT NULL COMMENT '雪花ID',
    `award_key`    varchar(32)     NOT NULL COMMENT '奖品对接标识（每一个都是一个对应的发奖策略）',
    `award_config` varchar(32)     NOT NULL COMMENT '奖品配置信息',
    `award_desc`   varchar(128)    NOT NULL COMMENT '奖品内容描述',
    `image`        varchar(200)    NOT NULL COMMENT '图片',
    `create_time`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
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
VALUES (100, 'user_credit_blacklist', '1', '黑名单积分',
        'https://marketing-lottery.oss-cn-beijing.aliyuncs.com/4888991e-43e4-4c91-a374-ec5b44932f71.png',
        '2025-02-17 13:16:03', '2025-07-06 19:37:31'),
       (101, 'user_credit_random', '1,100', '随机积分',
        'https://marketing-lottery.oss-cn-beijing.aliyuncs.com/cbc3b110-1b18-4031-91c2-a927cbc1f137.png',
        '2025-02-17 13:16:03', '2025-07-06 19:33:30'),
       (102, 'openai_use_count', '1', 'OpenAI会员卡',
        'https://marketing-lottery.oss-cn-beijing.aliyuncs.com/e25e3680-857e-46a9-a57c-d4725e3b41f4.png',
        '2025-02-17 13:16:03', '2025-07-06 19:36:37'),
       (103, 'openai_use_count', '1', '支付优惠券',
        'https://marketing-lottery.oss-cn-beijing.aliyuncs.com/eb32eddf-e39d-4f28-9131-2bd8c7c54fb2.png',
        '2025-02-17 13:16:03', '2025-07-06 19:36:48'),
       (104, 'openai_use_count', '1', '小米台灯',
        'https://marketing-lottery.oss-cn-beijing.aliyuncs.com/6868f875-a8d5-4127-958f-09b7ece798e3.png',
        '2025-02-17 13:16:03', '2025-07-06 19:36:55'),
       (105, 'openai_model', '1', '小米su7周体验',
        'https://marketing-lottery.oss-cn-beijing.aliyuncs.com/82803b35-eb41-4cb9-9366-e08762923090.png',
        '2025-02-17 13:16:03', '2025-07-06 19:37:02'),
       (106, 'openai_model', '1', '轻奢办公椅',
        'https://marketing-lottery.oss-cn-beijing.aliyuncs.com/8ada4c9e-2869-4694-9d1d-89c3c7f95854.png',
        '2025-02-17 13:16:03', '2025-07-06 19:37:07'),
       (107, 'openai_model', '1', '小霸王游戏机',
        'https://marketing-lottery.oss-cn-beijing.aliyuncs.com/f8772c2d-9374-4c03-aef7-547b5b39dcae.png',
        '2025-02-17 13:16:03', '2025-07-06 19:37:15'),
       (108, 'openai_use_count', '1', '暴走玩偶',
        'https://marketing-lottery.oss-cn-beijing.aliyuncs.com/54e1afd0-fc53-43fc-a410-70cac5b704fd.png',
        '2025-02-17 13:16:03', '2025-07-06 19:37:21'),
       (1947133372497252353, 'user_blacklist', '1', '黑名单积分',
        'https://marketing-lottery.oss-cn-beijing.aliyuncs.com/f3a3eaaf-8c2b-4df0-9e7c-ef02cfb78671.png',
        '2025-07-21 11:15:46', '2025-07-22 12:09:54'),
       (1947135858008137730, 'user_credit_random', '10,20', '随机积分（10-20）',
        'https://marketing-lottery.oss-cn-beijing.aliyuncs.com/d1f9910b-bf92-431b-b288-64c24ac647ae.png',
        '2025-07-21 11:25:39', '2025-07-21 23:32:30'),
       (1947136031354527745, 'new_award_2', '1', '一张会员卡',
        'https://marketing-lottery.oss-cn-beijing.aliyuncs.com/38bfcfdc-c23a-45f8-adb2-bd74e2b5cf02.png',
        '2025-07-21 11:26:20', '2025-07-21 14:42:24'),
       (1947136149856198658, 'new_award_3', '1', '支付立减券',
        'https://marketing-lottery.oss-cn-beijing.aliyuncs.com/e2f13ca4-414d-498a-85f2-5558b9adce56.png',
        '2025-07-21 11:26:48', '2025-07-21 11:26:48'),
       (1947136290151473154, 'new_award_4', '1', '一个办公椅',
        'https://marketing-lottery.oss-cn-beijing.aliyuncs.com/68ec2d0b-e42d-4629-b2b5-7f9b91a32a3c.png',
        '2025-07-21 11:27:22', '2025-07-21 14:42:01'),
       (1947136366194204674, 'new_award_5', '1', '一个小玩偶',
        'https://marketing-lottery.oss-cn-beijing.aliyuncs.com/07930802-bbb6-4c0b-9fdc-bcb77ae49ea4.png',
        '2025-07-21 11:27:40', '2025-07-21 14:41:34'),
       (1947136456283660289, 'new_award_6', '1', '一盏台灯',
        'https://marketing-lottery.oss-cn-beijing.aliyuncs.com/bf413c35-fd61-4ded-a484-7e49155ed069.png',
        '2025-07-21 11:28:01', '2025-07-21 14:41:11'),
       (1947136515402375169, 'new_award_7', '1', '一台游戏机',
        'https://marketing-lottery.oss-cn-beijing.aliyuncs.com/27641618-9a9c-46d3-9018-2648863172f6.png',
        '2025-07-21 11:28:15', '2025-07-21 14:40:47'),
       (1947136617739198466, 'new_award_8', '1', '一辆小米su7',
        'https://marketing-lottery.oss-cn-beijing.aliyuncs.com/ad69f93c-8b67-49b4-827e-c18cfcbc9842.png',
        '2025-07-21 11:28:40', '2025-07-21 14:40:32');
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
    KEY `idx_behavior_type` (`behavior_type`)
) ENGINE = InnoDB
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
VALUES (1, 100301, 'sign', '签到返利-抽奖额度', 'sku', '9011', 'open', '2025-03-28 18:42:59', '2025-06-20 15:07:01'),
       (2, 100301, 'sign', '签到返利-积分', 'integral', '10', 'open', '2025-03-28 18:42:59', '2025-06-15 23:51:57'),
       (4, 1934223919414280193, 'sign', '签到返积分', 'integral', '50', 'open', '2025-03-28 18:42:59',
        '2025-07-09 23:46:06'),
       (1934927347346096129, 1934223919414280193, 'sign', '签到返抽奖次数', 'sku', '1934859819538202625', 'open',
        '2025-06-17 18:53:23', '2025-06-17 19:50:23'),
       (1947277285757308929, 1947194890802733058, 'sign', '每日签到返抽奖次数', 'sku', '1947277048632332290', 'open',
        '2025-07-21 20:47:37', '2025-07-21 21:48:40'),
       (1947277561214029825, 1947194890802733058, 'sign', '每日签到返积分', 'integral', '10', 'open',
        '2025-07-21 20:48:43', '2025-07-21 20:48:43'),
       (1948271061317464066, 1948000932272812034, 'activity_gift', '活动赠送5次抽奖', 'gift', '5', 'open',
        '2025-07-24 14:36:32', '2025-07-24 14:36:32'),
       (1948271313437077505, 1948000932272812034, 'activity_gift', '第二次赠送', 'gift', '13', 'open',
        '2025-07-24 14:37:32', '2025-07-24 14:37:32'),
       (1948272437883842561, 1947194890802733058, 'activity_gift', '赠送你6次抽奖次数', 'gift', '6', 'open',
        '2025-07-24 14:42:01', '2025-07-24 14:49:41'),
       (1948274825676054529, 1947194890802733058, 'activity_gift', '第二次赠送你3次抽奖次数', 'gift', '3', 'open',
        '2025-07-24 14:51:30', '2025-07-24 14:51:30'),
       (1948288777088700417, 100301, 'activity_gift', '测试赠送', 'gift', '1', 'open', '2025-07-24 15:46:56',
        '2025-07-24 15:46:56');
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
    `id`          bigint unsigned NOT NULL COMMENT '雪花ID',
    `rule_model`  varchar(60)     NOT NULL COMMENT '规则模型（rule_random - 随机值计算、rule_lock_n - 抽奖n次后解锁、rule_luck_award - 幸运奖(兜底奖品)）',
    `rule_value`  varchar(256)    NOT NULL COMMENT '规则比值',
    `rule_desc`   varchar(512)    NOT NULL COMMENT '规则描述',
    `create_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `rule_pk` (`rule_model`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='策略规则表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule`
--

LOCK TABLES `rule` WRITE;
/*!40000 ALTER TABLE `rule`
    DISABLE KEYS */;
INSERT INTO `rule`
VALUES (13, 'rule_weight', '4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108',
        '必中奖范围', '2025-02-17 13:16:03', '2025-07-07 20:27:09'),
       (14, 'rule_blacklist', '101:user001,user002,user003', '黑名单抽奖，积分兜底', '2025-02-17 13:16:03',
        '2025-06-15 17:07:19'),
       (1947131330408075266, 'new_rule_blacklist', '1947133372497252353:user01,user02',
        'user01和user02为黑名单用户，只可抽到1947133372497252353奖品', '2025-07-21 11:07:39', '2025-07-22 12:17:12'),
       (1947131780389785601, 'new_rule_weight', '4000:1947136366194204674 5000:1947136290151473154,1947136617739198466',
        '到达0-4000幸运值，必中奖品1947136366194204674\n到达4000-5000幸运值，必中奖品1947136290151473154,1947136617739198466',
        '2025-07-21 11:09:27', '2025-07-22 12:26:37');
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
    `id`                 bigint unsigned NOT NULL COMMENT '雪花ID',
    `tree_name`          varchar(64)     NOT NULL COMMENT '规则树名称',
    `tree_desc`          varchar(128)             DEFAULT NULL COMMENT '规则树描述',
    `tree_node_rule_key` varchar(32)     NOT NULL COMMENT '规则树根入口规则',
    `create_time`        datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`        datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
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
VALUES (1, '抽奖规则树(抽奖1次后解锁)', '抽奖中、抽奖后规则树', 'rule_lock', '2025-02-17 13:16:03',
        '2025-07-06 20:52:27'),
       (2, '抽奖规则树(抽奖2次后解锁)', '抽奖中、抽奖后规则树', 'rule_lock', '2025-02-17 13:16:03',
        '2025-07-06 20:52:27'),
       (3, '抽奖规则树', '规则树-兜底奖', 'rule_stock', '2025-02-20 23:27:13', '2025-06-26 13:24:16'),
       (4, '抽奖规则树(抽奖3次后解锁)', '抽奖中、抽奖后规则树', 'rule_lock', '2025-03-19 20:51:14',
        '2025-07-06 20:52:27'),
       (1938188768824098818, '奖品规则v1-抽奖一次后解锁', '抽奖中、抽奖后规则树', 'rule_lock', '2025-06-26 18:53:06',
        '2025-06-26 20:56:13'),
       (1947168534748471298, 'new_tree抽奖5次后解锁', 'new_tree抽奖一次后解锁', 'rule_lock', '2025-07-21 13:35:30',
        '2025-07-21 15:59:11'),
       (1947168607834218498, 'new_tree抽奖两次后解锁', 'new_tree抽奖两次后解锁', 'rule_lock', '2025-07-21 13:35:47',
        '2025-07-21 13:35:47'),
       (1947168641753554945, 'new_tree抽奖三次后解锁', 'new_tree抽奖三次后解锁', 'rule_lock', '2025-07-21 13:35:55',
        '2025-07-21 13:35:55'),
       (1947168714872856578, 'new_tree无规则', 'new_tree次数规则', 'rule_stock', '2025-07-21 13:36:12',
        '2025-07-23 20:19:31'),
       (1947994741845037057, '默认规则', '默认规则，无次数规则', 'rule_stock', '2025-07-23 20:18:33',
        '2025-07-23 20:33:46');
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
    `id`           bigint unsigned NOT NULL COMMENT '雪花ID',
    `rule_tree_id` bigint          NOT NULL COMMENT '规则树ID',
    `rule_name`    varchar(32)     NOT NULL COMMENT '规则名',
    `rule_desc`    varchar(64)     NOT NULL COMMENT '规则描述',
    `rule_value`   varchar(128)             DEFAULT NULL COMMENT '规则的值',
    `create_time`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
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
VALUES (1, 1, 'rule_lock', '限定用户已完成1次抽奖后解锁', '1', '2025-02-17 13:16:03', '2025-06-26 13:33:43'),
       (2, 1, 'rule_luck_award', '兜底奖品随机积分', '101:1,100', '2025-02-17 13:16:03', '2025-06-26 13:33:43'),
       (3, 1, 'rule_stock', '库存扣减规则', NULL, '2025-02-17 13:16:03', '2025-06-26 13:33:43'),
       (4, 2, 'rule_lock', '限定用户已完成2次抽奖后解锁', '2', '2025-02-17 13:16:03', '2025-06-26 13:33:43'),
       (5, 2, 'rule_luck_award', '兜底奖品随机积分', '101:1,100', '2025-02-17 13:16:03', '2025-06-26 13:33:43'),
       (6, 2, 'rule_stock', '库存扣减规则', NULL, '2025-02-17 13:16:03', '2025-06-26 13:33:43'),
       (7, 3, 'rule_stock', '库存扣减规则', NULL, '2025-02-20 23:29:16', '2025-06-26 13:33:43'),
       (8, 3, 'rule_luck_award', '兜底奖品随机积分', '101:1,100', '2025-02-20 23:29:43', '2025-06-26 13:33:43'),
       (9, 4, 'rule_lock', '限定用户已完成3次抽奖后解锁', '3', '2025-03-19 20:52:07', '2025-06-26 13:33:43'),
       (10, 4, 'rule_luck_award', '兜底奖品随机积分', '101:1,100', '2025-03-19 20:52:07', '2025-06-26 13:33:43'),
       (11, 4, 'rule_stock', '库存扣减规则', NULL, '2025-03-19 20:52:07', '2025-06-26 13:33:43'),
       (1947169518279204865, 1947168534748471298, 'rule_stock', '库存扣减', '', '2025-07-21 13:39:24',
        '2025-07-21 15:33:25'),
       (1947170656571367426, 1947168534748471298, 'rule_lock', '抽奖一次后解锁', '1', '2025-07-21 13:43:55',
        '2025-07-21 15:59:36'),
       (1947179584369729537, 1947168607834218498, 'rule_stock', '库存扣减', '', '2025-07-21 14:19:24',
        '2025-07-21 15:33:20'),
       (1947179685393735682, 1947168607834218498, 'rule_lock', '抽奖两次后解锁', '2', '2025-07-21 14:19:48',
        '2025-07-21 15:33:15'),
       (1947180267789623297, 1947168641753554945, 'rule_stock', '库存扣减', '', '2025-07-21 14:22:07',
        '2025-07-21 15:33:09'),
       (1947180346839670786, 1947168641753554945, 'rule_lock', '抽奖三次后解锁', '3', '2025-07-21 14:22:26',
        '2025-07-21 15:33:00'),
       (1947180542596227074, 1947168534748471298, 'rule_luck_award', '幸运奖', '', '2025-07-21 14:23:12',
        '2025-07-21 15:32:33'),
       (1947180821630689281, 1947168607834218498, 'rule_luck_award', '幸运奖', '', '2025-07-21 14:24:19',
        '2025-07-21 15:32:37'),
       (1947180856678293505, 1947168641753554945, 'rule_luck_award', '幸运奖', '', '2025-07-21 14:24:27',
        '2025-07-21 15:32:42'),
       (1947183563245887490, 1947168714872856578, 'rule_stock', '库存扣减', '', '2025-07-21 14:35:13',
        '2025-07-21 15:32:28'),
       (1947184125689470978, 1947168714872856578, 'rule_luck_award', '幸运奖', '', '2025-07-21 14:37:27',
        '2025-07-21 15:32:24'),
       (1947995190753005569, 1947994741845037057, 'rule_stock', '默认节点', NULL, '2025-07-23 20:20:21',
        '2025-07-23 20:20:21'),
       (1947995290489360385, 1947994741845037057, 'rule_luck_award', '默认节点', NULL, '2025-07-23 20:20:43',
        '2025-07-23 20:20:43');
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
    `id`               bigint unsigned NOT NULL COMMENT '雪花ID',
    `rule_tree_id`     bigint          NOT NULL COMMENT '规则树ID',
    `rule_node_from`   varchar(32)     NOT NULL COMMENT 'From',
    `rule_node_to`     varchar(32)     NOT NULL COMMENT 'To',
    `rule_limit_type`  varchar(8)      NOT NULL COMMENT '限定类型；1:=;2:>;3:<;4:>=;5<=;6:enum[枚举范围];',
    `rule_limit_value` varchar(32)     NOT NULL COMMENT '限定值（到下个节点）',
    `create_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
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
VALUES (1, 1, 'rule_lock', 'rule_stock', 'EQUAL', 'ALLOW', '2025-02-17 13:16:03', '2025-06-26 13:35:34'),
       (2, 1, 'rule_lock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER', '2025-02-17 13:16:03', '2025-06-26 13:35:34'),
       (3, 1, 'rule_stock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER', '2025-02-17 13:16:03', '2025-06-26 13:35:34'),
       (4, 2, 'rule_lock', 'rule_stock', 'EQUAL', 'ALLOW', '2025-02-17 13:16:03', '2025-06-26 13:35:34'),
       (5, 2, 'rule_lock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER', '2025-02-17 13:16:03', '2025-06-26 13:35:34'),
       (6, 2, 'rule_stock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER', '2025-02-17 13:16:03', '2025-06-26 13:35:34'),
       (7, 3, 'rule_stock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER', '2025-02-20 23:31:42', '2025-07-24 15:39:28'),
       (8, 4, 'rule_lock', 'rule_stock', 'EQUAL', 'ALLOW', '2025-02-17 13:16:03', '2025-06-26 13:35:34'),
       (9, 4, 'rule_lock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER', '2025-02-17 13:16:03', '2025-06-26 13:35:34'),
       (10, 4, 'rule_stock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER', '2025-02-17 13:16:03', '2025-06-26 13:35:34'),
       (1947170388433707010, 1947168534748471298, 'rule_stock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER',
        '2025-07-21 13:42:51', '2025-07-21 15:34:56'),
       (1947170753426235394, 1947168534748471298, 'rule_lock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER',
        '2025-07-21 13:44:18', '2025-07-21 15:34:47'),
       (1947170911463415809, 1947168534748471298, 'rule_lock', 'rule_stock', 'EQUAL', 'ALLOW', '2025-07-21 13:44:56',
        '2025-07-21 15:35:02'),
       (1947180932565835777, 1947168607834218498, 'rule_lock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER',
        '2025-07-21 14:24:45', '2025-07-21 15:33:47'),
       (1947181058847940609, 1947168607834218498, 'rule_lock', 'rule_stock', 'EQUAL', 'ALLOW', '2025-07-21 14:25:15',
        '2025-07-21 15:34:31'),
       (1947181633996070914, 1947168607834218498, 'rule_stock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER',
        '2025-07-21 14:27:33', '2025-07-21 15:34:03'),
       (1947181782814171137, 1947168641753554945, 'rule_stock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER',
        '2025-07-21 14:28:08', '2025-07-21 15:34:41'),
       (1947181821548568577, 1947168641753554945, 'rule_lock', 'rule_stock', 'EQUAL', 'ALLOW', '2025-07-21 14:28:17',
        '2025-07-21 15:34:15'),
       (1947181870139580417, 1947168641753554945, 'rule_lock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER',
        '2025-07-21 14:28:29', '2025-07-21 15:34:09'),
       (1947184337204027393, 1947168714872856578, 'rule_stock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER',
        '2025-07-21 14:38:17', '2025-07-21 15:33:39'),
       (1947995350061060097, 1947994741845037057, 'rule_stock', 'rule_luck_award', 'EQUAL', 'TAKE_OVER',
        '2025-07-23 20:20:59', '2025-07-23 20:20:59');
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
    `id`            bigint unsigned NOT NULL COMMENT '雪花1ID',
    `strategy_desc` varchar(128)    NOT NULL COMMENT '抽奖策略描述',
    `rule_models`   varchar(256)             DEFAULT NULL COMMENT '规则模型',
    `create_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
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
VALUES (200001, '抽奖策略-规则树', 'rule_blacklist,rule_weight', '2025-02-20 23:17:19', '2025-06-15 16:55:16'),
       (1947130468034007042, '高级使用-抽奖策略，黑名单、权重', 'new_rule_blacklist,new_rule_weight',
        '2025-07-21 11:04:14', '2025-07-21 16:12:30'),
       (1947996138992865281, '默认策略', '', '2025-07-23 20:24:05', '2025-07-24 14:54:26');
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
    `id`                  bigint unsigned NOT NULL COMMENT '雪花ID',
    `strategy_id`         bigint          NOT NULL COMMENT '抽奖策略ID',
    `award_id`            bigint          NOT NULL COMMENT '抽奖奖品ID',
    `award_title`         varchar(128)    NOT NULL COMMENT '抽奖奖品标题',
    `award_subtitle`      varchar(128)             DEFAULT NULL COMMENT '抽奖奖品副标题',
    `award_count`         int             NOT NULL DEFAULT '0' COMMENT '奖品库存总量',
    `award_count_surplus` int             NOT NULL DEFAULT '0' COMMENT '奖品库存剩余',
    `award_rate`          decimal(6, 4)   NOT NULL COMMENT '奖品中奖概率',
    `rule_tree_id`        bigint          NOT NULL COMMENT '奖品规则ID',
    `sort`                int             NOT NULL DEFAULT '0' COMMENT '排序',
    `create_time`         datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_strategy_id_award_id` (`strategy_id`, `award_id`)
) ENGINE = InnoDB
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
VALUES (18, 200001, 101, '随机积分', NULL, 100000000, 99998655, 0.5000, 3, 1, '2025-02-20 23:20:00',
        '2025-08-26 17:50:25'),
       (19, 200001, 102, 'OpenAI会员卡', NULL, 100000000, 99999890, 0.0500, 3, 2, '2025-02-20 23:24:23',
        '2025-07-21 21:38:00'),
       (20, 200001, 103, '支付优惠券', NULL, 100000000, 99999763, 0.1000, 3, 3, '2025-02-20 23:24:23',
        '2025-08-26 17:50:15'),
       (21, 200001, 104, '小米台灯', NULL, 100000000, 99999999, 0.0001, 3, 4, '2025-02-20 23:24:23',
        '2025-06-26 13:41:11'),
       (22, 200001, 105, '小米su7周体验', '抽奖3次后解锁', 100000000, 99999812, 0.1000, 4, 6, '2025-02-20 23:24:23',
        '2025-08-26 17:50:15'),
       (23, 200001, 106, '轻奢办公椅', '抽奖2次后解锁', 100000000, 99999913, 0.0500, 2, 7, '2025-02-20 23:24:23',
        '2025-07-24 15:33:05'),
       (24, 200001, 107, '小霸王游戏机', '抽奖1次后解锁', 100000000, 99999901, 0.0500, 1, 8, '2025-02-20 23:24:23',
        '2025-08-26 16:31:10'),
       (25, 200001, 108, '暴走玩偶', '', 100000000, 99999580, 0.1499, 3, 5, '2025-02-20 23:24:23',
        '2025-08-26 17:50:20'),
       (1947183200526671874, 1947130468034007042, 1947135858008137730, '随机积分', '', 100000, 99978, 0.4000,
        1947168714872856578, 1, '2025-07-21 14:33:46', '2025-07-23 20:13:35'),
       (1947184834262605825, 1947130468034007042, 1947136031354527745, '会员卡', '', 1000, 994, 0.1000,
        1947168714872856578, 2, '2025-07-21 14:40:16', '2025-07-23 20:17:00'),
       (1947185579263270914, 1947130468034007042, 1947136366194204674, '小玩偶', '', 100, 92, 0.1000,
        1947168714872856578, 3, '2025-07-21 14:43:13', '2025-07-23 18:36:15'),
       (1947185673257623554, 1947130468034007042, 1947136149856198658, '支付立减券', '', 1000, 982, 0.1900,
        1947168714872856578, 4, '2025-07-21 14:43:36', '2025-07-23 18:36:25'),
       (1947185841688289282, 1947130468034007042, 1947136456283660289, '小台灯', '', 100, 94, 0.1000,
        1947168714872856578, 5, '2025-07-21 14:44:16', '2025-07-23 18:35:55'),
       (1947186044545802241, 1947130468034007042, 1947136515402375169, '游戏机', '抽奖1次后解锁', 10, 7, 0.0500,
        1947168534748471298, 6, '2025-07-21 14:45:04', '2025-07-23 18:35:40'),
       (1947186174485340162, 1947130468034007042, 1947136290151473154, '办公椅', '抽奖2次后解锁', 10, 9, 0.0500,
        1947168607834218498, 7, '2025-07-21 14:45:35', '2025-07-23 15:55:45'),
       (1947186340995014658, 1947130468034007042, 1947136617739198466, '小米su7', '抽奖3次后解锁', 10, 10, 0.0100,
        1947168641753554945, 8, '2025-07-21 14:46:15', '2025-07-21 15:07:42'),
       (1947507467000803330, 1947130468034007042, 1947133372497252353, '黑名单奖品', NULL, 100000, 100000, 0.0000,
        1947168714872856578, 100, '2025-07-22 12:02:17', '2025-07-22 12:02:17'),
       (1947999008999878657, 1947996138992865281, 1947133372497252353, '黑名单', NULL, 100000, 100000, 0.0000,
        1947994741845037057, 100, '2025-07-23 20:35:29', '2025-07-23 20:35:29'),
       (1947999135202291714, 1947996138992865281, 1947135858008137730, '随机积分', NULL, 100000, 99997, 0.5000,
        1947994741845037057, 1, '2025-07-23 20:36:01', '2025-07-24 14:46:25'),
       (1947999234556964865, 1947996138992865281, 1947136031354527745, '会员卡', NULL, 100000, 99998, 0.2000,
        1947994741845037057, 2, '2025-07-23 20:36:23', '2025-07-24 14:39:00'),
       (1947999387078635522, 1947996138992865281, 1947136290151473154, '舒适办公椅', NULL, 10000, 9997, 0.1000,
        1947994741845037057, 3, '2025-07-23 20:36:59', '2025-07-24 14:58:30'),
       (1947999533917024257, 1947996138992865281, 1947136366194204674, '小玩偶', NULL, 10000, 9999, 0.1000,
        1947994741845037057, 4, '2025-07-23 20:37:37', '2025-07-24 14:38:50'),
       (1947999679648116737, 1947996138992865281, 1947136456283660289, '特色小台灯', NULL, 100, 99, 0.0500,
        1947994741845037057, 5, '2025-07-23 20:38:11', '2025-07-24 14:38:45'),
       (1947999819712704513, 1947996138992865281, 1947136515402375169, '游戏机', '', 10, 10, 0.0250,
        1947994741845037057, 6, '2025-07-23 20:38:45', '2025-07-23 20:38:51'),
       (1948000054031691778, 1947996138992865281, 1947136149856198658, '1000元优惠券', NULL, 5, 5, 0.0150,
        1947994741845037057, 7, '2025-07-23 20:39:39', '2025-07-23 20:39:39'),
       (1948000162383147009, 1947996138992865281, 1947136617739198466, '小米SU7', NULL, 5, 5, 0.0100,
        1947994741845037057, 8, '2025-07-23 20:40:05', '2025-07-23 20:40:05');
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

-- Dump completed on 2025-09-27 18:45:12
