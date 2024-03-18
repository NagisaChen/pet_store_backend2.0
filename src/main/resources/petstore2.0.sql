/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : petstore2.0

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 18/03/2024 19:51:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for db_account
-- ----------------------------
DROP TABLE IF EXISTS `db_account`;
CREATE TABLE `db_account`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户表主键，唯一身份标识',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名，由用户进行设置',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码，加密处理',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户电话号码',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户收货地址',
  `role` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户个人角色，注册时填充',
  `register_time` datetime(0) NOT NULL COMMENT '用户的注册时间，注册时填充',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_name`(`username`) USING BTREE COMMENT '用户名唯一',
  UNIQUE INDEX `unique_email`(`email`) USING BTREE COMMENT '用户的电子邮箱只能对应一个账号'
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_account
-- ----------------------------
INSERT INTO `db_account` VALUES (1, 'nagisa', '$2a$10$wNoKFSFMBgH7Tl374txHbO5UqVlv4BcqcztVaOkPh.fUTfuoZx8G.', 'nagisachen@163.com', NULL, 'user', '2024-03-10 22:29:28');

-- ----------------------------
-- Table structure for db_cart
-- ----------------------------
DROP TABLE IF EXISTS `db_cart`;
CREATE TABLE `db_cart`  (
  `cart_id` int NULL DEFAULT NULL,
  `user_id` int NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_cart
-- ----------------------------

-- ----------------------------
-- Table structure for db_cart_item
-- ----------------------------
DROP TABLE IF EXISTS `db_cart_item`;
CREATE TABLE `db_cart_item`  (
  `id` int NOT NULL COMMENT '用来唯一表示购物车中的每一项商品',
  `product_id` int NOT NULL COMMENT '用来表示商品种类',
  `cart_id` int NOT NULL COMMENT '购物车编号，表示在哪个购物车中',
  `quantity` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '库存',
  PRIMARY KEY (`product_id`, `id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_cart_item
-- ----------------------------

-- ----------------------------
-- Table structure for db_order
-- ----------------------------
DROP TABLE IF EXISTS `db_order`;
CREATE TABLE `db_order`  (
  `order_id` int NOT NULL COMMENT '订单的唯一标识符',
  `user_id` int NULL DEFAULT NULL COMMENT '用户标识符',
  `order_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单状态',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '订单创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '订单最后更新时间',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_order
-- ----------------------------

-- ----------------------------
-- Table structure for db_order_item
-- ----------------------------
DROP TABLE IF EXISTS `db_order_item`;
CREATE TABLE `db_order_item`  (
  `order_item_id` int NOT NULL,
  `order_id` int NULL DEFAULT NULL,
  `product_id` int NULL DEFAULT NULL,
  `quantity` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `price` decimal(10, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`order_item_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_order_item
-- ----------------------------

-- ----------------------------
-- Table structure for db_product
-- ----------------------------
DROP TABLE IF EXISTS `db_product`;
CREATE TABLE `db_product`  (
  `product_id` int NOT NULL COMMENT '产品Id',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品名',
  `decription` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品描述',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '产品单价',
  `inventory` int NULL DEFAULT NULL COMMENT '库存',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`product_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_product
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
