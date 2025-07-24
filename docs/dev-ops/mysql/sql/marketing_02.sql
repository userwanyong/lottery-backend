CREATE DATABASE /*!32312 IF NOT EXISTS */ `marketing_02` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;
USE `marketing_02`;
-- MySQL dump 10.13  Distrib 8.0.35, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: marketing_02
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
-- Table structure for table `activity_account`
--

DROP TABLE IF EXISTS `activity_account`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='抽奖账户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_account`
--

LOCK TABLES `activity_account` WRITE;
/*!40000 ALTER TABLE `activity_account`
    DISABLE KEYS */;
INSERT INTO `activity_account`
VALUES (1946786050068213763, 'yong', 100301, 1000096, 999054, 1000096, 999054, 1000096, 999054, '2025-07-24 15:46:16',
        '2025-07-24 15:47:04'),
       (1947290016128466946, 'yong', 1934223919414280193, 10036, 8032, 10036, 8032, 10036, 8032, '2025-07-21 21:38:12',
        '2025-07-24 14:42:15'),
       (1947312484868177923, 'yong', 1947194890802733058, 66, 66, 66, 66, 66, 66, '2025-07-24 14:49:20',
        '2025-07-24 14:50:18'),
       (1948240418177056770, 'yong2', 1947194890802733058, 5, 5, 5, 5, 5, 5, '2025-07-24 12:34:46',
        '2025-07-24 12:34:46'),
       (1948271504626036737, 'yong', 1948000932272812034, 18, 8, 18, 8, 18, 8, '2025-07-24 14:38:18',
        '2025-07-24 14:58:26');
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
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='抽奖账户表-日次数';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_account_day`
--

LOCK TABLES `activity_account_day` WRITE;
/*!40000 ALTER TABLE `activity_account_day`
    DISABLE KEYS */;
INSERT INTO `activity_account_day`
VALUES (1946786554139668482, 'yong', 100301, '2025-07-20', 999042, 999017, '2025-07-20 12:17:39',
        '2025-07-20 12:22:32'),
       (1947289921769209858, 'yong', 100301, '2025-07-21', 999018, 999013, '2025-07-21 21:37:51',
        '2025-07-21 22:50:57'),
       (1947552033808662529, 'yong', 1947194890802733058, '2025-07-22', 6022, 6010, '2025-07-22 14:59:23',
        '2025-07-22 15:04:08'),
       (1947553252174282754, 'yong', 100301, '2025-07-22', 999014, 999013, '2025-07-22 15:04:13',
        '2025-07-22 15:04:13'),
       (1947941284995956737, 'yong', 100301, '2025-07-23', 999020, 999015, '2025-07-23 16:46:07',
        '2025-07-23 16:57:10'),
       (1947944201605914625, 'yong', 1934223919414280193, '2025-07-23', 8008, 8006, '2025-07-23 16:57:42',
        '2025-07-23 16:57:49'),
       (1948271545558249473, 'yong', 1948000932272812034, '2025-07-24', 18, 8, '2025-07-24 14:38:28',
        '2025-07-24 14:58:26'),
       (1948280493195780098, 'yong', 100301, '2025-07-24', 999061, 999054, '2025-07-24 15:14:01',
        '2025-07-24 15:47:04');
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
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='抽奖账户表-月次数';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_account_month`
--

LOCK TABLES `activity_account_month` WRITE;
/*!40000 ALTER TABLE `activity_account_month`
    DISABLE KEYS */;
INSERT INTO `activity_account_month`
VALUES (1946786554139668481, 'yong', 100301, '2025-07', 999097, 999054, '2025-07-20 12:17:39', '2025-07-24 15:47:04'),
       (1947944201538805761, 'yong', 1934223919414280193, '2025-07', 8034, 8032, '2025-07-23 16:57:42',
        '2025-07-24 14:42:15'),
       (1948271545495334913, 'yong', 1948000932272812034, '2025-07', 18, 8, '2025-07-24 14:38:28',
        '2025-07-24 14:58:26');
/*!40000 ALTER TABLE `activity_account_month`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_record_000`
--

DROP TABLE IF EXISTS `activity_record_000`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_record_000`
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
    `out_business_no` varchar(100)    NOT NULL COMMENT '保证幂等，不会重复消费',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`, `state`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户抽奖额度记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_record_000`
--

LOCK TABLES `activity_record_000` WRITE;
/*!40000 ALTER TABLE `activity_record_000`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `activity_record_000`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_record_001`
--

DROP TABLE IF EXISTS `activity_record_001`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_record_001`
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
    `out_business_no` varchar(100)    NOT NULL COMMENT '保证幂等，不会重复消费',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`, `state`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户抽奖额度记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_record_001`
--

LOCK TABLES `activity_record_001` WRITE;
/*!40000 ALTER TABLE `activity_record_001`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `activity_record_001`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_record_002`
--

DROP TABLE IF EXISTS `activity_record_002`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_record_002`
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
    `out_business_no` varchar(100)    NOT NULL COMMENT '保证幂等，不会重复消费',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`, `state`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户抽奖额度记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_record_002`
--

LOCK TABLES `activity_record_002` WRITE;
/*!40000 ALTER TABLE `activity_record_002`
    DISABLE KEYS */;
INSERT INTO `activity_record_002`
VALUES (1948240418114142209, 'yong2', 1947194890802733058, NULL, NULL, 5, 5, 5, 0.00, 'completed',
        'yong2_1947194890802733058_gift_5_20250724', '2025-07-24 12:34:46', '2025-07-24 12:34:46');
/*!40000 ALTER TABLE `activity_record_002`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_record_003`
--

DROP TABLE IF EXISTS `activity_record_003`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_record_003`
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
    `out_business_no` varchar(100)    NOT NULL COMMENT '保证幂等，不会重复消费',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`, `state`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户抽奖额度记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_record_003`
--

LOCK TABLES `activity_record_003` WRITE;
/*!40000 ALTER TABLE `activity_record_003`
    DISABLE KEYS */;
INSERT INTO `activity_record_003`
VALUES (1946786050068213762, 'yong', 100301, 9011, '测试活动', 1, 1, 1, 0.00, 'completed',
        'yong_100301_sku_9011_20250720', '2025-07-20 12:15:38', '2025-07-20 12:15:38'),
       (1946786774852333569, 'yong', 100301, 9011, '测试活动', 1, 1, 1, 1.99, 'completed', '564159026792',
        '2025-07-20 12:18:30', '2025-07-20 12:18:30'),
       (1946786776857210881, 'yong', 100301, 9012, '测试活动', 5, 5, 5, 5.99, 'completed', '997479956334',
        '2025-07-20 12:18:31', '2025-07-20 12:18:31'),
       (1946786781210898433, 'yong', 100301, 9011, '测试活动', 1, 1, 1, 1.99, 'completed', '982741481760',
        '2025-07-20 12:18:33', '2025-07-20 12:18:33'),
       (1946786782964117505, 'yong', 100301, 9012, '测试活动', 5, 5, 5, 5.99, 'completed', '759211064374',
        '2025-07-20 12:18:33', '2025-07-20 12:18:33'),
       (1946786788576096258, 'yong', 100301, 9012, '测试活动', 5, 5, 5, 5.99, 'completed', '911329561191',
        '2025-07-20 12:18:35', '2025-07-20 12:18:35'),
       (1946786793454071810, 'yong', 100301, 9012, '测试活动', 5, 5, 5, 5.99, 'completed', '134190325867',
        '2025-07-20 12:18:36', '2025-07-20 12:18:36'),
       (1946786797044396034, 'yong', 100301, 9012, '测试活动', 5, 5, 5, 5.99, 'completed', '728869427430',
        '2025-07-20 12:18:37', '2025-07-20 12:18:37'),
       (1946786801142231042, 'yong', 100301, 9012, '测试活动', 5, 5, 5, 5.99, 'completed', '040392134000',
        '2025-07-20 12:18:38', '2025-07-20 12:18:38'),
       (1946786805034545154, 'yong', 100301, 9012, '测试活动', 5, 5, 5, 5.99, 'completed', '489587779150',
        '2025-07-20 12:18:38', '2025-07-20 12:18:38'),
       (1946786808486457346, 'yong', 100301, 9012, '测试活动', 5, 5, 5, 5.99, 'completed', '826944825874',
        '2025-07-20 12:18:39', '2025-07-20 12:18:39'),
       (1947125849266442242, 'yong', 100301, 9011, '测试活动', 1, 1, 1, 0.00, 'completed',
        'yong_100301_sku_9011_20250721', '2025-07-21 10:45:52', '2025-07-21 10:45:52'),
       (1947312484868177922, 'yong', 1947194890802733058, 1947277048632332290, '一起来抽奖', 1, 1, 1, 0.00, 'completed',
        'yong_1947194890802733058_sku_1947277048632332290_20250721', '2025-07-21 23:07:31', '2025-07-21 23:07:31'),
       (1947318934151630849, 'yong', 1947194890802733058, 1947277048632332290, '一起来抽奖', 1, 1, 1, 5.99, 'completed',
        '651642341112', '2025-07-21 23:33:07', '2025-07-21 23:33:07'),
       (1947318942548627457, 'yong', 1947194890802733058, 1947277048632332290, '一起来抽奖', 1, 1, 1, 5.99, 'completed',
        '945857012249', '2025-07-21 23:33:09', '2025-07-21 23:33:09'),
       (1947318950329061378, 'yong', 1947194890802733058, 1947294503257800705, '一起来抽奖', 5, 5, 5, 24.99,
        'completed', '914819549386', '2025-07-21 23:33:11', '2025-07-21 23:33:11'),
       (1947318957732007938, 'yong', 1947194890802733058, 1947277048632332290, '一起来抽奖', 1, 1, 1, 5.99, 'completed',
        '476006310105', '2025-07-21 23:33:14', '2025-07-21 23:33:14'),
       (1947321602161008642, 'yong', 1947194890802733058, 1947277048632332290, '一起来抽奖', 1, 1, 1, 5.99, 'completed',
        '975762561529', '2025-07-21 23:43:44', '2025-07-21 23:43:44'),
       (1947321606879600642, 'yong', 1947194890802733058, 1947294503257800705, '一起来抽奖', 5, 5, 5, 24.99,
        'completed', '951298635576', '2025-07-21 23:43:46', '2025-07-21 23:43:46'),
       (1947323392252276738, 'yong', 1947194890802733058, 1947277048632332290, '一起来抽奖', 1, 1, 1, 5.99, 'completed',
        '046103325724', '2025-07-21 23:50:51', '2025-07-21 23:50:51'),
       (1947323395230232577, 'yong', 1947194890802733058, 1947294503257800705, '一起来抽奖', 5, 5, 5, 24.99,
        'completed', '764076145907', '2025-07-21 23:50:51', '2025-07-21 23:50:51'),
       (1947323698818150402, 'yong', 1947194890802733058, 1947294503257800705, '一起来抽奖', 5, 5, 5, 24.99,
        'completed', '321947260579', '2025-07-21 23:52:03', '2025-07-21 23:52:03'),
       (1947326166411010049, 'yong', 1947194890802733058, 1947277048632332290, '一起来抽奖', 1, 1, 1, 5.99, 'completed',
        '831470545248', '2025-07-22 00:01:52', '2025-07-22 00:01:52'),
       (1947326178238947330, 'yong', 1947194890802733058, 1947294503257800705, '一起来抽奖', 5, 5, 5, 24.99,
        'completed', '619607969786', '2025-07-22 00:01:55', '2025-07-22 00:01:55'),
       (1947496103695937538, 'yong', 1947194890802733058, 1947277048632332290, '一起来抽奖', 1, 1, 1, 0.00, 'completed',
        'yong_1947194890802733058_sku_1947277048632332290_20250722', '2025-07-22 11:17:08', '2025-07-22 11:17:08'),
       (1947496393887248385, 'yong', 1947194890802733058, 1947294317592739842, '一起来抽奖', 10, 10, 10, 45.99,
        'completed', '914463904655', '2025-07-22 11:18:17', '2025-07-22 11:18:17'),
       (1947496405555802113, 'yong', 1947194890802733058, 1947294503257800705, '一起来抽奖', 5, 5, 5, 24.99,
        'completed', '594802838564', '2025-07-22 11:18:20', '2025-07-22 11:18:20'),
       (1947511379091111937, 'yong', 1947194890802733058, 1947294503257800705, '一起来抽奖', 5, 5, 5, 24.99,
        'completed', '617559309177', '2025-07-22 12:17:50', '2025-07-22 12:17:50'),
       (1947511389585260545, 'yong', 1947194890802733058, 1947277048632332290, '一起来抽奖', 1, 1, 1, 5.99, 'completed',
        '695197656089', '2025-07-22 12:17:52', '2025-07-22 12:17:52'),
       (1947552640044974081, 'yong', 1947194890802733058, 1947276284354646017, '一起来抽奖', 20, 20, 20, 79.99,
        'completed', '812938148277', '2025-07-22 15:01:47', '2025-07-22 15:01:47'),
       (1947552664275468289, 'yong', 1947194890802733058, 1947277048632332290, '一起来抽奖', 1, 1, 1, 5.99, 'completed',
        '967125988927', '2025-07-22 15:01:53', '2025-07-22 15:01:53'),
       (1947553201725194242, 'yong', 100301, 9011, '测试活动', 1, 1, 1, 0.00, 'completed',
        'yong_100301_sku_9011_20250722', '2025-07-22 15:04:01', '2025-07-22 15:04:01'),
       (1947880720219242498, 'yong', 1947194890802733058, 1947277048632332290, '一起来抽奖', 1, 1, 1, 0.00, 'completed',
        'yong_1947194890802733058_sku_1947277048632332290_20250723', '2025-07-23 12:45:29', '2025-07-23 12:45:29'),
       (1947932764468674561, 'yong', 1947194890802733058, 1947277048632332290, '一起来抽奖', 1, 1, 1, 5.99, 'completed',
        '143358366359', '2025-07-23 16:12:16', '2025-07-23 16:12:16'),
       (1947932772286857217, 'yong', 1947194890802733058, 1947277048632332290, '一起来抽奖', 1, 1, 1, 5.99, 'completed',
        '260039945767', '2025-07-23 16:12:18', '2025-07-23 16:12:18'),
       (1947938626188955650, 'yong', 1947194890802733058, 1947277048632332290, '一起来抽奖', 1, 1, 1, 5.99, 'completed',
        '038523139152', '2025-07-23 16:35:34', '2025-07-23 16:35:34'),
       (1947938628202221569, 'yong', 1947194890802733058, 1947294503257800705, '一起来抽奖', 5, 5, 5, 24.99,
        'completed', '229077811965', '2025-07-23 16:35:34', '2025-07-23 16:35:34'),
       (1947940315771994113, 'yong', 1947194890802733058, 1947277048632332290, '一起来抽奖', 1, 1, 1, 5.99, 'completed',
        '571814659079', '2025-07-23 16:42:17', '2025-07-23 16:42:17'),
       (1947940318359879682, 'yong', 1947194890802733058, 1947294503257800705, '一起来抽奖', 5, 5, 5, 24.99,
        'completed', '487058719004', '2025-07-23 16:42:17', '2025-07-23 16:42:17'),
       (1947944011595554817, 'yong', 100301, 9011, '测试活动', 1, 1, 1, 1.99, 'completed', '466282722375',
        '2025-07-23 16:56:58', '2025-07-23 16:56:58'),
       (1947944012954509313, 'yong', 100301, 9012, '测试活动', 5, 5, 5, 5.99, 'completed', '262893498133',
        '2025-07-23 16:56:58', '2025-07-23 16:56:58'),
       (1947944031174565890, 'yong', 100301, 9011, '测试活动', 1, 1, 1, 0.00, 'completed',
        'yong_100301_sku_9011_20250723', '2025-07-23 16:57:02', '2025-07-23 16:57:02'),
       (1947944104734269441, 'yong', 1934223919414280193, 1934859819538202625, '活动v1', 10, 10, 10, 0.00, 'completed',
        'yong_1934223919414280193_sku_1934859819538202625_20250723', '2025-07-23 16:57:18', '2025-07-23 16:57:18'),
       (1948033317005787137, 'yong', 1947194890802733058, NULL, NULL, 10, 10, 10, 0.00, 'completed',
        'yong_1947194890802733058_gift_10_20250723', '2025-07-23 22:51:48', '2025-07-23 22:51:48'),
       (1948214823829393409, 'yong', 1947194890802733058, 1947277048632332290, '一起来抽奖', 1, 1, 1, 0.00, 'completed',
        'yong_1947194890802733058_sku_1947277048632332290_20250724', '2025-07-24 10:53:04', '2025-07-24 10:53:04'),
       (1948235771374735362, 'yong', 1947194890802733058, NULL, NULL, 5, 5, 5, 0.00, 'completed',
        'yong_1947194890802733058_gift_5_20250724', '2025-07-24 12:16:19', '2025-07-24 12:16:19'),
       (1948240922370146306, 'yong', 1947194890802733058, NULL, NULL, 12, 12, 12, 0.00, 'completed',
        'yong_1947194890802733058_gift_12_20250724', '2025-07-24 12:36:47', '2025-07-24 12:36:47'),
       (1948240923158675458, 'yong', 1947194890802733058, NULL, NULL, 1, 1, 1, 0.00, 'completed',
        'yong_1947194890802733058_gift_1_20250724', '2025-07-24 12:36:47', '2025-07-24 12:36:47'),
       (1948248138812784642, 'yong', 1947194890802733058, NULL, NULL, 12, 12, 12, 0.00, 'completed',
        'yong_1947194890802733058_gift_1948240805508448258_20250724', '2025-07-24 13:05:27', '2025-07-24 13:05:27'),
       (1948248139458707457, 'yong', 1947194890802733058, NULL, NULL, 1, 1, 1, 0.00, 'completed',
        'yong_1947194890802733058_gift_1948240881718951938_20250724', '2025-07-24 13:05:27', '2025-07-24 13:05:27'),
       (1948262982215643138, 'yong', 1948000932272812034, NULL, NULL, 5, 5, 5, 0.00, 'completed',
        'yong_1948000932272812034_gift_1948260349270667265_20250724', '2025-07-24 14:04:26', '2025-07-24 14:04:26'),
       (1948262982861565953, 'yong', 1948000932272812034, NULL, NULL, 10, 10, 10, 0.00, 'completed',
        'yong_1948000932272812034_gift_1948262879291617281_20250724', '2025-07-24 14:04:26', '2025-07-24 14:04:26'),
       (1948266782666010625, 'yong', 100301, NULL, NULL, 10, 10, 10, 0.00, 'completed',
        'yong_100301_gift_1948262879291617281_20250724', '2025-07-24 14:19:32', '2025-07-24 14:19:32'),
       (1948267853761228801, 'yong', 100301, 9011, '测试活动', 1, 1, 1, 1.99, 'completed', '537455864590',
        '2025-07-24 14:23:48', '2025-07-24 14:23:48'),
       (1948267882974556161, 'yong', 100301, 9011, '测试活动', 1, 1, 1, 1.99, 'completed', '374572616015',
        '2025-07-24 14:23:55', '2025-07-24 14:23:55'),
       (1948267891325415425, 'yong', 100301, 9011, '测试活动', 1, 1, 1, 1.99, 'completed', '280454051698',
        '2025-07-24 14:23:57', '2025-07-24 14:23:57'),
       (1948267901974753282, 'yong', 100301, 9012, '测试活动', 5, 5, 5, 5.99, 'completed', '894409931218',
        '2025-07-24 14:23:59', '2025-07-24 14:23:59'),
       (1948267910866677762, 'yong', 100301, 9012, '测试活动', 5, 5, 5, 5.99, 'completed', '394888122339',
        '2025-07-24 14:24:01', '2025-07-24 14:24:01'),
       (1948267914968707073, 'yong', 100301, 9012, '测试活动', 5, 5, 5, 5.99, 'completed', '376754640635',
        '2025-07-24 14:24:02', '2025-07-24 14:24:02'),
       (1948267918345121794, 'yong', 100301, 9012, '测试活动', 5, 5, 5, 5.99, 'completed', '581348765564',
        '2025-07-24 14:24:03', '2025-07-24 14:24:03'),
       (1948267921197248513, 'yong', 100301, 9012, '测试活动', 5, 5, 5, 5.99, 'completed', '383073198115',
        '2025-07-24 14:24:04', '2025-07-24 14:24:04'),
       (1948268207315890178, 'yong', 100301, NULL, NULL, 5, 5, 5, 0.00, 'completed',
        'yong_100301_gift_1948260349270667265_20250724', '2025-07-24 14:25:12', '2025-07-24 14:25:12'),
       (1948270410558615554, 'yong', 1934223919414280193, NULL, NULL, 10, 10, 10, 0.00, 'completed',
        'yong_1934223919414280193_gift_1948262879291617281_20250724', '2025-07-24 14:33:57', '2025-07-24 14:33:57'),
       (1948270582634131458, 'yong', 1934223919414280193, NULL, NULL, 5, 5, 5, 0.00, 'completed',
        'yong_1934223919414280193_gift_1948260349270667265_20250724', '2025-07-24 14:34:38', '2025-07-24 14:34:38'),
       (1948271504496013314, 'yong', 1948000932272812034, NULL, NULL, 13, 13, 13, 0.00, 'completed',
        'yong_1948000932272812034_gift_1948271313437077505_20250724', '2025-07-24 14:38:18', '2025-07-24 14:38:18'),
       (1948271522665738241, 'yong', 1948000932272812034, NULL, NULL, 5, 5, 5, 0.00, 'completed',
        'yong_1948000932272812034_gift_1948271061317464066_20250724', '2025-07-24 14:38:22', '2025-07-24 14:38:22'),
       (1948272466493190146, 'yong', 1934223919414280193, 1934859819538202625, '活动v1', 10, 10, 10, 0.00, 'completed',
        'yong_1934223919414280193_sku_1934927347346096129_20250724', '2025-07-24 14:42:07', '2025-07-24 14:42:07'),
       (1948272498298597378, 'yong', 1934223919414280193, NULL, NULL, 1, 1, 1, 0.00, 'completed',
        'yong_1934223919414280193_gift_1948272437883842561_2025', '2025-07-24 14:42:15', '2025-07-24 14:42:15'),
       (1948274282698235906, 'yong', 1947194890802733058, 1947277048632332290, '一起来抽奖', 1, 1, 1, 5.99, 'completed',
        '159218864705', '2025-07-24 14:49:20', '2025-07-24 14:49:20'),
       (1948274524403392514, 'yong', 1947194890802733058, NULL, NULL, 3, 3, 3, 0.00, 'completed',
        'yong_1947194890802733058_gift_1948274480673579009_2025', '2025-07-24 14:50:18', '2025-07-24 14:50:18'),
       (1948288418672840705, 'yong', 100301, 9011, '测试活动', 1, 1, 1, 0.00, 'completed', 'yong_100301_sku_1_20250724',
        '2025-07-24 15:45:31', '2025-07-24 15:45:31'),
       (1948288607970168834, 'yong', 100301, 9011, '测试活动', 1, 1, 1, 1.99, 'completed', '643122837883',
        '2025-07-24 15:46:16', '2025-07-24 15:46:16'),
       (1948288811800760321, 'yong', 100301, NULL, NULL, 1, 1, 1, 0.00, 'completed',
        'yong_100301_gift_1948288777088700417_2025', '2025-07-24 15:47:04', '2025-07-24 15:47:04');
/*!40000 ALTER TABLE `activity_record_003`
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
    `id`               bigint unsigned NOT NULL COMMENT '雪花ID',
    `activity_id`      bigint          NOT NULL COMMENT '活动ID',
    `user_id`          varchar(32)     NOT NULL COMMENT '用户ID',
    `total_amount`     decimal(10, 2)  NOT NULL COMMENT '总积分，显示总账户值，记得一个人获得的总积分',
    `available_amount` decimal(10, 2)  NOT NULL COMMENT '可用积分，每次扣减的值',
    `account_status`   varchar(8)      NOT NULL COMMENT '账户状态【open - 可用，close - 冻结】',
    `create_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
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
VALUES (1946786050714136578, 100301, 'yong', 1635.05, 1537.26, 'open', '2025-07-20 12:15:38', '2025-07-24 15:46:16'),
       (1947290013867737090, 1934223919414280193, 'yong', 371.80, 371.80, 'open', '2025-07-21 21:38:12',
        '2025-07-24 14:42:07'),
       (1947312485581209601, 1947194890802733058, 'yong', 694.00, 265.24, 'open', '2025-07-21 23:07:31',
        '2025-07-24 14:49:20'),
       (1948271570354974722, 1948000932272812034, 'yong', 51.80, 51.80, 'open', '2025-07-24 14:38:34',
        '2025-07-24 14:46:20');
/*!40000 ALTER TABLE `credit_account`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credit_record_000`
--

DROP TABLE IF EXISTS `credit_record_000`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `credit_record_000`
(
    `id`              bigint unsigned NOT NULL COMMENT '雪花ID',
    `user_id`         varchar(32)     NOT NULL COMMENT '用户ID',
    `activity_id`     bigint          NOT NULL COMMENT '活动ID',
    `trade_name`      varchar(32)     NOT NULL COMMENT '交易名称',
    `trade_type`      varchar(8)      NOT NULL DEFAULT 'forward' COMMENT '交易类型；forward-正向、reverse-逆向',
    `trade_amount`    decimal(10, 2)  NOT NULL COMMENT '交易金额',
    `out_business_no` varchar(100)    NOT NULL COMMENT '业务仿重ID - 外部透传。返利、行为等唯一标识',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户积分记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credit_record_000`
--

LOCK TABLES `credit_record_000` WRITE;
/*!40000 ALTER TABLE `credit_record_000`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `credit_record_000`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credit_record_001`
--

DROP TABLE IF EXISTS `credit_record_001`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `credit_record_001`
(
    `id`              bigint unsigned NOT NULL COMMENT '雪花ID',
    `user_id`         varchar(32)     NOT NULL COMMENT '用户ID',
    `activity_id`     bigint          NOT NULL COMMENT '活动ID',
    `trade_name`      varchar(32)     NOT NULL COMMENT '交易名称',
    `trade_type`      varchar(8)      NOT NULL DEFAULT 'forward' COMMENT '交易类型；forward-正向、reverse-逆向',
    `trade_amount`    decimal(10, 2)  NOT NULL COMMENT '交易金额',
    `out_business_no` varchar(100)    NOT NULL COMMENT '业务仿重ID - 外部透传。返利、行为等唯一标识',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户积分记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credit_record_001`
--

LOCK TABLES `credit_record_001` WRITE;
/*!40000 ALTER TABLE `credit_record_001`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `credit_record_001`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credit_record_002`
--

DROP TABLE IF EXISTS `credit_record_002`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `credit_record_002`
(
    `id`              bigint unsigned NOT NULL COMMENT '雪花ID',
    `user_id`         varchar(32)     NOT NULL COMMENT '用户ID',
    `activity_id`     bigint          NOT NULL COMMENT '活动ID',
    `trade_name`      varchar(32)     NOT NULL COMMENT '交易名称',
    `trade_type`      varchar(8)      NOT NULL DEFAULT 'forward' COMMENT '交易类型；forward-正向、reverse-逆向',
    `trade_amount`    decimal(10, 2)  NOT NULL COMMENT '交易金额',
    `out_business_no` varchar(100)    NOT NULL COMMENT '业务仿重ID - 外部透传。返利、行为等唯一标识',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户积分记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credit_record_002`
--

LOCK TABLES `credit_record_002` WRITE;
/*!40000 ALTER TABLE `credit_record_002`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `credit_record_002`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credit_record_003`
--

DROP TABLE IF EXISTS `credit_record_003`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `credit_record_003`
(
    `id`              bigint unsigned NOT NULL COMMENT '雪花ID',
    `user_id`         varchar(32)     NOT NULL COMMENT '用户ID',
    `activity_id`     bigint          NOT NULL COMMENT '活动ID',
    `trade_name`      varchar(32)     NOT NULL COMMENT '交易名称',
    `trade_type`      varchar(8)      NOT NULL DEFAULT 'forward' COMMENT '交易类型；forward-正向、reverse-逆向',
    `trade_amount`    decimal(10, 2)  NOT NULL COMMENT '交易金额',
    `out_business_no` varchar(100)    NOT NULL COMMENT '业务仿重ID - 外部透传。返利、行为等唯一标识',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户积分记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credit_record_003`
--

LOCK TABLES `credit_record_003` WRITE;
/*!40000 ALTER TABLE `credit_record_003`
    DISABLE KEYS */;
INSERT INTO `credit_record_003`
VALUES (1946786050772856834, 'yong', 100301, '每日签到', 'forward', 10.00, 'yong_100301_integral_10_20250720',
        '2025-07-20 12:15:38', '2025-07-20 12:15:38'),
       (1946786557281202177, 'yong', 100301, '抽奖奖品', 'forward', 6.09, 'yong_lottery_20250720121739',
        '2025-07-20 12:17:39', '2025-07-20 12:17:39'),
       (1946786572909178882, 'yong', 100301, '抽奖奖品', 'forward', 82.90, 'yong_lottery_20250720121743',
        '2025-07-20 12:17:43', '2025-07-20 12:17:43'),
       (1946786590726582274, 'yong', 100301, '抽奖奖品', 'forward', 20.90, 'yong_lottery_20250720121747',
        '2025-07-20 12:17:47', '2025-07-20 12:17:47'),
       (1946786620036378626, 'yong', 100301, '抽奖奖品', 'forward', 68.40, 'yong_lottery_20250720121754',
        '2025-07-20 12:17:54', '2025-07-20 12:17:54'),
       (1946786636431912962, 'yong', 100301, '抽奖奖品', 'forward', 79.30, 'yong_lottery_20250720121758',
        '2025-07-20 12:17:58', '2025-07-20 12:17:58'),
       (1946786666249220097, 'yong', 100301, '抽奖奖品', 'forward', 95.90, 'yong_lottery_20250720121805',
        '2025-07-20 12:18:05', '2025-07-20 12:18:05'),
       (1946786684888707073, 'yong', 100301, '抽奖奖品', 'forward', 83.00, 'yong_lottery_20250720121809',
        '2025-07-20 12:18:10', '2025-07-20 12:18:10'),
       (1946786702097936385, 'yong', 100301, '抽奖奖品', 'forward', 99.30, 'yong_lottery_20250720121813',
        '2025-07-20 12:18:14', '2025-07-20 12:18:14'),
       (1946786718476693505, 'yong', 100301, '抽奖奖品', 'forward', 90.80, 'yong_lottery_20250720121817',
        '2025-07-20 12:18:17', '2025-07-20 12:18:17'),
       (1946786737107791873, 'yong', 100301, '抽奖奖品', 'forward', 70.50, 'yong_lottery_20250720121822',
        '2025-07-20 12:18:22', '2025-07-20 12:18:22'),
       (1946786752823848962, 'yong', 100301, '抽奖奖品', 'forward', 80.90, 'yong_lottery_20250720121826',
        '2025-07-20 12:18:25', '2025-07-20 12:18:25'),
       (1946786775242403841, 'yong', 100301, '积分兑换', 'reverse', 1.99, '564159026792', '2025-07-20 12:18:31',
        '2025-07-20 12:18:31'),
       (1946786777310195713, 'yong', 100301, '积分兑换', 'reverse', 5.99, '997479956334', '2025-07-20 12:18:31',
        '2025-07-20 12:18:31'),
       (1946786781600968705, 'yong', 100301, '积分兑换', 'reverse', 1.99, '982741481760', '2025-07-20 12:18:33',
        '2025-07-20 12:18:33'),
       (1946786783425490946, 'yong', 100301, '积分兑换', 'reverse', 5.99, '759211064374', '2025-07-20 12:18:33',
        '2025-07-20 12:18:33'),
       (1946786789024886785, 'yong', 100301, '积分兑换', 'reverse', 5.99, '911329561191', '2025-07-20 12:18:35',
        '2025-07-20 12:18:35'),
       (1946786793907056642, 'yong', 100301, '积分兑换', 'reverse', 5.99, '134190325867', '2025-07-20 12:18:36',
        '2025-07-20 12:18:36'),
       (1946786797505769474, 'yong', 100301, '积分兑换', 'reverse', 5.99, '728869427430', '2025-07-20 12:18:37',
        '2025-07-20 12:18:37'),
       (1946786801599410177, 'yong', 100301, '积分兑换', 'reverse', 5.99, '040392134000', '2025-07-20 12:18:38',
        '2025-07-20 12:18:38'),
       (1946786805428809729, 'yong', 100301, '积分兑换', 'reverse', 5.99, '489587779150', '2025-07-20 12:18:39',
        '2025-07-20 12:18:39'),
       (1946786809010745345, 'yong', 100301, '积分兑换', 'reverse', 5.99, '826944825874', '2025-07-20 12:18:39',
        '2025-07-20 12:18:39'),
       (1946786933480910850, 'yong', 1934223919414280193, '每日签到', 'forward', 50.00,
        'yong_1934223919414280193_integral_50_20250720', '2025-07-20 12:19:09', '2025-07-20 12:19:09'),
       (1946786970961211394, 'yong', 1934223919414280193, '抽奖奖品', 'forward', 82.40, 'yong_lottery_20250720121918',
        '2025-07-20 12:19:18', '2025-07-20 12:19:18'),
       (1946786987453214721, 'yong', 1934223919414280193, '抽奖奖品', 'forward', 31.20, 'yong_lottery_20250720121921',
        '2025-07-20 12:19:21', '2025-07-20 12:19:21'),
       (1946787019204096001, 'yong', 1934223919414280193, '抽奖奖品', 'forward', 83.60, 'yong_lottery_20250720121929',
        '2025-07-20 12:19:29', '2025-07-20 12:19:29'),
       (1946787430711455746, 'yong', 100301, '抽奖奖品', 'forward', 27.30, 'yong_lottery_20250720122107',
        '2025-07-20 12:21:08', '2025-07-20 12:21:08'),
       (1946787464307830786, 'yong', 100301, '抽奖奖品', 'forward', 12.10, 'yong_lottery_20250720122115',
        '2025-07-20 12:21:16', '2025-07-20 12:21:16'),
       (1946787494460682241, 'yong', 100301, '抽奖奖品', 'forward', 56.30, 'yong_lottery_20250720122122',
        '2025-07-20 12:21:23', '2025-07-20 12:21:23'),
       (1946787512521355265, 'yong', 100301, '抽奖奖品', 'forward', 83.30, 'yong_lottery_20250720122127',
        '2025-07-20 12:21:27', '2025-07-20 12:21:27'),
       (1946787561355636737, 'yong', 100301, '抽奖奖品', 'forward', 50.00, 'yong_lottery_20250720122138',
        '2025-07-20 12:21:39', '2025-07-20 12:21:39'),
       (1946787580733321217, 'yong', 100301, '抽奖奖品', 'forward', 55.80, 'yong_lottery_20250720122143',
        '2025-07-20 12:21:43', '2025-07-20 12:21:43'),
       (1946787596881391618, 'yong', 100301, '抽奖奖品', 'forward', 14.00, 'yong_lottery_20250720122147',
        '2025-07-20 12:21:47', '2025-07-20 12:21:47'),
       (1946787642326675457, 'yong', 100301, '抽奖奖品', 'forward', 48.40, 'yong_lottery_20250720122158',
        '2025-07-20 12:21:58', '2025-07-20 12:21:58'),
       (1946787772417208322, 'yong', 100301, '抽奖奖品', 'forward', 29.00, 'yong_lottery_20250720122229',
        '2025-07-20 12:22:29', '2025-07-20 12:22:29'),
       (1946787786749145089, 'yong', 100301, '抽奖奖品', 'forward', 50.60, 'yong_lottery_20250720122232',
        '2025-07-20 12:22:32', '2025-07-20 12:22:32'),
       (1947125850113691650, 'yong', 100301, '每日签到', 'forward', 10.00, 'yong_100301_integral_10_20250721',
        '2025-07-21 10:45:53', '2025-07-21 10:45:53'),
       (1947289943042719746, 'yong', 100301, '抽奖奖品', 'forward', 77.70, 'yong_lottery_20250721213755',
        '2025-07-21 21:37:56', '2025-07-21 21:37:56'),
       (1947290261918875650, 'yong', 100301, '抽奖奖品', 'forward', 54.80, 'yong_lottery_20250721213911',
        '2025-07-21 21:39:11', '2025-07-21 21:39:11'),
       (1947312485581209602, 'yong', 1947194890802733058, '每日签到', 'forward', 10.00,
        'yong_1947194890802733058_integral_10_20250721', '2025-07-21 23:07:31', '2025-07-21 23:07:31'),
       (1947318856976437250, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 16.30, 'yong_lottery_20250721233249',
        '2025-07-21 23:32:50', '2025-07-21 23:32:50'),
       (1947318882435862530, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 17.90, 'yong_lottery_20250721233255',
        '2025-07-21 23:32:55', '2025-07-21 23:32:55'),
       (1947318934608809985, 'yong', 1947194890802733058, '积分兑换', 'reverse', 5.99, '651642341112',
        '2025-07-21 23:33:07', '2025-07-21 23:33:07'),
       (1947318943064526849, 'yong', 1947194890802733058, '积分兑换', 'reverse', 5.99, '945857012249',
        '2025-07-21 23:33:09', '2025-07-21 23:33:09'),
       (1947318950916263937, 'yong', 1947194890802733058, '积分兑换', 'reverse', 24.99, '914819549386',
        '2025-07-21 23:33:11', '2025-07-21 23:33:11'),
       (1947318958189187073, 'yong', 1947194890802733058, '积分兑换', 'reverse', 5.99, '476006310105',
        '2025-07-21 23:33:14', '2025-07-21 23:33:14'),
       (1947318969169874946, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 16.70, 'yong_lottery_20250721233316',
        '2025-07-21 23:33:17', '2025-07-21 23:33:17'),
       (1947320809626296322, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 16.90, 'yong_lottery_20250721234035',
        '2025-07-21 23:40:35', '2025-07-21 23:40:35'),
       (1947320854027198465, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 12.00, 'yong_lottery_20250721234045',
        '2025-07-21 23:40:46', '2025-07-21 23:40:46'),
       (1947321046092767233, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 12.70, 'yong_lottery_20250721234131',
        '2025-07-21 23:41:30', '2025-07-21 23:41:30'),
       (1947321602676908034, 'yong', 1947194890802733058, '积分兑换', 'reverse', 5.99, '975762561529',
        '2025-07-21 23:43:45', '2025-07-21 23:43:45'),
       (1947321607403888641, 'yong', 1947194890802733058, '积分兑换', 'reverse', 24.99, '951298635576',
        '2025-07-21 23:43:46', '2025-07-21 23:43:46'),
       (1947321815659470850, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 16.30, 'yong_lottery_20250721234434',
        '2025-07-21 23:44:34', '2025-07-21 23:44:34'),
       (1947323392768176130, 'yong', 1947194890802733058, '积分兑换', 'reverse', 5.99, '046103325724',
        '2025-07-21 23:50:51', '2025-07-21 23:50:51'),
       (1947323395679023106, 'yong', 1947194890802733058, '积分兑换', 'reverse', 24.99, '764076145907',
        '2025-07-21 23:50:51', '2025-07-21 23:50:51'),
       (1947323501564227586, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 13.50, 'yong_lottery_20250721235116',
        '2025-07-21 23:51:17', '2025-07-21 23:51:17'),
       (1947323580945625090, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 14.10, 'yong_lottery_20250721235135',
        '2025-07-21 23:51:36', '2025-07-21 23:51:36'),
       (1947323699271135233, 'yong', 1947194890802733058, '积分兑换', 'reverse', 24.99, '321947260579',
        '2025-07-21 23:52:03', '2025-07-21 23:52:03'),
       (1947323921946734593, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 18.40, 'yong_lottery_20250721235257',
        '2025-07-21 23:52:57', '2025-07-21 23:52:57'),
       (1947326167069515778, 'yong', 1947194890802733058, '积分兑换', 'reverse', 5.99, '831470545248',
        '2025-07-22 00:01:52', '2025-07-22 00:01:52'),
       (1947326178750652418, 'yong', 1947194890802733058, '积分兑换', 'reverse', 24.99, '619607969786',
        '2025-07-22 00:01:55', '2025-07-22 00:01:55'),
       (1947326935243767810, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 13.90, 'yong_lottery_20250722000455',
        '2025-07-22 00:04:56', '2025-07-22 00:04:56'),
       (1947496106048942082, 'yong', 1947194890802733058, '每日签到', 'forward', 10.00,
        'yong_1947194890802733058_integral_10_20250722', '2025-07-22 11:17:08', '2025-07-22 11:17:08'),
       (1947496263536668673, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 19.20, 'yong_lottery_20250722111746',
        '2025-07-22 11:17:46', '2025-07-22 11:17:46'),
       (1947496280729120770, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 16.50, 'yong_lottery_20250722111750',
        '2025-07-22 11:17:50', '2025-07-22 11:17:50'),
       (1947496324572180481, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 13.50, 'yong_lottery_20250722111801',
        '2025-07-22 11:18:01', '2025-07-22 11:18:01'),
       (1947496394336038914, 'yong', 1947194890802733058, '积分兑换', 'reverse', 45.99, '914463904655',
        '2025-07-22 11:18:17', '2025-07-22 11:18:17'),
       (1947496406071701506, 'yong', 1947194890802733058, '积分兑换', 'reverse', 24.99, '594802838564',
        '2025-07-22 11:18:20', '2025-07-22 11:18:20'),
       (1947497393272451073, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 19.10, 'yong_lottery_20250722112215',
        '2025-07-22 11:22:16', '2025-07-22 11:22:16'),
       (1947510714260373506, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 1.00, 'yong_lottery_20250722121511',
        '2025-07-22 12:15:11', '2025-07-22 12:15:11'),
       (1947510730743988225, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 1.00, 'yong_lottery_20250722121515',
        '2025-07-22 12:15:15', '2025-07-22 12:15:15'),
       (1947510745667321858, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 1.00, 'yong_lottery_20250722121519',
        '2025-07-22 12:15:19', '2025-07-22 12:15:19'),
       (1947510758736773122, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 1.00, 'yong_lottery_20250722121522',
        '2025-07-22 12:15:22', '2025-07-22 12:15:22'),
       (1947510774805151746, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 1.00, 'yong_lottery_20250722121526',
        '2025-07-22 12:15:25', '2025-07-22 12:15:25'),
       (1947510791943077889, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 1.00, 'yong_lottery_20250722121530',
        '2025-07-22 12:15:30', '2025-07-22 12:15:30'),
       (1947511379804143617, 'yong', 1947194890802733058, '积分兑换', 'reverse', 24.99, '617559309177',
        '2025-07-22 12:17:50', '2025-07-22 12:17:50'),
       (1947511390168268802, 'yong', 1947194890802733058, '积分兑换', 'reverse', 5.99, '695197656089',
        '2025-07-22 12:17:52', '2025-07-22 12:17:52'),
       (1947512557191413762, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 19.00, 'yong_lottery_20250722122231',
        '2025-07-22 12:22:31', '2025-07-22 12:22:31'),
       (1947515416280678401, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 12.50, 'yong_lottery_20250722123352',
        '2025-07-22 12:33:53', '2025-07-22 12:33:53'),
       (1947522788894019586, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 19.00, 'yong_lottery_20250722130310',
        '2025-07-22 13:03:10', '2025-07-22 13:03:10'),
       (1947523609064943618, 'yong', 1934223919414280193, '抽奖奖品', 'forward', 47.00, 'yong_lottery_20250722130626',
        '2025-07-22 13:06:26', '2025-07-22 13:06:26'),
       (1947523627830259714, 'yong', 1934223919414280193, '抽奖奖品', 'forward', 80.50, 'yong_lottery_20250722130630',
        '2025-07-22 13:06:30', '2025-07-22 13:06:30'),
       (1947550648492965889, 'yong', 100301, '抽奖奖品', 'forward', 7.46, 'yong_lottery_20250722145352',
        '2025-07-22 14:53:53', '2025-07-22 14:53:53'),
       (1947552434163367938, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 11.10, 'yong_lottery_20250722150058',
        '2025-07-22 15:00:58', '2025-07-22 15:00:58'),
       (1947552448021348354, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 18.40, 'yong_lottery_20250722150102',
        '2025-07-22 15:01:02', '2025-07-22 15:01:02'),
       (1947552473946341377, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 18.20, 'yong_lottery_20250722150108',
        '2025-07-22 15:01:08', '2025-07-22 15:01:08'),
       (1947881578273292289, 'yong', 1947194890802733058, '每日签到', 'forward', 10.00,
        'yong_1947194890802733058_integral_10_20250723', '2025-07-23 12:48:54', '2025-07-23 12:48:54'),
       (1947924201767714817, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 18.60, 'yong_lottery_20250723153814',
        '2025-07-23 15:38:15', '2025-07-23 15:38:15'),
       (1947927396946542593, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 18.00, 'yong_lottery_20250723155056',
        '2025-07-23 15:50:57', '2025-07-23 15:50:57'),
       (1947928653354500098, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 14.40, 'yong_lottery_20250723155556',
        '2025-07-23 15:55:56', '2025-07-23 15:55:56'),
       (1947932764925853698, 'yong', 1947194890802733058, '积分兑换', 'reverse', 5.99, '143358366359',
        '2025-07-23 16:12:16', '2025-07-23 16:12:16'),
       (1947932772681121793, 'yong', 1947194890802733058, '积分兑换', 'reverse', 5.99, '260039945767',
        '2025-07-23 16:12:18', '2025-07-23 16:12:18'),
       (1947938626646134786, 'yong', 1947194890802733058, '积分兑换', 'reverse', 5.99, '038523139152',
        '2025-07-23 16:35:34', '2025-07-23 16:35:34'),
       (1947938628789424130, 'yong', 1947194890802733058, '积分兑换', 'reverse', 24.99, '229077811965',
        '2025-07-23 16:35:34', '2025-07-23 16:35:34'),
       (1947940305294622721, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 19.40, 'yong_lottery_20250723164214',
        '2025-07-23 16:42:14', '2025-07-23 16:42:14'),
       (1947940316224978946, 'yong', 1947194890802733058, '积分兑换', 'reverse', 5.99, '571814659079',
        '2025-07-23 16:42:17', '2025-07-23 16:42:17'),
       (1947940318821253121, 'yong', 1947194890802733058, '积分兑换', 'reverse', 24.99, '487058719004',
        '2025-07-23 16:42:17', '2025-07-23 16:42:17'),
       (1947943938740494337, 'yong', 100301, '抽奖奖品', 'forward', 15.20, 'yong_lottery_20250723165640',
        '2025-07-23 16:56:39', '2025-07-23 16:56:39'),
       (1947943962987765762, 'yong', 100301, '抽奖奖品', 'forward', 16.30, 'yong_lottery_20250723165646',
        '2025-07-23 16:56:44', '2025-07-23 16:56:44'),
       (1947943981048438785, 'yong', 100301, '抽奖奖品', 'forward', 92.40, 'yong_lottery_20250723165650',
        '2025-07-23 16:56:49', '2025-07-23 16:56:49'),
       (1947944012044345345, 'yong', 100301, '积分兑换', 'reverse', 1.99, '466282722375', '2025-07-23 16:56:58',
        '2025-07-23 16:56:58'),
       (1947944013797564418, 'yong', 100301, '积分兑换', 'reverse', 5.99, '262893498133', '2025-07-23 16:56:58',
        '2025-07-23 16:56:58'),
       (1947944032223141889, 'yong', 100301, '每日签到', 'forward', 10.00, 'yong_100301_integral_10_20250723',
        '2025-07-23 16:57:02', '2025-07-23 16:57:02'),
       (1947944050774548481, 'yong', 100301, '抽奖奖品', 'forward', 55.90, 'yong_lottery_20250723165707',
        '2025-07-23 16:57:06', '2025-07-23 16:57:06'),
       (1947944101135556610, 'yong', 1934223919414280193, '每日签到', 'forward', 50.00,
        'yong_1934223919414280193_integral_50_20250723', '2025-07-23 16:57:18', '2025-07-23 16:57:18'),
       (1947944204923609089, 'yong', 1934223919414280193, '抽奖奖品', 'forward', 56.50, 'yong_lottery_20250723165744',
        '2025-07-23 16:57:43', '2025-07-23 16:57:43'),
       (1947944227128254465, 'yong', 1934223919414280193, '抽奖奖品', 'forward', 37.80, 'yong_lottery_20250723165749',
        '2025-07-23 16:57:48', '2025-07-23 16:57:48'),
       (1947962425949077505, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 14.60, 'yong_lottery_20250723181008',
        '2025-07-23 18:10:07', '2025-07-23 18:10:07'),
       (1947962462888312834, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 13.90, 'yong_lottery_20250723181017',
        '2025-07-23 18:10:18', '2025-07-23 18:10:18'),
       (1947964456407445506, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 16.50, 'yong_lottery_20250723181812',
        '2025-07-23 18:18:12', '2025-07-23 18:18:12'),
       (1947964532970270722, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 10.50, 'yong_lottery_20250723181830',
        '2025-07-23 18:18:31', '2025-07-23 18:18:31'),
       (1947964547969101825, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 13.30, 'yong_lottery_20250723181834',
        '2025-07-23 18:18:34', '2025-07-23 18:18:34'),
       (1947964590650339329, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 16.90, 'yong_lottery_20250723181844',
        '2025-07-23 18:18:44', '2025-07-23 18:18:44'),
       (1947964609726033921, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 13.30, 'yong_lottery_20250723181849',
        '2025-07-23 18:18:48', '2025-07-23 18:18:48'),
       (1947964734905036802, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 13.10, 'yong_lottery_20250723181918',
        '2025-07-23 18:19:18', '2025-07-23 18:19:18'),
       (1947968946082676737, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 10.80, 'yong_lottery_20250723183602',
        '2025-07-23 18:36:02', '2025-07-23 18:36:02'),
       (1947968976357163010, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 16.20, 'yong_lottery_20250723183610',
        '2025-07-23 18:36:11', '2025-07-23 18:36:11'),
       (1947968990810734593, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 14.40, 'yong_lottery_20250723183613',
        '2025-07-23 18:36:14', '2025-07-23 18:36:14'),
       (1947969019046789121, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 15.80, 'yong_lottery_20250723183620',
        '2025-07-23 18:36:21', '2025-07-23 18:36:21'),
       (1947993455313588226, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 16.60, 'yong_lottery_20250723201326',
        '2025-07-23 20:13:27', '2025-07-23 20:13:27'),
       (1947993473453953026, 'yong', 1947194890802733058, '抽奖奖品', 'forward', 16.70, 'yong_lottery_20250723201330',
        '2025-07-23 20:13:31', '2025-07-23 20:13:31'),
       (1948214825007992833, 'yong', 1947194890802733058, '每日签到', 'forward', 10.00,
        'yong_1947194890802733058_integral_10_20250724', '2025-07-24 10:53:05', '2025-07-24 10:53:05'),
       (1948267854218407937, 'yong', 100301, '积分兑换', 'reverse', 1.99, '537455864590', '2025-07-24 14:23:48',
        '2025-07-24 14:23:48'),
       (1948267883532398594, 'yong', 100301, '积分兑换', 'reverse', 1.99, '374572616015', '2025-07-24 14:23:55',
        '2025-07-24 14:23:55'),
       (1948267891845509122, 'yong', 100301, '积分兑换', 'reverse', 1.99, '280454051698', '2025-07-24 14:23:57',
        '2025-07-24 14:23:57'),
       (1948267902616481794, 'yong', 100301, '积分兑换', 'reverse', 5.99, '894409931218', '2025-07-24 14:23:59',
        '2025-07-24 14:23:59'),
       (1948267911319662593, 'yong', 100301, '积分兑换', 'reverse', 5.99, '394888122339', '2025-07-24 14:24:01',
        '2025-07-24 14:24:01'),
       (1948267915358777345, 'yong', 100301, '积分兑换', 'reverse', 5.99, '376754640635', '2025-07-24 14:24:02',
        '2025-07-24 14:24:02'),
       (1948267918861021186, 'yong', 100301, '积分兑换', 'reverse', 5.99, '581348765564', '2025-07-24 14:24:03',
        '2025-07-24 14:24:03'),
       (1948267921906085890, 'yong', 100301, '积分兑换', 'reverse', 5.99, '383073198115', '2025-07-24 14:24:04',
        '2025-07-24 14:24:04'),
       (1948271570354974721, 'yong', 1948000932272812034, '抽奖奖品', 'forward', 13.70, 'yong_lottery_20250724143834',
        '2025-07-24 14:38:34', '2025-07-24 14:38:34'),
       (1948271636331376642, 'yong', 1948000932272812034, '抽奖奖品', 'forward', 19.40, 'yong_lottery_20250724143849',
        '2025-07-24 14:38:49', '2025-07-24 14:38:49'),
       (1948272464345706498, 'yong', 1934223919414280193, '每日签到', 'forward', 50.00,
        'yong_1934223919414280193_integral_4_20250724', '2025-07-24 14:42:07', '2025-07-24 14:42:07'),
       (1948273527106953217, 'yong', 1948000932272812034, '抽奖奖品', 'forward', 18.70, 'yong_lottery_20250724144620',
        '2025-07-24 14:46:20', '2025-07-24 14:46:20'),
       (1948274283331575809, 'yong', 1947194890802733058, '积分兑换', 'reverse', 5.99, '159218864705',
        '2025-07-24 14:49:20', '2025-07-24 14:49:20'),
       (1948288419490729986, 'yong', 100301, '每日签到', 'forward', 10.00, 'yong_100301_integral_2_20250724',
        '2025-07-24 15:45:31', '2025-07-24 15:45:31'),
       (1948288608414765058, 'yong', 100301, '积分兑换', 'reverse', 1.99, '643122837883', '2025-07-24 15:46:16',
        '2025-07-24 15:46:16');
/*!40000 ALTER TABLE `credit_record_003`
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
    KEY `idx_create_time` (`update_time`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3007
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
VALUES (2859, 'send_rebate', 1947194890802733058, 'yong', '14434368534',
        'BaseEvent.EventMessage(id=14434368534, timestamp=Wed Jul 23 12:48:51 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1947194890802733058, rebateDesc=每日签到返抽奖次数, rebateType=sku, rebateConfig=1947277048632332290, bizId=yong_1947194890802733058_sku_1947277048632332290_20250723))',
        'completed', '2025-07-23 12:48:50', '2025-07-23 12:48:51'),
       (2860, 'send_rebate', 1947194890802733058, 'yong', '74160334480',
        'BaseEvent.EventMessage(id=74160334480, timestamp=Wed Jul 23 12:48:51 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1947194890802733058, rebateDesc=每日签到返积分, rebateType=integral, rebateConfig=10, bizId=yong_1947194890802733058_integral_10_20250723))',
        'completed', '2025-07-23 12:48:51', '2025-07-23 12:48:51'),
       (2861, 'credit_adjust_success', 1947194890802733058, 'yong', '95808866919',
        '{\"data\":{\"activityId\":1947194890802733058,\"amount\":10,\"orderId\":\"819107400220\",\"outBusinessNo\":\"yong_1947194890802733058_integral_10_20250723\",\"userId\":\"yong\"},\"id\":\"95808866919\",\"timestamp\":1753246132734}',
        'completed', '2025-07-23 12:48:54', '2025-07-23 12:48:54'),
       (2862, 'send_award', 1947194890802733058, 'yong', '52802451519',
        'BaseEvent.EventMessage(id=52802451519, timestamp=Wed Jul 23 15:32:31 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136149856198658, awardTitle=支付立减券, userOrderId=1947881506542305281, awardConfig=))',
        'completed', '2025-07-23 15:32:31', '2025-07-23 15:32:31'),
       (2863, 'send_award', 1947194890802733058, 'yong', '53308026617',
        'BaseEvent.EventMessage(id=53308026617, timestamp=Wed Jul 23 15:33:39 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136366194204674, awardTitle=小玩偶, userOrderId=1947923044592902145, awardConfig=))',
        'completed', '2025-07-23 15:33:41', '2025-07-23 15:33:41'),
       (2864, 'send_award', 1947194890802733058, 'yong', '26101424923',
        'BaseEvent.EventMessage(id=26101424923, timestamp=Wed Jul 23 15:38:14 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947135858008137730, awardTitle=随机积分, userOrderId=1947924198366134274, awardConfig=))',
        'completed', '2025-07-23 15:38:14', '2025-07-23 15:38:14'),
       (2865, 'send_award', 1947194890802733058, 'yong', '22664189683',
        'BaseEvent.EventMessage(id=22664189683, timestamp=Wed Jul 23 15:50:56 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947135858008137730, awardTitle=随机积分, userOrderId=1947927393154891778, awardConfig=))',
        'completed', '2025-07-23 15:50:57', '2025-07-23 15:50:57'),
       (2866, 'send_award', 1947194890802733058, 'yong', '78863444975',
        'BaseEvent.EventMessage(id=78863444975, timestamp=Wed Jul 23 15:54:02 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136149856198658, awardTitle=支付立减券, userOrderId=1947928174838939649, awardConfig=))',
        'completed', '2025-07-23 15:54:02', '2025-07-23 15:54:02'),
       (2867, 'send_award', 1947194890802733058, 'yong', '66952918144',
        'BaseEvent.EventMessage(id=66952918144, timestamp=Wed Jul 23 15:55:40 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136290151473154, awardTitle=办公椅, userOrderId=1947928582604980225, awardConfig=))',
        'completed', '2025-07-23 15:55:40', '2025-07-23 15:55:40'),
       (2868, 'send_award', 1947194890802733058, 'yong', '03602509479',
        'BaseEvent.EventMessage(id=03602509479, timestamp=Wed Jul 23 15:55:56 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947135858008137730, awardTitle=随机积分, userOrderId=1947928650678534145, awardConfig=))',
        'completed', '2025-07-23 15:55:56', '2025-07-23 15:55:56'),
       (2869, 'send_award', 1947194890802733058, 'yong', '42175227017',
        'BaseEvent.EventMessage(id=42175227017, timestamp=Wed Jul 23 16:11:05 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136149856198658, awardTitle=支付立减券, userOrderId=1947932401845927938, awardConfig=))',
        'completed', '2025-07-23 16:11:05', '2025-07-23 16:11:05'),
       (2870, 'send_award', 1947194890802733058, 'yong', '14149821878',
        'BaseEvent.EventMessage(id=14149821878, timestamp=Wed Jul 23 16:11:08 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136366194204674, awardTitle=小玩偶, userOrderId=1947932478236786689, awardConfig=))',
        'completed', '2025-07-23 16:11:08', '2025-07-23 16:11:08'),
       (2871, 'credit_adjust_success', 1947194890802733058, 'yong', '40086459631',
        '{\"data\":{\"activityId\":1947194890802733058,\"amount\":5.99,\"orderId\":\"002052558804\",\"outBusinessNo\":\"143358366359\",\"userId\":\"yong\"},\"id\":\"40086459631\",\"timestamp\":1753258336607}',
        'completed', '2025-07-23 16:12:16', '2025-07-23 16:12:16'),
       (2872, 'credit_adjust_success', 1947194890802733058, 'yong', '00392933396',
        '{\"data\":{\"activityId\":1947194890802733058,\"amount\":5.99,\"orderId\":\"194066721522\",\"outBusinessNo\":\"260039945767\",\"userId\":\"yong\"},\"id\":\"00392933396\",\"timestamp\":1753258338464}',
        'completed', '2025-07-23 16:12:18', '2025-07-23 16:12:18'),
       (2873, 'send_award', 1947194890802733058, 'yong', '17947425409',
        'BaseEvent.EventMessage(id=17947425409, timestamp=Wed Jul 23 16:35:25 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136456283660289, awardTitle=小台灯, userOrderId=1947938586200461314, awardConfig=))',
        'completed', '2025-07-23 16:35:25', '2025-07-23 16:35:25'),
       (2874, 'credit_adjust_success', 1947194890802733058, 'yong', '63878508948',
        '{\"data\":{\"activityId\":1947194890802733058,\"amount\":5.99,\"orderId\":\"921900926016\",\"outBusinessNo\":\"038523139152\",\"userId\":\"yong\"},\"id\":\"63878508948\",\"timestamp\":1753259734157}',
        'completed', '2025-07-23 16:35:34', '2025-07-23 16:35:34'),
       (2875, 'credit_adjust_success', 1947194890802733058, 'yong', '68972380502',
        '{\"data\":{\"activityId\":1947194890802733058,\"amount\":24.99,\"orderId\":\"120988392371\",\"outBusinessNo\":\"229077811965\",\"userId\":\"yong\"},\"id\":\"68972380502\",\"timestamp\":1753259734648}',
        'completed', '2025-07-23 16:35:34', '2025-07-23 16:35:34'),
       (2876, 'send_award', 1947194890802733058, 'yong', '96944850474',
        'BaseEvent.EventMessage(id=96944850474, timestamp=Wed Jul 23 16:42:14 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947135858008137730, awardTitle=随机积分, userOrderId=1947940301863682049, awardConfig=))',
        'completed', '2025-07-23 16:42:14', '2025-07-23 16:42:14'),
       (2877, 'credit_adjust_success', 1947194890802733058, 'yong', '25524547287',
        '{\"data\":{\"activityId\":1947194890802733058,\"amount\":5.99,\"orderId\":\"226209471958\",\"outBusinessNo\":\"571814659079\",\"userId\":\"yong\"},\"id\":\"25524547287\",\"timestamp\":1753260136981}',
        'completed', '2025-07-23 16:42:17', '2025-07-23 16:42:17'),
       (2878, 'credit_adjust_success', 1947194890802733058, 'yong', '74554283149',
        '{\"data\":{\"activityId\":1947194890802733058,\"amount\":24.99,\"orderId\":\"804192054089\",\"outBusinessNo\":\"487058719004\",\"userId\":\"yong\"},\"id\":\"74554283149\",\"timestamp\":1753260137603}',
        'completed', '2025-07-23 16:42:17', '2025-07-23 16:42:18'),
       (2879, 'send_award', 1947194890802733058, 'yong', '20304412899',
        'BaseEvent.EventMessage(id=20304412899, timestamp=Wed Jul 23 16:42:18 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136031354527745, awardTitle=会员卡, userOrderId=1947940319672696834, awardConfig=))',
        'completed', '2025-07-23 16:42:18', '2025-07-23 16:42:18'),
       (2880, 'send_award', 1947194890802733058, 'yong', '86629012283',
        'BaseEvent.EventMessage(id=86629012283, timestamp=Wed Jul 23 16:42:54 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136149856198658, awardTitle=支付立减券, userOrderId=1947940470365650946, awardConfig=))',
        'completed', '2025-07-23 16:42:54', '2025-07-23 16:42:54'),
       (2881, 'send_award', 100301, 'yong', '50605989235',
        'BaseEvent.EventMessage(id=50605989235, timestamp=Wed Jul 23 16:56:40 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=100301, awardId=101, awardTitle=随机积分, userOrderId=1947941284995956738, awardConfig=1,100))',
        'completed', '2025-07-23 16:56:39', '2025-07-23 16:56:39'),
       (2882, 'send_award', 100301, 'yong', '51963190580',
        'BaseEvent.EventMessage(id=51963190580, timestamp=Wed Jul 23 16:56:46 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=100301, awardId=101, awardTitle=随机积分, userOrderId=1947943959477133314, awardConfig=1,100))',
        'completed', '2025-07-23 16:56:44', '2025-07-23 16:56:44'),
       (2883, 'send_award', 100301, 'yong', '31116532157',
        'BaseEvent.EventMessage(id=31116532157, timestamp=Wed Jul 23 16:56:50 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=100301, awardId=101, awardTitle=随机积分, userOrderId=1947943978179534850, awardConfig=1,100))',
        'completed', '2025-07-23 16:56:48', '2025-07-23 16:56:49'),
       (2884, 'credit_adjust_success', 100301, 'yong', '34847272480',
        '{\"data\":{\"activityId\":100301,\"amount\":1.99,\"orderId\":\"079177195799\",\"outBusinessNo\":\"466282722375\",\"userId\":\"yong\"},\"id\":\"34847272480\",\"timestamp\":1753261018126}',
        'completed', '2025-07-23 16:56:58', '2025-07-23 16:56:58'),
       (2885, 'credit_adjust_success', 100301, 'yong', '41651400579',
        '{\"data\":{\"activityId\":100301,\"amount\":5.99,\"orderId\":\"188189812285\",\"outBusinessNo\":\"262893498133\",\"userId\":\"yong\"},\"id\":\"41651400579\",\"timestamp\":1753261018463}',
        'completed', '2025-07-23 16:56:58', '2025-07-23 16:56:58'),
       (2886, 'send_rebate', 100301, 'yong', '66070836029',
        'BaseEvent.EventMessage(id=66070836029, timestamp=Wed Jul 23 16:57:02 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=100301, rebateDesc=签到返利-抽奖额度, rebateType=sku, rebateConfig=9011, bizId=yong_100301_sku_9011_20250723))',
        'completed', '2025-07-23 16:57:02', '2025-07-23 16:57:02'),
       (2887, 'send_rebate', 100301, 'yong', '99381099085',
        'BaseEvent.EventMessage(id=99381099085, timestamp=Wed Jul 23 16:57:02 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=100301, rebateDesc=签到返利-积分, rebateType=integral, rebateConfig=10, bizId=yong_100301_integral_10_20250723))',
        'completed', '2025-07-23 16:57:02', '2025-07-23 16:57:02'),
       (2888, 'credit_adjust_success', 100301, 'yong', '44912741691',
        '{\"data\":{\"activityId\":100301,\"amount\":10,\"orderId\":\"637713976274\",\"outBusinessNo\":\"yong_100301_integral_10_20250723\",\"userId\":\"yong\"},\"id\":\"44912741691\",\"timestamp\":1753261022897}',
        'completed', '2025-07-23 16:57:02', '2025-07-23 16:57:02'),
       (2889, 'send_award', 100301, 'yong', '29454463734',
        'BaseEvent.EventMessage(id=29454463734, timestamp=Wed Jul 23 16:57:07 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=100301, awardId=101, awardTitle=随机积分, userOrderId=1947944047372967938, awardConfig=1,100))',
        'completed', '2025-07-23 16:57:06', '2025-07-23 16:57:06'),
       (2890, 'send_award', 100301, 'yong', '31615434620',
        'BaseEvent.EventMessage(id=31615434620, timestamp=Wed Jul 23 16:57:11 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=100301, awardId=107, awardTitle=小霸王游戏机, userOrderId=1947944063491678210, awardConfig=null))',
        'completed', '2025-07-23 16:57:10', '2025-07-23 16:57:10'),
       (2891, 'send_rebate', 1934223919414280193, 'yong', '79329614086',
        'BaseEvent.EventMessage(id=79329614086, timestamp=Wed Jul 23 16:57:19 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1934223919414280193, rebateDesc=签到返积分, rebateType=integral, rebateConfig=50, bizId=yong_1934223919414280193_integral_50_20250723))',
        'completed', '2025-07-23 16:57:17', '2025-07-23 16:57:18'),
       (2892, 'send_rebate', 1934223919414280193, 'yong', '67975383010',
        'BaseEvent.EventMessage(id=67975383010, timestamp=Wed Jul 23 16:57:19 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1934223919414280193, rebateDesc=签到返抽奖次数, rebateType=sku, rebateConfig=1934859819538202625, bizId=yong_1934223919414280193_sku_1934859819538202625_20250723))',
        'completed', '2025-07-23 16:57:17', '2025-07-23 16:57:18'),
       (2893, 'credit_adjust_success', 1934223919414280193, 'yong', '99251227048',
        '{\"data\":{\"activityId\":1934223919414280193,\"amount\":50,\"orderId\":\"196803148330\",\"outBusinessNo\":\"yong_1934223919414280193_integral_50_20250723\",\"userId\":\"yong\"},\"id\":\"99251227048\",\"timestamp\":1753261039381}',
        'completed', '2025-07-23 16:57:18', '2025-07-23 16:57:18'),
       (2894, 'send_award', 1934223919414280193, 'yong', '03547816176',
        'BaseEvent.EventMessage(id=03547816176, timestamp=Wed Jul 23 16:57:44 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1934223919414280193, awardId=101, awardTitle=随机积分, userOrderId=1947944201605914626, awardConfig=1,100))',
        'completed', '2025-07-23 16:57:43', '2025-07-23 16:57:43'),
       (2895, 'send_award', 1934223919414280193, 'yong', '22013570888',
        'BaseEvent.EventMessage(id=22013570888, timestamp=Wed Jul 23 16:57:49 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1934223919414280193, awardId=101, awardTitle=随机积分, userOrderId=1947944224003497986, awardConfig=1,100))',
        'completed', '2025-07-23 16:57:48', '2025-07-23 16:57:48'),
       (2896, 'send_award', 1947194890802733058, 'yong', '30024111021',
        'BaseEvent.EventMessage(id=30024111021, timestamp=Wed Jul 23 18:05:33 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136149856198658, awardTitle=支付立减券, userOrderId=1947941621358166018, awardConfig=))',
        'completed', '2025-07-23 18:05:34', '2025-07-23 18:05:34'),
       (2897, 'send_award', 1947194890802733058, 'yong', '82349114919',
        'BaseEvent.EventMessage(id=82349114919, timestamp=Wed Jul 23 18:09:59 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136366194204674, awardTitle=小玩偶, userOrderId=1947961457559781378, awardConfig=))',
        'completed', '2025-07-23 18:09:59', '2025-07-23 18:09:59'),
       (2898, 'send_award', 1947194890802733058, 'yong', '46928146317',
        'BaseEvent.EventMessage(id=46928146317, timestamp=Wed Jul 23 18:10:08 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947135858008137730, awardTitle=随机积分, userOrderId=1947962402846851073, awardConfig=))',
        'completed', '2025-07-23 18:10:07', '2025-07-23 18:10:07'),
       (2899, 'send_award', 1947194890802733058, 'yong', '39105929497',
        'BaseEvent.EventMessage(id=39105929497, timestamp=Wed Jul 23 18:10:17 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947135858008137730, awardTitle=随机积分, userOrderId=1947962437361778689, awardConfig=))',
        'completed', '2025-07-23 18:10:18', '2025-07-23 18:10:18'),
       (2900, 'send_award', 1947194890802733058, 'yong', '51924199876',
        'BaseEvent.EventMessage(id=51924199876, timestamp=Wed Jul 23 18:17:27 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136456283660289, awardTitle=小台灯, userOrderId=1947962522011222017, awardConfig=))',
        'completed', '2025-07-23 18:17:27', '2025-07-23 18:17:27'),
       (2901, 'send_award', 1947194890802733058, 'yong', '10183926383',
        'BaseEvent.EventMessage(id=10183926383, timestamp=Wed Jul 23 18:17:31 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136031354527745, awardTitle=会员卡, userOrderId=1947964281504968706, awardConfig=))',
        'completed', '2025-07-23 18:17:31', '2025-07-23 18:17:31'),
       (2902, 'send_award', 1947194890802733058, 'yong', '46553720139',
        'BaseEvent.EventMessage(id=46553720139, timestamp=Wed Jul 23 18:17:35 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136149856198658, awardTitle=支付立减券, userOrderId=1947964297980194818, awardConfig=))',
        'completed', '2025-07-23 18:17:35', '2025-07-23 18:17:35'),
       (2903, 'send_award', 1947194890802733058, 'yong', '09245563872',
        'BaseEvent.EventMessage(id=09245563872, timestamp=Wed Jul 23 18:17:39 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136149856198658, awardTitle=支付立减券, userOrderId=1947964317013946370, awardConfig=))',
        'completed', '2025-07-23 18:17:39', '2025-07-23 18:17:39'),
       (2904, 'send_award', 1947194890802733058, 'yong', '51609274026',
        'BaseEvent.EventMessage(id=51609274026, timestamp=Wed Jul 23 18:17:43 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=0, awardTitle=谢谢参与, userOrderId=1947964333224931330, awardConfig=谢谢参与))',
        'completed', '2025-07-23 18:17:43', '2025-07-23 18:17:43'),
       (2905, 'send_award', 1947194890802733058, 'yong', '29167986376',
        'BaseEvent.EventMessage(id=29167986376, timestamp=Wed Jul 23 18:18:12 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947135858008137730, awardTitle=随机积分, userOrderId=1947964453731479554, awardConfig=))',
        'completed', '2025-07-23 18:18:12', '2025-07-23 18:18:12'),
       (2906, 'send_award', 1947194890802733058, 'yong', '03812881884',
        'BaseEvent.EventMessage(id=03812881884, timestamp=Wed Jul 23 18:18:22 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136456283660289, awardTitle=小台灯, userOrderId=1947964493799665665, awardConfig=))',
        'completed', '2025-07-23 18:18:24', '2025-07-23 18:18:24'),
       (2907, 'send_award', 1947194890802733058, 'yong', '57512969016',
        'BaseEvent.EventMessage(id=57512969016, timestamp=Wed Jul 23 18:18:30 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947135858008137730, awardTitle=随机积分, userOrderId=1947964530508214274, awardConfig=))',
        'completed', '2025-07-23 18:18:31', '2025-07-23 18:18:31'),
       (2908, 'send_award', 1947194890802733058, 'yong', '76050186358',
        'BaseEvent.EventMessage(id=76050186358, timestamp=Wed Jul 23 18:18:34 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947135858008137730, awardTitle=随机积分, userOrderId=1947964545318301698, awardConfig=))',
        'completed', '2025-07-23 18:18:34', '2025-07-23 18:18:34'),
       (2909, 'send_award', 1947194890802733058, 'yong', '13137695419',
        'BaseEvent.EventMessage(id=13137695419, timestamp=Wed Jul 23 18:18:37 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136149856198658, awardTitle=支付立减券, userOrderId=1947964559180476417, awardConfig=))',
        'completed', '2025-07-23 18:18:37', '2025-07-23 18:18:37'),
       (2910, 'send_award', 1947194890802733058, 'yong', '00512016611',
        'BaseEvent.EventMessage(id=00512016611, timestamp=Wed Jul 23 18:18:40 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136366194204674, awardTitle=小玩偶, userOrderId=1947964573227200513, awardConfig=))',
        'completed', '2025-07-23 18:18:40', '2025-07-23 18:18:40'),
       (2911, 'send_award', 1947194890802733058, 'yong', '23213193093',
        'BaseEvent.EventMessage(id=23213193093, timestamp=Wed Jul 23 18:18:44 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947135858008137730, awardTitle=随机积分, userOrderId=1947964588037287937, awardConfig=))',
        'completed', '2025-07-23 18:18:44', '2025-07-23 18:18:44'),
       (2912, 'send_award', 1947194890802733058, 'yong', '76717111648',
        'BaseEvent.EventMessage(id=76717111648, timestamp=Wed Jul 23 18:18:48 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947135858008137730, awardTitle=随机积分, userOrderId=1947964605674336258, awardConfig=))',
        'completed', '2025-07-23 18:18:48', '2025-07-23 18:18:48'),
       (2913, 'send_award', 1947194890802733058, 'yong', '41496467492',
        'BaseEvent.EventMessage(id=41496467492, timestamp=Wed Jul 23 18:19:11 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136366194204674, awardTitle=小玩偶, userOrderId=1947964702692782081, awardConfig=))',
        'completed', '2025-07-23 18:19:11', '2025-07-23 18:19:11'),
       (2914, 'send_award', 1947194890802733058, 'yong', '48399918145',
        'BaseEvent.EventMessage(id=48399918145, timestamp=Wed Jul 23 18:19:15 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136149856198658, awardTitle=支付立减券, userOrderId=1947964717880356865, awardConfig=))',
        'completed', '2025-07-23 18:19:15', '2025-07-23 18:19:15'),
       (2915, 'send_award', 1947194890802733058, 'yong', '98347192720',
        'BaseEvent.EventMessage(id=98347192720, timestamp=Wed Jul 23 18:19:18 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947135858008137730, awardTitle=随机积分, userOrderId=1947964732560420865, awardConfig=))',
        'completed', '2025-07-23 18:19:18', '2025-07-23 18:19:18'),
       (2916, 'send_award', 1947194890802733058, 'yong', '20762478586',
        'BaseEvent.EventMessage(id=20762478586, timestamp=Wed Jul 23 18:20:07 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=0, awardTitle=谢谢参与, userOrderId=1947964938517524482, awardConfig=谢谢参与))',
        'completed', '2025-07-23 18:20:08', '2025-07-23 18:20:08'),
       (2917, 'send_award', 1947194890802733058, 'yong', '07434351562',
        'BaseEvent.EventMessage(id=07434351562, timestamp=Wed Jul 23 18:33:28 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=0, awardTitle=谢谢参与, userOrderId=1947968296536010754, awardConfig=谢谢参与))',
        'completed', '2025-07-23 18:33:29', '2025-07-23 18:33:30'),
       (2918, 'send_award', 1947194890802733058, 'yong', '95724861483',
        'BaseEvent.EventMessage(id=95724861483, timestamp=Wed Jul 23 18:33:32 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136031354527745, awardTitle=会员卡, userOrderId=1947968313065762817, awardConfig=))',
        'completed', '2025-07-23 18:33:34', '2025-07-23 18:33:34'),
       (2919, 'send_award', 1947194890802733058, 'yong', '51955594575',
        'BaseEvent.EventMessage(id=51955594575, timestamp=Wed Jul 23 18:34:49 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136456283660289, awardTitle=小台灯, userOrderId=1947968633770606594, awardConfig=))',
        'completed', '2025-07-23 18:34:49', '2025-07-23 18:34:49'),
       (2920, 'send_award', 1947194890802733058, 'yong', '84308157643',
        'BaseEvent.EventMessage(id=84308157643, timestamp=Wed Jul 23 18:34:55 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=0, awardTitle=谢谢参与, userOrderId=1947968661520121857, awardConfig=谢谢参与))',
        'completed', '2025-07-23 18:34:55', '2025-07-23 18:34:55'),
       (2921, 'send_award', 1947194890802733058, 'yong', '93907698235',
        'BaseEvent.EventMessage(id=93907698235, timestamp=Wed Jul 23 18:35:08 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136031354527745, awardTitle=会员卡, userOrderId=1947968715341430786, awardConfig=))',
        'completed', '2025-07-23 18:35:10', '2025-07-23 18:35:10'),
       (2922, 'send_award', 1947194890802733058, 'yong', '76414247619',
        'BaseEvent.EventMessage(id=76414247619, timestamp=Wed Jul 23 18:35:12 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=0, awardTitle=谢谢参与, userOrderId=1947968730759692289, awardConfig=谢谢参与))',
        'completed', '2025-07-23 18:35:13', '2025-07-23 18:35:13'),
       (2923, 'send_award', 1947194890802733058, 'yong', '31179513587',
        'BaseEvent.EventMessage(id=31179513587, timestamp=Wed Jul 23 18:35:15 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=0, awardTitle=谢谢参与, userOrderId=1947968744609284098, awardConfig=谢谢参与))',
        'completed', '2025-07-23 18:35:16', '2025-07-23 18:35:16'),
       (2924, 'send_award', 1947194890802733058, 'yong', '54924578856',
        'BaseEvent.EventMessage(id=54924578856, timestamp=Wed Jul 23 18:35:18 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136149856198658, awardTitle=支付立减券, userOrderId=1947968758081388546, awardConfig=))',
        'completed', '2025-07-23 18:35:19', '2025-07-23 18:35:19'),
       (2925, 'send_award', 1947194890802733058, 'yong', '08359784682',
        'BaseEvent.EventMessage(id=08359784682, timestamp=Wed Jul 23 18:35:21 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=0, awardTitle=谢谢参与, userOrderId=1947968772140695553, awardConfig=谢谢参与))',
        'completed', '2025-07-23 18:35:22', '2025-07-23 18:35:22'),
       (2926, 'send_award', 1947194890802733058, 'yong', '50860174985',
        'BaseEvent.EventMessage(id=50860174985, timestamp=Wed Jul 23 18:35:24 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=0, awardTitle=谢谢参与, userOrderId=1947968785361141762, awardConfig=谢谢参与))',
        'completed', '2025-07-23 18:35:25', '2025-07-23 18:35:25'),
       (2927, 'send_award', 1947194890802733058, 'yong', '51954661728',
        'BaseEvent.EventMessage(id=51954661728, timestamp=Wed Jul 23 18:35:28 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136149856198658, awardTitle=支付立减券, userOrderId=1947968798090854402, awardConfig=))',
        'completed', '2025-07-23 18:35:27', '2025-07-23 18:35:27'),
       (2928, 'send_award', 1947194890802733058, 'yong', '03271556999',
        'BaseEvent.EventMessage(id=03271556999, timestamp=Wed Jul 23 18:35:31 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=0, awardTitle=谢谢参与, userOrderId=1947968811911086082, awardConfig=谢谢参与))',
        'completed', '2025-07-23 18:35:30', '2025-07-23 18:35:30'),
       (2929, 'send_award', 1947194890802733058, 'yong', '54701278178',
        'BaseEvent.EventMessage(id=54701278178, timestamp=Wed Jul 23 18:35:34 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136515402375169, awardTitle=游戏机, userOrderId=1947968825680986114, awardConfig=))',
        'completed', '2025-07-23 18:35:34', '2025-07-23 18:35:34'),
       (2930, 'send_award', 1947194890802733058, 'yong', '40053953455',
        'BaseEvent.EventMessage(id=40053953455, timestamp=Wed Jul 23 18:35:37 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136456283660289, awardTitle=小台灯, userOrderId=1947968838054182913, awardConfig=))',
        'completed', '2025-07-23 18:35:39', '2025-07-23 18:35:39'),
       (2931, 'send_award', 1947194890802733058, 'yong', '38394481399',
        'BaseEvent.EventMessage(id=38394481399, timestamp=Wed Jul 23 18:35:40 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136149856198658, awardTitle=支付立减券, userOrderId=1947968850448351234, awardConfig=))',
        'completed', '2025-07-23 18:35:42', '2025-07-23 18:35:42'),
       (2932, 'send_award', 1947194890802733058, 'yong', '89619083075',
        'BaseEvent.EventMessage(id=89619083075, timestamp=Wed Jul 23 18:35:43 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136149856198658, awardTitle=支付立减券, userOrderId=1947968864352468994, awardConfig=))',
        'completed', '2025-07-23 18:35:45', '2025-07-23 18:35:45'),
       (2933, 'send_award', 1947194890802733058, 'yong', '35391133882',
        'BaseEvent.EventMessage(id=35391133882, timestamp=Wed Jul 23 18:35:46 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=0, awardTitle=谢谢参与, userOrderId=1947968876700499970, awardConfig=谢谢参与))',
        'completed', '2025-07-23 18:35:47', '2025-07-23 18:35:47'),
       (2934, 'send_award', 1947194890802733058, 'yong', '14656165964',
        'BaseEvent.EventMessage(id=14656165964, timestamp=Wed Jul 23 18:35:50 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136456283660289, awardTitle=小台灯, userOrderId=1947968890273267714, awardConfig=))',
        'completed', '2025-07-23 18:35:50', '2025-07-23 18:35:50'),
       (2935, 'send_award', 1947194890802733058, 'yong', '16193236119',
        'BaseEvent.EventMessage(id=16193236119, timestamp=Wed Jul 23 18:35:53 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136149856198658, awardTitle=支付立减券, userOrderId=1947968903254638594, awardConfig=))',
        'completed', '2025-07-23 18:35:54', '2025-07-23 18:35:54'),
       (2936, 'send_award', 1947194890802733058, 'yong', '92059785146',
        'BaseEvent.EventMessage(id=92059785146, timestamp=Wed Jul 23 18:35:56 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=0, awardTitle=谢谢参与, userOrderId=1947968916596719617, awardConfig=谢谢参与))',
        'completed', '2025-07-23 18:35:56', '2025-07-23 18:35:56'),
       (2937, 'send_award', 1947194890802733058, 'yong', '28837640450',
        'BaseEvent.EventMessage(id=28837640450, timestamp=Wed Jul 23 18:35:59 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136149856198658, awardTitle=支付立减券, userOrderId=1947968929942994946, awardConfig=))',
        'completed', '2025-07-23 18:35:59', '2025-07-23 18:35:59'),
       (2938, 'send_award', 1947194890802733058, 'yong', '08258360541',
        'BaseEvent.EventMessage(id=08258360541, timestamp=Wed Jul 23 18:36:02 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947135858008137730, awardTitle=随机积分, userOrderId=1947968943415099393, awardConfig=))',
        'completed', '2025-07-23 18:36:02', '2025-07-23 18:36:02'),
       (2939, 'send_award', 1947194890802733058, 'yong', '92986670394',
        'BaseEvent.EventMessage(id=92986670394, timestamp=Wed Jul 23 18:36:06 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136366194204674, awardTitle=小玩偶, userOrderId=1947968957877059585, awardConfig=))',
        'completed', '2025-07-23 18:36:05', '2025-07-23 18:36:05'),
       (2940, 'send_award', 1947194890802733058, 'yong', '23519366000',
        'BaseEvent.EventMessage(id=23519366000, timestamp=Wed Jul 23 18:36:10 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947135858008137730, awardTitle=随机积分, userOrderId=1947968973907689474, awardConfig=))',
        'completed', '2025-07-23 18:36:11', '2025-07-23 18:36:11'),
       (2941, 'send_award', 1947194890802733058, 'yong', '06663490009',
        'BaseEvent.EventMessage(id=06663490009, timestamp=Wed Jul 23 18:36:13 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947135858008137730, awardTitle=随机积分, userOrderId=1947968988122185730, awardConfig=))',
        'completed', '2025-07-23 18:36:14', '2025-07-23 18:36:14'),
       (2942, 'send_award', 1947194890802733058, 'yong', '64275565073',
        'BaseEvent.EventMessage(id=64275565073, timestamp=Wed Jul 23 18:36:16 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136149856198658, awardTitle=支付立减券, userOrderId=1947969002718363649, awardConfig=))',
        'completed', '2025-07-23 18:36:17', '2025-07-23 18:36:17'),
       (2943, 'send_award', 1947194890802733058, 'yong', '88443298259',
        'BaseEvent.EventMessage(id=88443298259, timestamp=Wed Jul 23 18:36:20 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947135858008137730, awardTitle=随机积分, userOrderId=1947969016534401026, awardConfig=))',
        'completed', '2025-07-23 18:36:20', '2025-07-23 18:36:20'),
       (2944, 'send_award', 1947194890802733058, 'yong', '16299737350',
        'BaseEvent.EventMessage(id=16299737350, timestamp=Wed Jul 23 20:13:22 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136031354527745, awardTitle=会员卡, userOrderId=1947993438007889922, awardConfig=))',
        'completed', '2025-07-23 20:13:24', '2025-07-23 20:13:24'),
       (2945, 'send_award', 1947194890802733058, 'yong', '97634024963',
        'BaseEvent.EventMessage(id=97634024963, timestamp=Wed Jul 23 20:13:26 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947135858008137730, awardTitle=随机积分, userOrderId=1947993452734091266, awardConfig=))',
        'completed', '2025-07-23 20:13:27', '2025-07-23 20:13:27'),
       (2946, 'send_award', 1947194890802733058, 'yong', '55152543159',
        'BaseEvent.EventMessage(id=55152543159, timestamp=Wed Jul 23 20:13:30 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947135858008137730, awardTitle=随机积分, userOrderId=1947993470996090882, awardConfig=))',
        'completed', '2025-07-23 20:13:31', '2025-07-23 20:13:31'),
       (2947, 'send_award', 1947194890802733058, 'yong', '74633233814',
        'BaseEvent.EventMessage(id=74633233814, timestamp=Wed Jul 23 20:16:50 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1947194890802733058, awardId=1947136031354527745, awardTitle=会员卡, userOrderId=1947994307260616706, awardConfig=))',
        'completed', '2025-07-23 20:16:48', '2025-07-23 20:16:48'),
       (2953, 'send_rebate', 1947194890802733058, 'yong', '40280768776',
        'BaseEvent.EventMessage(id=40280768776, timestamp=Wed Jul 23 22:51:50 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1947194890802733058, rebateDesc=或活动赠送抽奖次数, rebateType=gift, rebateConfig=10, bizId=yong_1947194890802733058_gift_10_20250723))',
        'completed', '2025-07-23 22:51:48', '2025-07-23 22:51:48'),
       (2954, 'send_rebate', 1947194890802733058, 'yong', '85290426776',
        'BaseEvent.EventMessage(id=85290426776, timestamp=Thu Jul 24 10:53:03 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1947194890802733058, rebateDesc=每日签到返抽奖次数, rebateType=sku, rebateConfig=1947277048632332290, bizId=yong_1947194890802733058_sku_1947277048632332290_20250724))',
        'completed', '2025-07-24 10:53:03', '2025-07-24 10:53:03'),
       (2955, 'send_rebate', 1947194890802733058, 'yong', '80603649600',
        'BaseEvent.EventMessage(id=80603649600, timestamp=Thu Jul 24 10:53:03 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1947194890802733058, rebateDesc=每日签到返积分, rebateType=integral, rebateConfig=10, bizId=yong_1947194890802733058_integral_10_20250724))',
        'completed', '2025-07-24 10:53:03', '2025-07-24 10:53:03'),
       (2956, 'credit_adjust_success', 1947194890802733058, 'yong', '12919915918',
        '{\"data\":{\"activityId\":1947194890802733058,\"amount\":10,\"orderId\":\"315099773565\",\"outBusinessNo\":\"yong_1947194890802733058_integral_10_20250724\",\"userId\":\"yong\"},\"id\":\"12919915918\",\"timestamp\":1753325584952}',
        'completed', '2025-07-24 10:53:05', '2025-07-24 10:53:05'),
       (2957, 'send_rebate', 1947194890802733058, 'yong', '62739667387',
        'BaseEvent.EventMessage(id=62739667387, timestamp=Thu Jul 24 12:16:18 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1947194890802733058, rebateDesc=活动赠送5次抽奖, rebateType=gift, rebateConfig=5, bizId=yong_1947194890802733058_gift_5_20250724))',
        'completed', '2025-07-24 12:16:18', '2025-07-24 12:16:18'),
       (2958, 'send_rebate', 1947194890802733058, 'yong2', '13179238879',
        'BaseEvent.EventMessage(id=13179238879, timestamp=Thu Jul 24 12:34:46 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong2, activityId=1947194890802733058, rebateDesc=活动赠送5次抽奖, rebateType=gift, rebateConfig=5, bizId=yong2_1947194890802733058_gift_5_20250724))',
        'completed', '2025-07-24 12:34:46', '2025-07-24 12:34:46'),
       (2959, 'send_rebate', 1947194890802733058, 'yong', '85804679402',
        'BaseEvent.EventMessage(id=85804679402, timestamp=Thu Jul 24 12:36:46 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1947194890802733058, rebateDesc=1, rebateType=gift, rebateConfig=12, bizId=yong_1947194890802733058_gift_12_20250724))',
        'completed', '2025-07-24 12:36:46', '2025-07-24 12:36:47'),
       (2960, 'send_rebate', 1947194890802733058, 'yong', '08468421816',
        'BaseEvent.EventMessage(id=08468421816, timestamp=Thu Jul 24 12:36:46 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1947194890802733058, rebateDesc=2, rebateType=gift, rebateConfig=1, bizId=yong_1947194890802733058_gift_1_20250724))',
        'completed', '2025-07-24 12:36:47', '2025-07-24 12:36:47'),
       (2961, 'send_rebate', 1947194890802733058, 'yong', '93542645031',
        'BaseEvent.EventMessage(id=93542645031, timestamp=Thu Jul 24 13:05:27 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1947194890802733058, rebateDesc=1, rebateType=gift, rebateConfig=12, bizId=yong_1947194890802733058_gift_1948240805508448258_20250724))',
        'completed', '2025-07-24 13:05:27', '2025-07-24 13:05:27'),
       (2962, 'send_rebate', 1947194890802733058, 'yong', '41809956790',
        'BaseEvent.EventMessage(id=41809956790, timestamp=Thu Jul 24 13:05:27 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1947194890802733058, rebateDesc=2, rebateType=gift, rebateConfig=1, bizId=yong_1947194890802733058_gift_1948240881718951938_20250724))',
        'completed', '2025-07-24 13:05:27', '2025-07-24 13:05:27'),
       (2963, 'send_rebate', 1948000932272812034, 'yong', '83967393037',
        'BaseEvent.EventMessage(id=83967393037, timestamp=Thu Jul 24 14:04:26 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1948000932272812034, rebateDesc=活动赠送5次抽奖次数, rebateType=gift, rebateConfig=5, bizId=yong_1948000932272812034_gift_1948260349270667265_20250724))',
        'completed', '2025-07-24 14:04:26', '2025-07-24 14:04:26'),
       (2964, 'send_rebate', 1948000932272812034, 'yong', '06454068761',
        'BaseEvent.EventMessage(id=06454068761, timestamp=Thu Jul 24 14:04:26 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1948000932272812034, rebateDesc=免费领10次抽奖, rebateType=gift, rebateConfig=10, bizId=yong_1948000932272812034_gift_1948262879291617281_20250724))',
        'completed', '2025-07-24 14:04:26', '2025-07-24 14:04:26'),
       (2965, 'send_rebate', 100301, 'yong', '50601815732',
        'BaseEvent.EventMessage(id=50601815732, timestamp=Thu Jul 24 14:19:32 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=100301, rebateDesc=免费领10次抽奖, rebateType=gift, rebateConfig=10, bizId=yong_100301_gift_1948262879291617281_20250724))',
        'completed', '2025-07-24 14:19:32', '2025-07-24 14:19:32'),
       (2966, 'credit_adjust_success', 100301, 'yong', '36752938129',
        '{\"data\":{\"activityId\":100301,\"amount\":1.99,\"orderId\":\"338569944845\",\"outBusinessNo\":\"537455864590\",\"userId\":\"yong\"},\"id\":\"36752938129\",\"timestamp\":1753338228121}',
        'completed', '2025-07-24 14:23:48', '2025-07-24 14:23:48'),
       (2967, 'credit_adjust_success', 100301, 'yong', '14412021849',
        '{\"data\":{\"activityId\":100301,\"amount\":1.99,\"orderId\":\"450473750076\",\"outBusinessNo\":\"374572616015\",\"userId\":\"yong\"},\"id\":\"14412021849\",\"timestamp\":1753338235104}',
        'completed', '2025-07-24 14:23:55', '2025-07-24 14:23:55'),
       (2968, 'credit_adjust_success', 100301, 'yong', '56681180166',
        '{\"data\":{\"activityId\":100301,\"amount\":1.99,\"orderId\":\"226130928508\",\"outBusinessNo\":\"280454051698\",\"userId\":\"yong\"},\"id\":\"56681180166\",\"timestamp\":1753338237094}',
        'completed', '2025-07-24 14:23:57', '2025-07-24 14:23:57'),
       (2969, 'credit_adjust_success', 100301, 'yong', '38721425678',
        '{\"data\":{\"activityId\":100301,\"amount\":5.99,\"orderId\":\"347358131634\",\"outBusinessNo\":\"894409931218\",\"userId\":\"yong\"},\"id\":\"38721425678\",\"timestamp\":1753338239664}',
        'completed', '2025-07-24 14:23:59', '2025-07-24 14:23:59'),
       (2970, 'credit_adjust_success', 100301, 'yong', '27255919882',
        '{\"data\":{\"activityId\":100301,\"amount\":5.99,\"orderId\":\"736887843575\",\"outBusinessNo\":\"394888122339\",\"userId\":\"yong\"},\"id\":\"27255919882\",\"timestamp\":1753338241746}',
        'completed', '2025-07-24 14:24:01', '2025-07-24 14:24:01'),
       (2971, 'credit_adjust_success', 100301, 'yong', '36205324228',
        '{\"data\":{\"activityId\":100301,\"amount\":5.99,\"orderId\":\"382050388645\",\"outBusinessNo\":\"376754640635\",\"userId\":\"yong\"},\"id\":\"36205324228\",\"timestamp\":1753338242706}',
        'completed', '2025-07-24 14:24:02', '2025-07-24 14:24:02'),
       (2972, 'credit_adjust_success', 100301, 'yong', '69446112273',
        '{\"data\":{\"activityId\":100301,\"amount\":5.99,\"orderId\":\"973240847668\",\"outBusinessNo\":\"581348765564\",\"userId\":\"yong\"},\"id\":\"69446112273\",\"timestamp\":1753338243543}',
        'completed', '2025-07-24 14:24:03', '2025-07-24 14:24:03'),
       (2973, 'credit_adjust_success', 100301, 'yong', '86412282031',
        '{\"data\":{\"activityId\":100301,\"amount\":5.99,\"orderId\":\"136965408130\",\"outBusinessNo\":\"383073198115\",\"userId\":\"yong\"},\"id\":\"86412282031\",\"timestamp\":1753338244220}',
        'completed', '2025-07-24 14:24:04', '2025-07-24 14:24:04'),
       (2974, 'send_rebate', 100301, 'yong', '27211803130',
        'BaseEvent.EventMessage(id=27211803130, timestamp=Thu Jul 24 14:25:12 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=100301, rebateDesc=活动赠送5次抽奖次数, rebateType=gift, rebateConfig=5, bizId=yong_100301_gift_1948260349270667265_20250724))',
        'completed', '2025-07-24 14:25:12', '2025-07-24 14:25:12'),
       (2975, 'send_rebate', 1934223919414280193, 'yong', '06856990965',
        'BaseEvent.EventMessage(id=06856990965, timestamp=Thu Jul 24 14:33:57 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1934223919414280193, rebateDesc=免费领10次抽奖, rebateType=gift, rebateConfig=10, bizId=yong_1934223919414280193_gift_1948262879291617281_20250724))',
        'completed', '2025-07-24 14:33:57', '2025-07-24 14:33:57'),
       (2976, 'send_rebate', 1934223919414280193, 'yong', '66536259188',
        'BaseEvent.EventMessage(id=66536259188, timestamp=Thu Jul 24 14:34:38 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1934223919414280193, rebateDesc=活动赠送5次抽奖次数, rebateType=gift, rebateConfig=5, bizId=yong_1934223919414280193_gift_1948260349270667265_20250724))',
        'completed', '2025-07-24 14:34:38', '2025-07-24 14:34:38'),
       (2977, 'send_rebate', 1948000932272812034, 'yong', '23243359537',
        'BaseEvent.EventMessage(id=23243359537, timestamp=Thu Jul 24 14:38:18 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1948000932272812034, rebateDesc=第二次赠送, rebateType=gift, rebateConfig=13, bizId=yong_1948000932272812034_gift_1948271313437077505_20250724))',
        'completed', '2025-07-24 14:38:18', '2025-07-24 14:38:18'),
       (2978, 'send_rebate', 1948000932272812034, 'yong', '53189335770',
        'BaseEvent.EventMessage(id=53189335770, timestamp=Thu Jul 24 14:38:22 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1948000932272812034, rebateDesc=活动赠送5次抽奖, rebateType=gift, rebateConfig=5, bizId=yong_1948000932272812034_gift_1948271061317464066_20250724))',
        'completed', '2025-07-24 14:38:22', '2025-07-24 14:38:22'),
       (2979, 'send_award', 1948000932272812034, 'yong', '16159120592',
        'BaseEvent.EventMessage(id=16159120592, timestamp=Thu Jul 24 14:38:28 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1948000932272812034, awardId=1947136290151473154, awardTitle=舒适办公椅, userOrderId=1948271545558249474, awardConfig=null))',
        'completed', '2025-07-24 14:38:28', '2025-07-24 14:38:28'),
       (2980, 'send_award', 1948000932272812034, 'yong', '31707348879',
        'BaseEvent.EventMessage(id=31707348879, timestamp=Thu Jul 24 14:38:34 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1948000932272812034, awardId=1947135858008137730, awardTitle=随机积分, userOrderId=1948271567548985346, awardConfig=null))',
        'completed', '2025-07-24 14:38:34', '2025-07-24 14:38:34'),
       (2981, 'send_award', 1948000932272812034, 'yong', '45379501667',
        'BaseEvent.EventMessage(id=45379501667, timestamp=Thu Jul 24 14:38:37 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1948000932272812034, awardId=1947136031354527745, awardTitle=会员卡, userOrderId=1948271579934765058, awardConfig=null))',
        'completed', '2025-07-24 14:38:37', '2025-07-24 14:38:37'),
       (2982, 'send_award', 1948000932272812034, 'yong', '23745823984',
        'BaseEvent.EventMessage(id=23745823984, timestamp=Thu Jul 24 14:38:40 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1948000932272812034, awardId=1947136456283660289, awardTitle=特色小台灯, userOrderId=1948271594182815745, awardConfig=null))',
        'completed', '2025-07-24 14:38:40', '2025-07-24 14:38:40'),
       (2983, 'send_award', 1948000932272812034, 'yong', '40854416948',
        'BaseEvent.EventMessage(id=40854416948, timestamp=Thu Jul 24 14:38:43 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1948000932272812034, awardId=1947136290151473154, awardTitle=舒适办公椅, userOrderId=1948271607319375874, awardConfig=null))',
        'completed', '2025-07-24 14:38:43', '2025-07-24 14:38:43'),
       (2984, 'send_award', 1948000932272812034, 'yong', '97794539943',
        'BaseEvent.EventMessage(id=97794539943, timestamp=Thu Jul 24 14:38:46 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1948000932272812034, awardId=1947136366194204674, awardTitle=小玩偶, userOrderId=1948271620854394882, awardConfig=null))',
        'completed', '2025-07-24 14:38:46', '2025-07-24 14:38:46'),
       (2985, 'send_award', 1948000932272812034, 'yong', '85477084674',
        'BaseEvent.EventMessage(id=85477084674, timestamp=Thu Jul 24 14:38:49 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1948000932272812034, awardId=1947135858008137730, awardTitle=随机积分, userOrderId=1948271634116784130, awardConfig=null))',
        'completed', '2025-07-24 14:38:49', '2025-07-24 14:38:49'),
       (2986, 'send_award', 1948000932272812034, 'yong', '92136526295',
        'BaseEvent.EventMessage(id=92136526295, timestamp=Thu Jul 24 14:38:53 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1948000932272812034, awardId=1947136031354527745, awardTitle=会员卡, userOrderId=1948271647467253762, awardConfig=null))',
        'completed', '2025-07-24 14:38:53', '2025-07-24 14:38:53'),
       (2987, 'send_rebate', 1934223919414280193, 'yong', '32672550520',
        'BaseEvent.EventMessage(id=32672550520, timestamp=Thu Jul 24 14:42:07 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1934223919414280193, rebateDesc=签到返积分, rebateType=integral, rebateConfig=50, bizId=yong_1934223919414280193_integral_4_20250724))',
        'completed', '2025-07-24 14:42:07', '2025-07-24 14:42:07'),
       (2988, 'send_rebate', 1934223919414280193, 'yong', '15073017813',
        'BaseEvent.EventMessage(id=15073017813, timestamp=Thu Jul 24 14:42:07 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1934223919414280193, rebateDesc=签到返抽奖次数, rebateType=sku, rebateConfig=1934859819538202625, bizId=yong_1934223919414280193_sku_1934927347346096129_20250724))',
        'completed', '2025-07-24 14:42:07', '2025-07-24 14:42:07'),
       (2989, 'credit_adjust_success', 1934223919414280193, 'yong', '06459321768',
        '{\"data\":{\"activityId\":1934223919414280193,\"amount\":50,\"orderId\":\"000489423652\",\"outBusinessNo\":\"yong_1934223919414280193_integral_4_20250724\",\"userId\":\"yong\"},\"id\":\"06459321768\",\"timestamp\":1753339327280}',
        'completed', '2025-07-24 14:42:07', '2025-07-24 14:42:07'),
       (2990, 'send_rebate', 1934223919414280193, 'yong', '25863512789',
        'BaseEvent.EventMessage(id=25863512789, timestamp=Thu Jul 24 14:42:15 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1934223919414280193, rebateDesc=赠送你1次抽奖次数, rebateType=gift, rebateConfig=1, bizId=yong_1934223919414280193_gift_1948272437883842561_2025))',
        'completed', '2025-07-24 14:42:15', '2025-07-24 14:42:15'),
       (2991, 'send_award', 1948000932272812034, 'yong', '96634362194',
        'BaseEvent.EventMessage(id=96634362194, timestamp=Thu Jul 24 14:46:20 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1948000932272812034, awardId=1947135858008137730, awardTitle=随机积分, userOrderId=1948273516164014081, awardConfig=null))',
        'completed', '2025-07-24 14:46:20', '2025-07-24 14:46:20'),
       (2992, 'credit_adjust_success', 1947194890802733058, 'yong', '79705807861',
        '{\"data\":{\"activityId\":1947194890802733058,\"amount\":5.99,\"orderId\":\"736748234898\",\"outBusinessNo\":\"159218864705\",\"userId\":\"yong\"},\"id\":\"79705807861\",\"timestamp\":1753339760927}',
        'completed', '2025-07-24 14:49:20', '2025-07-24 14:49:21'),
       (2993, 'send_rebate', 1947194890802733058, 'yong', '15249015385',
        'BaseEvent.EventMessage(id=15249015385, timestamp=Thu Jul 24 14:50:18 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=1947194890802733058, rebateDesc=第二次赠送你3次抽奖次数, rebateType=gift, rebateConfig=3, bizId=yong_1947194890802733058_gift_1948274480673579009_2025))',
        'completed', '2025-07-24 14:50:18', '2025-07-24 14:50:18'),
       (2994, 'send_award', 1948000932272812034, 'yong', '24981385154',
        'BaseEvent.EventMessage(id=24981385154, timestamp=Thu Jul 24 14:58:27 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=1948000932272812034, awardId=1947136290151473154, awardTitle=舒适办公椅, userOrderId=1948276568531001346, awardConfig=null))',
        'completed', '2025-07-24 14:58:27', '2025-07-24 14:58:27'),
       (2995, 'send_award', 100301, 'yong', '05693708121',
        'BaseEvent.EventMessage(id=05693708121, timestamp=Thu Jul 24 15:14:03 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=100301, awardId=0, awardTitle=谢谢参与, userOrderId=1948280493254500353, awardConfig=谢谢参与))',
        'completed', '2025-07-24 15:14:03', '2025-07-24 15:14:03'),
       (2996, 'send_award', 100301, 'yong', '30289280275',
        'BaseEvent.EventMessage(id=30289280275, timestamp=Thu Jul 24 15:32:26 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=100301, awardId=0, awardTitle=谢谢参与, userOrderId=1948285123262713857, awardConfig=谢谢参与))',
        'completed', '2025-07-24 15:32:26', '2025-07-24 15:32:26'),
       (2997, 'send_award', 100301, 'yong', '72335419603',
        'BaseEvent.EventMessage(id=72335419603, timestamp=Thu Jul 24 15:32:53 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=100301, awardId=0, awardTitle=谢谢参与, userOrderId=1948285238895480833, awardConfig=谢谢参与))',
        'completed', '2025-07-24 15:32:53', '2025-07-24 15:32:53'),
       (2998, 'send_award', 100301, 'yong', '34253848235',
        'BaseEvent.EventMessage(id=34253848235, timestamp=Thu Jul 24 15:32:58 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=100301, awardId=106, awardTitle=轻奢办公椅, userOrderId=1948285257736294402, awardConfig=null))',
        'completed', '2025-07-24 15:32:58', '2025-07-24 15:32:58'),
       (2999, 'send_award', 100301, 'yong', '36472089637',
        'BaseEvent.EventMessage(id=36472089637, timestamp=Thu Jul 24 15:34:43 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=100301, awardId=105, awardTitle=小米su7周体验, userOrderId=1948285697878167553, awardConfig=null))',
        'completed', '2025-07-24 15:34:43', '2025-07-24 15:34:43'),
       (3000, 'send_award', 100301, 'yong', '49311982699',
        'BaseEvent.EventMessage(id=49311982699, timestamp=Thu Jul 24 15:34:58 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=100301, awardId=0, awardTitle=谢谢参与, userOrderId=1948285764555018241, awardConfig=谢谢参与))',
        'completed', '2025-07-24 15:34:58', '2025-07-24 15:34:58'),
       (3001, 'send_award', 100301, 'yong', '66566162090',
        'BaseEvent.EventMessage(id=66566162090, timestamp=Thu Jul 24 15:36:44 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong, activityId=100301, awardId=0, awardTitle=谢谢参与, userOrderId=1948286203413434370, awardConfig=谢谢参与))',
        'completed', '2025-07-24 15:36:44', '2025-07-24 15:36:44'),
       (3002, 'send_rebate', 100301, 'yong', '83603843274',
        'BaseEvent.EventMessage(id=83603843274, timestamp=Thu Jul 24 15:45:30 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=100301, rebateDesc=签到返利-抽奖额度, rebateType=sku, rebateConfig=9011, bizId=yong_100301_sku_1_20250724))',
        'completed', '2025-07-24 15:45:30', '2025-07-24 15:45:30'),
       (3003, 'send_rebate', 100301, 'yong', '70809677840',
        'BaseEvent.EventMessage(id=70809677840, timestamp=Thu Jul 24 15:45:30 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=100301, rebateDesc=签到返利-积分, rebateType=integral, rebateConfig=10, bizId=yong_100301_integral_2_20250724))',
        'completed', '2025-07-24 15:45:30', '2025-07-24 15:45:30'),
       (3004, 'credit_adjust_success', 100301, 'yong', '70152889134',
        '{\"data\":{\"activityId\":100301,\"amount\":10,\"orderId\":\"667094695562\",\"outBusinessNo\":\"yong_100301_integral_2_20250724\",\"userId\":\"yong\"},\"id\":\"70152889134\",\"timestamp\":1753343131262}',
        'completed', '2025-07-24 15:45:31', '2025-07-24 15:45:31'),
       (3005, 'credit_adjust_success', 100301, 'yong', '31374604719',
        '{\"data\":{\"activityId\":100301,\"amount\":1.99,\"orderId\":\"082430488865\",\"outBusinessNo\":\"643122837883\",\"userId\":\"yong\"},\"id\":\"31374604719\",\"timestamp\":1753343176314}',
        'completed', '2025-07-24 15:46:16', '2025-07-24 15:46:16'),
       (3006, 'send_rebate', 100301, 'yong', '77031140003',
        'BaseEvent.EventMessage(id=77031140003, timestamp=Thu Jul 24 15:47:04 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong, activityId=100301, rebateDesc=测试赠送, rebateType=gift, rebateConfig=1, bizId=yong_100301_gift_1948288777088700417_2025))',
        'completed', '2025-07-24 15:47:04', '2025-07-24 15:47:04');
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
    `id`            bigint unsigned NOT NULL COMMENT '雪花ID',
    `user_id`       varchar(32)     NOT NULL COMMENT '用户ID',
    `activity_id`   bigint          NOT NULL COMMENT '活动ID',
    `strategy_id`   bigint          NOT NULL COMMENT '抽奖策略ID',
    `user_order_id` bigint          NOT NULL COMMENT '抽奖单ID【作为幂等使用】',
    `award_id`      bigint          NOT NULL COMMENT '奖品ID',
    `award_title`   varchar(128)    NOT NULL COMMENT '奖品标题（名称）',
    `award_time`    datetime        NOT NULL COMMENT '中奖时间',
    `award_state`   varchar(16)     NOT NULL DEFAULT 'create' COMMENT '奖品状态；create-创建、completed-发奖完成、、fail-发奖失败',
    `create_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`user_order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_award_id` (`strategy_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户中奖流水表';
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
    `id`            bigint unsigned NOT NULL COMMENT '雪花ID',
    `user_id`       varchar(32)     NOT NULL COMMENT '用户ID',
    `activity_id`   bigint          NOT NULL COMMENT '活动ID',
    `strategy_id`   bigint          NOT NULL COMMENT '抽奖策略ID',
    `user_order_id` bigint          NOT NULL COMMENT '抽奖单ID【作为幂等使用】',
    `award_id`      bigint          NOT NULL COMMENT '奖品ID',
    `award_title`   varchar(128)    NOT NULL COMMENT '奖品标题（名称）',
    `award_time`    datetime        NOT NULL COMMENT '中奖时间',
    `award_state`   varchar(16)     NOT NULL DEFAULT 'create' COMMENT '奖品状态；create-创建、completed-发奖完成、、fail-发奖失败',
    `create_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`user_order_id`),
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
    `id`            bigint unsigned NOT NULL COMMENT '雪花ID',
    `user_id`       varchar(32)     NOT NULL COMMENT '用户ID',
    `activity_id`   bigint          NOT NULL COMMENT '活动ID',
    `strategy_id`   bigint          NOT NULL COMMENT '抽奖策略ID',
    `user_order_id` bigint          NOT NULL COMMENT '抽奖单ID【作为幂等使用】',
    `award_id`      bigint          NOT NULL COMMENT '奖品ID',
    `award_title`   varchar(128)    NOT NULL COMMENT '奖品标题（名称）',
    `award_time`    datetime        NOT NULL COMMENT '中奖时间',
    `award_state`   varchar(16)     NOT NULL DEFAULT 'create' COMMENT '奖品状态；create-创建、completed-发奖完成、、fail-发奖失败',
    `create_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`user_order_id`),
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
    `id`            bigint unsigned NOT NULL COMMENT '雪花ID',
    `user_id`       varchar(32)     NOT NULL COMMENT '用户ID',
    `activity_id`   bigint          NOT NULL COMMENT '活动ID',
    `strategy_id`   bigint          NOT NULL COMMENT '抽奖策略ID',
    `user_order_id` bigint          NOT NULL COMMENT '抽奖单ID【作为幂等使用】',
    `award_id`      bigint          NOT NULL COMMENT '奖品ID',
    `award_title`   varchar(128)    NOT NULL COMMENT '奖品标题（名称）',
    `award_time`    datetime        NOT NULL COMMENT '中奖时间',
    `award_state`   varchar(16)     NOT NULL DEFAULT 'create' COMMENT '奖品状态；create-创建、completed-发奖完成、、fail-发奖失败',
    `create_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`user_order_id`),
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
INSERT INTO `user_award_record_003`
VALUES (1946786071333335042, 'yong', 100301, 200001, 1946786067894005762, 107, '小霸王游戏机', '2025-07-20 12:15:44',
        'create', '2025-07-20 12:15:43', '2025-07-20 12:15:43'),
       (1946786556693999617, 'yong', 100301, 200001, 1946786554139668483, 101, '随机积分', '2025-07-20 12:17:39',
        'complete', '2025-07-20 12:17:39', '2025-07-20 12:17:39'),
       (1946786572393279489, 'yong', 100301, 200001, 1946786570115772417, 101, '随机积分', '2025-07-20 12:17:43',
        'complete', '2025-07-20 12:17:43', '2025-07-20 12:17:43'),
       (1946786590080659458, 'yong', 100301, 200001, 1946786586586804225, 101, '随机积分', '2025-07-20 12:17:47',
        'complete', '2025-07-20 12:17:47', '2025-07-20 12:17:47'),
       (1946786605985460225, 'yong', 100301, 200001, 1946786602911035394, 105, '小米su7周体验', '2025-07-20 12:17:51',
        'create', '2025-07-20 12:17:50', '2025-07-20 12:17:50'),
       (1946786619457564674, 'yong', 100301, 200001, 1946786617272332290, 101, '随机积分', '2025-07-20 12:17:54',
        'complete', '2025-07-20 12:17:54', '2025-07-20 12:17:54'),
       (1946786635920207874, 'yong', 100301, 200001, 1946786633693032449, 101, '随机积分', '2025-07-20 12:17:58',
        'complete', '2025-07-20 12:17:57', '2025-07-20 12:17:58'),
       (1946786649304231938, 'yong', 100301, 200001, 1946786647102222338, 106, '轻奢办公椅', '2025-07-20 12:18:01',
        'create', '2025-07-20 12:18:02', '2025-07-20 12:18:02'),
       (1946786665653628930, 'yong', 100301, 200001, 1946786663510339585, 101, '随机积分', '2025-07-20 12:18:05',
        'complete', '2025-07-20 12:18:05', '2025-07-20 12:18:05'),
       (1946786683986931714, 'yong', 100301, 200001, 1946786679935234050, 101, '随机积分', '2025-07-20 12:18:10',
        'complete', '2025-07-20 12:18:10', '2025-07-20 12:18:10'),
       (1946786701514928130, 'yong', 100301, 200001, 1946786698901876738, 101, '随机积分', '2025-07-20 12:18:14',
        'complete', '2025-07-20 12:18:14', '2025-07-20 12:18:14'),
       (1946786717956599810, 'yong', 100301, 200001, 1946786715742007298, 101, '随机积分', '2025-07-20 12:18:18',
        'complete', '2025-07-20 12:18:17', '2025-07-20 12:18:17'),
       (1946786736516395010, 'yong', 100301, 200001, 1946786732988985346, 101, '随机积分', '2025-07-20 12:18:22',
        'complete', '2025-07-20 12:18:22', '2025-07-20 12:18:22'),
       (1946786752240840705, 'yong', 100301, 200001, 1946786749581651969, 101, '随机积分', '2025-07-20 12:18:26',
        'complete', '2025-07-20 12:18:25', '2025-07-20 12:18:25'),
       (1946786970386591746, 'yong', 1934223919414280193, 200001, 1946786968167804932, 101, '随机积分',
        '2025-07-20 12:19:18', 'complete', '2025-07-20 12:19:18', '2025-07-20 12:19:18'),
       (1946786987000229889, 'yong', 1934223919414280193, 200001, 1946786984798220290, 101, '随机积分',
        '2025-07-20 12:19:22', 'complete', '2025-07-20 12:19:21', '2025-07-20 12:19:21'),
       (1946787002791784450, 'yong', 1934223919414280193, 200001, 1946787000430391298, 105, '小米su7周体验',
        '2025-07-20 12:19:26', 'create', '2025-07-20 12:19:25', '2025-07-20 12:19:25'),
       (1946787018621087746, 'yong', 1934223919414280193, 200001, 1946787016360357889, 101, '随机积分',
        '2025-07-20 12:19:29', 'complete', '2025-07-20 12:19:29', '2025-07-20 12:19:29'),
       (1946787430191362050, 'yong', 100301, 200001, 1946787427913854977, 101, '随机积分', '2025-07-20 12:21:08',
        'complete', '2025-07-20 12:21:07', '2025-07-20 12:21:08'),
       (1946787463867428866, 'yong', 100301, 200001, 1946787461594116097, 101, '随机积分', '2025-07-20 12:21:16',
        'complete', '2025-07-20 12:21:15', '2025-07-20 12:21:16'),
       (1946787494011891713, 'yong', 100301, 200001, 1946787491520475138, 101, '随机积分', '2025-07-20 12:21:23',
        'complete', '2025-07-20 12:21:22', '2025-07-20 12:21:23'),
       (1946787511808323585, 'yong', 100301, 200001, 1946787509597925378, 101, '随机积分', '2025-07-20 12:21:27',
        'complete', '2025-07-20 12:21:26', '2025-07-20 12:21:27'),
       (1946787560902651906, 'yong', 100301, 200001, 1946787558696448002, 101, '随机积分', '2025-07-20 12:21:39',
        'complete', '2025-07-20 12:21:38', '2025-07-20 12:21:39'),
       (1946787579034628098, 'yong', 100301, 200001, 1946787574940987393, 101, '随机积分', '2025-07-20 12:21:43',
        'complete', '2025-07-20 12:21:43', '2025-07-20 12:21:43'),
       (1946787596252246018, 'yong', 100301, 200001, 1946787592859054082, 101, '随机积分', '2025-07-20 12:21:47',
        'complete', '2025-07-20 12:21:47', '2025-07-20 12:21:47'),
       (1946787611792142337, 'yong', 100301, 200001, 1946787609569161217, 106, '轻奢办公椅', '2025-07-20 12:21:51',
        'create', '2025-07-20 12:21:51', '2025-07-20 12:21:51'),
       (1946787626887442434, 'yong', 100301, 200001, 1946787624832233473, 105, '小米su7周体验', '2025-07-20 12:21:54',
        'create', '2025-07-20 12:21:54', '2025-07-20 12:21:54'),
       (1946787641550729217, 'yong', 100301, 200001, 1946787639327748097, 101, '随机积分', '2025-07-20 12:21:58',
        'complete', '2025-07-20 12:21:57', '2025-07-20 12:21:58'),
       (1946787772027138050, 'yong', 100301, 200001, 1946787769741242370, 101, '随机积分', '2025-07-20 12:22:29',
        'complete', '2025-07-20 12:22:29', '2025-07-20 12:22:29'),
       (1946787786166136833, 'yong', 100301, 200001, 1946787784018653186, 101, '随机积分', '2025-07-20 12:22:32',
        'complete', '2025-07-20 12:22:32', '2025-07-20 12:22:32'),
       (1947289924365484033, 'yong', 100301, 200001, 1947289921769209859, 107, '小霸王游戏机', '2025-07-21 21:37:52',
        'create', '2025-07-21 21:37:51', '2025-07-21 21:37:51'),
       (1947289942522626049, 'yong', 100301, 200001, 1947289940245118978, 101, '随机积分', '2025-07-21 21:37:56',
        'complete', '2025-07-21 21:37:56', '2025-07-21 21:37:56'),
       (1947290261331673089, 'yong', 100301, 200001, 1947290258336940034, 101, '随机积分', '2025-07-21 21:39:12',
        'complete', '2025-07-21 21:39:11', '2025-07-21 21:39:11'),
       (1947290293074165761, 'yong', 100301, 200001, 1947290290679218178, 107, '小霸王游戏机', '2025-07-21 21:39:19',
        'create', '2025-07-21 21:39:20', '2025-07-21 21:39:20'),
       (1947316200618979330, 'yong', 1947194890802733058, 1947130468034007042, 1947312520406515715, 1947135858008137730,
        '随机积分', '2025-07-21 23:22:16', 'create', '2025-07-21 23:22:16', '2025-07-21 23:22:16'),
       (1947317427796709377, 'yong', 1947194890802733058, 1947130468034007042, 1947317424999108611, 1947136149856198658,
        '支付立减券', '2025-07-21 23:27:09', 'create', '2025-07-21 23:27:08', '2025-07-21 23:27:08'),
       (1947317464748527617, 'yong', 1947194890802733058, 1947130468034007042, 1947317462156447745, 1947136031354527745,
        '会员卡', '2025-07-21 23:27:18', 'create', '2025-07-21 23:27:17', '2025-07-21 23:27:17'),
       (1947317484914741250, 'yong', 1947194890802733058, 1947130468034007042, 1947317482461073410, 1947135858008137730,
        '随机积分', '2025-07-21 23:27:22', 'create', '2025-07-21 23:27:21', '2025-07-21 23:27:21'),
       (1947317502069444610, 'yong', 1947194890802733058, 1947130468034007042, 1947317499968098305, 1947136031354527745,
        '会员卡', '2025-07-21 23:27:27', 'create', '2025-07-21 23:27:27', '2025-07-21 23:27:27'),
       (1947317520264335362, 'yong', 1947194890802733058, 1947130468034007042, 1947317517475123201, 1947136290151473154,
        '办公椅', '2025-07-21 23:27:31', 'create', '2025-07-21 23:27:31', '2025-07-21 23:27:31'),
       (1947317581933187073, 'yong', 1947194890802733058, 1947130468034007042, 1947317579601154050, 1947135858008137730,
        '随机积分', '2025-07-21 23:27:46', 'create', '2025-07-21 23:27:45', '2025-07-21 23:27:45'),
       (1947318597978165249, 'yong', 1947194890802733058, 1947130468034007042, 1947318595235090433, 1947135858008137730,
        '随机积分', '2025-07-21 23:31:48', 'create', '2025-07-21 23:31:48', '2025-07-21 23:31:48'),
       (1947318856393428993, 'yong', 1947194890802733058, 1947130468034007042, 1947318854350802946, 1947135858008137730,
        '随机积分', '2025-07-21 23:32:49', 'complete', '2025-07-21 23:32:49', '2025-07-21 23:32:50'),
       (1947318881924157442, 'yong', 1947194890802733058, 1947130468034007042, 1947318879910891522, 1947135858008137730,
        '随机积分', '2025-07-21 23:32:56', 'complete', '2025-07-21 23:32:55', '2025-07-21 23:32:55'),
       (1947318904841834497, 'yong', 1947194890802733058, 1947130468034007042, 1947318902757265409, 1947136031354527745,
        '会员卡', '2025-07-21 23:33:01', 'create', '2025-07-21 23:33:00', '2025-07-21 23:33:00'),
       (1947318968259710978, 'yong', 1947194890802733058, 1947130468034007042, 1947318966074478593, 1947135858008137730,
        '随机积分', '2025-07-21 23:33:16', 'complete', '2025-07-21 23:33:16', '2025-07-21 23:33:17'),
       (1947320809114591234, 'yong', 1947194890802733058, 1947130468034007042, 1947320806740615170, 1947135858008137730,
        '随机积分', '2025-07-21 23:40:35', 'complete', '2025-07-21 23:40:35', '2025-07-21 23:40:35'),
       (1947320835756810242, 'yong', 1947194890802733058, 1947130468034007042, 1947320833143758850, 1947136456283660289,
        '小台灯', '2025-07-21 23:40:41', 'create', '2025-07-21 23:40:41', '2025-07-21 23:40:41'),
       (1947320853578407938, 'yong', 1947194890802733058, 1947130468034007042, 1947320851326066690, 1947135858008137730,
        '随机积分', '2025-07-21 23:40:46', 'complete', '2025-07-21 23:40:46', '2025-07-21 23:40:46'),
       (1947320932804616194, 'yong', 1947194890802733058, 1947130468034007042, 1947320930413862914, 1947136149856198658,
        '支付立减券', '2025-07-21 23:41:05', 'create', '2025-07-21 23:41:04', '2025-07-21 23:41:04'),
       (1947320963406258177, 'yong', 1947194890802733058, 1947130468034007042, 1947320961447518210, 1947135858008137730,
        '随机积分', '2025-07-21 23:41:12', 'complete', '2025-07-21 23:41:12', '2025-07-21 23:41:30'),
       (1947321536335601665, 'yong', 1947194890802733058, 1947130468034007042, 1947321370383769601, 1947136366194204674,
        '小玩偶', '2025-07-21 23:43:28', 'create', '2025-07-21 23:43:28', '2025-07-21 23:43:28'),
       (1947321581655056386, 'yong', 1947194890802733058, 1947130468034007042, 1947321567407005697, 1947136149856198658,
        '支付立减券', '2025-07-21 23:43:39', 'create', '2025-07-21 23:43:38', '2025-07-21 23:43:38'),
       (1947321813709119490, 'yong', 1947194890802733058, 1947130468034007042, 1947321608947392514, 1947135858008137730,
        '随机积分', '2025-07-21 23:44:35', 'complete', '2025-07-21 23:44:34', '2025-07-21 23:44:34'),
       (1947323283582054401, 'yong', 1947194890802733058, 1947130468034007042, 1947322875627200513, 1947136366194204674,
        '小玩偶', '2025-07-21 23:50:25', 'create', '2025-07-21 23:50:25', '2025-07-21 23:50:25'),
       (1947323307569278978, 'yong', 1947194890802733058, 1947130468034007042, 1947323305497292801, 1947136149856198658,
        '支付立减券', '2025-07-21 23:50:31', 'create', '2025-07-21 23:50:30', '2025-07-21 23:50:30'),
       (1947323328398192641, 'yong', 1947194890802733058, 1947130468034007042, 1947323326326206465, 1947136456283660289,
        '小台灯', '2025-07-21 23:50:36', 'create', '2025-07-21 23:50:36', '2025-07-21 23:50:36'),
       (1947323350044995585, 'yong', 1947194890802733058, 1947130468034007042, 1947323347712962562, 1947136031354527745,
        '会员卡', '2025-07-21 23:50:41', 'create', '2025-07-21 23:50:41', '2025-07-21 23:50:41'),
       (1947323370815188993, 'yong', 1947194890802733058, 1947130468034007042, 1947323368608985090, 1947136031354527745,
        '会员卡', '2025-07-21 23:50:46', 'create', '2025-07-21 23:50:46', '2025-07-21 23:50:46'),
       (1947323400858988545, 'yong', 1947194890802733058, 1947130468034007042, 1947323398795390977, 1947136456283660289,
        '小台灯', '2025-07-21 23:50:53', 'create', '2025-07-21 23:50:52', '2025-07-21 23:50:52'),
       (1947323416998670338, 'yong', 1947194890802733058, 1947130468034007042, 1947323414981210113, 1947136366194204674,
        '小玩偶', '2025-07-21 23:50:57', 'create', '2025-07-21 23:50:56', '2025-07-21 23:50:56'),
       (1947323431498379266, 'yong', 1947194890802733058, 1947130468034007042, 1947323429430587394, 1947136149856198658,
        '支付立减券', '2025-07-21 23:51:00', 'create', '2025-07-21 23:50:59', '2025-07-21 23:50:59'),
       (1947323445947756545, 'yong', 1947194890802733058, 1947130468034007042, 1947323443821244418, 1947135858008137730,
        '随机积分', '2025-07-21 23:51:04', 'complete', '2025-07-21 23:51:04', '2025-07-21 23:51:17'),
       (1947323580425531394, 'yong', 1947194890802733058, 1947130468034007042, 1947323578550677506, 1947135858008137730,
        '随机积分', '2025-07-21 23:51:36', 'complete', '2025-07-21 23:51:36', '2025-07-21 23:51:36'),
       (1947323672545030146, 'yong', 1947194890802733058, 1947130468034007042, 1947323670531764226, 1947136456283660289,
        '小台灯', '2025-07-21 23:51:58', 'create', '2025-07-21 23:51:57', '2025-07-21 23:51:57'),
       (1947323707068346370, 'yong', 1947194890802733058, 1947130468034007042, 1947323704996360194, 1947136149856198658,
        '支付立减券', '2025-07-21 23:52:06', 'create', '2025-07-21 23:52:05', '2025-07-21 23:52:05'),
       (1947323722725683202, 'yong', 1947194890802733058, 1947130468034007042, 1947323720704028674, 1947136456283660289,
        '小台灯', '2025-07-21 23:52:10', 'create', '2025-07-21 23:52:10', '2025-07-21 23:52:10'),
       (1947323921493749762, 'yong', 1947194890802733058, 1947130468034007042, 1947323735845466114, 1947135858008137730,
        '随机积分', '2025-07-21 23:52:57', 'complete', '2025-07-21 23:52:57', '2025-07-21 23:52:57'),
       (1947326134278447105, 'yong', 1947194890802733058, 1947130468034007042, 1947324441985904641, 1947136149856198658,
        '支付立减券', '2025-07-22 00:01:45', 'create', '2025-07-22 00:01:45', '2025-07-22 00:01:45'),
       (1947326183775428609, 'yong', 1947194890802733058, 1947130468034007042, 1947326181841854465, 1947136149856198658,
        '支付立减券', '2025-07-22 00:01:56', 'create', '2025-07-22 00:01:56', '2025-07-22 00:01:56'),
       (1947326918915342338, 'yong', 1947194890802733058, 1947130468034007042, 1947326916365205506, 1947136456283660289,
        '小台灯', '2025-07-22 00:04:52', 'create', '2025-07-22 00:04:52', '2025-07-22 00:04:52'),
       (1947326934727868418, 'yong', 1947194890802733058, 1947130468034007042, 1947326932395835394, 1947135858008137730,
        '随机积分', '2025-07-22 00:04:55', 'complete', '2025-07-22 00:04:55', '2025-07-22 00:04:56'),
       (1947326949701533697, 'yong', 1947194890802733058, 1947130468034007042, 1947326947499524098, 1947136031354527745,
        '会员卡', '2025-07-22 00:04:59', 'create', '2025-07-22 00:04:59', '2025-07-22 00:04:59'),
       (1947496262760722433, 'yong', 1947194890802733058, 1947130468034007042, 1947496258318954498, 1947135858008137730,
        '随机积分', '2025-07-22 11:17:46', 'complete', '2025-07-22 11:17:46', '2025-07-22 11:17:46'),
       (1947496280209027073, 'yong', 1947194890802733058, 1947130468034007042, 1947496277939908610, 1947135858008137730,
        '随机积分', '2025-07-22 11:17:51', 'complete', '2025-07-22 11:17:50', '2025-07-22 11:17:50'),
       (1947496324056281089, 'yong', 1947194890802733058, 1947130468034007042, 1947496294318665729, 1947135858008137730,
        '随机积分', '2025-07-22 11:18:01', 'complete', '2025-07-22 11:18:01', '2025-07-22 11:18:01'),
       (1947496339474542594, 'yong', 1947194890802733058, 1947130468034007042, 1947496337025069057, 1947136149856198658,
        '支付立减券', '2025-07-22 11:18:05', 'create', '2025-07-22 11:18:04', '2025-07-22 11:18:04'),
       (1947497392756551682, 'yong', 1947194890802733058, 1947130468034007042, 1947497390734897153, 1947135858008137730,
        '随机积分', '2025-07-22 11:22:16', 'complete', '2025-07-22 11:22:15', '2025-07-22 11:22:16'),
       (1947508256679161857, 'yong', 1947194890802733058, 1947130468034007042, 1947497500239785985, 1947133372497252353,
        '黑名单奖品', '2025-07-22 12:05:26', 'create', '2025-07-22 12:05:26', '2025-07-22 12:05:26'),
       (1947508345673904130, 'yong', 1947194890802733058, 1947130468034007042, 1947508345095090177, 1947133372497252353,
        '黑名单奖品', '2025-07-22 12:05:47', 'create', '2025-07-22 12:05:47', '2025-07-22 12:05:47'),
       (1947508388401278977, 'yong', 1947194890802733058, 1947130468034007042, 1947508387893768194, 1947133372497252353,
        '黑名单奖品', '2025-07-22 12:05:57', 'create', '2025-07-22 12:05:57', '2025-07-22 12:05:57'),
       (1947508451001266178, 'yong', 1947194890802733058, 1947130468034007042, 1947508450414063617, 1947133372497252353,
        '黑名单奖品', '2025-07-22 12:06:12', 'create', '2025-07-22 12:06:12', '2025-07-22 12:06:12'),
       (1947510713744474114, 'yong', 1947194890802733058, 1947130468034007042, 1947510713018859522, 1947133372497252353,
        '黑名单奖品', '2025-07-22 12:15:12', 'complete', '2025-07-22 12:15:11', '2025-07-22 12:15:12'),
       (1947510730291003393, 'yong', 1947194890802733058, 1947130468034007042, 1947510729833824258, 1947133372497252353,
        '黑名单奖品', '2025-07-22 12:15:16', 'complete', '2025-07-22 12:15:15', '2025-07-22 12:15:15'),
       (1947510745067536386, 'yong', 1947194890802733058, 1947130468034007042, 1947510744484528130, 1947133372497252353,
        '黑名单奖品', '2025-07-22 12:15:19', 'complete', '2025-07-22 12:15:18', '2025-07-22 12:15:19'),
       (1947510758283788290, 'yong', 1947194890802733058, 1947130468034007042, 1947510757629476865, 1947133372497252353,
        '黑名单奖品', '2025-07-22 12:15:22', 'complete', '2025-07-22 12:15:22', '2025-07-22 12:15:22'),
       (1947510774285058050, 'yong', 1947194890802733058, 1947130468034007042, 1947510773559443458, 1947133372497252353,
        '黑名单奖品', '2025-07-22 12:15:26', 'complete', '2025-07-22 12:15:25', '2025-07-22 12:15:26'),
       (1947510791167131649, 'yong', 1947194890802733058, 1947130468034007042, 1947510790512820226, 1947133372497252353,
        '黑名单奖品', '2025-07-22 12:15:30', 'complete', '2025-07-22 12:15:30', '2025-07-22 12:15:30'),
       (1947511242310664193, 'yong', 1947194890802733058, 1947130468034007042, 1947511238078611458, 1947136149856198658,
        '支付立减券', '2025-07-22 12:17:18', 'create', '2025-07-22 12:17:17', '2025-07-22 12:17:17'),
       (1947511331418652674, 'yong', 1947194890802733058, 1947130468034007042, 1947511329271169025, 1947136149856198658,
        '支付立减券', '2025-07-22 12:17:39', 'create', '2025-07-22 12:17:39', '2025-07-22 12:17:39'),
       (1947511351798775809, 'yong', 1947194890802733058, 1947130468034007042, 1947511349458354177, 1947136031354527745,
        '会员卡', '2025-07-22 12:17:44', 'create', '2025-07-22 12:17:43', '2025-07-22 12:17:43'),
       (1947512556734234625, 'yong', 1947194890802733058, 1947130468034007042, 1947512554129571841, 1947135858008137730,
        '随机积分', '2025-07-22 12:22:31', 'complete', '2025-07-22 12:22:31', '2025-07-22 12:22:31'),
       (1947515415823499266, 'yong', 1947194890802733058, 1947130468034007042, 1947512622681276417, 1947135858008137730,
        '随机积分', '2025-07-22 12:33:53', 'complete', '2025-07-22 12:33:52', '2025-07-22 12:33:53'),
       (1947522663375278081, 'yong', 1947194890802733058, 1947130468034007042, 1947515521058586626, 1947136617739198466,
        '小米su7', '2025-07-22 13:02:41', 'create', '2025-07-22 13:02:40', '2025-07-22 13:02:40'),
       (1947522702977896449, 'yong', 1947194890802733058, 1947130468034007042, 1947522683449217026, 1947136366194204674,
        '小玩偶', '2025-07-22 13:02:50', 'create', '2025-07-22 13:02:50', '2025-07-22 13:02:50'),
       (1947522748536426497, 'yong', 1947194890802733058, 1947130468034007042, 1947522744891576321, 1947136149856198658,
        '支付立减券', '2025-07-22 13:03:01', 'create', '2025-07-22 13:03:01', '2025-07-22 13:03:01'),
       (1947522767112998913, 'yong', 1947194890802733058, 1947130468034007042, 1947522763115827202, 1947136366194204674,
        '小玩偶', '2025-07-22 13:03:06', 'create', '2025-07-22 13:03:05', '2025-07-22 13:03:05'),
       (1947522788050964481, 'yong', 1947194890802733058, 1947130468034007042, 1947522783223320577, 1947135858008137730,
        '随机积分', '2025-07-22 13:03:11', 'complete', '2025-07-22 13:03:10', '2025-07-22 13:03:10'),
       (1947523557487587329, 'yong', 1934223919414280193, 200001, 1947523554597711874, 105, '小米su7周体验',
        '2025-07-22 13:06:14', 'create', '2025-07-22 13:06:13', '2025-07-22 13:06:13'),
       (1947523607370444802, 'yong', 1934223919414280193, 200001, 1947523572071182337, 101, '随机积分',
        '2025-07-22 13:06:26', 'complete', '2025-07-22 13:06:25', '2025-07-22 13:06:26'),
       (1947523626332893185, 'yong', 1934223919414280193, 200001, 1947523622436384769, 101, '随机积分',
        '2025-07-22 13:06:30', 'complete', '2025-07-22 13:06:30', '2025-07-22 13:06:30'),
       (1947523903379255298, 'yong', 1947194890802733058, 1947130468034007042, 1947523899084288001, 1947136290151473154,
        '办公椅', '2025-07-22 13:07:36', 'create', '2025-07-22 13:07:36', '2025-07-22 13:07:36'),
       (1947524009373511681, 'yong', 1947194890802733058, 1947130468034007042, 1947524007288942594, 1947136290151473154,
        '办公椅', '2025-07-22 13:08:02', 'create', '2025-07-22 13:08:01', '2025-07-22 13:08:01'),
       (1947524092349427713, 'yong', 1947194890802733058, 1947130468034007042, 1947524089421803521, 1947136290151473154,
        '办公椅', '2025-07-22 13:08:22', 'create', '2025-07-22 13:08:21', '2025-07-22 13:08:21'),
       (1947524182430494722, 'yong', 1947194890802733058, 1947130468034007042, 1947524178835976193, 1947136290151473154,
        '办公椅', '2025-07-22 13:08:43', 'create', '2025-07-22 13:08:42', '2025-07-22 13:08:42'),
       (1947550168941412354, 'yong', 1947194890802733058, 1947130468034007042, 1947547744134578177, 1947136290151473154,
        '办公椅', '2025-07-22 14:51:59', 'create', '2025-07-22 14:51:58', '2025-07-22 14:51:58'),
       (1947550647905763329, 'yong', 100301, 200001, 1947308317156163586, 101, '随机积分', '2025-07-22 14:53:53',
        'complete', '2025-07-22 14:53:52', '2025-07-22 14:53:53'),
       (1947550886662324226, 'yong', 1947194890802733058, 1947130468034007042, 1947550884007329794, 1947136149856198658,
        '支付立减券', '2025-07-22 14:54:50', 'create', '2025-07-22 14:54:49', '2025-07-22 14:54:49'),
       (1947552035578658817, 'yong', 1947194890802733058, 1947130468034007042, 1947552033875771394, 1947136366194204674,
        '小玩偶', '2025-07-22 14:59:24', 'create', '2025-07-22 14:59:23', '2025-07-22 14:59:23'),
       (1947922762303660033, 'yong', 1947194890802733058, 1947130468034007042, 1947881506542305281, 1947136149856198658,
        '支付立减券', '2025-07-23 15:32:32', 'create', '2025-07-23 15:32:31', '2025-07-23 15:32:31'),
       (1947923047168204801, 'yong', 1947194890802733058, 1947130468034007042, 1947923044592902145, 1947136366194204674,
        '小玩偶', '2025-07-23 15:33:40', 'create', '2025-07-23 15:33:41', '2025-07-23 15:33:41'),
       (1947924201184706562, 'yong', 1947194890802733058, 1947130468034007042, 1947924198366134274, 1947135858008137730,
        '随机积分', '2025-07-23 15:38:15', 'complete', '2025-07-23 15:38:14', '2025-07-23 15:38:15'),
       (1947927396355145729, 'yong', 1947194890802733058, 1947130468034007042, 1947927393154891778, 1947135858008137730,
        '随机积分', '2025-07-23 15:50:57', 'complete', '2025-07-23 15:50:57', '2025-07-23 15:50:57'),
       (1947928177628151809, 'yong', 1947194890802733058, 1947130468034007042, 1947928174838939649, 1947136149856198658,
        '支付立减券', '2025-07-23 15:54:03', 'create', '2025-07-23 15:54:02', '2025-07-23 15:54:02'),
       (1947928585662627842, 'yong', 1947194890802733058, 1947130468034007042, 1947928582604980225, 1947136290151473154,
        '办公椅', '2025-07-23 15:55:40', 'create', '2025-07-23 15:55:40', '2025-07-23 15:55:40'),
       (1947928652763103234, 'yong', 1947194890802733058, 1947130468034007042, 1947928650678534145, 1947135858008137730,
        '随机积分', '2025-07-23 15:55:56', 'complete', '2025-07-23 15:55:56', '2025-07-23 15:55:56'),
       (1947932466706644993, 'yong', 1947194890802733058, 1947130468034007042, 1947932401845927938, 1947136149856198658,
        '支付立减券', '2025-07-23 16:11:06', 'create', '2025-07-23 16:11:05', '2025-07-23 16:11:05'),
       (1947932480510099458, 'yong', 1947194890802733058, 1947130468034007042, 1947932478236786689, 1947136366194204674,
        '小玩偶', '2025-07-23 16:11:09', 'create', '2025-07-23 16:11:08', '2025-07-23 16:11:08'),
       (1947938589329412097, 'yong', 1947194890802733058, 1947130468034007042, 1947938586200461314, 1947136456283660289,
        '小台灯', '2025-07-23 16:35:25', 'create', '2025-07-23 16:35:25', '2025-07-23 16:35:25'),
       (1947940304644505601, 'yong', 1947194890802733058, 1947130468034007042, 1947940301863682049, 1947135858008137730,
        '随机积分', '2025-07-23 16:42:14', 'complete', '2025-07-23 16:42:14', '2025-07-23 16:42:14'),
       (1947940322063450114, 'yong', 1947194890802733058, 1947130468034007042, 1947940319672696834, 1947136031354527745,
        '会员卡', '2025-07-23 16:42:18', 'create', '2025-07-23 16:42:18', '2025-07-23 16:42:18'),
       (1947940472974508033, 'yong', 1947194890802733058, 1947130468034007042, 1947940470365650946, 1947136149856198658,
        '支付立减券', '2025-07-23 16:42:54', 'create', '2025-07-23 16:42:54', '2025-07-23 16:42:54'),
       (1947943938077794306, 'yong', 100301, 200001, 1947941284995956738, 101, '随机积分', '2025-07-23 16:56:41',
        'complete', '2025-07-23 16:56:39', '2025-07-23 16:56:39'),
       (1947943962216013826, 'yong', 100301, 200001, 1947943959477133314, 101, '随机积分', '2025-07-23 16:56:46',
        'complete', '2025-07-23 16:56:44', '2025-07-23 16:56:44'),
       (1947943980465430530, 'yong', 100301, 200001, 1947943978179534850, 101, '随机积分', '2025-07-23 16:56:51',
        'complete', '2025-07-23 16:56:48', '2025-07-23 16:56:49'),
       (1947944049990213633, 'yong', 100301, 200001, 1947944047372967938, 101, '随机积分', '2025-07-23 16:57:07',
        'complete', '2025-07-23 16:57:06', '2025-07-23 16:57:06'),
       (1947944066675154946, 'yong', 100301, 200001, 1947944063491678210, 107, '小霸王游戏机', '2025-07-23 16:57:11',
        'create', '2025-07-23 16:57:10', '2025-07-23 16:57:10'),
       (1947944204336406530, 'yong', 1934223919414280193, 200001, 1947944201605914626, 101, '随机积分',
        '2025-07-23 16:57:44', 'complete', '2025-07-23 16:57:43', '2025-07-23 16:57:43'),
       (1947944226612355074, 'yong', 1934223919414280193, 200001, 1947944224003497986, 101, '随机积分',
        '2025-07-23 16:57:49', 'complete', '2025-07-23 16:57:48', '2025-07-23 16:57:48'),
       (1947961274625212418, 'yong', 1947194890802733058, 1947130468034007042, 1947941621358166018, 1947136149856198658,
        '支付立减券', '2025-07-23 18:05:34', 'create', '2025-07-23 18:05:34', '2025-07-23 18:05:34'),
       (1947962387999014914, 'yong', 1947194890802733058, 1947130468034007042, 1947961457559781378, 1947136366194204674,
        '小玩偶', '2025-07-23 18:09:59', 'create', '2025-07-23 18:09:59', '2025-07-23 18:09:59'),
       (1947962425244434434, 'yong', 1947194890802733058, 1947130468034007042, 1947962402846851073, 1947135858008137730,
        '随机积分', '2025-07-23 18:10:08', 'complete', '2025-07-23 18:10:07', '2025-07-23 18:10:07'),
       (1947962462380802049, 'yong', 1947194890802733058, 1947130468034007042, 1947962437361778689, 1947135858008137730,
        '随机积分', '2025-07-23 18:10:17', 'complete', '2025-07-23 18:10:18', '2025-07-23 18:10:18'),
       (1947964266808127489, 'yong', 1947194890802733058, 1947130468034007042, 1947962522011222017, 1947136456283660289,
        '小台灯', '2025-07-23 18:17:27', 'create', '2025-07-23 18:17:27', '2025-07-23 18:17:27'),
       (1947964284105437186, 'yong', 1947194890802733058, 1947130468034007042, 1947964281504968706, 1947136031354527745,
        '会员卡', '2025-07-23 18:17:31', 'create', '2025-07-23 18:17:31', '2025-07-23 18:17:31'),
       (1947964303004971009, 'yong', 1947194890802733058, 1947130468034007042, 1947964297980194818, 1947136149856198658,
        '支付立减券', '2025-07-23 18:17:36', 'create', '2025-07-23 18:17:35', '2025-07-23 18:17:35'),
       (1947964319027212290, 'yong', 1947194890802733058, 1947130468034007042, 1947964317013946370, 1947136149856198658,
        '支付立减券', '2025-07-23 18:17:40', 'create', '2025-07-23 18:17:39', '2025-07-23 18:17:39'),
       (1947964336274190338, 'yong', 1947194890802733058, 1947130468034007042, 1947964333224931330, 0, '谢谢参与',
        '2025-07-23 18:17:44', 'create', '2025-07-23 18:17:43', '2025-07-23 18:17:43'),
       (1947964455954460673, 'yong', 1947194890802733058, 1947130468034007042, 1947964453731479554, 1947135858008137730,
        '随机积分', '2025-07-23 18:18:12', 'complete', '2025-07-23 18:18:12', '2025-07-23 18:18:12'),
       (1947964498597949441, 'yong', 1947194890802733058, 1947130468034007042, 1947964493799665665, 1947136456283660289,
        '小台灯', '2025-07-23 18:18:23', 'create', '2025-07-23 18:18:24', '2025-07-23 18:18:24'),
       (1947964532513091586, 'yong', 1947194890802733058, 1947130468034007042, 1947964530508214274, 1947135858008137730,
        '随机积分', '2025-07-23 18:18:31', 'complete', '2025-07-23 18:18:31', '2025-07-23 18:18:31'),
       (1947964547386093570, 'yong', 1947194890802733058, 1947130468034007042, 1947964545318301698, 1947135858008137730,
        '随机积分', '2025-07-23 18:18:34', 'complete', '2025-07-23 18:18:34', '2025-07-23 18:18:34'),
       (1947964561143410690, 'yong', 1947194890802733058, 1947130468034007042, 1947964559180476417, 1947136149856198658,
        '支付立减券', '2025-07-23 18:18:37', 'create', '2025-07-23 18:18:37', '2025-07-23 18:18:37'),
       (1947964575458570241, 'yong', 1947194890802733058, 1947130468034007042, 1947964573227200513, 1947136366194204674,
        '小玩偶', '2025-07-23 18:18:41', 'create', '2025-07-23 18:18:40', '2025-07-23 18:18:40'),
       (1947964590193160194, 'yong', 1947194890802733058, 1947130468034007042, 1947964588037287937, 1947135858008137730,
        '随机积分', '2025-07-23 18:18:44', 'complete', '2025-07-23 18:18:44', '2025-07-23 18:18:44'),
       (1947964609273049090, 'yong', 1947194890802733058, 1947130468034007042, 1947964605674336258, 1947135858008137730,
        '随机积分', '2025-07-23 18:18:49', 'complete', '2025-07-23 18:18:48', '2025-07-23 18:18:48'),
       (1947964705326804993, 'yong', 1947194890802733058, 1947130468034007042, 1947964702692782081, 1947136366194204674,
        '小玩偶', '2025-07-23 18:19:12', 'create', '2025-07-23 18:19:11', '2025-07-23 18:19:11'),
       (1947964719885234178, 'yong', 1947194890802733058, 1947130468034007042, 1947964717880356865, 1947136149856198658,
        '支付立减券', '2025-07-23 18:19:15', 'create', '2025-07-23 18:19:15', '2025-07-23 18:19:15'),
       (1947964734384943106, 'yong', 1947194890802733058, 1947130468034007042, 1947964732560420865, 1947135858008137730,
        '随机积分', '2025-07-23 18:19:19', 'complete', '2025-07-23 18:19:18', '2025-07-23 18:19:18'),
       (1947964940291715074, 'yong', 1947194890802733058, 1947130468034007042, 1947964938517524482, 0, '谢谢参与',
        '2025-07-23 18:20:08', 'create', '2025-07-23 18:20:08', '2025-07-23 18:20:08'),
       (1947968298519916546, 'yong', 1947194890802733058, 1947130468034007042, 1947968296536010754, 0, '谢谢参与',
        '2025-07-23 18:33:29', 'create', '2025-07-23 18:33:29', '2025-07-23 18:33:29'),
       (1947968316618338305, 'yong', 1947194890802733058, 1947130468034007042, 1947968313065762817, 1947136031354527745,
        '会员卡', '2025-07-23 18:33:33', 'create', '2025-07-23 18:33:34', '2025-07-23 18:33:34'),
       (1947968638329815041, 'yong', 1947194890802733058, 1947130468034007042, 1947968633770606594, 1947136456283660289,
        '小台灯', '2025-07-23 18:34:49', 'create', '2025-07-23 18:34:49', '2025-07-23 18:34:49'),
       (1947968663462084610, 'yong', 1947194890802733058, 1947130468034007042, 1947968661520121857, 0, '谢谢参与',
        '2025-07-23 18:34:56', 'create', '2025-07-23 18:34:55', '2025-07-23 18:34:55'),
       (1947968717765738498, 'yong', 1947194890802733058, 1947130468034007042, 1947968715341430786, 1947136031354527745,
        '会员卡', '2025-07-23 18:35:08', 'create', '2025-07-23 18:35:10', '2025-07-23 18:35:10'),
       (1947968732567437313, 'yong', 1947194890802733058, 1947130468034007042, 1947968730759692289, 0, '谢谢参与',
        '2025-07-23 18:35:12', 'create', '2025-07-23 18:35:13', '2025-07-23 18:35:13'),
       (1947968746333143042, 'yong', 1947194890802733058, 1947130468034007042, 1947968744609284098, 0, '谢谢参与',
        '2025-07-23 18:35:15', 'create', '2025-07-23 18:35:16', '2025-07-23 18:35:16'),
       (1947968760413421569, 'yong', 1947194890802733058, 1947130468034007042, 1947968758081388546, 1947136149856198658,
        '支付立减券', '2025-07-23 18:35:19', 'create', '2025-07-23 18:35:19', '2025-07-23 18:35:19'),
       (1947968773696782337, 'yong', 1947194890802733058, 1947130468034007042, 1947968772140695553, 0, '谢谢参与',
        '2025-07-23 18:35:22', 'create', '2025-07-23 18:35:22', '2025-07-23 18:35:22'),
       (1947968786980143106, 'yong', 1947194890802733058, 1947130468034007042, 1947968785361141762, 0, '谢谢参与',
        '2025-07-23 18:35:25', 'create', '2025-07-23 18:35:25', '2025-07-23 18:35:25'),
       (1947968800020234242, 'yong', 1947194890802733058, 1947130468034007042, 1947968798090854402, 1947136149856198658,
        '支付立减券', '2025-07-23 18:35:28', 'create', '2025-07-23 18:35:27', '2025-07-23 18:35:27'),
       (1947968813685276673, 'yong', 1947194890802733058, 1947130468034007042, 1947968811911086082, 0, '谢谢参与',
        '2025-07-23 18:35:31', 'create', '2025-07-23 18:35:30', '2025-07-23 18:35:30'),
       (1947968828176596994, 'yong', 1947194890802733058, 1947130468034007042, 1947968825680986114, 1947136515402375169,
        '游戏机', '2025-07-23 18:35:35', 'create', '2025-07-23 18:35:34', '2025-07-23 18:35:34'),
       (1947968840130363394, 'yong', 1947194890802733058, 1947130468034007042, 1947968838054182913, 1947136456283660289,
        '小台灯', '2025-07-23 18:35:38', 'create', '2025-07-23 18:35:39', '2025-07-23 18:35:39'),
       (1947968852834910209, 'yong', 1947194890802733058, 1947130468034007042, 1947968850448351234, 1947136149856198658,
        '支付立减券', '2025-07-23 18:35:41', 'create', '2025-07-23 18:35:42', '2025-07-23 18:35:42'),
       (1947968866533507074, 'yong', 1947194890802733058, 1947130468034007042, 1947968864352468994, 1947136149856198658,
        '支付立减券', '2025-07-23 18:35:44', 'create', '2025-07-23 18:35:45', '2025-07-23 18:35:45'),
       (1947968878244003841, 'yong', 1947194890802733058, 1947130468034007042, 1947968876700499970, 0, '谢谢参与',
        '2025-07-23 18:35:47', 'create', '2025-07-23 18:35:47', '2025-07-23 18:35:47'),
       (1947968892332670977, 'yong', 1947194890802733058, 1947130468034007042, 1947968890273267714, 1947136456283660289,
        '小台灯', '2025-07-23 18:35:50', 'create', '2025-07-23 18:35:50', '2025-07-23 18:35:50'),
       (1947968907033706497, 'yong', 1947194890802733058, 1947130468034007042, 1947968903254638594, 1947136149856198658,
        '支付立减券', '2025-07-23 18:35:54', 'create', '2025-07-23 18:35:54', '2025-07-23 18:35:54'),
       (1947968918207332353, 'yong', 1947194890802733058, 1947130468034007042, 1947968916596719617, 0, '谢谢参与',
        '2025-07-23 18:35:56', 'create', '2025-07-23 18:35:56', '2025-07-23 18:35:56'),
       (1947968931876569090, 'yong', 1947194890802733058, 1947130468034007042, 1947968929942994946, 1947136149856198658,
        '支付立减券', '2025-07-23 18:36:00', 'create', '2025-07-23 18:35:59', '2025-07-23 18:35:59'),
       (1947968945508057090, 'yong', 1947194890802733058, 1947130468034007042, 1947968943415099393, 1947135858008137730,
        '随机积分', '2025-07-23 18:36:03', 'complete', '2025-07-23 18:36:02', '2025-07-23 18:36:02'),
       (1947968961186365442, 'yong', 1947194890802733058, 1947130468034007042, 1947968957877059585, 1947136366194204674,
        '小玩偶', '2025-07-23 18:36:07', 'create', '2025-07-23 18:36:05', '2025-07-23 18:36:05'),
       (1947968975845457922, 'yong', 1947194890802733058, 1947130468034007042, 1947968973907689474, 1947135858008137730,
        '随机积分', '2025-07-23 18:36:10', 'complete', '2025-07-23 18:36:11', '2025-07-23 18:36:11'),
       (1947968990231920642, 'yong', 1947194890802733058, 1947130468034007042, 1947968988122185730, 1947135858008137730,
        '随机积分', '2025-07-23 18:36:13', 'complete', '2025-07-23 18:36:14', '2025-07-23 18:36:14'),
       (1947969004651937794, 'yong', 1947194890802733058, 1947130468034007042, 1947969002718363649, 1947136149856198658,
        '支付立减券', '2025-07-23 18:36:17', 'create', '2025-07-23 18:36:17', '2025-07-23 18:36:17'),
       (1947969018597998593, 'yong', 1947194890802733058, 1947130468034007042, 1947969016534401026, 1947135858008137730,
        '随机积分', '2025-07-23 18:36:20', 'complete', '2025-07-23 18:36:20', '2025-07-23 18:36:21'),
       (1947993440688050178, 'yong', 1947194890802733058, 1947130468034007042, 1947993438007889922, 1947136031354527745,
        '会员卡', '2025-07-23 20:13:23', 'create', '2025-07-23 20:13:24', '2025-07-23 20:13:24'),
       (1947993454868992002, 'yong', 1947194890802733058, 1947130468034007042, 1947993452734091266, 1947135858008137730,
        '随机积分', '2025-07-23 20:13:26', 'complete', '2025-07-23 20:13:27', '2025-07-23 20:13:27'),
       (1947993472870944769, 'yong', 1947194890802733058, 1947130468034007042, 1947993470996090882, 1947135858008137730,
        '随机积分', '2025-07-23 20:13:31', 'complete', '2025-07-23 20:13:31', '2025-07-23 20:13:31'),
       (1947994309542318082, 'yong', 1947194890802733058, 1947130468034007042, 1947994307260616706, 1947136031354527745,
        '会员卡', '2025-07-23 20:16:50', 'create', '2025-07-23 20:16:48', '2025-07-23 20:16:48'),
       (1948271548288741377, 'yong', 1948000932272812034, 1947996138992865281, 1948271545558249474, 1947136290151473154,
        '舒适办公椅', '2025-07-24 14:38:29', 'create', '2025-07-24 14:38:28', '2025-07-24 14:38:28'),
       (1948271569830686721, 'yong', 1948000932272812034, 1947996138992865281, 1948271567548985346, 1947135858008137730,
        '随机积分', '2025-07-24 14:38:34', 'complete', '2025-07-24 14:38:34', '2025-07-24 14:38:34'),
       (1948271582401015810, 'yong', 1948000932272812034, 1947996138992865281, 1948271579934765058, 1947136031354527745,
        '会员卡', '2025-07-24 14:38:37', 'create', '2025-07-24 14:38:37', '2025-07-24 14:38:37'),
       (1948271597651505153, 'yong', 1948000932272812034, 1947996138992865281, 1948271594182815745, 1947136456283660289,
        '特色小台灯', '2025-07-24 14:38:41', 'create', '2025-07-24 14:38:40', '2025-07-24 14:38:40'),
       (1948271609269727234, 'yong', 1948000932272812034, 1947996138992865281, 1948271607319375874, 1947136290151473154,
        '舒适办公椅', '2025-07-24 14:38:43', 'create', '2025-07-24 14:38:43', '2025-07-24 14:38:43'),
       (1948271623446474753, 'yong', 1948000932272812034, 1947996138992865281, 1948271620854394882, 1947136366194204674,
        '小玩偶', '2025-07-24 14:38:47', 'create', '2025-07-24 14:38:46', '2025-07-24 14:38:46'),
       (1948271635945500674, 'yong', 1948000932272812034, 1947996138992865281, 1948271634116784130, 1947135858008137730,
        '随机积分', '2025-07-24 14:38:50', 'complete', '2025-07-24 14:38:49', '2025-07-24 14:38:49'),
       (1948271649354690561, 'yong', 1948000932272812034, 1947996138992865281, 1948271647467253762, 1947136031354527745,
        '会员卡', '2025-07-24 14:38:53', 'create', '2025-07-24 14:38:53', '2025-07-24 14:38:53'),
       (1948273525555060737, 'yong', 1948000932272812034, 1947996138992865281, 1948273516164014081, 1947135858008137730,
        '随机积分', '2025-07-24 14:46:20', 'complete', '2025-07-24 14:46:20', '2025-07-24 14:46:20'),
       (1948276573627080706, 'yong', 1948000932272812034, 1947996138992865281, 1948276568531001346, 1947136290151473154,
        '舒适办公椅', '2025-07-24 14:58:27', 'create', '2025-07-24 14:58:27', '2025-07-24 14:58:27'),
       (1948280499701145601, 'yong', 100301, 200001, 1948280493254500353, 0, '谢谢参与', '2025-07-24 15:14:03',
        'create', '2025-07-24 15:14:03', '2025-07-24 15:14:03'),
       (1948285127385714689, 'yong', 100301, 200001, 1948285123262713857, 0, '谢谢参与', '2025-07-24 15:32:26',
        'create', '2025-07-24 15:32:26', '2025-07-24 15:32:26'),
       (1948285241315594242, 'yong', 100301, 200001, 1948285238895480833, 0, '谢谢参与', '2025-07-24 15:32:54',
        'create', '2025-07-24 15:32:53', '2025-07-24 15:32:53'),
       (1948285260542283778, 'yong', 100301, 200001, 1948285257736294402, 106, '轻奢办公椅', '2025-07-24 15:32:58',
        'create', '2025-07-24 15:32:58', '2025-07-24 15:32:58'),
       (1948285700495413249, 'yong', 100301, 200001, 1948285697878167553, 105, '小米su7周体验', '2025-07-24 15:34:43',
        'create', '2025-07-24 15:34:43', '2025-07-24 15:34:43'),
       (1948285766757027841, 'yong', 100301, 200001, 1948285764555018241, 0, '谢谢参与', '2025-07-24 15:34:59',
        'create', '2025-07-24 15:34:58', '2025-07-24 15:34:58'),
       (1948286211634270210, 'yong', 100301, 200001, 1948286203413434370, 0, '谢谢参与', '2025-07-24 15:36:45',
        'create', '2025-07-24 15:36:44', '2025-07-24 15:36:44');
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
    `id`              bigint unsigned NOT NULL COMMENT '雪花ID',
    `activity_id`     bigint                   DEFAULT NULL COMMENT '活动ID',
    `user_id`         varchar(32)     NOT NULL COMMENT '用户ID',
    `behavior_type`   varchar(16)     NOT NULL COMMENT '行为类型（sign 签到、openai_pay 支付）',
    `rebate_desc`     varchar(128)    NOT NULL COMMENT '返利描述',
    `rebate_type`     varchar(16)     NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
    `rebate_config`   varchar(32)     NOT NULL COMMENT '返利配置【sku值，积分值】',
    `out_business_no` varchar(100)    NOT NULL COMMENT '业务仿重ID - 外部透传，方便查询使用',
    `biz_id`          varchar(64)     NOT NULL COMMENT '业务ID - 拼接的唯一值',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_biz_id` (`biz_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='返利订单表';
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
    `id`              bigint unsigned NOT NULL COMMENT '雪花ID',
    `activity_id`     bigint          NOT NULL COMMENT '活动ID',
    `user_id`         varchar(32)     NOT NULL COMMENT '用户ID',
    `behavior_type`   varchar(16)     NOT NULL COMMENT '行为类型（sign 签到、openai_pay 支付）',
    `rebate_desc`     varchar(128)    NOT NULL COMMENT '返利描述',
    `rebate_type`     varchar(16)     NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
    `rebate_config`   varchar(32)     NOT NULL COMMENT '返利配置【sku值，积分值】',
    `out_business_no` varchar(100)    NOT NULL COMMENT '业务仿重ID - 外部透传，方便查询使用',
    `biz_id`          varchar(64)     NOT NULL COMMENT '业务ID - 拼接的唯一值',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_biz_id` (`biz_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='返利订单表';
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
    `id`              bigint unsigned NOT NULL COMMENT '雪花ID',
    `activity_id`     bigint          NOT NULL COMMENT '活动ID',
    `user_id`         varchar(32)     NOT NULL COMMENT '用户ID',
    `behavior_type`   varchar(16)     NOT NULL COMMENT '行为类型（sign 签到、openai_pay 支付）',
    `rebate_desc`     varchar(128)    NOT NULL COMMENT '返利描述',
    `rebate_type`     varchar(16)     NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
    `rebate_config`   varchar(32)     NOT NULL COMMENT '返利配置【sku值，积分值】',
    `out_business_no` varchar(100)    NOT NULL COMMENT '业务仿重ID - 外部透传，方便查询使用',
    `biz_id`          varchar(64)     NOT NULL COMMENT '业务ID - 拼接的唯一值',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_biz_id` (`biz_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='返利订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_behavior_rebate_order_002`
--

LOCK TABLES `user_behavior_rebate_order_002` WRITE;
/*!40000 ALTER TABLE `user_behavior_rebate_order_002`
    DISABLE KEYS */;
INSERT INTO `user_behavior_rebate_order_002`
VALUES (1948240417464025090, 1947194890802733058, 'yong2', 'activity_gift', '活动赠送5次抽奖', 'gift', '5', '20250724',
        'yong2_1947194890802733058_gift_5_20250724', '2025-07-24 12:34:46', '2025-07-24 12:34:46');
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
    `id`              bigint unsigned NOT NULL COMMENT '雪花ID',
    `user_id`         varchar(32)     NOT NULL COMMENT '用户ID',
    `activity_id`     bigint          NOT NULL COMMENT '活动ID',
    `behavior_type`   varchar(16)     NOT NULL COMMENT '行为类型（sign 签到、openai_pay 支付）',
    `rebate_desc`     varchar(128)    NOT NULL COMMENT '返利描述',
    `rebate_type`     varchar(16)     NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
    `rebate_config`   varchar(32)     NOT NULL COMMENT '返利配置【sku值，积分值】',
    `out_business_no` varchar(100)    NOT NULL COMMENT '业务仿重ID - 外部透传，方便查询使用',
    `biz_id`          varchar(64)     NOT NULL COMMENT '业务ID - 拼接的唯一值',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_biz_id` (`biz_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='返利订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_behavior_rebate_order_003`
--

LOCK TABLES `user_behavior_rebate_order_003` WRITE;
/*!40000 ALTER TABLE `user_behavior_rebate_order_003`
    DISABLE KEYS */;
INSERT INTO `user_behavior_rebate_order_003`
VALUES (1946786048197554178, 'yong', 100301, 'sign', '签到返利-抽奖额度', 'sku', '9011', '20250720',
        'yong_100301_sku_9011_20250720', '2025-07-20 12:15:38', '2025-07-20 12:15:38'),
       (1946786048247885825, 'yong', 100301, 'sign', '签到返利-积分', 'integral', '10', '20250720',
        'yong_100301_integral_10_20250720', '2025-07-20 12:15:38', '2025-07-20 12:15:38'),
       (1946786932965011457, 'yong', 1934223919414280193, 'sign', '签到返积分', 'integral', '50', '20250720',
        'yong_1934223919414280193_integral_50_20250720', '2025-07-20 12:19:09', '2025-07-20 12:19:09'),
       (1946786933032120321, 'yong', 1934223919414280193, 'sign', '签到返抽奖次数', 'sku', '1934859819538202625',
        '20250720', 'yong_1934223919414280193_sku_1934859819538202625_20250720', '2025-07-20 12:19:09',
        '2025-07-20 12:19:09'),
       (1947125847412559873, 'yong', 100301, 'sign', '签到返利-抽奖额度', 'sku', '9011', '20250721',
        'yong_100301_sku_9011_20250721', '2025-07-21 10:45:52', '2025-07-21 10:45:52'),
       (1947125847437725698, 'yong', 100301, 'sign', '签到返利-积分', 'integral', '10', '20250721',
        'yong_100301_integral_10_20250721', '2025-07-21 10:45:52', '2025-07-21 10:45:52'),
       (1947312483001712642, 'yong', 1947194890802733058, 'sign', '每日签到返抽奖次数', 'sku', '1947277048632332290',
        '20250721', 'yong_1947194890802733058_sku_1947277048632332290_20250721', '2025-07-21 23:07:30',
        '2025-07-21 23:07:30'),
       (1947312483056238593, 'yong', 1947194890802733058, 'sign', '每日签到返积分', 'integral', '10', '20250721',
        'yong_1947194890802733058_integral_10_20250721', '2025-07-21 23:07:30', '2025-07-21 23:07:30'),
       (1947496100768313345, 'yong', 1947194890802733058, 'sign', '每日签到返抽奖次数', 'sku', '1947277048632332290',
        '20250722', 'yong_1947194890802733058_sku_1947277048632332290_20250722', '2025-07-22 11:17:07',
        '2025-07-22 11:17:07'),
       (1947496100831227906, 'yong', 1947194890802733058, 'sign', '每日签到返积分', 'integral', '10', '20250722',
        'yong_1947194890802733058_integral_10_20250722', '2025-07-22 11:17:07', '2025-07-22 11:17:07'),
       (1947881574158680066, 'yong', 1947194890802733058, 'sign', '每日签到返抽奖次数', 'sku', '1947277048632332290',
        '20250723', 'yong_1947194890802733058_sku_1947277048632332290_20250723', '2025-07-23 12:48:50',
        '2025-07-23 12:48:50'),
       (1947881574158680067, 'yong', 1947194890802733058, 'sign', '每日签到返积分', 'integral', '10', '20250723',
        'yong_1947194890802733058_integral_10_20250723', '2025-07-23 12:48:50', '2025-07-23 12:48:50'),
       (1947944029089996801, 'yong', 100301, 'sign', '签到返利-抽奖额度', 'sku', '9011', '20250723',
        'yong_100301_sku_9011_20250723', '2025-07-23 16:57:02', '2025-07-23 16:57:02'),
       (1947944029089996802, 'yong', 100301, 'sign', '签到返利-积分', 'integral', '10', '20250723',
        'yong_100301_integral_10_20250723', '2025-07-23 16:57:02', '2025-07-23 16:57:02'),
       (1947944100623851521, 'yong', 1934223919414280193, 'sign', '签到返积分', 'integral', '50', '20250723',
        'yong_1934223919414280193_integral_50_20250723', '2025-07-23 16:57:17', '2025-07-23 16:57:17'),
       (1947944100686766081, 'yong', 1934223919414280193, 'sign', '签到返抽奖次数', 'sku', '1934859819538202625',
        '20250723', 'yong_1934223919414280193_sku_1934859819538202625_20250723', '2025-07-23 16:57:17',
        '2025-07-23 16:57:17'),
       (1948214820247457794, 'yong', 1947194890802733058, 'sign', '每日签到返抽奖次数', 'sku', '1947277048632332290',
        '20250724', 'yong_1947194890802733058_sku_1947277048632332290_20250724', '2025-07-24 10:53:03',
        '2025-07-24 10:53:03'),
       (1948214820310372354, 'yong', 1947194890802733058, 'sign', '每日签到返积分', 'integral', '10', '20250724',
        'yong_1947194890802733058_integral_10_20250724', '2025-07-24 10:53:03', '2025-07-24 10:53:03'),
       (1948272463884333058, 'yong', 1934223919414280193, 'sign', '签到返积分', 'integral', '50', '20250724',
        'yong_1934223919414280193_integral_4_20250724', '2025-07-24 14:42:07', '2025-07-24 14:42:07'),
       (1948272463884333059, 'yong', 1934223919414280193, 'sign', '签到返抽奖次数', 'sku', '1934859819538202625',
        '20250724', 'yong_1934223919414280193_sku_1934927347346096129_20250724', '2025-07-24 14:42:07',
        '2025-07-24 14:42:07'),
       (1948272497845612545, 'yong', 1934223919414280193, 'activity_gift', '赠送你1次抽奖次数', 'gift', '1', '2025',
        'yong_1934223919414280193_gift_1948272437883842561_2025', '2025-07-24 14:42:15', '2025-07-24 14:42:15'),
       (1948274523879104513, 'yong', 1947194890802733058, 'activity_gift', '第二次赠送你3次抽奖次数', 'gift', '3',
        '2025', 'yong_1947194890802733058_gift_1948274480673579009_2025', '2025-07-24 14:50:18', '2025-07-24 14:50:18'),
       (1948288415250288642, 'yong', 100301, 'sign', '签到返利-抽奖额度', 'sku', '9011', '20250724',
        'yong_100301_sku_1_20250724', '2025-07-24 15:45:30', '2025-07-24 15:45:30'),
       (1948288415443226625, 'yong', 100301, 'sign', '签到返利-积分', 'integral', '10', '20250724',
        'yong_100301_integral_2_20250724', '2025-07-24 15:45:30', '2025-07-24 15:45:30'),
       (1948288810756378625, 'yong', 100301, 'activity_gift', '测试赠送', 'gift', '1', '2025',
        'yong_100301_gift_1948288777088700417_2025', '2025-07-24 15:47:04', '2025-07-24 15:47:04');
/*!40000 ALTER TABLE `user_behavior_rebate_order_003`
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
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='抽奖订单表';
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
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='抽奖订单表';
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
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='抽奖订单表';
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
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='抽奖订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_order_003`
--

LOCK TABLES `user_order_003` WRITE;
/*!40000 ALTER TABLE `user_order_003`
    DISABLE KEYS */;
INSERT INTO `user_order_003`
VALUES (1946786067894005762, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:15:42', '2025-07-20 12:15:43'),
       (1946786554139668483, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:17:39', '2025-07-20 12:17:39'),
       (1946786570115772417, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:17:42', '2025-07-20 12:17:43'),
       (1946786586586804225, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:17:46', '2025-07-20 12:17:47'),
       (1946786602911035394, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:17:50', '2025-07-20 12:17:51'),
       (1946786617272332290, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:17:53', '2025-07-20 12:17:54'),
       (1946786633693032449, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:17:57', '2025-07-20 12:17:57'),
       (1946786647102222338, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:18:00', '2025-07-20 12:18:02'),
       (1946786663510339585, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:18:05', '2025-07-20 12:18:05'),
       (1946786679935234050, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:18:09', '2025-07-20 12:18:10'),
       (1946786698901876738, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:18:13', '2025-07-20 12:18:14'),
       (1946786715742007298, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:18:17', '2025-07-20 12:18:17'),
       (1946786732988985346, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:18:21', '2025-07-20 12:18:22'),
       (1946786749581651969, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:18:25', '2025-07-20 12:18:25'),
       (1946786968167804932, 'yong', 1934223919414280193, '活动v1', 200001, 'used', '2025-07-20 12:19:17',
        '2025-07-20 12:19:18'),
       (1946786984798220290, 'yong', 1934223919414280193, '活动v1', 200001, 'used', '2025-07-20 12:19:21',
        '2025-07-20 12:19:21'),
       (1946787000430391298, 'yong', 1934223919414280193, '活动v1', 200001, 'used', '2025-07-20 12:19:24',
        '2025-07-20 12:19:25'),
       (1946787016360357889, 'yong', 1934223919414280193, '活动v1', 200001, 'used', '2025-07-20 12:19:28',
        '2025-07-20 12:19:29'),
       (1946787427913854977, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:21:06', '2025-07-20 12:21:07'),
       (1946787461594116097, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:21:15', '2025-07-20 12:21:15'),
       (1946787491520475138, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:21:22', '2025-07-20 12:21:22'),
       (1946787509597925378, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:21:26', '2025-07-20 12:21:26'),
       (1946787558696448002, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:21:37', '2025-07-20 12:21:38'),
       (1946787574940987393, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:21:42', '2025-07-20 12:21:43'),
       (1946787592859054082, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:21:46', '2025-07-20 12:21:47'),
       (1946787609569161217, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:21:50', '2025-07-20 12:21:51'),
       (1946787624832233473, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:21:54', '2025-07-20 12:21:54'),
       (1946787639327748097, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:21:57', '2025-07-20 12:21:57'),
       (1946787769741242370, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:22:28', '2025-07-20 12:22:29'),
       (1946787784018653186, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-20 12:22:31', '2025-07-20 12:22:32'),
       (1947289921769209859, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-21 21:37:51', '2025-07-21 21:37:51'),
       (1947289940245118978, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-21 21:37:55', '2025-07-21 21:37:56'),
       (1947290022377979907, 'yong', 1934223919414280193, '活动v1', 200001, 'used', '2025-07-21 21:38:15',
        '2025-07-21 21:38:16'),
       (1947290258336940034, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-21 21:39:10', '2025-07-21 21:39:11'),
       (1947290290679218178, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-21 21:39:19', '2025-07-21 21:39:20'),
       (1947308317156163586, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-21 22:50:56', '2025-07-22 14:53:52'),
       (1947312520406515715, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:07:39', '2025-07-21 23:22:16'),
       (1947317424999108611, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:27:08', '2025-07-21 23:27:08'),
       (1947317462156447745, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:27:16', '2025-07-21 23:27:17'),
       (1947317482461073410, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:27:21', '2025-07-21 23:27:21'),
       (1947317499968098305, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:27:26', '2025-07-21 23:27:27'),
       (1947317517475123201, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:27:30', '2025-07-21 23:27:31'),
       (1947317579601154050, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:27:44', '2025-07-21 23:27:45'),
       (1947318595235090433, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:31:47', '2025-07-21 23:31:48'),
       (1947318854350802946, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:32:49', '2025-07-21 23:32:49'),
       (1947318879910891522, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:32:55', '2025-07-21 23:32:55'),
       (1947318902757265409, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:33:00', '2025-07-21 23:33:00'),
       (1947318966074478593, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:33:16', '2025-07-21 23:33:16'),
       (1947320806740615170, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:40:34', '2025-07-21 23:40:35'),
       (1947320833143758850, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:40:40', '2025-07-21 23:40:41'),
       (1947320851326066690, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:40:45', '2025-07-21 23:40:46'),
       (1947320930413862914, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:41:04', '2025-07-21 23:41:04'),
       (1947320961447518210, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:41:12', '2025-07-21 23:41:12'),
       (1947321370383769601, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:42:49', '2025-07-21 23:43:28'),
       (1947321567407005697, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:43:35', '2025-07-21 23:43:38'),
       (1947321608947392514, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:43:46', '2025-07-21 23:44:34'),
       (1947322875627200513, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:48:47', '2025-07-21 23:50:25'),
       (1947323305497292801, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:50:30', '2025-07-21 23:50:30'),
       (1947323326326206465, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:50:35', '2025-07-21 23:50:36'),
       (1947323347712962562, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:50:40', '2025-07-21 23:50:41'),
       (1947323368608985090, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:50:45', '2025-07-21 23:50:46'),
       (1947323398795390977, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:50:52', '2025-07-21 23:50:52'),
       (1947323414981210113, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:50:56', '2025-07-21 23:50:56'),
       (1947323429430587394, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:50:59', '2025-07-21 23:50:59'),
       (1947323443821244418, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:51:02', '2025-07-21 23:51:04'),
       (1947323578550677506, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:51:36', '2025-07-21 23:51:36'),
       (1947323670531764226, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:51:57', '2025-07-21 23:51:57'),
       (1947323704996360194, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:52:04', '2025-07-21 23:52:05'),
       (1947323720704028674, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:52:10', '2025-07-21 23:52:10'),
       (1947323735845466114, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:52:13', '2025-07-21 23:52:57'),
       (1947324441985904641, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-21 23:55:01', '2025-07-22 00:01:45'),
       (1947326181841854465, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 00:01:55', '2025-07-22 00:01:56'),
       (1947326916365205506, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 00:04:51', '2025-07-22 00:04:52'),
       (1947326932395835394, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 00:04:55', '2025-07-22 00:04:55'),
       (1947326947499524098, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 00:04:58', '2025-07-22 00:04:59'),
       (1947496258318954498, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 11:17:45', '2025-07-22 11:17:46'),
       (1947496277939908610, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 11:17:50', '2025-07-22 11:17:50'),
       (1947496294318665729, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 11:17:53', '2025-07-22 11:18:01'),
       (1947496337025069057, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 11:18:04', '2025-07-22 11:18:04'),
       (1947497390734897153, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 11:22:15', '2025-07-22 11:22:15'),
       (1947497500239785985, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 11:22:41', '2025-07-22 12:05:26'),
       (1947508345095090177, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 12:05:47', '2025-07-22 12:05:47'),
       (1947508387893768194, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 12:05:57', '2025-07-22 12:05:57'),
       (1947508450414063617, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 12:06:12', '2025-07-22 12:06:12'),
       (1947510713018859522, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 12:15:11', '2025-07-22 12:15:11'),
       (1947510729833824258, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 12:15:15', '2025-07-22 12:15:15'),
       (1947510744484528130, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 12:15:18', '2025-07-22 12:15:18'),
       (1947510757629476865, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 12:15:21', '2025-07-22 12:15:22'),
       (1947510773559443458, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 12:15:25', '2025-07-22 12:15:25'),
       (1947510790512820226, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 12:15:29', '2025-07-22 12:15:30'),
       (1947511238078611458, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 12:17:16', '2025-07-22 12:17:17'),
       (1947511329271169025, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 12:17:38', '2025-07-22 12:17:39'),
       (1947511349458354177, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 12:17:43', '2025-07-22 12:17:43'),
       (1947512554129571841, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 12:22:30', '2025-07-22 12:22:31'),
       (1947512622681276417, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 12:22:46', '2025-07-22 12:33:52'),
       (1947515521058586626, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 12:34:18', '2025-07-22 13:02:40'),
       (1947522683449217026, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 13:02:45', '2025-07-22 13:02:50'),
       (1947522744891576321, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 13:03:00', '2025-07-22 13:03:01'),
       (1947522763115827202, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 13:03:04', '2025-07-22 13:03:05'),
       (1947522783223320577, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 13:03:09', '2025-07-22 13:03:10'),
       (1947523554597711874, 'yong', 1934223919414280193, '活动v1', 200001, 'used', '2025-07-22 13:06:13',
        '2025-07-22 13:06:13'),
       (1947523572071182337, 'yong', 1934223919414280193, '活动v1', 200001, 'used', '2025-07-22 13:06:17',
        '2025-07-22 13:06:25'),
       (1947523622436384769, 'yong', 1934223919414280193, '活动v1', 200001, 'used', '2025-07-22 13:06:29',
        '2025-07-22 13:06:30'),
       (1947523899084288001, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 13:07:35', '2025-07-22 13:07:36'),
       (1947524007288942594, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 13:08:01', '2025-07-22 13:08:01'),
       (1947524089421803521, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 13:08:20', '2025-07-22 13:08:21'),
       (1947524178835976193, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 13:08:42', '2025-07-22 13:08:42'),
       (1947547744134578177, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 14:42:20', '2025-07-22 14:51:58'),
       (1947550884007329794, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 14:54:49', '2025-07-22 14:54:49'),
       (1947552033875771394, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 14:59:23', '2025-07-22 14:59:23'),
       (1947552151429529602, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 14:59:51', '2025-07-22 14:59:51'),
       (1947552255511183361, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 15:00:16', '2025-07-22 15:00:16'),
       (1947552403574308866, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 15:00:51', '2025-07-22 15:00:51'),
       (1947552417558118401, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 15:00:54', '2025-07-22 15:00:55'),
       (1947552431122497537, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 15:00:57', '2025-07-22 15:00:58'),
       (1947552445160833026, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-22 15:01:01', '2025-07-22 15:01:01'),
       (1947881506542305281, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 12:48:35', '2025-07-23 15:32:31'),
       (1947923044592902145, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 15:33:40', '2025-07-23 15:33:41'),
       (1947924198366134274, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 15:38:14', '2025-07-23 15:38:14'),
       (1947927393154891778, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 15:50:56', '2025-07-23 15:50:57'),
       (1947928174838939649, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 15:54:01', '2025-07-23 15:54:02'),
       (1947928582604980225, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 15:55:39', '2025-07-23 15:55:40'),
       (1947928650678534145, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 15:55:55', '2025-07-23 15:55:56'),
       (1947932401845927938, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 16:10:50', '2025-07-23 16:11:05'),
       (1947932478236786689, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 16:11:08', '2025-07-23 16:11:08'),
       (1947938586200461314, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 16:35:24', '2025-07-23 16:35:25'),
       (1947940301863682049, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 16:42:14', '2025-07-23 16:42:14'),
       (1947940319672696834, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 16:42:18', '2025-07-23 16:42:18'),
       (1947940470365650946, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 16:42:53', '2025-07-23 16:42:54'),
       (1947941284995956738, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-23 16:46:07', '2025-07-23 16:56:39'),
       (1947941621358166018, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 16:47:29', '2025-07-23 18:05:34'),
       (1947943959477133314, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-23 16:56:44', '2025-07-23 16:56:44'),
       (1947943978179534850, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-23 16:56:48', '2025-07-23 16:56:48'),
       (1947944047372967938, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-23 16:57:06', '2025-07-23 16:57:06'),
       (1947944063491678210, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-23 16:57:09', '2025-07-23 16:57:10'),
       (1947944201605914626, 'yong', 1934223919414280193, '活动v1', 200001, 'used', '2025-07-23 16:57:42',
        '2025-07-23 16:57:43'),
       (1947944224003497986, 'yong', 1934223919414280193, '活动v1', 200001, 'used', '2025-07-23 16:57:47',
        '2025-07-23 16:57:48'),
       (1947961457559781378, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:06:17', '2025-07-23 18:09:59'),
       (1947962402846851073, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:10:02', '2025-07-23 18:10:07'),
       (1947962437361778689, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:10:12', '2025-07-23 18:10:18'),
       (1947962522011222017, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:10:31', '2025-07-23 18:17:27'),
       (1947964281504968706, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:17:30', '2025-07-23 18:17:31'),
       (1947964297980194818, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:17:34', '2025-07-23 18:17:35'),
       (1947964317013946370, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:17:38', '2025-07-23 18:17:39'),
       (1947964333224931330, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:17:42', '2025-07-23 18:17:43'),
       (1947964453731479554, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:18:11', '2025-07-23 18:18:12'),
       (1947964493799665665, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:18:22', '2025-07-23 18:18:24'),
       (1947964530508214274, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:18:31', '2025-07-23 18:18:31'),
       (1947964545318301698, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:18:34', '2025-07-23 18:18:34'),
       (1947964559180476417, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:18:37', '2025-07-23 18:18:37'),
       (1947964573227200513, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:18:40', '2025-07-23 18:18:40'),
       (1947964588037287937, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:18:43', '2025-07-23 18:18:44'),
       (1947964605674336258, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:18:47', '2025-07-23 18:18:48'),
       (1947964702692782081, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:19:11', '2025-07-23 18:19:11'),
       (1947964717880356865, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:19:14', '2025-07-23 18:19:15'),
       (1947964732560420865, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:19:17', '2025-07-23 18:19:18'),
       (1947964938517524482, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:20:08', '2025-07-23 18:20:08'),
       (1947968296536010754, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:33:29', '2025-07-23 18:33:29'),
       (1947968313065762817, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:33:33', '2025-07-23 18:33:34'),
       (1947968633770606594, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:34:48', '2025-07-23 18:34:49'),
       (1947968661520121857, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:34:55', '2025-07-23 18:34:55'),
       (1947968715341430786, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:35:09', '2025-07-23 18:35:10'),
       (1947968730759692289, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:35:12', '2025-07-23 18:35:13'),
       (1947968744609284098, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:35:15', '2025-07-23 18:35:16'),
       (1947968758081388546, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:35:18', '2025-07-23 18:35:19'),
       (1947968772140695553, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:35:21', '2025-07-23 18:35:22'),
       (1947968785361141762, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:35:24', '2025-07-23 18:35:25'),
       (1947968798090854402, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:35:27', '2025-07-23 18:35:27'),
       (1947968811911086082, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:35:30', '2025-07-23 18:35:30'),
       (1947968825680986114, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:35:33', '2025-07-23 18:35:34'),
       (1947968838054182913, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:35:38', '2025-07-23 18:35:39'),
       (1947968850448351234, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:35:41', '2025-07-23 18:35:42'),
       (1947968864352468994, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:35:44', '2025-07-23 18:35:45'),
       (1947968876700499970, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:35:47', '2025-07-23 18:35:47'),
       (1947968890273267714, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:35:50', '2025-07-23 18:35:50'),
       (1947968903254638594, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:35:53', '2025-07-23 18:35:54'),
       (1947968916596719617, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:35:56', '2025-07-23 18:35:56'),
       (1947968929942994946, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:35:59', '2025-07-23 18:35:59'),
       (1947968943415099393, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:36:01', '2025-07-23 18:36:02'),
       (1947968957877059585, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:36:05', '2025-07-23 18:36:05'),
       (1947968973907689474, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:36:11', '2025-07-23 18:36:11'),
       (1947968988122185730, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:36:14', '2025-07-23 18:36:14'),
       (1947969002718363649, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:36:17', '2025-07-23 18:36:17'),
       (1947969016534401026, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 18:36:20', '2025-07-23 18:36:20'),
       (1947993438007889922, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 20:13:23', '2025-07-23 20:13:24'),
       (1947993452734091266, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 20:13:26', '2025-07-23 20:13:27'),
       (1947993470996090882, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 20:13:30', '2025-07-23 20:13:31'),
       (1947994307260616706, 'yong', 1947194890802733058, '一起来抽奖', 1947130468034007042, 'used',
        '2025-07-23 20:16:48', '2025-07-23 20:16:48'),
       (1948271545558249474, 'yong', 1948000932272812034, '无限制抽奖', 1947996138992865281, 'used',
        '2025-07-24 14:38:28', '2025-07-24 14:38:28'),
       (1948271567548985346, 'yong', 1948000932272812034, '无限制抽奖', 1947996138992865281, 'used',
        '2025-07-24 14:38:33', '2025-07-24 14:38:34'),
       (1948271579934765058, 'yong', 1948000932272812034, '无限制抽奖', 1947996138992865281, 'used',
        '2025-07-24 14:38:36', '2025-07-24 14:38:37'),
       (1948271594182815745, 'yong', 1948000932272812034, '无限制抽奖', 1947996138992865281, 'used',
        '2025-07-24 14:38:39', '2025-07-24 14:38:40'),
       (1948271607319375874, 'yong', 1948000932272812034, '无限制抽奖', 1947996138992865281, 'used',
        '2025-07-24 14:38:43', '2025-07-24 14:38:43'),
       (1948271620854394882, 'yong', 1948000932272812034, '无限制抽奖', 1947996138992865281, 'used',
        '2025-07-24 14:38:46', '2025-07-24 14:38:46'),
       (1948271634116784130, 'yong', 1948000932272812034, '无限制抽奖', 1947996138992865281, 'used',
        '2025-07-24 14:38:49', '2025-07-24 14:38:49'),
       (1948271647467253762, 'yong', 1948000932272812034, '无限制抽奖', 1947996138992865281, 'used',
        '2025-07-24 14:38:52', '2025-07-24 14:38:53'),
       (1948273516164014081, 'yong', 1948000932272812034, '无限制抽奖', 1947996138992865281, 'used',
        '2025-07-24 14:46:18', '2025-07-24 14:46:20'),
       (1948276568531001346, 'yong', 1948000932272812034, '无限制抽奖', 1947996138992865281, 'used',
        '2025-07-24 14:58:25', '2025-07-24 14:58:27'),
       (1948280493254500353, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-24 15:14:01', '2025-07-24 15:14:03'),
       (1948285123262713857, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-24 15:32:25', '2025-07-24 15:32:26'),
       (1948285238895480833, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-24 15:32:52', '2025-07-24 15:32:53'),
       (1948285257736294402, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-24 15:32:57', '2025-07-24 15:32:58'),
       (1948285697878167553, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-24 15:34:42', '2025-07-24 15:34:43'),
       (1948285764555018241, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-24 15:34:58', '2025-07-24 15:34:58'),
       (1948286203413434370, 'yong', 100301, '测试活动', 200001, 'used', '2025-07-24 15:36:42', '2025-07-24 15:36:44');
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

-- Dump completed on 2025-07-24 21:44:11

