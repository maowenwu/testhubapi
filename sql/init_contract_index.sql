/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : 127.0.0.1:3306
 Source Schema         : contract_index

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : 65001

 Date: 04/05/2018 11:41:22
*/

drop database if exists contract_index;

create database contract_index;

use contract_index;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for contract_price_index
-- ----------------------------
DROP TABLE IF EXISTS `contract_price_index`;
CREATE TABLE `contract_price_index` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '价格指数ID',
  `index_symbol` varchar(20) NOT NULL COMMENT '指数交易币对',
  `index_price` decimal(36,18) NOT NULL COMMENT '指数价格',
  `input_time` datetime NOT NULL COMMENT '录入时间',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `is_weight_change` tinyint(4) NOT NULL COMMENT '权重是否变更 0:否 1:是',
  `last_time` datetime DEFAULT NULL COMMENT '最近一次变更时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for contract_price_index_calc_record
-- ----------------------------
DROP TABLE IF EXISTS `contract_price_index_calc_record`;
CREATE TABLE `contract_price_index_calc_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '价格指数ID',
  `exchange_id` bigint(20) unsigned NOT NULL COMMENT '交易所ID',
  `target_symbol` varchar(32) NOT NULL COMMENT '目标交易币对',
  `target_price` decimal(36,18) NOT NULL COMMENT '目标价格',
  `weight` decimal(10,2) NOT NULL COMMENT '权重数',
  `original_weight` decimal(10,2) NOT NULL COMMENT '原始权重数',
  `grab_time` datetime NOT NULL COMMENT '抓取时间',
  `input_time` datetime NOT NULL COMMENT '录入时间',
  `price_index_id` bigint(20) NOT NULL COMMENT '价格指数id',
  PRIMARY KEY (`id`),
  KEY `price_index_id_ind` (`price_index_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for contract_price_index_his
-- ----------------------------
DROP TABLE IF EXISTS `contract_price_index_his`;
CREATE TABLE `contract_price_index_his` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `exchange_id` bigint(20) unsigned NOT NULL COMMENT '交易所ID',
  `target_symbol` varchar(32) NOT NULL COMMENT '目标交易币对',
  `source_symbol` varchar(32) NOT NULL COMMENT '来源交易币对',
  `target_price` decimal(36,18) NOT NULL COMMENT '目标价格',
  `source_price` decimal(36,18) NOT NULL COMMENT '来源价格',
  `rate` decimal(36,18) NOT NULL COMMENT '汇率',
  `status` int(11) NOT NULL COMMENT '状态 1:有效 0:无效',
  `input_time` datetime NOT NULL COMMENT '录入时间',
  `origin` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57476 DEFAULT CHARSET=utf8;
-- ----------------------------
-- Table structure for exchange_index_weight_conf
-- ----------------------------
DROP TABLE IF EXISTS `exchange_index_weight_conf`;
CREATE TABLE `exchange_index_weight_conf` (
  `weight_id` bigint(20) unsigned zerofill NOT NULL AUTO_INCREMENT COMMENT '权重ID',
  `exchange_id` bigint(20) NOT NULL COMMENT '交易所ID',
  `index_symbol` varchar(20) NOT NULL COMMENT '指数交易币对',
  `source_symbol` varchar(20) NOT NULL COMMENT '来源交易币对',
  `exchange_symbol` varchar(20) NOT NULL COMMENT '交易所对应的交易币对',
  `weight` decimal(10,2) NOT NULL COMMENT '权重数',
  `is_grab_valid` smallint(6) NOT NULL COMMENT '是否抓取有效 1:有效 0:无效',
  `input_by` varchar(64) NOT NULL COMMENT '录入人员',
  `input_time` datetime NOT NULL COMMENT '录入时间',
  `updator` varchar(64) DEFAULT NULL COMMENT '更新人员',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`weight_id`),
  UNIQUE KEY `U_1` (`exchange_id`,`index_symbol`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of exchange_index_weight_conf
-- ----------------------------
BEGIN;
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000001, 1, 'BTC-USD', 'BTC-USD', 'btcusd', 25.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000002, 2, 'BTC-USD', 'BTC-USD', 'BTC-USD', 25.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000003, 3, 'BTC-USD', 'BTC-USD', 'tBTCUSD', 25.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000004, 5, 'BTC-USD', 'BTC-USD', 'btcusd', 25.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000005, 1, 'LTC-USD', 'LTC-USD', 'ltcusd', 25.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000006, 2, 'LTC-USD', 'LTC-USD', 'LTC-USD', 25.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000007, 3, 'LTC-USD', 'LTC-USD', 'tLTCUSD', 25.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000008, 4, 'LTC-USD', 'LTC-USD', 'LTCUSD', 25.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000009, 1, 'ETH-USD', 'ETH-USD', 'ethusd', 20.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000010, 2, 'ETH-USD', 'ETH-USD', 'ETH-USD', 20.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000011, 3, 'ETH-USD', 'ETH-USD', 'tETHUSD', 20.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000012, 4, 'ETH-USD', 'ETH-USD', 'ETHUSD', 20.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000013, 5, 'ETH-USD', 'ETH-USD', 'ethusd', 20.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000014, 3, 'ETC-USD', 'ETC-USD', 'tETCUSD', 25.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000015, 6, 'ETC-USD', 'ETC-BTC', 'etcbtc', 25.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000016, 7, 'ETC-USD', 'ETC-BTC', 'BTC_ETC', 25.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000017, 8, 'ETC-USD', 'ETC-BTC', 'BTC-ETC', 25.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000018, 3, 'BCH-USD', 'BCH-USD', 'tBCHUSD', 33.33, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000019, 6, 'BCH-USD', 'BCH-BTC', 'bchbtc', 33.33, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000020, 8, 'BCH-USD', 'BCH-BTC', 'BTC-BCC', 33.33, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000021, 3, 'XRP-USD', 'XRP-BTC', 'tXRPBTC', 25.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000022, 6, 'XRP-USD', 'XRP-BTC', 'xrpbtc', 25.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000023, 7, 'XRP-USD', 'XRP-BTC', 'BTC_XRP', 25.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000024, 8, 'XRP-USD', 'XRP-BTC', 'BTC-XRP', 25.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000025, 3, 'EOS-USD', 'EOS-USD', 'tEOSUSD', 50.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_index_weight_conf` VALUES (00000000000000000026, 6, 'EOS-USD', 'EOS-BTC', 'eosbtc', 50.00, 1, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for exchange_info
-- ----------------------------
DROP TABLE IF EXISTS `exchange_info`;
CREATE TABLE `exchange_info` (
  `exchange_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '交易所ID',
  `full_name` varchar(64) NOT NULL COMMENT '交易所名称',
  `short_name` varchar(32) NOT NULL COMMENT '交易所简称',
  `is_valid` smallint(6) NOT NULL COMMENT '是否有效 1:有效  0:无效',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `input_by` varchar(32) NOT NULL COMMENT '录入者',
  `input_time` datetime NOT NULL COMMENT '录入时间',
  `updator` varchar(32) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`exchange_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of exchange_info
-- ----------------------------
BEGIN;
INSERT INTO `exchange_info` VALUES (1, 'bitstamp', 'bitstamp', 1, NULL, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_info` VALUES (2, 'gdax', 'gdax', 1, NULL, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_info` VALUES (3, 'bitfinex', 'bitfinex', 1, NULL, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_info` VALUES (4, 'kraken', 'kraken', 1, NULL, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_info` VALUES (5, 'gemini', 'gemini', 1, NULL, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_info` VALUES (6, 'huobi', 'huobi', 1, NULL, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_info` VALUES (7, 'poloniex', 'poloniex', 1, NULL, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_info` VALUES (8, 'bittrex', 'bittrex', 1, NULL, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `exchange_info` VALUES (9, '中位数', 'median', 1, NULL, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for exchange_rate
-- ----------------------------
DROP TABLE IF EXISTS `exchange_rate`;
CREATE TABLE `exchange_rate` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `exchange_symbol` varchar(32) NOT NULL COMMENT '汇率交易币对',
  `exchange_rate` decimal(36,18) NOT NULL COMMENT '汇率',
  `input_time` datetime NOT NULL COMMENT '录入时间',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for exchange_rate_his
-- ----------------------------
DROP TABLE IF EXISTS `exchange_rate_his`;
CREATE TABLE `exchange_rate_his` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `exchange_id` bigint(20) unsigned NOT NULL COMMENT '交易所ID',
  `exchange_symbol` varchar(32) NOT NULL COMMENT '汇率交易币对',
  `exchange_rate` decimal(36,18) NOT NULL COMMENT '汇率',
  `exchange_time` datetime NOT NULL COMMENT '交易时间',
  `input_time` datetime NOT NULL COMMENT '录入时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for index_info
-- ----------------------------
DROP TABLE IF EXISTS `index_info`;
CREATE TABLE `index_info` (
  `id` bigint(20) NOT NULL COMMENT '指数id',
  `index_name` varchar(32) NOT NULL COMMENT '指数名称',
  `index_symbol` varchar(20) NOT NULL COMMENT '指数交易币对',
  `is_valid` smallint(6) NOT NULL COMMENT '是否有效 1:有效 0:无效',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `input_by` varchar(32) NOT NULL COMMENT '录入人员',
  `input_time` datetime NOT NULL COMMENT '录入时间',
  `updator` varchar(32) DEFAULT NULL COMMENT '更新人员',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of index_info
-- ----------------------------
BEGIN;
INSERT INTO `index_info` VALUES (1, 'BTC美元指数', 'BTC-USD', 1, NULL, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `index_info` VALUES (2, 'LTC美元指数', 'LTC-USD', 1, NULL, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `index_info` VALUES (3, 'ETH美元指数', 'ETH-USD', 1, NULL, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `index_info` VALUES (4, 'ETC美元指数', 'ETC-USD', 1, NULL, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `index_info` VALUES (5, 'BCH美元指数', 'BCH-USD', 1, NULL, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `index_info` VALUES (6, 'XRP美元指数', 'XRP-USD', 1, NULL, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
INSERT INTO `index_info` VALUES (7, 'EOS美元指数', 'EOS-USD', 1, NULL, 'mjy', '2018-04-19 18:16:18', NULL, NULL);
COMMIT;

drop table if exists contract_price_index_okex;

/*==============================================================*/
/* Table: contract_price_index_okex                             */
/*==============================================================*/
create table contract_price_index_okex
(
   id                   bigint(20) unsigned not null auto_increment comment '价格指数ID',
   index_symbol         varchar(20) not null comment '指数交易币对',
   index_price          decimal(36,18) not null comment '指数价格',
   input_time           datetime not null comment '录入时间',
   remark               varchar(128) comment '备注',
   primary key (id)
)
ENGINE=InnoDB AUTO_INCREMENT=427172 DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS = 1;
