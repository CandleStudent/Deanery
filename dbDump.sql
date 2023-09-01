CREATE DATABASE  IF NOT EXISTS `users` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `users`;
-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: users
-- ------------------------------------------------------
-- Server version	8.0.31

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
-- Table structure for table `enterdata`
--

DROP TABLE IF EXISTS `enterdata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enterdata` (
  `id` int NOT NULL AUTO_INCREMENT,
  `login` varchar(30) DEFAULT NULL,
  `pass` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login` (`login`),
  CONSTRAINT `enterdata_ibfk_1` FOREIGN KEY (`id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enterdata`
--

LOCK TABLES `enterdata` WRITE;
/*!40000 ALTER TABLE `enterdata` DISABLE KEYS */;
INSERT INTO `enterdata` VALUES (1,'admin','password'),(2,'AABrahmanov','qwerty'),(3,'RIIvanov','qwerty'),(4,'AMPeshkov','qwerty'),(5,'LNTolstoy','qwerty'),(6,'MULermontov','qwerty');
/*!40000 ALTER TABLE `enterdata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` int NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'administrator'),(2,'student');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `studentsusers`
--

DROP TABLE IF EXISTS `studentsusers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `studentsusers` (
  `userId` int NOT NULL,
  `studentId` int DEFAULT NULL,
  PRIMARY KEY (`userId`),
  UNIQUE KEY `studentId` (`studentId`),
  CONSTRAINT `studentsusers_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `studentsusers`
--

LOCK TABLES `studentsusers` WRITE;
/*!40000 ALTER TABLE `studentsusers` DISABLE KEYS */;
INSERT INTO `studentsusers` VALUES (3,1),(2,2),(4,11),(5,12),(6,13);
/*!40000 ALTER TABLE `studentsusers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `roleId` int DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,1),(2,2),(3,2),(4,2),(5,2),(6,2);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

CREATE DATABASE  IF NOT EXISTS `studentsdb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `studentsdb`;
-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: studentsdb
-- ------------------------------------------------------
-- Server version	8.0.31

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
-- Table structure for table `academicgroups`
--

DROP TABLE IF EXISTS `academicgroups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `academicgroups` (
  `GroupNum` int NOT NULL,
  `Term` int DEFAULT NULL,
  `GraduationYear` date DEFAULT NULL,
  `DirectionId` int DEFAULT NULL,
  `IsGraduated` tinyint(1) DEFAULT '0',
  `StartDate` date DEFAULT NULL,
  `LastSession` int DEFAULT NULL,
  PRIMARY KEY (`GroupNum`),
  KEY `DirectionId` (`DirectionId`),
  CONSTRAINT `academicgroups_ibfk_1` FOREIGN KEY (`DirectionId`) REFERENCES `directions` (`DirectionId`),
  CONSTRAINT `academicgroups_chk_1` CHECK (((`Term` > 0) and (`Term` < 5)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `academicgroups`
--

LOCK TABLES `academicgroups` WRITE;
/*!40000 ALTER TABLE `academicgroups` DISABLE KEYS */;
INSERT INTO `academicgroups` VALUES (111,2,'2025-06-25',1,0,'2021-09-01',3),(112,2,'2025-06-25',1,0,'2021-09-01',3),(221,1,'2026-06-25',2,0,'2022-09-01',1),(231,1,'2026-06-25',3,0,'2022-09-01',1);
/*!40000 ALTER TABLE `academicgroups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `directions`
--

DROP TABLE IF EXISTS `directions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `directions` (
  `DirectionId` int NOT NULL,
  `DirectionName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`DirectionId`),
  UNIQUE KEY `directionId` (`DirectionId`),
  UNIQUE KEY `directionName` (`DirectionName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `directions`
--

LOCK TABLES `directions` WRITE;
/*!40000 ALTER TABLE `directions` DISABLE KEYS */;
INSERT INTO `directions` VALUES (3,'Информационная безопасность'),(2,'Прикладная информатика'),(1,'Прикладная математика и информатика');
/*!40000 ALTER TABLE `directions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `disciplines`
--

DROP TABLE IF EXISTS `disciplines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `disciplines` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `DisciplineName` varchar(70) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Name` (`DisciplineName`),
  UNIQUE KEY `Name_2` (`DisciplineName`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `disciplines`
--

LOCK TABLES `disciplines` WRITE;
/*!40000 ALTER TABLE `disciplines` DISABLE KEYS */;
INSERT INTO `disciplines` VALUES (7,'Алгебра и геометрия'),(32,'Аппаратные средства вычислительной техники'),(13,'Базы данных'),(36,'Безопасность вычислительных сетей'),(18,'Введение в информационную безопасность'),(44,'Выполнение и защита выпускной квалификационной работ'),(11,'Дискретная математика'),(12,'Дифференциальные уравнения'),(40,'Защита информации от утечки по техническим каналам'),(1,'Иностранный язык'),(4,'Информатика'),(26,'Криптографические методы защиты информации'),(15,'Математическая логика'),(2,'Математический анализ'),(38,'Машинное обучение'),(41,'Методы и средства криптографической защиты информации'),(14,'Методы оптимизации'),(42,'Моделирование информационных процессов'),(29,'Моделирование информационных систем'),(28,'Моделирование операций'),(21,'Объектно-ориентированный анализ и проектирование'),(10,'Операционные системы'),(22,'Основы информационной безопасности'),(30,'Основы программирования'),(23,'Основы программной инженерии'),(24,'Проектирование программных систем'),(20,'Проектирование человеко-машинного интерфейса'),(37,'Сети и системы передачи информации'),(35,'Теоретические основы компьютерной безопасности'),(19,'Теория автоматов и формальных языков'),(6,'Теория вероятностей'),(16,'Теория игр и принятие решений'),(43,'Теория кодирования информации'),(25,'Теория систем и системный анализ'),(39,'Технологии баз данных'),(33,'Технологии и методы программирования'),(27,'Управление программными проектами'),(9,'Физика'),(8,'Физическая культура'),(17,'Численные методы'),(34,'Электроника и схемотехника'),(3,'Языки и методы программирования'),(31,'Языки программирования');
/*!40000 ALTER TABLE `disciplines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grades`
--

DROP TABLE IF EXISTS `grades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grades` (
  `StudentId` int NOT NULL,
  `SubjectId` int NOT NULL,
  `ExamSessionNumber` int DEFAULT NULL,
  `ExamDate` date DEFAULT NULL,
  `ExamPoints` int DEFAULT '0',
  KEY `StudentId` (`StudentId`),
  KEY `SubjectId` (`SubjectId`),
  CONSTRAINT `grades_ibfk_1` FOREIGN KEY (`StudentId`) REFERENCES `students` (`Id`),
  CONSTRAINT `grades_ibfk_2` FOREIGN KEY (`SubjectId`) REFERENCES `disciplines` (`Id`),
  CONSTRAINT `grades_chk_1` CHECK ((`ExamSessionNumber` < 13)),
  CONSTRAINT `grades_chk_2` CHECK ((`ExamPoints` <= 100))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grades`
--

LOCK TABLES `grades` WRITE;
/*!40000 ALTER TABLE `grades` DISABLE KEYS */;
INSERT INTO `grades` VALUES (2,3,1,'2022-01-10',57),(2,2,1,'2022-01-11',58),(2,7,1,'2022-01-12',59),(2,4,2,'2022-06-10',68),(2,7,2,'2022-06-10',69),(2,2,2,'2022-06-10',60),(2,11,2,'2022-06-10',94);
/*!40000 ALTER TABLE `grades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sessions`
--

DROP TABLE IF EXISTS `sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sessions` (
  `DirectionId` int DEFAULT NULL,
  `ExamSessionNum` int DEFAULT NULL,
  `DisciplineId` int DEFAULT NULL,
  KEY `DirectionId` (`DirectionId`),
  KEY `DisciplineId` (`DisciplineId`),
  CONSTRAINT `sessions_ibfk_1` FOREIGN KEY (`DirectionId`) REFERENCES `directions` (`DirectionId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sessions_ibfk_2` FOREIGN KEY (`DisciplineId`) REFERENCES `disciplines` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sessions`
--

LOCK TABLES `sessions` WRITE;
/*!40000 ALTER TABLE `sessions` DISABLE KEYS */;
INSERT INTO `sessions` VALUES (1,1,3),(1,1,2),(1,1,7),(1,2,4),(1,2,7),(1,2,2),(1,2,11),(1,3,4),(1,3,6),(1,3,12),(1,3,11),(1,3,2),(1,4,10),(1,4,1),(1,5,13),(1,5,14),(1,3,15),(1,3,19),(1,7,16),(1,7,17),(1,8,44),(2,8,44),(3,8,44),(2,1,18),(2,1,4),(2,1,7),(2,1,2),(2,2,4),(2,2,11),(2,2,2),(2,3,19),(2,3,15),(2,3,20),(2,3,21),(2,4,1),(2,4,22),(2,4,6),(2,4,13),(2,4,23),(2,5,24),(2,5,25),(2,6,25),(2,7,27),(2,7,28),(2,7,29),(3,1,7),(3,1,9),(3,1,30),(3,1,2),(3,2,31),(3,2,22),(3,2,2),(3,3,9),(3,3,32),(3,3,6),(3,4,11),(3,4,1),(3,4,33),(3,4,34),(3,5,35),(3,5,15),(3,5,37),(3,6,36),(3,6,38),(3,7,40),(3,7,42),(3,7,43);
/*!40000 ALTER TABLE `sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `LastName` varchar(30) DEFAULT NULL,
  `FirstName` varchar(20) DEFAULT NULL,
  `Patronim` varchar(20) DEFAULT NULL,
  `Birthday` date DEFAULT NULL,
  `Address` varchar(100) DEFAULT NULL,
  `GroupNum` int DEFAULT NULL,
  `PhoneNum` char(11) DEFAULT NULL,
  `AdmissionDate` date DEFAULT NULL,
  `isExpelled` tinyint(1) DEFAULT '0',
  `Document` char(10) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `PhoneNum` (`PhoneNum`),
  UNIQUE KEY `PhoneNum_2` (`PhoneNum`),
  UNIQUE KEY `Document` (`Document`),
  KEY `GroupNum` (`GroupNum`),
  CONSTRAINT `students_ibfk_2` FOREIGN KEY (`GroupNum`) REFERENCES `academicgroups` (`GroupNum`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES (1,'Иванов','Рамиль','Иванович','2003-01-10','ул. Красной Позиции, 2',111,'89855555555','2021-08-21',0,'9214111111'),(2,'Брахманов','Александр','Александрович','2003-02-10','ул. Красной Позиции, 2',112,'89175555555','2022-08-21',0,'9215111111'),(10,'Рябунов','Геннадий','Викторович','1999-08-17','ул. Кремлевская, 18',NULL,'86547895412','2022-08-21',1,'4875456178'),(11,'Пешков','Алексей','Максимович','2000-03-28','ул. Горького, 2',231,'85554786987','2022-08-24',0,'6548341748'),(12,'Толстой','Лев','Николаевич','2003-09-09','ул. Некрасова, 44',231,'87414546258','2021-08-21',0,'9812458746'),(13,'Лермонтов','Михаил','Юрьевич','2002-10-15','ул. Пушкина, 71',112,'85472365412','2021-08-21',0,'6578452147');
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
