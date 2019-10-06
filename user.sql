/*
SQLyog Professional v12.09 (64 bit)
MySQL - 5.5.49 : Database - order_db_1
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`order_db_1` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `order_db_1`;

/*Table structure for table `t_order_1` */

DROP TABLE IF EXISTS `t_order_1`;

CREATE TABLE `t_order_1` (
  `order_id` bigint(20) NOT NULL COMMENT '订单',
  `price` decimal(10,2) NOT NULL COMMENT '订单价格',
  `user_id` bigint(20) NOT NULL COMMENT '下单价格',
  `status` varchar(50) NOT NULL COMMENT '订单状态',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `t_order_1` */

/*Table structure for table `t_order_2` */

DROP TABLE IF EXISTS `t_order_2`;

CREATE TABLE `t_order_2` (
  `order_id` bigint(20) NOT NULL COMMENT '订单',
  `price` decimal(10,2) NOT NULL COMMENT '订单价格',
  `user_id` bigint(20) NOT NULL COMMENT '下单价格',
  `status` varchar(50) NOT NULL COMMENT '订单状态',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `t_order_2` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
