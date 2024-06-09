/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.154.101
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : 192.168.154.101:3306
 Source Schema         : tree3life

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 09/06/2024 08:40:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog`  (
  `id` int(11) NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '博客名称',
  `uploader` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上传者',
  `state` int(2) NULL DEFAULT NULL COMMENT '当前博客的状态\r\n0：默认值\r\n-1：删除\r\n1：处于回收站',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '博客内容',
  `category_id` int(11) NULL DEFAULT NULL COMMENT '文件分类id',
  `deleted` tinyint(4) NULL DEFAULT 0,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '博客' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of blog
-- ----------------------------
INSERT INTO `blog` VALUES (1, NULL, NULL, NULL, '博客内容xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int(11) NOT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '父级分类id',
  `type` int(3) NULL DEFAULT NULL COMMENT '1：blog分类；2：好友分类；3：群聊分类',
  `deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `delete_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '博客，好友、群聊 分类' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of category
-- ----------------------------

-- ----------------------------
-- Table structure for chat_friends
-- ----------------------------
DROP TABLE IF EXISTS `chat_friends`;
CREATE TABLE `chat_friends`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `category_id` int(11) NULL DEFAULT NULL COMMENT '用户对好友进行的分类id',
  `remark_name` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '对好友的备注名称',
  `friend_id` int(11) NULL DEFAULT NULL COMMENT '好友的id',
  `deleted` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '朋友\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_friends
-- ----------------------------
INSERT INTO `chat_friends` VALUES (1, 1, NULL, NULL, 2, 0, NULL, NULL, NULL);
INSERT INTO `chat_friends` VALUES (2, 2, NULL, NULL, 1, 0, NULL, NULL, NULL);
INSERT INTO `chat_friends` VALUES (3, 1, NULL, NULL, 3, 0, NULL, NULL, NULL);
INSERT INTO `chat_friends` VALUES (4, 2, NULL, NULL, 3, 0, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for chat_group
-- ----------------------------
DROP TABLE IF EXISTS `chat_group`;
CREATE TABLE `chat_group`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `group_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '群聊名称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像链接',
  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '简介',
  `creator` int(11) NULL DEFAULT 0 COMMENT '创建人id',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '被删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聊天群的信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_group
-- ----------------------------
INSERT INTO `chat_group` VALUES (1, '群聊a', NULL, '简介1', 1, 0, '2024-05-21 08:50:03', '2024-04-30 06:40:55', NULL);
INSERT INTO `chat_group` VALUES (2, '群聊b', NULL, '简介2', 1, 0, '2024-05-22 16:17:02', '2024-04-30 06:40:49', NULL);

-- ----------------------------
-- Table structure for chat_group_member
-- ----------------------------
DROP TABLE IF EXISTS `chat_group_member`;
CREATE TABLE `chat_group_member`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NULL DEFAULT NULL COMMENT '群id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '成员id',
  `remark_name` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '该成员对群聊的备注',
  `deleted` tinyint(3) NULL DEFAULT 0,
  `deleted_time` datetime NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '维护群聊 与 成员之间的关系\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_group_member
-- ----------------------------
INSERT INTO `chat_group_member` VALUES (1, 1, 1, NULL, 0, NULL, '2024-05-21 08:50:26', '2024-05-21 08:50:29');
INSERT INTO `chat_group_member` VALUES (2, 1, 2, NULL, 0, NULL, '2024-05-21 08:50:43', '2024-05-21 08:50:45');
INSERT INTO `chat_group_member` VALUES (3, 2, 1, NULL, 0, NULL, '2024-05-22 16:18:08', '2024-05-22 16:18:11');
INSERT INTO `chat_group_member` VALUES (4, 2, 3, NULL, 0, NULL, '2024-05-22 16:18:13', '2024-05-22 16:18:17');

-- ----------------------------
-- Table structure for chat_history
-- ----------------------------
DROP TABLE IF EXISTS `chat_history`;
CREATE TABLE `chat_history`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `command` int(3) NULL DEFAULT NULL COMMENT '消息类型，用于区分消息类型：具体参考command类（command、to确定唯一一条消息）',
  `state` int(3) NULL DEFAULT NULL COMMENT '消息状态',
  `from` int(11) NULL DEFAULT NULL COMMENT '发件人id',
  `to` int(11) NULL DEFAULT NULL COMMENT '收件人id/群id',
  `content_type` int(3) NULL DEFAULT NULL COMMENT '内容类型',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息内容，如果是文件类型，本字段存储该文件在文件服务器中的地址',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间、发送时间',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '送达时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `deleted_time` datetime NULL DEFAULT NULL COMMENT '逻辑删除时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 106 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聊天历史记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_history
-- ----------------------------
INSERT INTO `chat_history` VALUES (1, 11, NULL, 1, 2, NULL, 'a', '2024-05-15 00:31:15', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (2, 11, NULL, 2, 1, NULL, 'b', '2024-05-20 01:31:19', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (3, 11, NULL, 1, 2, NULL, 'c', '2024-05-20 02:31:27', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (4, 11, NULL, 1, 2, NULL, 'd', '2024-05-21 00:31:35', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (5, 11, NULL, 2, 1, NULL, 'e', '2024-05-22 04:31:42', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (6, 101, NULL, 1, 1, NULL, 'q', '2024-05-21 10:23:20', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (7, 101, NULL, 2, 1, NULL, 'w', '2024-05-21 10:23:24', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (8, 101, NULL, 1, 2, NULL, '群聊2，用户1', '2024-05-22 16:19:29', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (9, 101, NULL, 3, 2, NULL, '群聊2，用户3', '2024-05-22 16:19:31', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (10, 11, NULL, 1, 3, NULL, '执', '2024-05-23 08:49:05', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (11, 11, NULL, 1, 3, NULL, '笔', '2024-05-15 08:49:09', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (12, 11, NULL, 3, 1, NULL, '画', '2024-05-11 08:49:13', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (13, 11, NULL, 1, 3, NULL, '青', '2024-05-08 08:49:27', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (14, 11, NULL, 3, 1, NULL, '眸', '2024-05-11 08:49:33', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (15, 11, 0, 2, 1, NULL, 'f', '2024-05-24 11:01:34', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (26, 11, 0, 2, 1, NULL, 'aaa', '2024-05-24 12:25:45', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (27, 11, 0, 2, 1, NULL, 'bbb', '2024-05-24 12:28:23', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (28, 11, 0, 2, 1, NULL, 'ccc', '2024-05-24 12:30:12', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (29, 11, 0, 2, 1, NULL, 'acx', '2024-05-24 12:38:03', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (30, 11, 0, 2, 1, NULL, '1', '2024-05-24 12:43:42', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (31, 11, 0, 1, 2, NULL, '2', '2024-05-24 12:43:44', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (32, 11, 0, 1, 2, NULL, '3', '2024-05-24 12:43:48', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (33, 11, 0, 2, 1, NULL, 'xxxxx', '2024-05-24 12:44:54', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (34, 11, 0, 1, 2, NULL, 'asddd', '2024-05-24 12:44:57', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (35, 11, 0, 2, 1, NULL, 'cccsasd', '2024-05-24 15:32:14', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (36, 11, 0, 2, 1, NULL, 'sdasdx', '2024-05-24 18:16:32', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (37, 11, 0, 2, 1, NULL, 'aaaacx', '2024-05-24 18:32:00', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (38, 11, 0, 1, 2, NULL, 'aaaaa', '2024-05-24 19:33:44', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (39, 11, 0, 1, 2, NULL, 'aaaaaaaaaaaaaaa', '2024-05-24 19:34:03', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (40, 11, 0, 2, 1, NULL, '执笔华清某', '2024-05-24 19:35:49', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (41, 11, 0, 2, 1, NULL, '执笔华清某xxxxx', '2024-05-24 19:36:04', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (42, 11, 0, 1, 2, NULL, 'aaaaaaxxxxxx', '2024-05-24 19:36:35', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (43, 11, 0, 2, 1, NULL, '聊天记录1', '2024-05-24 19:47:14', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (44, 11, 0, 2, 1, NULL, '上官2-->aaa1', '2024-05-24 20:09:41', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (45, 11, 0, 2, 1, NULL, NULL, '2024-05-24 20:10:50', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (46, 11, 0, 2, 1, NULL, '上官2-->aaa1', '2024-05-24 20:15:13', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (47, 11, 0, 2, 1, NULL, 'vvca', '2024-05-25 09:33:41', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (48, 11, 0, 1, 2, NULL, 'asdcssssssssssssssssssssss', '2024-05-25 09:37:01', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (49, 11, 0, 2, 1, NULL, 'ccccccc', '2024-05-25 10:20:05', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (50, 11, 0, 1, 2, NULL, 'aaa', '2024-05-25 10:20:11', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (51, 11, 0, 1, 2, NULL, 'csaa', '2024-05-25 10:26:51', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (52, 11, 0, 2, 1, NULL, 'cccc', '2024-05-25 10:26:54', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (53, 11, 0, 2, 1, NULL, '执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执', '2024-05-25 10:34:31', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (54, 11, 0, 2, 1, NULL, 'asdddddddddddd', '2024-05-25 10:42:28', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (55, 11, 0, 1, 2, NULL, '执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执执', '2024-05-25 11:20:41', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (56, 11, 0, 2, 1, NULL, '啊啊啊啊啊嗷嗷嗷嗷aaa', '2024-05-25 11:21:10', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (57, 11, 0, 2, 1, NULL, '张三', '2024-05-25 21:02:54', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (58, 11, 0, 2, 1, NULL, 'aaaaa', '2024-05-26 09:54:24', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (59, 11, 0, 1, 2, NULL, 'aaasssssssssss', '2024-05-26 17:03:23', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (60, 11, 0, 1, 2, NULL, 'aaaaaa', '2024-05-26 17:10:05', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (61, 11, 0, 1, 2, NULL, 'aaaaa', '2024-05-26 17:13:21', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (62, 11, 0, 1, 2, NULL, 'aaaa', '2024-05-26 17:28:15', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (63, 11, 0, 1, 2, NULL, 'aaaa', '2024-05-26 18:45:37', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (64, 11, 0, 1, 2, NULL, 'xxxxxxxxx', '2024-05-26 18:47:49', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (65, 11, 0, 1, 2, NULL, 'aaa', '2024-05-26 18:49:19', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (66, 11, 0, 1, 2, NULL, 'aaaxxxx', '2024-05-26 19:06:10', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (67, 11, 0, 1, 2, NULL, 'vcfxcfx', '2024-05-26 19:07:13', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (68, 11, 0, 1, 2, NULL, 'acca', '2024-05-26 19:08:27', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (69, 11, 0, 1, 2, NULL, 'aasd', '2024-05-26 19:09:28', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (70, 11, 0, 1, 2, NULL, 'xfsadf', '2024-05-26 19:16:24', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (71, 11, 0, 1, 2, NULL, 'xccc', '2024-05-26 19:34:51', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (72, 11, 0, 1, 2, NULL, 'xxxxxxxxxx', '2024-05-26 19:37:00', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (73, 11, 0, 1, 2, NULL, 'aadd', '2024-05-26 19:38:17', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (74, 11, 0, 1, 2, NULL, 'aasd', '2024-05-26 19:39:49', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (75, 11, 0, 1, 2, NULL, 'aaaaa', '2024-05-26 20:08:21', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (76, 11, 0, 1, 2, NULL, 'asdfa', '2024-05-26 20:13:31', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (77, 11, 0, 1, 2, NULL, 'asdas', '2024-05-26 20:18:26', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (78, 11, 0, 1, 2, NULL, '水电费', '2024-05-26 20:24:11', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (79, 11, 0, 1, 2, NULL, 'aaaaaa', '2024-05-26 20:24:33', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (80, 11, 0, 1, 2, NULL, 'asdasd', '2024-05-26 20:26:12', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (81, 11, 0, 1, 2, NULL, 'axxxxxxxxc', '2024-05-28 09:30:31', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (82, 11, 2, 1, 2, NULL, '未读1', '2024-05-28 11:21:39', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (83, 11, 2, 1, 2, NULL, '未读2', '2024-05-28 13:32:31', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (84, -2, -1, NULL, -1, NULL, NULL, '2024-05-28 18:51:55', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (85, -2, -1, 2, -1, NULL, NULL, '2024-05-26 20:08:21', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (86, -2, -1, NULL, -1, NULL, NULL, '2024-05-28 19:03:15', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (87, -2, -1, NULL, -1, NULL, NULL, '2024-05-28 19:06:47', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (88, -2, -1, 2, -1, NULL, NULL, '2024-05-28 09:30:31', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (89, -2, -1, NULL, -1, NULL, '张三丰', '2024-05-28 19:17:08', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (90, -2, -1, NULL, -1, NULL, NULL, '2024-05-28 19:17:40', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (91, -2, -1, NULL, -1, NULL, NULL, '2024-05-28 19:18:10', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (92, -2, -1, NULL, -1, NULL, NULL, '2024-05-28 19:18:37', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (93, -2, -1, 1, -1, NULL, NULL, '2024-05-28 19:20:00', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (94, -2, -1, NULL, -1, NULL, NULL, '2024-05-28 19:20:14', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (95, -2, -1, NULL, -1, NULL, NULL, '2024-05-28 19:46:37', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (96, -2, -1, NULL, -1, NULL, NULL, '2024-05-28 19:48:30', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (97, -2, -1, 1, -1, NULL, NULL, '2024-05-28 19:57:31', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (98, 11, 2, 3, 2, NULL, 'xxxxxxxxxxxx', '2024-05-28 11:21:39', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (99, -2, 0, -999, -1, NULL, NULL, '2024-05-29 11:42:26', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (102, 11, 2, 1, 2, NULL, '张三n', '2024-06-01 11:03:27', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (103, 11, 2, 2, 1, NULL, 'xxxxxxxxxx啊水水', '2024-06-01 11:05:31', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (104, 11, 0, 2, 1, NULL, 'aaaa', '2024-06-06 15:11:59', NULL, NULL, NULL, 0);
INSERT INTO `chat_history` VALUES (105, -2, 0, 2, -1, NULL, NULL, '2024-06-06 15:12:04', NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for favorite
-- ----------------------------
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL COMMENT '用户id',
  `blog_id` int(11) NOT NULL COMMENT '博客id',
  `deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '收藏表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of favorite
-- ----------------------------

-- ----------------------------
-- Table structure for following
-- ----------------------------
DROP TABLE IF EXISTS `following`;
CREATE TABLE `following`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL COMMENT '用户id',
  `following_id` int(11) NOT NULL COMMENT '被关注用户id',
  `deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `deleted_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '关注' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of following
-- ----------------------------

-- ----------------------------
-- Table structure for history
-- ----------------------------
DROP TABLE IF EXISTS `history`;
CREATE TABLE `history`  (
  `id` int(11) NOT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `blog_id` int(11) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted_time` datetime NULL DEFAULT NULL,
  `deleted` tinyint(3) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '浏览记录\r\n完成\r\n  浏览记录功能\r\n  浏览量统计功能' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of history
-- ----------------------------

-- ----------------------------
-- Table structure for notify
-- ----------------------------
DROP TABLE IF EXISTS `notify`;
CREATE TABLE `notify`  (
  `id` int(11) NOT NULL,
  `to` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `from` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知内容',
  `type` int(11) NULL DEFAULT NULL COMMENT '通知类型',
  `state` int(11) NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '各种通知' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notify
-- ----------------------------

-- ----------------------------
-- Table structure for sys_page
-- ----------------------------
DROP TABLE IF EXISTS `sys_page`;
CREATE TABLE `sys_page`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '页面、模块名称',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '页面功能描述',
  `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '页面路径',
  `component_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '前端组件的路径（例如：../pages/system/User或@/pages/system/User）',
  `weights` int(2) NULL DEFAULT 99 COMMENT '权重;用于排序；标识页面的排列次序；数字越大约靠后',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否被删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '\r\n父级节点{0：顶级节点;正数：父节点id；-1：非菜单页}',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限模块：页面、功能模块\r\n' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_page
-- ----------------------------
INSERT INTO `sys_page` VALUES (1, '系统模块', '用于管理员对系统进行相关配置', '/admin/sys', NULL, 99, 0, '2022-05-01 09:41:30', '2022-08-30 19:55:05', 0);
INSERT INTO `sys_page` VALUES (2, '角色管理', '分配角色等', '/admin/sys/role', NULL, 2, 0, '2022-05-01 09:42:13', '2022-08-30 19:55:15', 1);
INSERT INTO `sys_page` VALUES (3, '系统资源', '对系统中页面及接口权限的配置', '/admin/sys/perm', NULL, 1, 0, '2022-05-01 09:55:08', '2022-08-31 12:39:47', 1);
INSERT INTO `sys_page` VALUES (4, '用户管理', '用户管理', '/admin/sys/user', NULL, 3, 0, '2022-05-01 09:55:04', '2022-08-31 12:39:50', 1);
INSERT INTO `sys_page` VALUES (5, '出错了', '系统出错处理页', '/error', NULL, 99, 0, '2022-05-01 09:55:00', '2022-05-03 16:24:20', -1);
INSERT INTO `sys_page` VALUES (6, '首页', '首页', '/admin/home', NULL, 1, 0, '2022-08-30 19:56:13', '2022-08-30 19:56:16', 0);
INSERT INTO `sys_page` VALUES (7, '登录', '登录页', '/login', NULL, 99, 0, '2022-08-30 19:56:16', '2022-08-30 19:56:18', -1);
INSERT INTO `sys_page` VALUES (8, '业务模块', '业务模块', '/admin/business', NULL, 88, 0, '2022-08-30 19:56:19', '2022-11-05 20:02:48', 0);
INSERT INTO `sys_page` VALUES (9, '业务模块1', '业务模块1', '/admin/business/service1', NULL, 1, 0, '2022-08-30 19:57:35', '2022-11-05 20:02:51', 8);
INSERT INTO `sys_page` VALUES (10, '业务模块2', '业务模块2', '/admin/business/service2', NULL, 2, 0, '2022-08-30 19:57:32', '2022-11-05 20:02:54', 8);

-- ----------------------------
-- Table structure for sys_page_perm
-- ----------------------------
DROP TABLE IF EXISTS `sys_page_perm`;
CREATE TABLE `sys_page_perm`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `page_id` int(11) NULL DEFAULT NULL COMMENT '页面id',
  `perm_id` int(11) NULL DEFAULT NULL COMMENT '权限id',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限模块：角色资源、角色权限关联表;\r\n角色和权限、资源间也是一对多的关系，一个角色会关联多个权限、资源' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_page_perm
-- ----------------------------
INSERT INTO `sys_page_perm` VALUES (1, 4, 1, '2022-05-03 13:19:33', '2022-05-03 13:26:38', 0);
INSERT INTO `sys_page_perm` VALUES (2, 4, 2, '2022-05-03 13:19:36', '2022-05-03 13:26:39', 0);

-- ----------------------------
-- Table structure for sys_perm
-- ----------------------------
DROP TABLE IF EXISTS `sys_perm`;
CREATE TABLE `sys_perm`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `permission` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限名称',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限描述',
  `locked` tinyint(1) NULL DEFAULT 0 COMMENT '是否被锁定',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否被删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限模块：权限、资源表\r\n' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_perm
-- ----------------------------
INSERT INTO `sys_perm` VALUES (1, 'user:create', '用户创建信息', 0, 0, '2022-05-03 13:17:13', '2022-05-03 13:17:22');
INSERT INTO `sys_perm` VALUES (2, 'user:list', '查询用户信息', 0, 0, '2022-05-04 13:18:10', '2022-05-03 13:18:15');
INSERT INTO `sys_perm` VALUES (3, 'user:list', '查询所有用户信息', 1, 0, '2022-08-30 19:23:13', '2022-08-30 19:23:13');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `locked` tinyint(1) NULL DEFAULT 0 COMMENT '角色是否被锁定',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '角色是否被删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限模块：角色表\r\n' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'admin', '管理员', '2022-11-07 20:02:24', '2022-11-07 20:02:26', 0, 0);
INSERT INTO `sys_role` VALUES (2, '游客', '未登录者', '2022-11-07 20:02:27', '2022-11-07 20:02:29', 0, 0);
INSERT INTO `sys_role` VALUES (3, '普通', '已登陆', '2022-11-07 20:02:31', '2022-11-07 20:02:32', 0, 0);
INSERT INTO `sys_role` VALUES (4, 'spring-security', 'ROLE_user', NULL, '2022-11-07 20:03:15', 0, 0);

-- ----------------------------
-- Table structure for sys_role_page
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_page`;
CREATE TABLE `sys_role_page`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色id',
  `page_id` int(11) NULL DEFAULT NULL COMMENT '页面id',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限模块：角色资源、角色权限关联表;\r\n角色和权限、资源间也是一对多的关系，一个角色会关联多个权限、资源' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_page
-- ----------------------------
INSERT INTO `sys_role_page` VALUES (1, 1, 1, NULL, '2022-05-03 14:02:00', 0);
INSERT INTO `sys_role_page` VALUES (2, 1, 2, NULL, '2022-05-03 14:02:00', 0);
INSERT INTO `sys_role_page` VALUES (3, 1, 3, NULL, '2022-05-03 14:02:01', 0);
INSERT INTO `sys_role_page` VALUES (4, 1, 4, NULL, '2022-05-03 14:02:13', 0);
INSERT INTO `sys_role_page` VALUES (5, 1, 5, NULL, '2022-05-03 14:02:15', 0);
INSERT INTO `sys_role_page` VALUES (6, 1, 6, NULL, '2022-05-03 14:02:16', 0);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色id',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限模块：用户角色关联表\r\n描述指定用户与角色间的依赖关系。其中用户表与角色表是一对多的关系，一个用户可以拥有多个角色' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1, NULL, NULL, 0);
INSERT INTO `sys_user_role` VALUES (2, 1, 2, NULL, NULL, 0);
INSERT INTO `sys_user_role` VALUES (3, 1, 3, NULL, NULL, 0);
INSERT INTO `sys_user_role` VALUES (4, 2, 2, NULL, NULL, 0);
INSERT INTO `sys_user_role` VALUES (5, 7, 4, NULL, '2022-05-20 22:38:48', 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像链接',
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `age` int(3) NULL DEFAULT NULL COMMENT '年龄',
  `gender` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `phone_linked` tinyint(1) NULL DEFAULT NULL COMMENT '是否绑定手机号',
  `openid` int(11) NULL DEFAULT NULL COMMENT '微信openid',
  `wechat_linked` tinyint(1) NULL DEFAULT NULL COMMENT '是否绑定微信',
  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '简介',
  `locked` tinyint(1) NULL DEFAULT 0 COMMENT '被锁定',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '被删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  `payload` json NULL COMMENT '用于后期追加用户相关的字段；可通过mysql5.7以上对json字段进行处理',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限模块：用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (-1, '系统', '5cd165fa86794ae2136f1a0a882190e9', NULL, '系统', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, '2024-05-20 11:56:21', '2024-04-29 05:27:16', NULL, NULL);
INSERT INTO `user` VALUES (1, 'aaa', '73f68f52d534ed300aaab14b464b5303', 'https://tree3.oss-cn-hangzhou.aliyuncs.com/favor/caticonO.png', '赵消息', 18, '男', NULL, NULL, NULL, NULL, NULL, 0, 0, '2022-04-28 20:23:12', '2024-05-02 01:49:01', NULL, NULL);
INSERT INTO `user` VALUES (2, '上官', '73f68f52d534ed300aaab14b464b5303', 'https://tree3.oss-cn-hangzhou.aliyuncs.com/md/photo/1716553547-aaa.jpg', '上官燕', 19, '女', NULL, NULL, NULL, NULL, '执笔画清眸啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊', 1, 0, '2022-05-03 11:22:50', '2024-05-02 01:49:05', NULL, NULL);
INSERT INTO `user` VALUES (3, '婉儿', '5cd165fa86794ae2136f1a0a882190e9', NULL, '婉儿', 16, '女', NULL, NULL, NULL, NULL, NULL, 1, 0, '2022-05-03 11:22:53', '2024-04-27 01:48:22', NULL, NULL);
INSERT INTO `user` VALUES (4, '执笔画清眸', '5cd165fa86794ae2136f1a0a882190e9', NULL, '白玲', 18, '女', NULL, NULL, NULL, NULL, NULL, 1, 0, '2022-05-03 11:22:56', '2022-07-26 15:13:41', NULL, NULL);
INSERT INTO `user` VALUES (5, '东方', '5cd165fa86794ae2136f1a0a882190e9', NULL, '东方朔', 26, '男', NULL, NULL, NULL, NULL, NULL, 1, 0, '2022-05-03 11:22:59', '2022-07-26 15:13:39', NULL, NULL);
INSERT INTO `user` VALUES (6, 'bbb', '73f68f52d534ed300aaab14b464b5303', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, NULL, '2024-05-02 03:00:51', NULL, NULL);
INSERT INTO `user` VALUES (7, 'ccc', '73f68f52d534ed300aaab14b464b5303', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, NULL, '2024-05-02 03:00:51', NULL, NULL);
INSERT INTO `user` VALUES (8, 'ddd', '73f68f52d534ed300aaab14b464b5303', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, NULL, '2024-05-02 03:00:52', NULL, NULL);
INSERT INTO `user` VALUES (9, 'eee', '73f68f52d534ed300aaab14b464b5303', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, NULL, '2024-05-02 03:00:52', NULL, NULL);
INSERT INTO `user` VALUES (10, 'fff', '73f68f52d534ed300aaab14b464b5303', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, NULL, '2024-05-02 03:00:53', NULL, NULL);
INSERT INTO `user` VALUES (11, 'ggg', '73f68f52d534ed300aaab14b464b5303', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, NULL, '2024-05-02 03:00:54', NULL, NULL);

-- ----------------------------
-- View structure for role_perm
-- ----------------------------
DROP VIEW IF EXISTS `role_perm`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `role_perm` AS select distinct `user`.`id` AS `user_id`,`user`.`username` AS `username`,`user`.`password` AS `password`,`user`.`name` AS `name`,`user`.`age` AS `age`,`user`.`gender` AS `gender`,`user`.`locked` AS `locked`,`role`.`id` AS `role_id`,`role`.`role` AS `role`,`role`.`description` AS `role_desc`,`page`.`title` AS `title`,`page`.`description` AS `page_desc`,`page`.`path` AS `path`,`page`.`weights` AS `weights`,`page`.`parent_id` AS `parent_id`,`perm`.`permission` AS `permission`,`perm`.`description` AS `description` from ((((((`user` left join `sys_user_role` `ur` on(((`user`.`id` = `ur`.`user_id`) and (`user`.`deleted` = 0) and (`ur`.`deleted` = 0)))) left join `sys_role` `role` on(((`role`.`id` = `ur`.`role_id`) and (`role`.`deleted` = 0)))) left join `sys_role_page` `rp` on(((`role`.`id` = `rp`.`role_id`) and (`rp`.`deleted` = 0)))) left join `sys_page` `page` on(((`rp`.`page_id` = `page`.`id`) and (`page`.`deleted` = 0)))) left join `sys_page_perm` `pp` on(((`page`.`id` = `pp`.`page_id`) and (`pp`.`deleted` = 0)))) left join `sys_perm` `perm` on(((`perm`.`id` = `pp`.`perm_id`) and (`perm`.`deleted` = 0))));

SET FOREIGN_KEY_CHECKS = 1;
