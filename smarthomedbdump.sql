CREATE DATABASE  IF NOT EXISTS `smarthomedb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `smarthomedb`;
-- MySQL dump 10.13  Distrib 8.0.25, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: smarthomedb
-- ------------------------------------------------------
-- Server version	8.0.25

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
-- Table structure for table `alarm`
--

DROP TABLE IF EXISTS `alarm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alarm` (
  `AlarmId` int unsigned NOT NULL AUTO_INCREMENT,
  `AlarmTime` datetime NOT NULL,
  `SongOfAlarm` varchar(45) NOT NULL,
  `UserId` int NOT NULL,
  PRIMARY KEY (`AlarmId`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alarm`
--

LOCK TABLES `alarm` WRITE;
/*!40000 ALTER TABLE `alarm` DISABLE KEYS */;
INSERT INTO `alarm` VALUES (1,'2021-06-20 22:32:15','RHCP By the way',2),(2,'2021-06-20 08:32:15','Morning song',1),(3,'2021-06-21 09:32:15','Hello monday',3),(4,'2021-06-21 10:32:15','Morning song',4),(5,'2021-06-19 09:32:15','Alarm tone',3),(6,'2021-06-24 09:32:15','Alarm tone',1),(7,'2021-07-21 19:32:15','Alarm tone',2);
/*!40000 ALTER TABLE `alarm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `song`
--

DROP TABLE IF EXISTS `song`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `song` (
  `SongId` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `UserId` int NOT NULL,
  PRIMARY KEY (`SongId`),
  UNIQUE KEY `SongId_UNIQUE` (`SongId`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `song`
--

LOCK TABLES `song` WRITE;
/*!40000 ALTER TABLE `song` DISABLE KEYS */;
INSERT INTO `song` VALUES (1,'Doors - Riders on the storm',2),(2,'Rolling Stones - Angie',1),(3,'Pink Floyd High Hopes',1),(4,'Bob Dylan Like a Rolling Stone',3),(5,'John Lennon Imagine',4),(6,'Ray Charles Hit the road jack',1),(7,'RHCP Snow',2),(8,'Van Morrison Moondance',1),(9,'Rolling Stones Brown Sugar',2),(10,'The velvet Underground Heroin',3),(11,'Nirvana All Apologies',3),(12,'Led Zeppelin Ramble On',4),(13,'Deep Purple Smoke on th Water',3),(14,'ZHU Money',1),(15,'RHCP Dark Necessities',3);
/*!40000 ALTER TABLE `song` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task` (
  `TaskId` int NOT NULL AUTO_INCREMENT,
  `Destination` varchar(45) DEFAULT NULL,
  `Duration` bigint NOT NULL,
  `StartDatetime` datetime NOT NULL,
  `AlarmId` int DEFAULT NULL,
  `UserId` int NOT NULL,
  PRIMARY KEY (`TaskId`),
  UNIQUE KEY `TaskId_UNIQUE` (`TaskId`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (1,'Nis',44214,'2020-05-11 00:00:00',1,1),(2,'Beograd',122,'2020-05-12 04:10:00',2,1),(3,'Bulevar Oslobodjenja 32, Beograd',53325,'2021-05-12 04:10:00',3,2),(4,'Kragujevac',4212,'2021-05-12 01:10:20',4,3),(5,'Beogradska Arena',244,'2021-07-12 04:10:00',5,4),(6,'Fruska gora',5353253,'2021-05-12 04:15:00',6,3),(7,'Avala',421421,'2021-11-12 14:10:00',7,2);
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `UserId` int NOT NULL AUTO_INCREMENT,
  `Username` varchar(45) NOT NULL,
  `Password` varchar(45) NOT NULL,
  PRIMARY KEY (`UserId`),
  UNIQUE KEY `Username_UNIQUE` (`Username`)
) ENGINE=MEMORY AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','admin123'),(2,'zika','zika123'),(3,'pera','pera123'),(4,'mika','mika123');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-21  2:11:02
