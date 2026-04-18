-- MySQL dump 10.13  Distrib 9.5.0, for macos15 (arm64)
--
-- Host: localhost    Database: gestion_stock_inox
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.28-MariaDB

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
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `categorie` varchar(255) DEFAULT NULL,
  `code` varchar(255) NOT NULL,
  `designation` varchar(255) NOT NULL,
  `epaisseur` varchar(255) DEFAULT NULL,
  `matiere` varchar(255) DEFAULT NULL,
  `prix_achat` decimal(15,2) NOT NULL,
  `prix_vente` decimal(15,2) NOT NULL,
  `seuil_alerte` int(11) NOT NULL,
  `stock` int(11) NOT NULL,
  `unite` varchar(255) DEFAULT NULL,
  `fournisseur_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK3otu0qlvnsc9wsl3uv7mq8l9w` (`code`),
  KEY `FK4yq75afa4ln2e9pu3ol936j7w` (`fournisseur_id`),
  CONSTRAINT `FK4yq75afa4ln2e9pu3ol936j7w` FOREIGN KEY (`fournisseur_id`) REFERENCES `fournisseur` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article`
--

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;
INSERT INTO `article` VALUES (1,'phonnnne','A1','smart','2','jej',10.00,12.00,0,880,'hh',1),(5,'Tube','A2','Tube 1','','',14.00,16.00,4,85,'',2),(6,'Tube','A3','Tube 2','','',15.00,17.00,5,130,'',2);
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bon_livraison`
--

DROP TABLE IF EXISTS `bon_livraison`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bon_livraison` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_creation` datetime(6) DEFAULT NULL,
  `numero` varchar(255) DEFAULT NULL,
  `statut` enum('NON_PAYE','PARTIELLEMENT_PAYE','PAYE') DEFAULT NULL,
  `total` decimal(15,2) NOT NULL,
  `client_id` bigint(20) NOT NULL,
  `montant_paye` decimal(15,2) NOT NULL,
  `resteapayer` decimal(15,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6v44al6gkbhwvqcrj2y6b9fo0` (`numero`),
  KEY `FK2bo5o2mr5vee64d7sx241d47x` (`client_id`),
  CONSTRAINT `FK2bo5o2mr5vee64d7sx241d47x` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bon_livraison`
--

LOCK TABLES `bon_livraison` WRITE;
/*!40000 ALTER TABLE `bon_livraison` DISABLE KEYS */;
INSERT INTO `bon_livraison` VALUES (2,'2026-04-17 20:01:14.000000','BL-00002','PARTIELLEMENT_PAYE',735.00,2,700.00,35.00),(3,'2026-04-17 20:09:19.000000','BL-00003','NON_PAYE',85.00,1,0.00,0.00);
/*!40000 ALTER TABLE `bon_livraison` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `adresse` varchar(255) DEFAULT NULL,
  `nom` varchar(255) NOT NULL,
  `solde` decimal(15,2) NOT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
INSERT INTO `client` VALUES (1,'amana','Brahim',7085.00,'0777208828'),(2,'ana hna','Amine',35.00,'07772077');
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fournisseur`
--

DROP TABLE IF EXISTS `fournisseur`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fournisseur` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `adresse` varchar(255) DEFAULT NULL,
  `nom` varchar(255) NOT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `ville` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fournisseur`
--

LOCK TABLES `fournisseur` WRITE;
/*!40000 ALTER TABLE `fournisseur` DISABLE KEYS */;
INSERT INTO `fournisseur` VALUES (1,NULL,'ali',NULL,'Casablanca'),(2,'amana im','Amine','0777208828','marrakech');
/*!40000 ALTER TABLE `fournisseur` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ligne_bon_livraison`
--

DROP TABLE IF EXISTS `ligne_bon_livraison`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ligne_bon_livraison` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `prix_unitaire` decimal(15,2) NOT NULL,
  `quantite` int(11) DEFAULT NULL,
  `total` decimal(15,2) NOT NULL,
  `article_id` bigint(20) DEFAULT NULL,
  `bon_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2mm1uvyihv23ammur9hmp1k8h` (`article_id`),
  KEY `FK73om8ry5iji3ui1qix1883o44` (`bon_id`),
  CONSTRAINT `FK2mm1uvyihv23ammur9hmp1k8h` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`),
  CONSTRAINT `FK73om8ry5iji3ui1qix1883o44` FOREIGN KEY (`bon_id`) REFERENCES `bon_livraison` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ligne_bon_livraison`
--

LOCK TABLES `ligne_bon_livraison` WRITE;
/*!40000 ALTER TABLE `ligne_bon_livraison` DISABLE KEYS */;
INSERT INTO `ligne_bon_livraison` VALUES (2,16.00,15,240.00,5,2),(3,17.00,15,255.00,6,2),(4,12.00,20,240.00,1,2),(5,17.00,5,85.00,6,3);
/*!40000 ALTER TABLE `ligne_bon_livraison` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mouvement`
--

DROP TABLE IF EXISTS `mouvement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mouvement` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_mvt` datetime(6) DEFAULT NULL,
  `observation` varchar(255) DEFAULT NULL,
  `quantite` int(11) NOT NULL,
  `reference_document` varchar(255) DEFAULT NULL,
  `source` enum('ACHAT','AJUSTEMENT','RETOUR_CLIENT','RETOUR_FOURNISSEUR','VENTE') NOT NULL,
  `type` enum('ENTREE','SORTIE') NOT NULL,
  `article_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2y21p8fjund14mvwx4ounccrl` (`article_id`),
  CONSTRAINT `FK2y21p8fjund14mvwx4ounccrl` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mouvement`
--

LOCK TABLES `mouvement` WRITE;
/*!40000 ALTER TABLE `mouvement` DISABLE KEYS */;
INSERT INTO `mouvement` VALUES (2,'2026-04-17 18:58:29.000000','',4000,'','VENTE','SORTIE',1),(5,'2026-04-17 19:41:58.000000','Sortie via bon de livraison',100,'BL-00001','VENTE','SORTIE',1),(6,'2026-04-17 20:01:14.000000','Sortie via bon de livraison',15,'BL-00002','VENTE','SORTIE',5),(7,'2026-04-17 20:01:14.000000','Sortie via bon de livraison',15,'BL-00002','VENTE','SORTIE',6),(8,'2026-04-17 20:01:14.000000','Sortie via bon de livraison',20,'BL-00002','VENTE','SORTIE',1),(9,'2026-04-17 20:09:19.000000','Sortie via bon de livraison',5,'BL-00003','VENTE','SORTIE',6);
/*!40000 ALTER TABLE `mouvement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paiement`
--

DROP TABLE IF EXISTS `paiement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paiement` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_paiement` datetime(6) DEFAULT NULL,
  `mode` enum('CHEQUE','ESPECE','VIREMENT') DEFAULT NULL,
  `montant` decimal(15,2) NOT NULL,
  `observation` varchar(255) DEFAULT NULL,
  `reference` varchar(255) DEFAULT NULL,
  `client_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2x9kwbd40g3ic8j4n3v512j7h` (`client_id`),
  CONSTRAINT `FK2x9kwbd40g3ic8j4n3v512j7h` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paiement`
--

LOCK TABLES `paiement` WRITE;
/*!40000 ALTER TABLE `paiement` DISABLE KEYS */;
INSERT INTO `paiement` VALUES (4,'2026-04-17 22:18:49.000000','ESPECE',700.00,'','',2);
/*!40000 ALTER TABLE `paiement` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-18  3:40:00
