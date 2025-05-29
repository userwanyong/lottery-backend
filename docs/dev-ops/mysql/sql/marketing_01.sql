-- MySQL dump 10.13  Distrib 8.0.35, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: marketing_01
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activity_account`
--
CREATE DATABASE /*!32312 IF NOT EXISTS*/ `marketing_01` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;
USE `marketing_01`;

DROP TABLE IF EXISTS `activity_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_account` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `total_count` int NOT NULL COMMENT '总次数',
  `total_count_surplus` int NOT NULL COMMENT '总次数-剩余',
  `day_count` int NOT NULL COMMENT '日次数',
  `day_count_surplus` int NOT NULL COMMENT '日次数-剩余',
  `month_count` int NOT NULL COMMENT '月次数',
  `month_count_surplus` int NOT NULL COMMENT '月次数-剩余',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_user_id_activity_id` (`user_id`,`activity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='活动账户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_account`
--

LOCK TABLES `activity_account` WRITE;
/*!40000 ALTER TABLE `activity_account` DISABLE KEYS */;
INSERT INTO `activity_account` VALUES (1,'yong3',100301,7,5,7,5,7,5,'2025-05-06 23:45:46','2025-05-09 14:15:40'),(2,'yong4',100301,7,5,7,5,7,5,'2025-05-09 23:35:13','2025-05-09 23:35:30');
/*!40000 ALTER TABLE `activity_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_account_day`
--

DROP TABLE IF EXISTS `activity_account_day`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_account_day` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `day` varchar(10) NOT NULL COMMENT '日期（yyyy-mm-dd）',
  `day_count` int NOT NULL COMMENT '日次数',
  `day_count_surplus` int NOT NULL COMMENT '日次数-剩余',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_user_id_activity_id_day` (`user_id`,`activity_id`,`day`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='活动账户表-日次数';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_account_day`
--

LOCK TABLES `activity_account_day` WRITE;
/*!40000 ALTER TABLE `activity_account_day` DISABLE KEYS */;
INSERT INTO `activity_account_day` VALUES (1,'yong3',100301,'2025-05-06',6,5,'2025-05-06 23:46:06','2025-05-06 23:46:06'),(2,'yong3',100301,'2025-05-09',7,5,'2025-05-09 14:15:39','2025-05-09 14:15:39'),(3,'yong4',100301,'2025-05-09',7,5,'2025-05-09 23:34:34','2025-05-09 23:35:30');
/*!40000 ALTER TABLE `activity_account_day` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_account_month`
--

DROP TABLE IF EXISTS `activity_account_month`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_account_month` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `month` varchar(7) NOT NULL COMMENT '月（yyyy-mm）',
  `month_count` int NOT NULL COMMENT '月次数',
  `month_count_surplus` int NOT NULL COMMENT '月次数-剩余',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_user_id_activity_id_month` (`user_id`,`activity_id`,`month`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='活动账户表-月次数';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_account_month`
--

LOCK TABLES `activity_account_month` WRITE;
/*!40000 ALTER TABLE `activity_account_month` DISABLE KEYS */;
INSERT INTO `activity_account_month` VALUES (1,'yong3',100301,'2025-05',7,5,'2025-05-06 23:46:06','2025-05-09 14:15:40'),(2,'yong4',100301,'2025-05',7,5,'2025-05-09 23:34:34','2025-05-09 23:35:30');
/*!40000 ALTER TABLE `activity_account_month` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_order_000`
--

DROP TABLE IF EXISTS `activity_order_000`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_order_000` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `sku` bigint NOT NULL COMMENT '商品sku',
  `activity_name` varchar(64) NOT NULL COMMENT '活动名称',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
  `order_id` varchar(12) NOT NULL COMMENT '额度单ID',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `total_count` int NOT NULL COMMENT '总次数',
  `day_count` int NOT NULL COMMENT '日次数',
  `month_count` int NOT NULL COMMENT '月次数',
  `pay_amount` decimal(10,2) DEFAULT NULL COMMENT '支付积分',
  `state` varchar(10) NOT NULL DEFAULT 'complete' COMMENT '订单状态（complete-完成 wait_pay-等待支付）',
  `out_business_no` varchar(64) NOT NULL COMMENT '保证幂等，不会重复消费',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  UNIQUE KEY `uq_out_business_no` (`out_business_no`),
  KEY `idx_user_id_activity_id` (`user_id`,`activity_id`,`state`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='抽奖额度单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_order_000`
--

LOCK TABLES `activity_order_000` WRITE;
/*!40000 ALTER TABLE `activity_order_000` DISABLE KEYS */;
INSERT INTO `activity_order_000` VALUES (1,'yong4',100301,9011,'测试活动test',200001,'728544278045','2025-05-09 23:33:04',1,1,1,0.00,'completed','yong4_sku_20250509','2025-05-09 23:33:03','2025-05-09 23:33:03'),(2,'yong4',100301,9011,'测试活动test',200001,'274672695922','2025-05-09 23:34:58',1,1,1,1.99,'completed','746280633262','2025-05-09 23:34:58','2025-05-09 23:34:58'),(3,'yong4',100301,9012,'测试活动test',200001,'417149028235','2025-05-09 23:35:13',5,5,5,5.99,'completed','042654932328','2025-05-09 23:35:13','2025-05-09 23:35:13');
/*!40000 ALTER TABLE `activity_order_000` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_order_001`
--

DROP TABLE IF EXISTS `activity_order_001`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_order_001` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `sku` bigint NOT NULL COMMENT '商品sku',
  `activity_name` varchar(64) NOT NULL COMMENT '活动名称',
  `strategy_id` bigint NOT NULL COMMENT '额度单ID',
  `order_id` varchar(12) NOT NULL COMMENT '订单ID',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `total_count` int NOT NULL COMMENT '总次数',
  `day_count` int NOT NULL COMMENT '日次数',
  `month_count` int NOT NULL COMMENT '月次数',
  `pay_amount` decimal(10,2) DEFAULT NULL COMMENT '支付积分',
  `state` varchar(10) NOT NULL DEFAULT 'complete' COMMENT '订单状态（complete-完成 wait_pay-等待支付）',
  `out_business_no` varchar(64) NOT NULL COMMENT '保证幂等，不会重复消费',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  UNIQUE KEY `uq_out_business_no` (`out_business_no`),
  KEY `idx_user_id_activity_id` (`user_id`,`activity_id`,`state`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='抽奖额度单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_order_001`
--

LOCK TABLES `activity_order_001` WRITE;
/*!40000 ALTER TABLE `activity_order_001` DISABLE KEYS */;
INSERT INTO `activity_order_001` VALUES (1,'yong3',100301,9011,'测试活动',200001,'876102944133','2025-05-06 23:45:07',1,1,1,0.00,'completed','yong3_sku_20250506','2025-05-06 23:45:06','2025-05-06 23:45:06'),(2,'yong3',100301,9011,'测试活动',200001,'372357944323','2025-05-06 23:45:41',1,1,1,1.99,'completed','805300751919','2025-05-06 23:45:41','2025-05-06 23:45:41'),(3,'yong3',100301,9011,'测试活动',200001,'531692258340','2025-05-06 23:45:43',1,1,1,1.99,'completed','575573639351','2025-05-06 23:45:42','2025-05-06 23:45:42'),(4,'yong3',100301,9011,'测试活动',200001,'452112169304','2025-05-06 23:45:43',1,1,1,1.99,'completed','134988844337','2025-05-06 23:45:43','2025-05-06 23:45:43'),(5,'yong3',100301,9011,'测试活动',200001,'700898650093','2025-05-06 23:45:44',1,1,1,1.99,'completed','000416506136','2025-05-06 23:45:43','2025-05-06 23:45:43'),(6,'yong3',100301,9011,'测试活动',200001,'625065988129','2025-05-06 23:45:46',1,1,1,1.99,'completed','940789090831','2025-05-06 23:45:46','2025-05-06 23:45:46'),(7,'yong3',100301,9011,'测试活动',200001,'198160695801','2025-05-09 12:26:23',1,1,1,0.00,'completed','yong3_sku_20250509','2025-05-09 12:26:23','2025-05-09 12:26:23');
/*!40000 ALTER TABLE `activity_order_001` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_order_002`
--

DROP TABLE IF EXISTS `activity_order_002`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_order_002` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `sku` bigint NOT NULL COMMENT '商品sku',
  `activity_name` varchar(64) NOT NULL COMMENT '活动名称',
  `strategy_id` bigint NOT NULL COMMENT '额度单ID',
  `order_id` varchar(12) NOT NULL COMMENT '订单ID',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `total_count` int NOT NULL COMMENT '总次数',
  `day_count` int NOT NULL COMMENT '日次数',
  `month_count` int NOT NULL COMMENT '月次数',
  `pay_amount` decimal(10,2) DEFAULT NULL COMMENT '支付积分',
  `state` varchar(10) NOT NULL DEFAULT 'complete' COMMENT '订单状态（complete-完成 wait_pay-等待支付）',
  `out_business_no` varchar(64) NOT NULL COMMENT '保证幂等，不会重复消费',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  UNIQUE KEY `uq_out_business_no` (`out_business_no`),
  KEY `idx_user_id_activity_id` (`user_id`,`activity_id`,`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='抽奖额度单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_order_002`
--

LOCK TABLES `activity_order_002` WRITE;
/*!40000 ALTER TABLE `activity_order_002` DISABLE KEYS */;
/*!40000 ALTER TABLE `activity_order_002` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_order_003`
--

DROP TABLE IF EXISTS `activity_order_003`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_order_003` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `sku` bigint NOT NULL COMMENT '商品sku',
  `activity_name` varchar(64) NOT NULL COMMENT '活动名称',
  `strategy_id` bigint NOT NULL COMMENT '额度单ID',
  `order_id` varchar(12) NOT NULL COMMENT '订单ID',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `total_count` int NOT NULL COMMENT '总次数',
  `day_count` int NOT NULL COMMENT '日次数',
  `month_count` int NOT NULL COMMENT '月次数',
  `pay_amount` decimal(10,2) DEFAULT NULL COMMENT '支付积分',
  `state` varchar(10) NOT NULL DEFAULT 'complete' COMMENT '订单状态（complete-完成 wait_pay-等待支付）',
  `out_business_no` varchar(64) NOT NULL COMMENT '保证幂等，不会重复消费',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  UNIQUE KEY `uq_out_business_no` (`out_business_no`),
  KEY `idx_user_id_activity_id` (`user_id`,`activity_id`,`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='抽奖额度单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_order_003`
--

LOCK TABLES `activity_order_003` WRITE;
/*!40000 ALTER TABLE `activity_order_003` DISABLE KEYS */;
/*!40000 ALTER TABLE `activity_order_003` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credit_account`
--

DROP TABLE IF EXISTS `credit_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `credit_account` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `total_amount` decimal(10,2) NOT NULL COMMENT '总积分，显示总账户值，记得一个人获得的总积分',
  `available_amount` decimal(10,2) NOT NULL COMMENT '可用积分，每次扣减的值',
  `account_status` varchar(8) NOT NULL COMMENT '账户状态【open - 可用，close - 冻结】',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='积分账户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credit_account`
--

LOCK TABLES `credit_account` WRITE;
/*!40000 ALTER TABLE `credit_account` DISABLE KEYS */;
INSERT INTO `credit_account` VALUES (1,'yong3',20.00,10.05,'open','2025-05-06 23:45:06','2025-05-09 12:26:23'),(2,'yong4',57.50,49.52,'open','2025-05-09 23:33:03','2025-05-09 23:35:13');
/*!40000 ALTER TABLE `credit_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `topic` varchar(32) NOT NULL COMMENT '消息主题',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `message_id` varchar(11) NOT NULL COMMENT '消息编号',
  `message` varchar(512) NOT NULL COMMENT '消息主体',
  `state` varchar(16) NOT NULL DEFAULT 'create' COMMENT '任务状态；create-创建、completed-完成、fail-失败',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_message_id` (`message_id`),
  KEY `idx_state` (`state`),
  KEY `idx_create_time` (`update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='任务表，发送MQ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (1,'send_rebate','yong3','42146903042','BaseEvent.EventMessage(id=42146903042, timestamp=Tue May 06 23:45:06 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong3, rebateDesc=签到返利-抽奖额度, rebateType=sku, rebateConfig=9011, bizId=yong3_sku_20250506))','completed','2025-05-06 23:45:06','2025-05-06 23:45:06'),(2,'send_rebate','yong3','77420091015','BaseEvent.EventMessage(id=77420091015, timestamp=Tue May 06 23:45:06 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong3, rebateDesc=签到返利-积分, rebateType=integral, rebateConfig=10, bizId=yong3_integral_20250506))','completed','2025-05-06 23:45:06','2025-05-06 23:45:06'),(3,'credit_adjust_success','yong3','28053559491','{\"data\":{\"amount\":10,\"orderId\":\"885677462256\",\"outBusinessNo\":\"yong3_integral_20250506\",\"userId\":\"yong3\"},\"id\":\"28053559491\",\"timestamp\":1746546306792}','completed','2025-05-06 23:45:06','2025-05-06 23:45:06'),(4,'credit_adjust_success','yong3','59117456516','{\"data\":{\"amount\":1.99,\"orderId\":\"918365715030\",\"outBusinessNo\":\"805300751919\",\"userId\":\"yong3\"},\"id\":\"59117456516\",\"timestamp\":1746546341103}','completed','2025-05-06 23:45:41','2025-05-06 23:45:41'),(5,'credit_adjust_success','yong3','01670893747','{\"data\":{\"amount\":1.99,\"orderId\":\"390825784387\",\"outBusinessNo\":\"575573639351\",\"userId\":\"yong3\"},\"id\":\"01670893747\",\"timestamp\":1746546342758}','completed','2025-05-06 23:45:42','2025-05-06 23:45:42'),(6,'credit_adjust_success','yong3','45620118072','{\"data\":{\"amount\":1.99,\"orderId\":\"817750311641\",\"outBusinessNo\":\"134988844337\",\"userId\":\"yong3\"},\"id\":\"45620118072\",\"timestamp\":1746546343478}','completed','2025-05-06 23:45:43','2025-05-06 23:45:43'),(7,'credit_adjust_success','yong3','82867262129','{\"data\":{\"amount\":1.99,\"orderId\":\"942929030359\",\"outBusinessNo\":\"000416506136\",\"userId\":\"yong3\"},\"id\":\"82867262129\",\"timestamp\":1746546343828}','completed','2025-05-06 23:45:43','2025-05-06 23:45:43'),(8,'credit_adjust_success','yong3','06445363933','{\"data\":{\"amount\":1.99,\"orderId\":\"031907063665\",\"outBusinessNo\":\"940789090831\",\"userId\":\"yong3\"},\"id\":\"06445363933\",\"timestamp\":1746546346074}','completed','2025-05-06 23:45:46','2025-05-06 23:45:46'),(9,'send_award','yong3','62387702557','BaseEvent.EventMessage(id=62387702557, timestamp=Tue May 06 23:46:06 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong3, awardId=108, awardTitle=暴走玩偶, orderId=771112413364, awardConfig=null))','completed','2025-05-06 23:46:06','2025-05-06 23:46:06'),(10,'send_rebate','yong3','38057455108','BaseEvent.EventMessage(id=38057455108, timestamp=Fri May 09 12:26:23 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong3, rebateDesc=签到返利-抽奖额度, rebateType=sku, rebateConfig=9011, bizId=yong3_sku_20250509))','completed','2025-05-09 12:26:23','2025-05-09 12:26:23'),(11,'send_rebate','yong3','67993842018','BaseEvent.EventMessage(id=67993842018, timestamp=Fri May 09 12:26:23 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong3, rebateDesc=签到返利-积分, rebateType=integral, rebateConfig=10, bizId=yong3_integral_20250509))','completed','2025-05-09 12:26:23','2025-05-09 12:26:23'),(12,'credit_adjust_success','yong3','50353283656','{\"data\":{\"amount\":10,\"orderId\":\"010594408200\",\"outBusinessNo\":\"yong3_integral_20250509\",\"userId\":\"yong3\"},\"id\":\"50353283656\",\"timestamp\":1746764783208}','completed','2025-05-09 12:26:23','2025-05-09 12:26:23'),(13,'send_award','yong3','20113248706','BaseEvent.EventMessage(id=20113248706, timestamp=Fri May 09 14:15:40 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong3, awardId=106, awardTitle=轻奢办公椅, orderId=721037622343, awardConfig=null))','completed','2025-05-09 14:15:40','2025-05-09 14:15:40'),(14,'send_rebate','yong4','30084974598','BaseEvent.EventMessage(id=30084974598, timestamp=Fri May 09 23:33:03 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong4, rebateDesc=签到返利-抽奖额度, rebateType=sku, rebateConfig=9011, bizId=yong4_sku_20250509))','completed','2025-05-09 23:33:03','2025-05-09 23:33:03'),(15,'send_rebate','yong4','43993895402','BaseEvent.EventMessage(id=43993895402, timestamp=Fri May 09 23:33:03 CST 2025, data=SendRebateMessageEvent.RebateMessage(userId=yong4, rebateDesc=签到返利-积分, rebateType=integral, rebateConfig=10, bizId=yong4_integral_20250509))','completed','2025-05-09 23:33:03','2025-05-09 23:33:03'),(16,'credit_adjust_success','yong4','63401651823','{\"data\":{\"amount\":10,\"orderId\":\"733755870413\",\"outBusinessNo\":\"yong4_integral_20250509\",\"userId\":\"yong4\"},\"id\":\"63401651823\",\"timestamp\":1746804783916}','completed','2025-05-09 23:33:03','2025-05-09 23:33:03'),(17,'send_award','yong4','25383688554','BaseEvent.EventMessage(id=25383688554, timestamp=Fri May 09 23:34:34 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong4, awardId=101, awardTitle=随机积分, orderId=077299860661, awardConfig=null))','completed','2025-05-09 23:34:34','2025-05-09 23:34:34'),(18,'credit_adjust_success','yong4','88673616683','{\"data\":{\"amount\":1.99,\"orderId\":\"296621432149\",\"outBusinessNo\":\"746280633262\",\"userId\":\"yong4\"},\"id\":\"88673616683\",\"timestamp\":1746804898512}','completed','2025-05-09 23:34:58','2025-05-09 23:34:58'),(19,'credit_adjust_success','yong4','87898009892','{\"data\":{\"amount\":5.99,\"orderId\":\"560494876679\",\"outBusinessNo\":\"042654932328\",\"userId\":\"yong4\"},\"id\":\"87898009892\",\"timestamp\":1746804913206}','completed','2025-05-09 23:35:13','2025-05-09 23:35:13'),(20,'send_award','yong4','82847048470','BaseEvent.EventMessage(id=82847048470, timestamp=Fri May 09 23:35:30 CST 2025, data=SendAwardMessageEvent.SendAwardMessage(userId=yong4, awardId=104, awardTitle=小米台灯, orderId=449478788297, awardConfig=null))','completed','2025-05-09 23:35:30','2025-05-09 23:35:30');
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_award_record_000`
--

DROP TABLE IF EXISTS `user_award_record_000`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_award_record_000` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
  `order_id` varchar(12) NOT NULL COMMENT '抽奖单ID【作为幂等使用】',
  `award_id` bigint NOT NULL COMMENT '奖品ID',
  `award_title` varchar(128) NOT NULL COMMENT '奖品标题（名称）',
  `award_time` datetime NOT NULL COMMENT '中奖时间',
  `award_state` varchar(16) NOT NULL DEFAULT 'create' COMMENT '奖品状态；create-创建、completed-发奖完成、、fail-发奖失败',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_activity_id` (`activity_id`),
  KEY `idx_award_id` (`strategy_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户中奖记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_award_record_000`
--

LOCK TABLES `user_award_record_000` WRITE;
/*!40000 ALTER TABLE `user_award_record_000` DISABLE KEYS */;
INSERT INTO `user_award_record_000` VALUES (1,'yong4',100301,200001,'077299860661',101,'随机积分','2025-05-09 23:34:35','complete','2025-05-09 23:34:34','2025-05-09 23:34:34'),(2,'yong4',100301,200001,'449478788297',104,'小米台灯','2025-05-09 23:35:30','create','2025-05-09 23:35:30','2025-05-09 23:35:30');
/*!40000 ALTER TABLE `user_award_record_000` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_award_record_001`
--

DROP TABLE IF EXISTS `user_award_record_001`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_award_record_001` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
  `order_id` varchar(12) NOT NULL COMMENT '抽奖单ID【作为幂等使用】',
  `award_id` bigint NOT NULL COMMENT '奖品ID',
  `award_title` varchar(128) NOT NULL COMMENT '奖品标题（名称）',
  `award_time` datetime NOT NULL COMMENT '中奖时间',
  `award_state` varchar(16) NOT NULL DEFAULT 'create' COMMENT '奖品状态；create-创建、completed-发奖完成、、fail-发奖失败',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_activity_id` (`activity_id`),
  KEY `idx_award_id` (`strategy_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户中奖记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_award_record_001`
--

LOCK TABLES `user_award_record_001` WRITE;
/*!40000 ALTER TABLE `user_award_record_001` DISABLE KEYS */;
INSERT INTO `user_award_record_001` VALUES (1,'yong3',100301,200001,'771112413364',108,'暴走玩偶','2025-05-06 23:46:07','create','2025-05-06 23:46:06','2025-05-06 23:46:06'),(2,'yong3',100301,200001,'721037622343',106,'轻奢办公椅','2025-05-09 14:15:40','create','2025-05-09 14:15:40','2025-05-09 14:15:40');
/*!40000 ALTER TABLE `user_award_record_001` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_award_record_002`
--

DROP TABLE IF EXISTS `user_award_record_002`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_award_record_002` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
  `order_id` varchar(12) NOT NULL COMMENT '抽奖单ID【作为幂等使用】',
  `award_id` bigint NOT NULL COMMENT '奖品ID',
  `award_title` varchar(128) NOT NULL COMMENT '奖品标题（名称）',
  `award_time` datetime NOT NULL COMMENT '中奖时间',
  `award_state` varchar(16) NOT NULL DEFAULT 'create' COMMENT '奖品状态；create-创建、completed-发奖完成、、fail-发奖失败',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_activity_id` (`activity_id`),
  KEY `idx_award_id` (`strategy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户中奖记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_award_record_002`
--

LOCK TABLES `user_award_record_002` WRITE;
/*!40000 ALTER TABLE `user_award_record_002` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_award_record_002` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_award_record_003`
--

DROP TABLE IF EXISTS `user_award_record_003`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_award_record_003` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
  `order_id` varchar(12) NOT NULL COMMENT '抽奖单ID【作为幂等使用】',
  `award_id` bigint NOT NULL COMMENT '奖品ID',
  `award_title` varchar(128) NOT NULL COMMENT '奖品标题（名称）',
  `award_time` datetime NOT NULL COMMENT '中奖时间',
  `award_state` varchar(16) NOT NULL DEFAULT 'create' COMMENT '奖品状态；create-创建、completed-发奖完成、、fail-发奖失败',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_activity_id` (`activity_id`),
  KEY `idx_award_id` (`strategy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户中奖记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_award_record_003`
--

LOCK TABLES `user_award_record_003` WRITE;
/*!40000 ALTER TABLE `user_award_record_003` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_award_record_003` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_behavior_rebate_order_000`
--

DROP TABLE IF EXISTS `user_behavior_rebate_order_000`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_behavior_rebate_order_000` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `order_id` varchar(12) NOT NULL COMMENT '订单ID',
  `behavior_type` varchar(16) NOT NULL COMMENT '行为类型（sign 签到、openai_pay 支付）',
  `rebate_desc` varchar(128) NOT NULL COMMENT '返利描述',
  `rebate_type` varchar(16) NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
  `rebate_config` varchar(32) NOT NULL COMMENT '返利配置【sku值，积分值】',
  `out_business_no` varchar(64) NOT NULL COMMENT '业务仿重ID - 外部透传，方便查询使用',
  `biz_id` varchar(64) NOT NULL COMMENT '业务ID - 拼接的唯一值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  UNIQUE KEY `uq_biz_id` (`biz_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户行为返利流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_behavior_rebate_order_000`
--

LOCK TABLES `user_behavior_rebate_order_000` WRITE;
/*!40000 ALTER TABLE `user_behavior_rebate_order_000` DISABLE KEYS */;
INSERT INTO `user_behavior_rebate_order_000` VALUES (1,'yong4','344319879217','sign','签到返利-抽奖额度','sku','9011','20250509','yong4_sku_20250509','2025-05-09 23:33:03','2025-05-09 23:33:03'),(2,'yong4','518647936767','sign','签到返利-积分','integral','10','20250509','yong4_integral_20250509','2025-05-09 23:33:03','2025-05-09 23:33:03');
/*!40000 ALTER TABLE `user_behavior_rebate_order_000` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_behavior_rebate_order_001`
--

DROP TABLE IF EXISTS `user_behavior_rebate_order_001`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_behavior_rebate_order_001` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `order_id` varchar(12) NOT NULL COMMENT '订单ID',
  `behavior_type` varchar(16) NOT NULL COMMENT '行为类型（sign 签到、openai_pay 支付）',
  `rebate_desc` varchar(128) NOT NULL COMMENT '返利描述',
  `rebate_type` varchar(16) NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
  `rebate_config` varchar(32) NOT NULL COMMENT '返利配置【sku值，积分值】',
  `out_business_no` varchar(64) NOT NULL COMMENT '业务仿重ID - 外部透传，方便查询使用',
  `biz_id` varchar(64) NOT NULL COMMENT '业务ID - 拼接的唯一值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  UNIQUE KEY `uq_biz_id` (`biz_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户行为返利流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_behavior_rebate_order_001`
--

LOCK TABLES `user_behavior_rebate_order_001` WRITE;
/*!40000 ALTER TABLE `user_behavior_rebate_order_001` DISABLE KEYS */;
INSERT INTO `user_behavior_rebate_order_001` VALUES (1,'yong3','568334974688','sign','签到返利-抽奖额度','sku','9011','20250506','yong3_sku_20250506','2025-05-06 23:45:06','2025-05-06 23:45:06'),(2,'yong3','896703796718','sign','签到返利-积分','integral','10','20250506','yong3_integral_20250506','2025-05-06 23:45:06','2025-05-06 23:45:06'),(3,'yong3','565804494616','sign','签到返利-抽奖额度','sku','9011','20250509','yong3_sku_20250509','2025-05-09 12:26:23','2025-05-09 12:26:23'),(4,'yong3','039739865361','sign','签到返利-积分','integral','10','20250509','yong3_integral_20250509','2025-05-09 12:26:23','2025-05-09 12:26:23');
/*!40000 ALTER TABLE `user_behavior_rebate_order_001` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_behavior_rebate_order_002`
--

DROP TABLE IF EXISTS `user_behavior_rebate_order_002`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_behavior_rebate_order_002` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `order_id` varchar(12) NOT NULL COMMENT '订单ID',
  `behavior_type` varchar(16) NOT NULL COMMENT '行为类型（sign 签到、openai_pay 支付）',
  `rebate_desc` varchar(128) NOT NULL COMMENT '返利描述',
  `rebate_type` varchar(16) NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
  `rebate_config` varchar(32) NOT NULL COMMENT '返利配置【sku值，积分值】',
  `out_business_no` varchar(64) NOT NULL COMMENT '业务仿重ID - 外部透传，方便查询使用',
  `biz_id` varchar(64) NOT NULL COMMENT '业务ID - 拼接的唯一值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  UNIQUE KEY `uq_biz_id` (`biz_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户行为返利流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_behavior_rebate_order_002`
--

LOCK TABLES `user_behavior_rebate_order_002` WRITE;
/*!40000 ALTER TABLE `user_behavior_rebate_order_002` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_behavior_rebate_order_002` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_behavior_rebate_order_003`
--

DROP TABLE IF EXISTS `user_behavior_rebate_order_003`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_behavior_rebate_order_003` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `order_id` varchar(12) NOT NULL COMMENT '订单ID',
  `behavior_type` varchar(16) NOT NULL COMMENT '行为类型（sign 签到、openai_pay 支付）',
  `rebate_desc` varchar(128) NOT NULL COMMENT '返利描述',
  `rebate_type` varchar(16) NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
  `rebate_config` varchar(32) NOT NULL COMMENT '返利配置【sku值，积分值】',
  `out_business_no` varchar(64) NOT NULL COMMENT '业务仿重ID - 外部透传，方便查询使用',
  `biz_id` varchar(64) NOT NULL COMMENT '业务ID - 拼接的唯一值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  UNIQUE KEY `uq_biz_id` (`biz_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户行为返利流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_behavior_rebate_order_003`
--

LOCK TABLES `user_behavior_rebate_order_003` WRITE;
/*!40000 ALTER TABLE `user_behavior_rebate_order_003` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_behavior_rebate_order_003` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_credit_order_000`
--

DROP TABLE IF EXISTS `user_credit_order_000`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_credit_order_000` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `order_id` varchar(12) NOT NULL COMMENT '订单ID',
  `trade_name` varchar(32) NOT NULL COMMENT '交易名称',
  `trade_type` varchar(8) NOT NULL DEFAULT 'forward' COMMENT '交易类型；forward-正向、reverse-逆向',
  `trade_amount` decimal(10,2) NOT NULL COMMENT '交易金额',
  `out_business_no` varchar(64) NOT NULL COMMENT '业务仿重ID - 外部透传。返利、行为等唯一标识',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  UNIQUE KEY `uq_out_business_no` (`out_business_no`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户积分流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_credit_order_000`
--

LOCK TABLES `user_credit_order_000` WRITE;
/*!40000 ALTER TABLE `user_credit_order_000` DISABLE KEYS */;
INSERT INTO `user_credit_order_000` VALUES (1,'yong4','733755870413','每日签到','forward',10.00,'yong4_integral_20250509','2025-05-09 23:33:03','2025-05-09 23:33:03'),(2,'yong4','296621432149','积分兑换','reverse',1.99,'746280633262','2025-05-09 23:34:58','2025-05-09 23:34:58'),(3,'yong4','560494876679','积分兑换','reverse',5.99,'042654932328','2025-05-09 23:35:13','2025-05-09 23:35:13');
/*!40000 ALTER TABLE `user_credit_order_000` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_credit_order_001`
--

DROP TABLE IF EXISTS `user_credit_order_001`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_credit_order_001` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `order_id` varchar(12) NOT NULL COMMENT '订单ID',
  `trade_name` varchar(32) NOT NULL COMMENT '交易名称',
  `trade_type` varchar(8) NOT NULL DEFAULT 'forward' COMMENT '交易类型；forward-正向、reverse-逆向',
  `trade_amount` decimal(10,2) NOT NULL COMMENT '交易金额',
  `out_business_no` varchar(64) NOT NULL COMMENT '业务仿重ID - 外部透传。返利、行为等唯一标识',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  UNIQUE KEY `uq_out_business_no` (`out_business_no`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户积分流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_credit_order_001`
--

LOCK TABLES `user_credit_order_001` WRITE;
/*!40000 ALTER TABLE `user_credit_order_001` DISABLE KEYS */;
INSERT INTO `user_credit_order_001` VALUES (1,'yong3','885677462256','每日签到','forward',10.00,'yong3_integral_20250506','2025-05-06 23:45:06','2025-05-06 23:45:06'),(2,'yong3','918365715030','积分兑换','reverse',1.99,'805300751919','2025-05-06 23:45:41','2025-05-06 23:45:41'),(3,'yong3','390825784387','积分兑换','reverse',1.99,'575573639351','2025-05-06 23:45:42','2025-05-06 23:45:42'),(4,'yong3','817750311641','积分兑换','reverse',1.99,'134988844337','2025-05-06 23:45:43','2025-05-06 23:45:43'),(5,'yong3','942929030359','积分兑换','reverse',1.99,'000416506136','2025-05-06 23:45:43','2025-05-06 23:45:43'),(6,'yong3','031907063665','积分兑换','reverse',1.99,'940789090831','2025-05-06 23:45:46','2025-05-06 23:45:46'),(7,'yong3','010594408200','每日签到','forward',10.00,'yong3_integral_20250509','2025-05-09 12:26:23','2025-05-09 12:26:23');
/*!40000 ALTER TABLE `user_credit_order_001` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_credit_order_002`
--

DROP TABLE IF EXISTS `user_credit_order_002`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_credit_order_002` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `order_id` varchar(12) NOT NULL COMMENT '订单ID',
  `trade_name` varchar(32) NOT NULL COMMENT '交易名称',
  `trade_type` varchar(8) NOT NULL DEFAULT 'forward' COMMENT '交易类型；forward-正向、reverse-逆向',
  `trade_amount` decimal(10,2) NOT NULL COMMENT '交易金额',
  `out_business_no` varchar(64) NOT NULL COMMENT '业务仿重ID - 外部透传。返利、行为等唯一标识',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  UNIQUE KEY `uq_out_business_no` (`out_business_no`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户积分流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_credit_order_002`
--

LOCK TABLES `user_credit_order_002` WRITE;
/*!40000 ALTER TABLE `user_credit_order_002` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_credit_order_002` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_credit_order_003`
--

DROP TABLE IF EXISTS `user_credit_order_003`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_credit_order_003` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `order_id` varchar(12) NOT NULL COMMENT '订单ID',
  `trade_name` varchar(32) NOT NULL COMMENT '交易名称',
  `trade_type` varchar(8) NOT NULL DEFAULT 'forward' COMMENT '交易类型；forward-正向、reverse-逆向',
  `trade_amount` decimal(10,2) NOT NULL COMMENT '交易金额',
  `out_business_no` varchar(64) NOT NULL COMMENT '业务仿重ID - 外部透传。返利、行为等唯一标识',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  UNIQUE KEY `uq_out_business_no` (`out_business_no`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户积分流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_credit_order_003`
--

LOCK TABLES `user_credit_order_003` WRITE;
/*!40000 ALTER TABLE `user_credit_order_003` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_credit_order_003` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_order_000`
--

DROP TABLE IF EXISTS `user_order_000`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_order_000` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `order_id` varchar(12) NOT NULL COMMENT '抽奖单ID',
  `activity_name` varchar(64) NOT NULL COMMENT '活动名称',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `order_state` varchar(16) NOT NULL DEFAULT 'create' COMMENT '订单状态；create-创建、used-已使用、cancel-已作废',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  KEY `idx_user_id_activity_id` (`user_id`,`activity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户抽奖单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_order_000`
--

LOCK TABLES `user_order_000` WRITE;
/*!40000 ALTER TABLE `user_order_000` DISABLE KEYS */;
INSERT INTO `user_order_000` VALUES (1,'yong4',100301,'077299860661','测试活动test',200001,'2025-05-09 23:34:35','used','2025-05-09 23:34:34','2025-05-09 23:34:34'),(2,'yong4',100301,'449478788297','测试活动test',200001,'2025-05-09 23:35:30','used','2025-05-09 23:35:30','2025-05-09 23:35:30');
/*!40000 ALTER TABLE `user_order_000` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_order_001`
--

DROP TABLE IF EXISTS `user_order_001`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_order_001` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `order_id` varchar(12) NOT NULL COMMENT '抽奖单ID',
  `activity_name` varchar(64) NOT NULL COMMENT '活动名称',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `order_state` varchar(16) NOT NULL DEFAULT 'create' COMMENT '订单状态；create-创建、used-已使用、cancel-已作废',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  KEY `idx_user_id_activity_id` (`user_id`,`activity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户抽奖单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_order_001`
--

LOCK TABLES `user_order_001` WRITE;
/*!40000 ALTER TABLE `user_order_001` DISABLE KEYS */;
INSERT INTO `user_order_001` VALUES (1,'yong3',100301,'771112413364','测试活动',200001,'2025-05-06 23:46:07','used','2025-05-06 23:46:06','2025-05-06 23:46:06'),(2,'yong3',100301,'721037622343','测试活动test',200001,'2025-05-09 14:15:40','used','2025-05-09 14:15:40','2025-05-09 14:15:40');
/*!40000 ALTER TABLE `user_order_001` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_order_002`
--

DROP TABLE IF EXISTS `user_order_002`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_order_002` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `order_id` varchar(12) NOT NULL COMMENT '抽奖单ID',
  `activity_name` varchar(64) NOT NULL COMMENT '活动名称',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `order_state` varchar(16) NOT NULL DEFAULT 'create' COMMENT '订单状态；create-创建、used-已使用、cancel-已作废',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  KEY `idx_user_id_activity_id` (`user_id`,`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户抽奖单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_order_002`
--

LOCK TABLES `user_order_002` WRITE;
/*!40000 ALTER TABLE `user_order_002` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_order_002` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_order_003`
--

DROP TABLE IF EXISTS `user_order_003`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_order_003` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `order_id` varchar(12) NOT NULL COMMENT '抽奖单ID',
  `activity_name` varchar(64) NOT NULL COMMENT '活动名称',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `order_state` varchar(16) NOT NULL DEFAULT 'create' COMMENT '订单状态；create-创建、used-已使用、cancel-已作废',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_order_id` (`order_id`),
  KEY `idx_user_id_activity_id` (`user_id`,`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户抽奖单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_order_003`
--

LOCK TABLES `user_order_003` WRITE;
/*!40000 ALTER TABLE `user_order_003` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_order_003` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-29 12:22:23
