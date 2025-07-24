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
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2025-07-24 21:42:40
