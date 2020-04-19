/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80015
 Source Host           : localhost:3306
 Source Schema         : raa_system

 Target Server Type    : MySQL
 Target Server Version : 80015
 File Encoding         : 65001

 Date: 17/04/2020 16:28:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for academy
-- ----------------------------
DROP TABLE IF EXISTS `academy`;
CREATE TABLE `academy`  (
  `academy_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '学院id',
  `academy_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学院名称',
  PRIMARY KEY (`academy_id`, `academy_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of academy
-- ----------------------------
INSERT INTO `academy` VALUES (1, '信息科学与工程学院');
INSERT INTO `academy` VALUES (2, '电气及其自动化');
INSERT INTO `academy` VALUES (9, '机械及其自动化');
INSERT INTO `academy` VALUES (10, '软件学院');

-- ----------------------------
-- Table structure for admin_info
-- ----------------------------
DROP TABLE IF EXISTS `admin_info`;
CREATE TABLE `admin_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `aid` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `major_id` int(11) NULL DEFAULT NULL,
  `state` int(2) NULL DEFAULT 1 COMMENT '当前管理员的状态（是否可以参与管理）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `userID`(`aid`) USING BTREE,
  INDEX `majorID`(`major_id`) USING BTREE,
  CONSTRAINT `majorID` FOREIGN KEY (`major_id`) REFERENCES `major` (`major_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `userID` FOREIGN KEY (`aid`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_info
-- ----------------------------
INSERT INTO `admin_info` VALUES (9, '33333', 3, 1);
INSERT INTO `admin_info` VALUES (12, '55555', 3, 1);
INSERT INTO `admin_info` VALUES (21, '77777', 3, 1);
INSERT INTO `admin_info` VALUES (30, '44444', 3, 1);
INSERT INTO `admin_info` VALUES (38, '22222', 1, 1);
INSERT INTO `admin_info` VALUES (39, '22222', 2, 1);
INSERT INTO `admin_info` VALUES (40, '22222', 3, 1);
INSERT INTO `admin_info` VALUES (41, '22222', 9, 1);

-- ----------------------------
-- Table structure for class
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class`  (
  `class_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '学生班级id',
  `class_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学生班级名称',
  `grade_id` int(11) NOT NULL COMMENT '所属年级',
  PRIMARY KEY (`class_id`, `class_name`) USING BTREE,
  INDEX `class_id`(`class_id`) USING BTREE,
  INDEX `grade_id`(`grade_id`) USING BTREE,
  CONSTRAINT `grade_id` FOREIGN KEY (`grade_id`) REFERENCES `grade` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of class
-- ----------------------------
INSERT INTO `class` VALUES (1, '1班', 1);
INSERT INTO `class` VALUES (2, '2班', 1);
INSERT INTO `class` VALUES (3, '1班', 2);
INSERT INTO `class` VALUES (12, '2班', 2);
INSERT INTO `class` VALUES (9, '1班', 5);
INSERT INTO `class` VALUES (11, '1班', 6);
INSERT INTO `class` VALUES (15, '2班', 9);
INSERT INTO `class` VALUES (16, '1班', 9);
INSERT INTO `class` VALUES (13, '2班', 11);
INSERT INTO `class` VALUES (14, '1班', 11);

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `course_id` int(11) NOT NULL COMMENT '课程id',
  `course_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程名称',
  PRIMARY KEY (`course_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (1, 'java程序设计与开发');
INSERT INTO `course` VALUES (2, '计算机组成基础');

-- ----------------------------
-- Table structure for course_class
-- ----------------------------
DROP TABLE IF EXISTS `course_class`;
CREATE TABLE `course_class`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_id` int(11) NOT NULL COMMENT '课程名称',
  `class_id` int(11) NOT NULL COMMENT '班级名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `course_id`(`course_id`) USING BTREE,
  INDEX `class_id3`(`class_id`) USING BTREE,
  CONSTRAINT `class_id3` FOREIGN KEY (`class_id`) REFERENCES `class` (`class_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `course_id` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_class
-- ----------------------------
INSERT INTO `course_class` VALUES (9, 1, 9);

-- ----------------------------
-- Table structure for course_teacher
-- ----------------------------
DROP TABLE IF EXISTS `course_teacher`;
CREATE TABLE `course_teacher`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_id` int(11) NOT NULL COMMENT '课程id',
  `teacher_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '教师id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `course_id1`(`course_id`) USING BTREE,
  INDEX `teacher_id3`(`teacher_id`) USING BTREE,
  CONSTRAINT `course_id1` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `teacher_id3` FOREIGN KEY (`teacher_id`) REFERENCES `teacher_info` (`tid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_teacher
-- ----------------------------
INSERT INTO `course_teacher` VALUES (1, 1, '12345');
INSERT INTO `course_teacher` VALUES (2, 2, '12345');

-- ----------------------------
-- Table structure for examination_class
-- ----------------------------
DROP TABLE IF EXISTS `examination_class`;
CREATE TABLE `examination_class`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `examination_id` int(11) NOT NULL,
  `class_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `examination_id2`(`examination_id`) USING BTREE,
  INDEX `class_id5`(`class_id`) USING BTREE,
  CONSTRAINT `class_id5` FOREIGN KEY (`class_id`) REFERENCES `class` (`class_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `examination_id2` FOREIGN KEY (`examination_id`) REFERENCES `examination_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of examination_class
-- ----------------------------
INSERT INTO `examination_class` VALUES (24, 5, 9);
INSERT INTO `examination_class` VALUES (30, 6, 9);

-- ----------------------------
-- Table structure for examination_course_teacher
-- ----------------------------
DROP TABLE IF EXISTS `examination_course_teacher`;
CREATE TABLE `examination_course_teacher`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `examination_id` int(11) NOT NULL COMMENT '考试id',
  `course_id` int(11) NOT NULL COMMENT '课程id',
  `teacher_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '教师id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `examination_id`(`examination_id`) USING BTREE,
  INDEX `teacher_id5`(`teacher_id`) USING BTREE,
  INDEX `course_id5`(`course_id`) USING BTREE,
  CONSTRAINT `course_id5` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `examination_id` FOREIGN KEY (`examination_id`) REFERENCES `examination_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `teacher_id5` FOREIGN KEY (`teacher_id`) REFERENCES `teacher_info` (`tid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of examination_course_teacher
-- ----------------------------
INSERT INTO `examination_course_teacher` VALUES (1, 5, 1, '12345');
INSERT INTO `examination_course_teacher` VALUES (2, 6, 1, '12345');

-- ----------------------------
-- Table structure for examination_info
-- ----------------------------
DROP TABLE IF EXISTS `examination_info`;
CREATE TABLE `examination_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '考试id',
  `examination_title` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '考试名称',
  `examination_main` json NULL COMMENT '考试内容',
  `sdate` datetime(6) NULL DEFAULT NULL,
  `edate` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of examination_info
-- ----------------------------
INSERT INTO `examination_info` VALUES (5, 'java第一次作业', '[{\"main\": \"相加问题相加问题相加问题相加问题相加问题相加问题相加问题相加问题相加问题相加问题相加问题相加问题相加问题相加问题相加问题相加问题\", \"title\": \"相加问题\", \"judgeRules\": [{\"name\": \"相加问题\", \"value\": \"100\"}], \"totalScores\": \"100\"}, {\"key\": 1585554972037, \"main\": \"相乘问题相乘问题相乘问题相乘问题相乘问题相乘问题相乘问题相乘问题相乘问题相乘问题相乘问题相乘问题相乘问题相乘问题相乘问题\", \"title\": \"相乘问题\", \"judgeRules\": [{\"name\": \"相乘问题1\", \"value\": \"10\"}, {\"key\": 1585554985315, \"name\": \"相乘问题2\", \"value\": \"90\"}], \"totalScores\": \"100\"}]', '2020-04-01 02:51:21.000000', '2020-04-09 04:14:26.000000');
INSERT INTO `examination_info` VALUES (6, 'java第二次考试', '[{\"main\": \"相见问题相见问题相见问题相见问题相见问题相见问题\", \"title\": \"相见问题\", \"judgeRules\": [{\"name\": \"相见问题\", \"value\": \"100\"}], \"totalScores\": \"100\"}]', '2020-03-25 11:08:48.000000', '2020-03-25 13:08:53.000000');

-- ----------------------------
-- Table structure for examination_student
-- ----------------------------
DROP TABLE IF EXISTS `examination_student`;
CREATE TABLE `examination_student`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `examination_id` int(11) NOT NULL,
  `answer` json NULL,
  `state` int(2) NULL DEFAULT 0,
  `comment` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `iscomment` int(2) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `student_id6`(`student_id`) USING BTREE,
  INDEX `examination_id3`(`examination_id`) USING BTREE,
  CONSTRAINT `examination_id3` FOREIGN KEY (`examination_id`) REFERENCES `examination_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `student_id6` FOREIGN KEY (`student_id`) REFERENCES `student_info` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of examination_student
-- ----------------------------
INSERT INTO `examination_student` VALUES (4, '16033', 5, '{\"code\": \"\", \"judge\": [], \"question\": {\"key\": 1585554972037, \"main\": \"相乘问题相乘问题相乘问题相乘问题相乘问题相乘问题相乘问题相乘问题相乘问题相乘问题相乘问题相乘问题相乘问题相乘问题相乘问题\", \"title\": \"相乘问题\", \"judgeRules\": [{\"name\": \"相乘问题1\", \"value\": \"10\"}, {\"key\": 1585554985315, \"name\": \"相乘问题2\", \"value\": \"90\"}], \"totalScores\": \"100\"}}', 1, '', 0);
INSERT INTO `examination_student` VALUES (8, '16033', 6, NULL, 0, '', 0);

-- ----------------------------
-- Table structure for grade
-- ----------------------------
DROP TABLE IF EXISTS `grade`;
CREATE TABLE `grade`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '年级id',
  `grade_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '年级名称',
  `major_id` int(11) NOT NULL COMMENT '所属专业的id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id`(`id`, `grade_name`, `major_id`) USING BTREE,
  INDEX `major_id`(`major_id`) USING BTREE,
  CONSTRAINT `major_id` FOREIGN KEY (`major_id`) REFERENCES `major` (`major_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of grade
-- ----------------------------
INSERT INTO `grade` VALUES (1, '2016级', 1);
INSERT INTO `grade` VALUES (2, '2018级', 1);
INSERT INTO `grade` VALUES (5, '2016级', 3);
INSERT INTO `grade` VALUES (6, '2019级', 1);
INSERT INTO `grade` VALUES (8, '2016级', 5);
INSERT INTO `grade` VALUES (9, '2016级', 2);
INSERT INTO `grade` VALUES (10, '2019级', 2);
INSERT INTO `grade` VALUES (11, '2016级', 9);
INSERT INTO `grade` VALUES (12, '2019级', 9);

-- ----------------------------
-- Table structure for homework_class
-- ----------------------------
DROP TABLE IF EXISTS `homework_class`;
CREATE TABLE `homework_class`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `homework_id` int(11) NOT NULL COMMENT '家庭作业id',
  `class_id` int(11) NOT NULL COMMENT '班级id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `homework_id`(`homework_id`) USING BTREE,
  INDEX `class_id4`(`class_id`) USING BTREE,
  CONSTRAINT `class_id4` FOREIGN KEY (`class_id`) REFERENCES `class` (`class_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `homework_id` FOREIGN KEY (`homework_id`) REFERENCES `homework_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of homework_class
-- ----------------------------
INSERT INTO `homework_class` VALUES (62, 32, 9);

-- ----------------------------
-- Table structure for homework_course_teacher
-- ----------------------------
DROP TABLE IF EXISTS `homework_course_teacher`;
CREATE TABLE `homework_course_teacher`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `homework_id` int(11) NOT NULL COMMENT '作业id',
  `course_id` int(11) NOT NULL COMMENT '课程id',
  `teacher_id` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '教师id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `course_id2`(`course_id`) USING BTREE,
  INDEX `homework_id3`(`homework_id`) USING BTREE,
  INDEX `teacher_id`(`teacher_id`) USING BTREE,
  CONSTRAINT `course_id2` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `homework_id3` FOREIGN KEY (`homework_id`) REFERENCES `homework_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `teacher_id` FOREIGN KEY (`teacher_id`) REFERENCES `teacher_info` (`tid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of homework_course_teacher
-- ----------------------------
INSERT INTO `homework_course_teacher` VALUES (10, 22, 1, '12345');
INSERT INTO `homework_course_teacher` VALUES (11, 23, 2, '12345');
INSERT INTO `homework_course_teacher` VALUES (12, 24, 1, '12345');
INSERT INTO `homework_course_teacher` VALUES (13, 25, 1, '12345');
INSERT INTO `homework_course_teacher` VALUES (19, 31, 1, '12345');
INSERT INTO `homework_course_teacher` VALUES (20, 32, 1, '12345');

-- ----------------------------
-- Table structure for homework_info
-- ----------------------------
DROP TABLE IF EXISTS `homework_info`;
CREATE TABLE `homework_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '作业编号',
  `title` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作业标题',
  `main` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作业内容',
  `sdate` datetime(6) NOT NULL COMMENT '作业发布时间',
  `edate` datetime(6) NOT NULL COMMENT '作业截止时间',
  `judge` json NULL COMMENT '评分标准',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of homework_info
-- ----------------------------
INSERT INTO `homework_info` VALUES (22, 'java第一次作业', '这是一次针对java的旅程\n', '2020-04-01 04:00:00.000000', '2020-04-30 04:00:00.000000', '[{\"name\": \"评分标准0\", \"value\": 40}, {\"key\": 1583464301325, \"name\": \"评分标准1\", \"value\": 20}, {\"key\": 1583464322749, \"name\": \"评分标准2\", \"value\": 40}]');
INSERT INTO `homework_info` VALUES (23, '计算机组成基础', 'asjdhasdajsidjias', '2020-03-03 04:00:00.000000', '2020-04-12 16:00:00.000000', '[{\"name\": \"评分标准0\", \"value\": 40}, {\"key\": 1583464301325, \"name\": \"评分标准1\", \"value\": 20}, {\"key\": 1583464322749, \"name\": \"评分标准2\", \"value\": 40}]');
INSERT INTO `homework_info` VALUES (24, 'java第二次作业', 'asdasdasdasdasdasdasdasd', '2020-03-03 04:00:00.000000', '2020-04-18 16:00:00.000000', '[{\"name\": \"评分标准0\", \"value\": 40}, {\"key\": 1583464301325, \"name\": \"评分标准1\", \"value\": 20}, {\"key\": 1583464322749, \"name\": \"评分标准2\", \"value\": 40}]');
INSERT INTO `homework_info` VALUES (25, 'java第三次作业', 'java第三次作业java第三次作业java第三次作业java第三次作业java第三次作业java第三次作业java第三次作业java第三次作业', '2020-03-01 04:00:00.000000', '2020-03-17 16:00:00.000000', '[{\"name\": \"评分标准0\", \"value\": 40}, {\"key\": 1583464301325, \"name\": \"评分标准1\", \"value\": 20}, {\"key\": 1583464322749, \"name\": \"评分标准2\", \"value\": 40}]');
INSERT INTO `homework_info` VALUES (31, '测试作业', 'adasdasdasd\'', '2020-03-31 04:00:00.000000', '2020-03-31 12:00:00.000000', '[{\"name\": \"sdfsf\", \"value\": 100}]');
INSERT INTO `homework_info` VALUES (32, 'java第n次作业', 'classValueclassValueclassValueclassValueclassValue', '2020-04-06 04:00:00.000000', '2020-04-23 04:00:00.000000', '[{\"name\": \"asdas\", \"value\": 100}]');

-- ----------------------------
-- Table structure for homework_student
-- ----------------------------
DROP TABLE IF EXISTS `homework_student`;
CREATE TABLE `homework_student`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `student_id` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学生id',
  `homework_id` int(11) NOT NULL COMMENT '作业id',
  `state` int(2) NOT NULL DEFAULT 0 COMMENT '作业状态',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学生提交作业的路径',
  `comment` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '教师评语',
  `iscomment` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否全部评测完毕',
  `judge` json NULL COMMENT '评测内容',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `student_id5`(`student_id`) USING BTREE,
  INDEX `homework_id5`(`homework_id`) USING BTREE,
  CONSTRAINT `homework_id5` FOREIGN KEY (`homework_id`) REFERENCES `homework_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `student_id5` FOREIGN KEY (`student_id`) REFERENCES `student_info` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of homework_student
-- ----------------------------
INSERT INTO `homework_student` VALUES (52, '16033', 32, 0, NULL, NULL, 0, NULL);

-- ----------------------------
-- Table structure for major
-- ----------------------------
DROP TABLE IF EXISTS `major`;
CREATE TABLE `major`  (
  `major_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '教师专业id',
  `major_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '教师专业名称',
  `academy_id` int(11) NOT NULL COMMENT '学院id',
  PRIMARY KEY (`major_id`, `major_name`) USING BTREE,
  INDEX `major_id`(`major_id`) USING BTREE,
  INDEX `academy_id`(`academy_id`) USING BTREE,
  CONSTRAINT `academy_id` FOREIGN KEY (`academy_id`) REFERENCES `academy` (`academy_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of major
-- ----------------------------
INSERT INTO `major` VALUES (1, '计算机科学与技术', 1);
INSERT INTO `major` VALUES (2, '测控专业与技术', 1);
INSERT INTO `major` VALUES (5, '人工智能', 1);
INSERT INTO `major` VALUES (6, '大数据', 1);
INSERT INTO `major` VALUES (3, '电气及其自动化', 2);
INSERT INTO `major` VALUES (8, '软件工程', 10);
INSERT INTO `major` VALUES (9, '计算机科学与技术(软件)', 10);

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` int(11) NOT NULL COMMENT 'id',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '整体导航',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路径',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件名称',
  `requireAuth` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '需要的权限等级',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '父级路由',
  `keepAlive` tinyint(1) NULL DEFAULT NULL COMMENT '是否保持存活',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, '/', NULL, NULL, '所有', NULL, NULL, 0);
INSERT INTO `menu` VALUES (2, '/', '/home', 'Home', '作业中心', '1', 1, 0);
INSERT INTO `menu` VALUES (3, '/', '/home', 'Home', '考试中心', '1', 1, 0);
INSERT INTO `menu` VALUES (4, '/sub/**', '/subhomework', 'SubHomework', '发布作业', '1', 2, 0);
INSERT INTO `menu` VALUES (5, '/check/teacher/**', '/check/teacher/homework', 'CheckHomework', '查看作业', '1', 2, 0);
INSERT INTO `menu` VALUES (6, '/check/student/**', '/check/student/homework', 'ScheckHomework', '查看作业(学生)', '1', 2, 0);
INSERT INTO `menu` VALUES (8, '/sub/**', '/subexamination', 'SubExamination', '发布考试', '1', 3, 0);
INSERT INTO `menu` VALUES (9, '/check/student/**', '/check/student/onlinetest', 'ScheckOnlineTest', '参加考试', '1', 3, 0);
INSERT INTO `menu` VALUES (10, '/check/teacher/**', '/check/teacher/onlinetest', 'CheckOnlineTest', '查看考试', '1', 3, 0);
INSERT INTO `menu` VALUES (11, '/', '/home', 'Home', '专业管理员管理', '1', 1, 0);
INSERT INTO `menu` VALUES (12, '/create/**', '/root', 'Admin', '创建专业管理员', '1', 11, 0);
INSERT INTO `menu` VALUES (13, '/', '/home', 'Home', '管理专业', '1', 1, 0);
INSERT INTO `menu` VALUES (14, '/management/**', '/management/admin', 'ManagementST', '管理教师和学生', '1', 13, 0);

-- ----------------------------
-- Table structure for menu_role
-- ----------------------------
DROP TABLE IF EXISTS `menu_role`;
CREATE TABLE `menu_role`  (
  `id` int(11) NOT NULL COMMENT 'id',
  `menu_id` int(11) NOT NULL COMMENT '目录id',
  `role_id` int(11) NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`, `menu_id`, `role_id`) USING BTREE,
  INDEX `menu_id`(`menu_id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  CONSTRAINT `menu_id` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu_role
-- ----------------------------
INSERT INTO `menu_role` VALUES (5, 4, 3);
INSERT INTO `menu_role` VALUES (6, 5, 3);
INSERT INTO `menu_role` VALUES (7, 6, 4);
INSERT INTO `menu_role` VALUES (9, 8, 3);
INSERT INTO `menu_role` VALUES (10, 9, 4);
INSERT INTO `menu_role` VALUES (11, 10, 3);
INSERT INTO `menu_role` VALUES (12, 12, 1);
INSERT INTO `menu_role` VALUES (13, 14, 2);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(11) NOT NULL COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限名称（依据spring-security）',
  `nameZh` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '具体名称',
  PRIMARY KEY (`id`, `name`, `nameZh`) USING BTREE,
  INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 'Role_root', '超级管理员');
INSERT INTO `role` VALUES (2, 'Role_admin', '专业管理员');
INSERT INTO `role` VALUES (3, 'Role_teacher', '教师');
INSERT INTO `role` VALUES (4, 'Role_student', '学生');

-- ----------------------------
-- Table structure for student_info
-- ----------------------------
DROP TABLE IF EXISTS `student_info`;
CREATE TABLE `student_info`  (
  `id` bigint(255) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学生id',
  `sclass_id` int(11) NOT NULL COMMENT '学生班级id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sid`(`sid`) USING BTREE,
  INDEX `class_id`(`sclass_id`) USING BTREE,
  CONSTRAINT `class_id` FOREIGN KEY (`sclass_id`) REFERENCES `class` (`class_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sid` FOREIGN KEY (`sid`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student_info
-- ----------------------------
INSERT INTO `student_info` VALUES (1, '16011', 1);
INSERT INTO `student_info` VALUES (2, '16012', 1);
INSERT INTO `student_info` VALUES (3, '16013', 2);
INSERT INTO `student_info` VALUES (4, '16033', 9);

-- ----------------------------
-- Table structure for teacher_info
-- ----------------------------
DROP TABLE IF EXISTS `teacher_info`;
CREATE TABLE `teacher_info`  (
  `id` bigint(255) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '教师id',
  `tmajor_id` int(11) NOT NULL COMMENT '教师专业',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `tid`(`tid`) USING BTREE,
  INDEX `tmajor_id`(`tmajor_id`) USING BTREE,
  CONSTRAINT `tid` FOREIGN KEY (`tid`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tmajor_id` FOREIGN KEY (`tmajor_id`) REFERENCES `major` (`major_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacher_info
-- ----------------------------
INSERT INTO `teacher_info` VALUES (1, '12345', 1);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学生id/教师id',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学生姓名/教师姓名/管理员id',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '$2a$10$YzHCc/BPLZ0LPgSDiYa/ROApYQrRUMJUayNWxCd5PXlfIuxk3yE7i' COMMENT '学生密码/教师密码/管理员密码',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `id`(`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `username`(`username`) USING BTREE COMMENT '通过username加快查询'
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '11111', 'root', '$2a$10$YzHCc/BPLZ0LPgSDiYa/ROApYQrRUMJUayNWxCd5PXlfIuxk3yE7i', NULL, NULL, NULL);
INSERT INTO `user` VALUES (2, '12345', 'teacher1', '$2a$10$YzHCc/BPLZ0LPgSDiYa/ROApYQrRUMJUayNWxCd5PXlfIuxk3yE7i', NULL, NULL, NULL);
INSERT INTO `user` VALUES (3, '16011', 'student138', '$2a$10$YzHCc/BPLZ0LPgSDiYa/ROApYQrRUMJUayNWxCd5PXlfIuxk3yE7i', NULL, NULL, NULL);
INSERT INTO `user` VALUES (4, '16012', 'student2', '$2a$10$YzHCc/BPLZ0LPgSDiYa/ROApYQrRUMJUayNWxCd5PXlfIuxk3yE7i', NULL, NULL, NULL);
INSERT INTO `user` VALUES (5, '16013', 'student381', '$2a$10$YzHCc/BPLZ0LPgSDiYa/ROApYQrRUMJUayNWxCd5PXlfIuxk3yE7i', NULL, NULL, NULL);
INSERT INTO `user` VALUES (6, '16033', 'student4', '$2a$10$YzHCc/BPLZ0LPgSDiYa/ROApYQrRUMJUayNWxCd5PXlfIuxk3yE7i', NULL, NULL, NULL);
INSERT INTO `user` VALUES (7, '22222', 'admin', '$2a$10$YzHCc/BPLZ0LPgSDiYa/ROApYQrRUMJUayNWxCd5PXlfIuxk3yE7i', NULL, '45431155@qq.com', '1843266487');
INSERT INTO `user` VALUES (9, '33333', 'admin1', '$2a$10$YzHCc/BPLZ0LPgSDiYa/ROApYQrRUMJUayNWxCd5PXlfIuxk3yE7i', NULL, '1312312.12', '1235345345');
INSERT INTO `user` VALUES (10, '44444', 'sdadasd', '$2a$10$YzHCc/BPLZ0LPgSDiYa/ROApYQrRUMJUayNWxCd5PXlfIuxk3yE7i', NULL, '3546383453', '1234567897');
INSERT INTO `user` VALUES (11, '55555', 'cvxcvxc', '$2a$10$YzHCc/BPLZ0LPgSDiYa/ROApYQrRUMJUayNWxCd5PXlfIuxk3yE7i', NULL, '12354678945', '123456486546');
INSERT INTO `user` VALUES (15, '77777', '厂长', '$2a$10$YzHCc/BPLZ0LPgSDiYa/ROApYQrRUMJUayNWxCd5PXlfIuxk3yE7i', NULL, '6453453@qq.com', '7351327637');
INSERT INTO `user` VALUES (16, '88888', '恭喜发财', '$2a$10$YzHCc/BPLZ0LPgSDiYa/ROApYQrRUMJUayNWxCd5PXlfIuxk3yE7i', NULL, '4534534@qq.com', '42453123123');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id4`(`role_id`) USING BTREE,
  INDEX `user_id0`(`user_id`) USING BTREE,
  CONSTRAINT `role_id4` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_id0` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, '11111', 1);
INSERT INTO `user_role` VALUES (2, '12345', 3);
INSERT INTO `user_role` VALUES (3, '16011', 4);
INSERT INTO `user_role` VALUES (4, '16012', 4);
INSERT INTO `user_role` VALUES (5, '16013', 4);
INSERT INTO `user_role` VALUES (6, '16033', 4);
INSERT INTO `user_role` VALUES (9, '33333', 2);
INSERT INTO `user_role` VALUES (10, '44444', 2);
INSERT INTO `user_role` VALUES (11, '55555', 2);
INSERT INTO `user_role` VALUES (12, '77777', 2);
INSERT INTO `user_role` VALUES (14, '22222', 2);

SET FOREIGN_KEY_CHECKS = 1;
