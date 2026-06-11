-- MySQL dump 10.13  Distrib 8.0.25, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: factory_db
-- ------------------------------------------------------
-- Server version	8.0.23

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `inventory_stocks`
--

DROP TABLE IF EXISTS `inventory_stocks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_stocks` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `material_id` bigint NOT NULL,
  `lot_number` varchar(100) NOT NULL,
  `origin` varchar(255) DEFAULT NULL COMMENT '産地（未設定可）',
  `quantity` decimal(12,3) NOT NULL DEFAULT '0.000' COMMENT '実在庫量',
  `reserved_quantity` decimal(12,3) NOT NULL DEFAULT '0.000' COMMENT '仮消費ロック量',
  `expiry_date` date DEFAULT NULL,
  `arrival_date` date DEFAULT NULL,
  `inspected` tinyint(1) NOT NULL DEFAULT '0' COMMENT '検品済みフラグ',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_material_lot_origin` (`material_id`,`lot_number`,`origin`),
  CONSTRAINT `fk_stock_material` FOREIGN KEY (`material_id`) REFERENCES `materials` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_stocks`
--

LOCK TABLES `inventory_stocks` WRITE;
/*!40000 ALTER TABLE `inventory_stocks` DISABLE KEYS */;
INSERT INTO `inventory_stocks` VALUES (1,1,'Test-001','愛知県',20.000,0.000,'2027-06-11','2026-06-11',1,'2026-06-11 00:40:59'),(2,1,'Test-002','三重県',20.000,0.000,'2027-06-11','2026-06-11',1,'2026-06-11 00:41:43'),(3,2,'Test-003','愛知県',20.000,0.000,'2027-06-11','2026-06-11',0,'2026-06-11 00:43:07');
/*!40000 ALTER TABLE `inventory_stocks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory_transactions`
--

DROP TABLE IF EXISTS `inventory_transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_transactions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `stock_id` bigint NOT NULL,
  `transaction_type` varchar(20) NOT NULL COMMENT 'IN / PRODUCTION / DISPOSAL',
  `quantity_change` decimal(12,3) NOT NULL COMMENT '変化量（消費・廃棄はマイナス）',
  `product_code` varchar(100) DEFAULT NULL,
  `product_number` varchar(100) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `transaction_date` date NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_tx_stock` (`stock_id`),
  CONSTRAINT `fk_tx_stock` FOREIGN KEY (`stock_id`) REFERENCES `inventory_stocks` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_transactions`
--

LOCK TABLES `inventory_transactions` WRITE;
/*!40000 ALTER TABLE `inventory_transactions` DISABLE KEYS */;
INSERT INTO `inventory_transactions` VALUES (1,1,'IN',20.000,NULL,NULL,'入荷: lot=Test-001','2026-06-11','2026-06-11 00:41:00'),(2,2,'IN',20.000,NULL,NULL,'入荷: lot=Test-002','2026-06-11','2026-06-11 00:41:44'),(3,3,'IN',20.000,NULL,NULL,'入荷: lot=Test-003','2026-06-11','2026-06-11 00:43:07');
/*!40000 ALTER TABLE `inventory_transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_stocks`
--

DROP TABLE IF EXISTS `item_stocks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_stocks` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `item_id` bigint NOT NULL,
  `lot_number` varchar(100) NOT NULL COMMENT '製造日ベースで採番',
  `quantity` decimal(12,3) NOT NULL DEFAULT '0.000',
  `min_stock` decimal(12,3) DEFAULT NULL COMMENT '適正在庫（手動設定）',
  `production_date` date NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_item_lot` (`item_id`,`lot_number`),
  KEY `fk_istock_item` (`item_id`),
  CONSTRAINT `fk_istock_item` FOREIGN KEY (`item_id`) REFERENCES `items` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_stocks`
--

LOCK TABLES `item_stocks` WRITE;
/*!40000 ALTER TABLE `item_stocks` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_stocks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `items`
--

DROP TABLE IF EXISTS `items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `sales_unit` varchar(20) NOT NULL,
  `batch_size` decimal(12,3) DEFAULT NULL COMMENT '1製造あたりの製造数',
  `standard_cost` decimal(12,3) DEFAULT NULL,
  `sales_price` decimal(12,3) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '論理削除フラグ',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_item_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `items`
--

LOCK TABLES `items` WRITE;
/*!40000 ALTER TABLE `items` DISABLE KEYS */;
INSERT INTO `items` VALUES (1,'うどん','個',198.000,70.000,200.000,1),(2,'玄米うどん','個',198.000,70.000,200.000,1),(3,'ラーメンウェーブ','個',195.000,70.200,200.000,1);
/*!40000 ALTER TABLE `items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `materials`
--

DROP TABLE IF EXISTS `materials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `materials` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `unit` varchar(20) NOT NULL,
  `material_type` varchar(20) NOT NULL DEFAULT 'RAW' COMMENT 'RAW=原料, ADDITIVE=添加物',
  `is_powder` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1=粉体, 0=液体',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '論理削除フラグ',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_material_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `materials`
--

LOCK TABLES `materials` WRITE;
/*!40000 ALTER TABLE `materials` DISABLE KEYS */;
INSERT INTO `materials` VALUES (1,'米粉','g','RAW',1,0),(2,'玄米粉','g','RAW',1,0),(3,'α米','g','ADDITIVE',1,0),(4,'F-800','g','ADDITIVE',1,0),(5,'アルギン酸エステル','g','ADDITIVE',1,0),(6,'キサンタンガム','g','ADDITIVE',1,0),(7,'クチナシ','g','ADDITIVE',1,0),(8,'V-B2','g','ADDITIVE',1,0),(9,'FKハイパー','ml','ADDITIVE',0,0),(10,'酢','ml','ADDITIVE',0,0),(11,'酒精','ml','ADDITIVE',0,0);
/*!40000 ALTER TABLE `materials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `production_details`
--

DROP TABLE IF EXISTS `production_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `production_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `stock_id` bigint NOT NULL,
  `material_id` bigint NOT NULL,
  `origin` varchar(255) DEFAULT NULL COMMENT '使用した産地（スナップショット）',
  `lot_number` varchar(100) DEFAULT NULL COMMENT '使用ロット番号（スナップショット）',
  `expiry_date` date DEFAULT NULL COMMENT '賞味期限（スナップショット）',
  `quantity_used` decimal(12,3) NOT NULL COMMENT '使用量',
  PRIMARY KEY (`id`),
  KEY `fk_pd_order` (`order_id`),
  KEY `fk_pd_stock` (`stock_id`),
  KEY `fk_pd_material` (`material_id`),
  CONSTRAINT `fk_pd_material` FOREIGN KEY (`material_id`) REFERENCES `materials` (`id`),
  CONSTRAINT `fk_pd_order` FOREIGN KEY (`order_id`) REFERENCES `production_orders` (`id`),
  CONSTRAINT `fk_pd_stock` FOREIGN KEY (`stock_id`) REFERENCES `inventory_stocks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `production_details`
--

LOCK TABLES `production_details` WRITE;
/*!40000 ALTER TABLE `production_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `production_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `production_orders`
--

DROP TABLE IF EXISTS `production_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `production_orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `item_id` bigint NOT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'TEMP' COMMENT 'TEMP / DONE / DISPOSED',
  `water_amount` decimal(12,3) DEFAULT NULL COMMENT '使用した加水量',
  `production_date` date NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_po_item` (`item_id`),
  CONSTRAINT `fk_po_item` FOREIGN KEY (`item_id`) REFERENCES `items` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `production_orders`
--

LOCK TABLES `production_orders` WRITE;
/*!40000 ALTER TABLE `production_orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `production_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recipe_origins`
--

DROP TABLE IF EXISTS `recipe_origins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recipe_origins` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `recipe_id` bigint NOT NULL,
  `origin` varchar(255) NOT NULL COMMENT '使用可能な産地名',
  PRIMARY KEY (`id`),
  KEY `fk_ro_recipe` (`recipe_id`),
  CONSTRAINT `fk_ro_recipe` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recipe_origins`
--

LOCK TABLES `recipe_origins` WRITE;
/*!40000 ALTER TABLE `recipe_origins` DISABLE KEYS */;
INSERT INTO `recipe_origins` VALUES (1,1,'三重県'),(2,1,'愛知県');
/*!40000 ALTER TABLE `recipe_origins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recipes`
--

DROP TABLE IF EXISTS `recipes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recipes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `item_id` bigint NOT NULL,
  `material_id` bigint NOT NULL,
  `quantity` decimal(12,3) NOT NULL COMMENT '使用量',
  `min_water_amount` decimal(12,3) DEFAULT NULL,
  `max_water_amount` decimal(12,3) DEFAULT NULL,
  `min_hydration_rate` decimal(5,2) DEFAULT NULL COMMENT '最低加水率(%)',
  `max_hydration_rate` decimal(5,2) DEFAULT NULL COMMENT '最大加水率(%)',
  PRIMARY KEY (`id`),
  KEY `fk_recipe_item` (`item_id`),
  KEY `fk_recipe_material` (`material_id`),
  CONSTRAINT `fk_recipe_item` FOREIGN KEY (`item_id`) REFERENCES `items` (`id`),
  CONSTRAINT `fk_recipe_material` FOREIGN KEY (`material_id`) REFERENCES `materials` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recipes`
--

LOCK TABLES `recipes` WRITE;
/*!40000 ALTER TABLE `recipes` DISABLE KEYS */;
INSERT INTO `recipes` VALUES (1,3,1,15000.000,9199.900,10036.200,55.00,60.00),(2,3,3,1200.000,9199.900,10036.200,55.00,60.00),(3,3,5,60.000,9199.900,10036.200,55.00,60.00),(4,3,6,450.000,9199.900,10036.200,55.00,60.00),(5,3,7,17.000,9199.900,10036.200,55.00,60.00),(6,3,9,81.000,9199.900,10036.200,55.00,60.00),(7,3,10,45.000,9199.900,10036.200,55.00,60.00),(8,3,11,450.000,9199.900,10036.200,55.00,60.00);
/*!40000 ALTER TABLE `recipes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-11 13:50:26
