-- MySQL dump 10.13  Distrib 8.0.35, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: marketing_02
-- ------------------------------------------------------
-- Server version	8.0.35

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
-- Table structure for table `activity_account`
--

DROP TABLE IF EXISTS `activity_account`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_account`
(
    `id`                  bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`             varchar(32)     NOT NULL COMMENT '用户ID',
    `activity_id`         bigint          NOT NULL COMMENT '活动ID',
    `total_count`         int             NOT NULL COMMENT '总次数',
    `total_count_surplus` int             NOT NULL COMMENT '总次数-剩余',
    `day_count`           int             NOT NULL COMMENT '日次数',
    `day_count_surplus`   int             NOT NULL COMMENT '日次数-剩余',
    `month_count`         int             NOT NULL COMMENT '月次数',
    `month_count_surplus` int             NOT NULL COMMENT '月次数-剩余',
    `create_time`         datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_user_id_activity_id` (`user_id`, `activity_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='活动账户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_account`
--

LOCK TABLES `activity_account` WRITE;
/*!40000 ALTER TABLE `activity_account`
    DISABLE KEYS */;
INSERT INTO `activity_account`
VALUES (1, 'yong', 100301, 7, 6, 7, 6, 7, 6, '2025-03-29 14:26:13', '2025-03-29 14:26:13');
/*!40000 ALTER TABLE `activity_account`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_account_day`
--

DROP TABLE IF EXISTS `activity_account_day`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_account_day`
(
    `id`                int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`           varchar(32)  NOT NULL COMMENT '用户ID',
    `activity_id`       bigint       NOT NULL COMMENT '活动ID',
    `day`               varchar(10)  NOT NULL COMMENT '日期（yyyy-mm-dd）',
    `day_count`         int          NOT NULL COMMENT '日次数',
    `day_count_surplus` int          NOT NULL COMMENT '日次数-剩余',
    `create_time`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_user_id_activity_id_day` (`user_id`, `activity_id`, `day`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='活动账户表-日次数';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_account_day`
--

LOCK TABLES `activity_account_day` WRITE;
/*!40000 ALTER TABLE `activity_account_day`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `activity_account_day`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_account_month`
--

DROP TABLE IF EXISTS `activity_account_month`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_account_month`
(
    `id`                  int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`             varchar(32)  NOT NULL COMMENT '用户ID',
    `activity_id`         bigint       NOT NULL COMMENT '活动ID',
    `month`               varchar(7)   NOT NULL COMMENT '月（yyyy-mm）',
    `month_count`         int          NOT NULL COMMENT '月次数',
    `month_count_surplus` int          NOT NULL COMMENT '月次数-剩余',
    `create_time`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_user_id_activity_id_month` (`user_id`, `activity_id`, `month`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='活动账户表-月次数';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_account_month`
--

LOCK TABLES `activity_account_month` WRITE;
/*!40000 ALTER TABLE `activity_account_month`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `activity_account_month`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_order_000`
--

DROP TABLE IF EXISTS `activity_order_000`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_order_000`
(
    `id`              bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`         varchar(32)     NOT NULL COMMENT '用户ID',
    `activity_id`     bigint          NOT NULL COMMENT '活动ID',
    `sku`             bigint          NOT NULL COMMENT '商品sku',
    `activity_name`   varchar(64)     NOT NULL COMMENT '活动名称',
    `strategy_id`     bigint          NOT NULL COMMENT '抽奖策略ID',
    `order_id`        varchar(12)     NOT NULL COMMENT '额度单ID',
    `order_time`      datetime        NOT NULL COMMENT '下单时间',
    `total_count`     int             NOT NULL COMMENT '总次数',
    `day_count`       int             NOT NULL COMMENT '日次数',
    `month_count`     int             NOT NULL COMMENT '月次数',
    `pay_amount`      decimal(10, 2)           DEFAULT NULL COMMENT '支付积分',
    `state`           varchar(10)     NOT NULL DEFAULT 'complete' COMMENT '订单状态（complete-完成 wait_pay-等待支付）',
    `out_business_no` varchar(64)     NOT NULL COMMENT '保证幂等，不会重复消费',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`, `state`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='抽奖额度单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_order_000`
--

LOCK TABLES `activity_order_000` WRITE;
/*!40000 ALTER TABLE `activity_order_000`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `activity_order_000`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_order_001`
--

DROP TABLE IF EXISTS `activity_order_001`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_order_001`
(
    `id`              bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`         varchar(32)     NOT NULL COMMENT '用户ID',
    `activity_id`     bigint          NOT NULL COMMENT '活动ID',
    `sku`             bigint          NOT NULL COMMENT '商品sku',
    `activity_name`   varchar(64)     NOT NULL COMMENT '活动名称',
    `strategy_id`     bigint          NOT NULL COMMENT '额度单ID',
    `order_id`        varchar(12)     NOT NULL COMMENT '订单ID',
    `order_time`      datetime        NOT NULL COMMENT '下单时间',
    `total_count`     int             NOT NULL COMMENT '总次数',
    `day_count`       int             NOT NULL COMMENT '日次数',
    `month_count`     int             NOT NULL COMMENT '月次数',
    `pay_amount`      decimal(10, 2)           DEFAULT NULL COMMENT '支付积分',
    `state`           varchar(10)     NOT NULL DEFAULT 'complete' COMMENT '订单状态（complete-完成 wait_pay-等待支付）',
    `out_business_no` varchar(64)     NOT NULL COMMENT '保证幂等，不会重复消费',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`, `state`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='抽奖额度单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_order_001`
--

LOCK TABLES `activity_order_001` WRITE;
/*!40000 ALTER TABLE `activity_order_001`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `activity_order_001`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_order_002`
--

DROP TABLE IF EXISTS `activity_order_002`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_order_002`
(
    `id`              bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`         varchar(32)     NOT NULL COMMENT '用户ID',
    `activity_id`     bigint          NOT NULL COMMENT '活动ID',
    `sku`             bigint          NOT NULL COMMENT '商品sku',
    `activity_name`   varchar(64)     NOT NULL COMMENT '活动名称',
    `strategy_id`     bigint          NOT NULL COMMENT '额度单ID',
    `order_id`        varchar(12)     NOT NULL COMMENT '订单ID',
    `order_time`      datetime        NOT NULL COMMENT '下单时间',
    `total_count`     int             NOT NULL COMMENT '总次数',
    `day_count`       int             NOT NULL COMMENT '日次数',
    `month_count`     int             NOT NULL COMMENT '月次数',
    `pay_amount`      decimal(10, 2)           DEFAULT NULL COMMENT '支付积分',
    `state`           varchar(10)     NOT NULL DEFAULT 'complete' COMMENT '订单状态（complete-完成 wait_pay-等待支付）',
    `out_business_no` varchar(64)     NOT NULL COMMENT '保证幂等，不会重复消费',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`, `state`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='抽奖额度单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_order_002`
--

LOCK TABLES `activity_order_002` WRITE;
/*!40000 ALTER TABLE `activity_order_002`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `activity_order_002`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_order_003`
--

DROP TABLE IF EXISTS `activity_order_003`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_order_003`
(
    `id`              bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`         varchar(32)     NOT NULL COMMENT '用户ID',
    `activity_id`     bigint          NOT NULL COMMENT '活动ID',
    `sku`             bigint          NOT NULL COMMENT '商品sku',
    `activity_name`   varchar(64)     NOT NULL COMMENT '活动名称',
    `strategy_id`     bigint          NOT NULL COMMENT '额度单ID',
    `order_id`        varchar(12)     NOT NULL COMMENT '订单ID',
    `order_time`      datetime        NOT NULL COMMENT '下单时间',
    `total_count`     int             NOT NULL COMMENT '总次数',
    `day_count`       int             NOT NULL COMMENT '日次数',
    `month_count`     int             NOT NULL COMMENT '月次数',
    `pay_amount`      decimal(10, 2)           DEFAULT NULL COMMENT '支付积分',
    `state`           varchar(10)     NOT NULL DEFAULT 'complete' COMMENT '订单状态（complete-完成 wait_pay-等待支付）',
    `out_business_no` varchar(64)     NOT NULL COMMENT '保证幂等，不会重复消费',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`, `state`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='抽奖额度单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_order_003`
--

LOCK TABLES `activity_order_003` WRITE;
/*!40000 ALTER TABLE `activity_order_003`
    DISABLE KEYS */;
INSERT INTO `activity_order_003`
VALUES (5, 'yong', 100301, 9011, '测试活动', 200001, '493362807001', '2025-03-29 14:25:32', 1, 1, 1, 0.00, 'completed',
        'yong_sku_20250329', '2025-03-29 14:25:32', '2025-03-29 14:25:32'),
       (6, 'yong', 100301, 9011, '测试活动', 200001, '342866235201', '2025-03-29 14:26:09', 1, 1, 1, -1.99, 'completed',
        '428416563416', '2025-03-29 14:26:08', '2025-03-29 14:26:08'),
       (7, 'yong', 100301, 9011, '测试活动', 200001, '596316871204', '2025-03-29 14:26:12', 1, 1, 1, -1.99, 'completed',
        '839301174685', '2025-03-29 14:26:11', '2025-03-29 14:26:11'),
       (8, 'yong', 100301, 9011, '测试活动', 200001, '705191608810', '2025-03-29 14:26:13', 1, 1, 1, -1.99, 'completed',
        '751928339311', '2025-03-29 14:26:13', '2025-03-29 14:26:13');
/*!40000 ALTER TABLE `activity_order_003`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credit_account`
--

DROP TABLE IF EXISTS `credit_account`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `credit_account`
(
    `id`               bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`          varchar(32)     NOT NULL COMMENT '用户ID',
    `total_amount`     decimal(10, 2)  NOT NULL COMMENT '总积分，显示总账户值，记得一个人获得的总积分',
    `available_amount` decimal(10, 2)  NOT NULL COMMENT '可用积分，每次扣减的值',
    `account_status`   varchar(8)      NOT NULL COMMENT '账户状态【open - 可用，close - 冻结】',
    `create_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='积分账户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credit_account`
--

LOCK TABLES `credit_account` WRITE;
/*!40000 ALTER TABLE `credit_account`
    DISABLE KEYS */;
INSERT INTO `credit_account`
VALUES (1, 'yong', 24.03, 24.03, 'open', '2025-03-29 14:17:16', '2025-03-29 14:26:13');
/*!40000 ALTER TABLE `credit_account`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task`
(
    `id`          int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `topic`       varchar(32)  NOT NULL COMMENT '消息主题',
    `user_id`     varchar(32)  NOT NULL COMMENT '用户ID',
    `message_id`  varchar(11)  NOT NULL COMMENT '消息编号',
    `message`     varchar(512) NOT NULL COMMENT '消息主体',
    `state`       varchar(16)  NOT NULL DEFAULT 'create' COMMENT '任务状态；create-创建、completed-完成、fail-失败',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_message_id` (`message_id`),
    KEY `idx_state` (`state`),
    KEY `idx_create_time` (`update_time`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 19
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='任务表，发送MQ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task`
    DISABLE KEYS */;
INSERT INTO `task`
VALUES (10, 'send_rebate', 'yong', '91074438929',
        'BaseEvent.EventMessage(id=91074438929, timestamp=Sat Mar 29 14:24:55 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, rebateDesc=签到返利-抽奖额度, rebateType=sku, rebateConfig=9011, bizId=yong_sku_20250329))',
        'completed', '2025-03-29 14:24:55', '2025-03-29 14:24:55'),
       (11, 'send_rebate', 'yong', '86012037463',
        'BaseEvent.EventMessage(id=86012037463, timestamp=Sat Mar 29 14:24:55 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, rebateDesc=签到返利-积分, rebateType=integral, rebateConfig=10, bizId=yong_integral_20250329))',
        'completed', '2025-03-29 14:24:55', '2025-03-29 14:24:55'),
       (12, 'credit_adjust_success', 'yong', '28911157907',
        '{\"data\":{\"amount\":10,\"orderId\":\"761466706309\",\"outBusinessNo\":\"yong_integral_20250329\",\"userId\":\"yong\"},\"id\":\"28911157907\",\"timestamp\":1743229495932}',
        'completed', '2025-03-29 14:24:55', '2025-03-29 14:24:55'),
       (13, 'send_rebate', 'yong', '75811355518',
        'BaseEvent.EventMessage(id=75811355518, timestamp=Sat Mar 29 14:25:32 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, rebateDesc=签到返利-抽奖额度, rebateType=sku, rebateConfig=9011, bizId=yong_sku_20250329))',
        'completed', '2025-03-29 14:25:32', '2025-03-29 14:25:32'),
       (14, 'send_rebate', 'yong', '95188223160',
        'BaseEvent.EventMessage(id=95188223160, timestamp=Sat Mar 29 14:25:32 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, rebateDesc=签到返利-积分, rebateType=integral, rebateConfig=10, bizId=yong_integral_20250329))',
        'completed', '2025-03-29 14:25:32', '2025-03-29 14:25:32'),
       (15, 'credit_adjust_success', 'yong', '59671939751',
        '{\"data\":{\"amount\":10,\"orderId\":\"726476987818\",\"outBusinessNo\":\"yong_integral_20250329\",\"userId\":\"yong\"},\"id\":\"59671939751\",\"timestamp\":1743229532378}',
        'completed', '2025-03-29 14:25:32', '2025-03-29 14:25:32'),
       (16, 'credit_adjust_success', 'yong', '27397338862',
        '{\"data\":{\"amount\":-1.99,\"orderId\":\"667476948971\",\"outBusinessNo\":\"428416563416\",\"userId\":\"yong\"},\"id\":\"27397338862\",\"timestamp\":1743229568559}',
        'completed', '2025-03-29 14:26:08', '2025-03-29 14:26:08'),
       (17, 'credit_adjust_success', 'yong', '76592333205',
        '{\"data\":{\"amount\":-1.99,\"orderId\":\"066127599272\",\"outBusinessNo\":\"839301174685\",\"userId\":\"yong\"},\"id\":\"76592333205\",\"timestamp\":1743229572003}',
        'completed', '2025-03-29 14:26:12', '2025-03-29 14:26:12'),
       (18, 'credit_adjust_success', 'yong', '86944231987',
        '{\"data\":{\"amount\":-1.99,\"orderId\":\"519450013047\",\"outBusinessNo\":\"751928339311\",\"userId\":\"yong\"},\"id\":\"86944231987\",\"timestamp\":1743229573109}',
        'completed', '2025-03-29 14:26:13', '2025-03-29 14:26:13');
/*!40000 ALTER TABLE `task`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_award_record_000`
--

DROP TABLE IF EXISTS `user_award_record_000`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_award_record_000`
(
    `id`          int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`     varchar(32)  NOT NULL COMMENT '用户ID',
    `activity_id` bigint       NOT NULL COMMENT '活动ID',
    `strategy_id` bigint       NOT NULL COMMENT '抽奖策略ID',
    `order_id`    varchar(12)  NOT NULL COMMENT '抽奖单ID【作为幂等使用】',
    `award_id`    bigint       NOT NULL COMMENT '奖品ID',
    `award_title` varchar(128) NOT NULL COMMENT '奖品标题（名称）',
    `award_time`  datetime     NOT NULL COMMENT '中奖时间',
    `award_state` varchar(16)  NOT NULL DEFAULT 'create' COMMENT '奖品状态；create-创建、completed-发奖完成、、fail-发奖失败',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_award_id` (`strategy_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户中奖记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_award_record_000`
--

LOCK TABLES `user_award_record_000` WRITE;
/*!40000 ALTER TABLE `user_award_record_000`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `user_award_record_000`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_award_record_001`
--

DROP TABLE IF EXISTS `user_award_record_001`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_award_record_001`
(
    `id`          int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`     varchar(32)  NOT NULL COMMENT '用户ID',
    `activity_id` bigint       NOT NULL COMMENT '活动ID',
    `strategy_id` bigint       NOT NULL COMMENT '抽奖策略ID',
    `order_id`    varchar(12)  NOT NULL COMMENT '抽奖单ID【作为幂等使用】',
    `award_id`    bigint       NOT NULL COMMENT '奖品ID',
    `award_title` varchar(128) NOT NULL COMMENT '奖品标题（名称）',
    `award_time`  datetime     NOT NULL COMMENT '中奖时间',
    `award_state` varchar(16)  NOT NULL DEFAULT 'create' COMMENT '奖品状态；create-创建、completed-发奖完成、、fail-发奖失败',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_award_id` (`strategy_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户中奖记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_award_record_001`
--

LOCK TABLES `user_award_record_001` WRITE;
/*!40000 ALTER TABLE `user_award_record_001`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `user_award_record_001`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_award_record_002`
--

DROP TABLE IF EXISTS `user_award_record_002`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_award_record_002`
(
    `id`          int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`     varchar(32)  NOT NULL COMMENT '用户ID',
    `activity_id` bigint       NOT NULL COMMENT '活动ID',
    `strategy_id` bigint       NOT NULL COMMENT '抽奖策略ID',
    `order_id`    varchar(12)  NOT NULL COMMENT '抽奖单ID【作为幂等使用】',
    `award_id`    bigint       NOT NULL COMMENT '奖品ID',
    `award_title` varchar(128) NOT NULL COMMENT '奖品标题（名称）',
    `award_time`  datetime     NOT NULL COMMENT '中奖时间',
    `award_state` varchar(16)  NOT NULL DEFAULT 'create' COMMENT '奖品状态；create-创建、completed-发奖完成、、fail-发奖失败',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_award_id` (`strategy_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户中奖记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_award_record_002`
--

LOCK TABLES `user_award_record_002` WRITE;
/*!40000 ALTER TABLE `user_award_record_002`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `user_award_record_002`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_award_record_003`
--

DROP TABLE IF EXISTS `user_award_record_003`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_award_record_003`
(
    `id`          int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`     varchar(32)  NOT NULL COMMENT '用户ID',
    `activity_id` bigint       NOT NULL COMMENT '活动ID',
    `strategy_id` bigint       NOT NULL COMMENT '抽奖策略ID',
    `order_id`    varchar(12)  NOT NULL COMMENT '抽奖单ID【作为幂等使用】',
    `award_id`    bigint       NOT NULL COMMENT '奖品ID',
    `award_title` varchar(128) NOT NULL COMMENT '奖品标题（名称）',
    `award_time`  datetime     NOT NULL COMMENT '中奖时间',
    `award_state` varchar(16)  NOT NULL DEFAULT 'create' COMMENT '奖品状态；create-创建、completed-发奖完成、、fail-发奖失败',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_award_id` (`strategy_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户中奖记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_award_record_003`
--

LOCK TABLES `user_award_record_003` WRITE;
/*!40000 ALTER TABLE `user_award_record_003`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `user_award_record_003`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_behavior_rebate_order_000`
--

DROP TABLE IF EXISTS `user_behavior_rebate_order_000`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_behavior_rebate_order_000`
(
    `id`              int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`         varchar(32)  NOT NULL COMMENT '用户ID',
    `order_id`        varchar(12)  NOT NULL COMMENT '订单ID',
    `behavior_type`   varchar(16)  NOT NULL COMMENT '行为类型（sign 签到、openai_pay 支付）',
    `rebate_desc`     varchar(128) NOT NULL COMMENT '返利描述',
    `rebate_type`     varchar(16)  NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
    `rebate_config`   varchar(32)  NOT NULL COMMENT '返利配置【sku值，积分值】',
    `out_business_no` varchar(64)  NOT NULL COMMENT '业务仿重ID - 外部透传，方便查询使用',
    `biz_id`          varchar(64)  NOT NULL COMMENT '业务ID - 拼接的唯一值',
    `create_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    UNIQUE KEY `uq_biz_id` (`biz_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户行为返利流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_behavior_rebate_order_000`
--

LOCK TABLES `user_behavior_rebate_order_000` WRITE;
/*!40000 ALTER TABLE `user_behavior_rebate_order_000`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `user_behavior_rebate_order_000`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_behavior_rebate_order_001`
--

DROP TABLE IF EXISTS `user_behavior_rebate_order_001`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_behavior_rebate_order_001`
(
    `id`              int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`         varchar(32)  NOT NULL COMMENT '用户ID',
    `order_id`        varchar(12)  NOT NULL COMMENT '订单ID',
    `behavior_type`   varchar(16)  NOT NULL COMMENT '行为类型（sign 签到、openai_pay 支付）',
    `rebate_desc`     varchar(128) NOT NULL COMMENT '返利描述',
    `rebate_type`     varchar(16)  NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
    `rebate_config`   varchar(32)  NOT NULL COMMENT '返利配置【sku值，积分值】',
    `out_business_no` varchar(64)  NOT NULL COMMENT '业务仿重ID - 外部透传，方便查询使用',
    `biz_id`          varchar(64)  NOT NULL COMMENT '业务ID - 拼接的唯一值',
    `create_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    UNIQUE KEY `uq_biz_id` (`biz_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户行为返利流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_behavior_rebate_order_001`
--

LOCK TABLES `user_behavior_rebate_order_001` WRITE;
/*!40000 ALTER TABLE `user_behavior_rebate_order_001`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `user_behavior_rebate_order_001`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_behavior_rebate_order_002`
--

DROP TABLE IF EXISTS `user_behavior_rebate_order_002`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_behavior_rebate_order_002`
(
    `id`              int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`         varchar(32)  NOT NULL COMMENT '用户ID',
    `order_id`        varchar(12)  NOT NULL COMMENT '订单ID',
    `behavior_type`   varchar(16)  NOT NULL COMMENT '行为类型（sign 签到、openai_pay 支付）',
    `rebate_desc`     varchar(128) NOT NULL COMMENT '返利描述',
    `rebate_type`     varchar(16)  NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
    `rebate_config`   varchar(32)  NOT NULL COMMENT '返利配置【sku值，积分值】',
    `out_business_no` varchar(64)  NOT NULL COMMENT '业务仿重ID - 外部透传，方便查询使用',
    `biz_id`          varchar(64)  NOT NULL COMMENT '业务ID - 拼接的唯一值',
    `create_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    UNIQUE KEY `uq_biz_id` (`biz_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户行为返利流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_behavior_rebate_order_002`
--

LOCK TABLES `user_behavior_rebate_order_002` WRITE;
/*!40000 ALTER TABLE `user_behavior_rebate_order_002`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `user_behavior_rebate_order_002`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_behavior_rebate_order_003`
--

DROP TABLE IF EXISTS `user_behavior_rebate_order_003`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_behavior_rebate_order_003`
(
    `id`              int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`         varchar(32)  NOT NULL COMMENT '用户ID',
    `order_id`        varchar(12)  NOT NULL COMMENT '订单ID',
    `behavior_type`   varchar(16)  NOT NULL COMMENT '行为类型（sign 签到、openai_pay 支付）',
    `rebate_desc`     varchar(128) NOT NULL COMMENT '返利描述',
    `rebate_type`     varchar(16)  NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
    `rebate_config`   varchar(32)  NOT NULL COMMENT '返利配置【sku值，积分值】',
    `out_business_no` varchar(64)  NOT NULL COMMENT '业务仿重ID - 外部透传，方便查询使用',
    `biz_id`          varchar(64)  NOT NULL COMMENT '业务ID - 拼接的唯一值',
    `create_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    UNIQUE KEY `uq_biz_id` (`biz_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户行为返利流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_behavior_rebate_order_003`
--

LOCK TABLES `user_behavior_rebate_order_003` WRITE;
/*!40000 ALTER TABLE `user_behavior_rebate_order_003`
    DISABLE KEYS */;
INSERT INTO `user_behavior_rebate_order_003`
VALUES (9, 'yong', '050951071257', 'sign', '签到返利-抽奖额度', 'sku', '9011', '20250329', 'yong_sku_20250329',
        '2025-03-29 14:25:32', '2025-03-29 14:25:32'),
       (10, 'yong', '742588255840', 'sign', '签到返利-积分', 'integral', '10', '20250329', 'yong_integral_20250329',
        '2025-03-29 14:25:32', '2025-03-29 14:25:32');
/*!40000 ALTER TABLE `user_behavior_rebate_order_003`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_credit_order_000`
--

DROP TABLE IF EXISTS `user_credit_order_000`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_credit_order_000`
(
    `id`              bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`         varchar(32)     NOT NULL COMMENT '用户ID',
    `order_id`        varchar(12)     NOT NULL COMMENT '订单ID',
    `trade_name`      varchar(32)     NOT NULL COMMENT '交易名称',
    `trade_type`      varchar(8)      NOT NULL DEFAULT 'forward' COMMENT '交易类型；forward-正向、reverse-逆向',
    `trade_amount`    decimal(10, 2)  NOT NULL COMMENT '交易金额',
    `out_business_no` varchar(64)     NOT NULL COMMENT '业务仿重ID - 外部透传。返利、行为等唯一标识',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户积分流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_credit_order_000`
--

LOCK TABLES `user_credit_order_000` WRITE;
/*!40000 ALTER TABLE `user_credit_order_000`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `user_credit_order_000`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_credit_order_001`
--

DROP TABLE IF EXISTS `user_credit_order_001`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_credit_order_001`
(
    `id`              bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`         varchar(32)     NOT NULL COMMENT '用户ID',
    `order_id`        varchar(12)     NOT NULL COMMENT '订单ID',
    `trade_name`      varchar(32)     NOT NULL COMMENT '交易名称',
    `trade_type`      varchar(8)      NOT NULL DEFAULT 'forward' COMMENT '交易类型；forward-正向、reverse-逆向',
    `trade_amount`    decimal(10, 2)  NOT NULL COMMENT '交易金额',
    `out_business_no` varchar(64)     NOT NULL COMMENT '业务仿重ID - 外部透传。返利、行为等唯一标识',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户积分流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_credit_order_001`
--

LOCK TABLES `user_credit_order_001` WRITE;
/*!40000 ALTER TABLE `user_credit_order_001`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `user_credit_order_001`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_credit_order_002`
--

DROP TABLE IF EXISTS `user_credit_order_002`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_credit_order_002`
(
    `id`              bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`         varchar(32)     NOT NULL COMMENT '用户ID',
    `order_id`        varchar(12)     NOT NULL COMMENT '订单ID',
    `trade_name`      varchar(32)     NOT NULL COMMENT '交易名称',
    `trade_type`      varchar(8)      NOT NULL DEFAULT 'forward' COMMENT '交易类型；forward-正向、reverse-逆向',
    `trade_amount`    decimal(10, 2)  NOT NULL COMMENT '交易金额',
    `out_business_no` varchar(64)     NOT NULL COMMENT '业务仿重ID - 外部透传。返利、行为等唯一标识',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户积分流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_credit_order_002`
--

LOCK TABLES `user_credit_order_002` WRITE;
/*!40000 ALTER TABLE `user_credit_order_002`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `user_credit_order_002`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_credit_order_003`
--

DROP TABLE IF EXISTS `user_credit_order_003`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_credit_order_003`
(
    `id`              bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`         varchar(32)     NOT NULL COMMENT '用户ID',
    `order_id`        varchar(12)     NOT NULL COMMENT '订单ID',
    `trade_name`      varchar(32)     NOT NULL COMMENT '交易名称',
    `trade_type`      varchar(8)      NOT NULL DEFAULT 'forward' COMMENT '交易类型；forward-正向、reverse-逆向',
    `trade_amount`    decimal(10, 2)  NOT NULL COMMENT '交易金额',
    `out_business_no` varchar(64)     NOT NULL COMMENT '业务仿重ID - 外部透传。返利、行为等唯一标识',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户积分流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_credit_order_003`
--

LOCK TABLES `user_credit_order_003` WRITE;
/*!40000 ALTER TABLE `user_credit_order_003`
    DISABLE KEYS */;
INSERT INTO `user_credit_order_003`
VALUES (5, 'yong', '726476987818', '每日签到', 'forward', 10.00, 'yong_integral_20250329', '2025-03-29 14:25:32',
        '2025-03-29 14:25:32'),
       (6, 'yong', '667476948971', '积分兑换', 'reverse', -1.99, '428416563416', '2025-03-29 14:26:08',
        '2025-03-29 14:26:08'),
       (7, 'yong', '066127599272', '积分兑换', 'reverse', -1.99, '839301174685', '2025-03-29 14:26:12',
        '2025-03-29 14:26:12'),
       (8, 'yong', '519450013047', '积分兑换', 'reverse', -1.99, '751928339311', '2025-03-29 14:26:13',
        '2025-03-29 14:26:13');
/*!40000 ALTER TABLE `user_credit_order_003`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_order_000`
--

DROP TABLE IF EXISTS `user_order_000`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_order_000`
(
    `id`            int unsigned NOT NULL AUTO_INCREMENT,
    `user_id`       varchar(32)  NOT NULL COMMENT '用户ID',
    `activity_id`   bigint       NOT NULL COMMENT '活动ID',
    `order_id`      varchar(12)  NOT NULL COMMENT '抽奖单ID',
    `activity_name` varchar(64)  NOT NULL COMMENT '活动名称',
    `strategy_id`   bigint       NOT NULL COMMENT '抽奖策略ID',
    `order_time`    datetime     NOT NULL COMMENT '下单时间',
    `order_state`   varchar(16)  NOT NULL DEFAULT 'create' COMMENT '订单状态；create-创建、used-已使用、cancel-已作废',
    `create_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户抽奖单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_order_000`
--

LOCK TABLES `user_order_000` WRITE;
/*!40000 ALTER TABLE `user_order_000`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `user_order_000`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_order_001`
--

DROP TABLE IF EXISTS `user_order_001`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_order_001`
(
    `id`            int unsigned NOT NULL AUTO_INCREMENT,
    `user_id`       varchar(32)  NOT NULL COMMENT '用户ID',
    `activity_id`   bigint       NOT NULL COMMENT '活动ID',
    `order_id`      varchar(12)  NOT NULL COMMENT '抽奖单ID',
    `activity_name` varchar(64)  NOT NULL COMMENT '活动名称',
    `strategy_id`   bigint       NOT NULL COMMENT '抽奖策略ID',
    `order_time`    datetime     NOT NULL COMMENT '下单时间',
    `order_state`   varchar(16)  NOT NULL DEFAULT 'create' COMMENT '订单状态；create-创建、used-已使用、cancel-已作废',
    `create_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户抽奖单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_order_001`
--

LOCK TABLES `user_order_001` WRITE;
/*!40000 ALTER TABLE `user_order_001`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `user_order_001`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_order_002`
--

DROP TABLE IF EXISTS `user_order_002`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_order_002`
(
    `id`            int unsigned NOT NULL AUTO_INCREMENT,
    `user_id`       varchar(32)  NOT NULL COMMENT '用户ID',
    `activity_id`   bigint       NOT NULL COMMENT '活动ID',
    `order_id`      varchar(12)  NOT NULL COMMENT '抽奖单ID',
    `activity_name` varchar(64)  NOT NULL COMMENT '活动名称',
    `strategy_id`   bigint       NOT NULL COMMENT '抽奖策略ID',
    `order_time`    datetime     NOT NULL COMMENT '下单时间',
    `order_state`   varchar(16)  NOT NULL DEFAULT 'create' COMMENT '订单状态；create-创建、used-已使用、cancel-已作废',
    `create_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户抽奖单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_order_002`
--

LOCK TABLES `user_order_002` WRITE;
/*!40000 ALTER TABLE `user_order_002`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `user_order_002`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_order_003`
--

DROP TABLE IF EXISTS `user_order_003`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_order_003`
(
    `id`            int unsigned NOT NULL AUTO_INCREMENT,
    `user_id`       varchar(32)  NOT NULL COMMENT '用户ID',
    `activity_id`   bigint       NOT NULL COMMENT '活动ID',
    `order_id`      varchar(12)  NOT NULL COMMENT '抽奖单ID',
    `activity_name` varchar(64)  NOT NULL COMMENT '活动名称',
    `strategy_id`   bigint       NOT NULL COMMENT '抽奖策略ID',
    `order_time`    datetime     NOT NULL COMMENT '下单时间',
    `order_state`   varchar(16)  NOT NULL DEFAULT 'create' COMMENT '订单状态；create-创建、used-已使用、cancel-已作废',
    `create_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户抽奖单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_order_003`
--

LOCK TABLES `user_order_003` WRITE;
/*!40000 ALTER TABLE `user_order_003`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `user_order_003`
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

-- Dump completed on 2025-05-06 22:08:08
