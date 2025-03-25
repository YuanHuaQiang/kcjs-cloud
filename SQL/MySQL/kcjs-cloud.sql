CREATE DATABASE `kcjs-cloud` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE user_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    email VARCHAR(100) COMMENT '邮箱',
    age INT COMMENT '年龄',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    count INT NOT NULL COMMENT '购买数量',
    amount DECIMAL(10,2) NOT NULL COMMENT '订单金额',
    status INT DEFAULT 0 COMMENT '订单状态 0:创建中 1:已完成',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

CREATE TABLE storage (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '库存ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    total INT NOT NULL COMMENT '总库存',
    used INT NOT NULL COMMENT '已用库存',
    residue INT NOT NULL COMMENT '剩余库存'
);

CREATE TABLE  account (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '账户ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    total DECIMAL(10,2) NOT NULL COMMENT '总额度',
    used DECIMAL(10,2) NOT NULL COMMENT '已用额度',
    residue DECIMAL(10,2) NOT NULL COMMENT '剩余额度'
);


 CREATE TABLE IF NOT EXISTS `undo_log` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `branch_id` BIGINT(20) NOT NULL,
  `xid` VARCHAR(100) NOT NULL,
  `context` VARCHAR(128) NOT NULL,
  `rollback_info` LONGBLOB NOT NULL,
  `log_status` INT(11) NOT NULL,
  `log_created` DATETIME NOT NULL,
  `log_modified` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB;


select ui1_0.id,ui1_0.age,ui1_0.create_time,ui1_0.email,ui1_0.update_time,ui1_0.username from user_info ui1_0;


delete from   storage;
delete from  orders;
delete from   account;

select * from  storage;
select * from orders;
select * from  account;
select(select count(*) from  orders),(select count(*) from  account);

select * from orders where status = 0;

CREATE TABLE `orders_0` LIKE `orders`;
CREATE TABLE `orders_1` LIKE `orders`;
CREATE TABLE `orders_2` LIKE `orders`;
CREATE TABLE `orders_3` LIKE `orders`;

drop  table message_log;
CREATE TABLE `message_log` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT comment '消息ID，唯一标识',
    `message_body` TEXT NOT NULL COMMENT '消息内容，存储消息体',
    `status` TINYINT(2) DEFAULT 0 comment '状态 0: 待发送, 1: 发送中, 2: 发送成功, 3: 发送失败',
    `retry_count` TINYINT(3) DEFAULT 0 comment '重试次数',
    `create_time` BIGINT(20) comment '创建时间（UNIX 时间戳）',
    `update_time` BIGINT(20) comment '最后更新时间',
    `send_time` BIGINT(20) comment '消息发送时间',
    `error_message` VARCHAR(255) comment '错误信息，失败时记录',
    `message_type` VARCHAR(50) comment '消息类型（可选，例如：订单通知、支付成功等）',
    `correlation_id` VARCHAR(50) comment '关联ID（如订单ID等）',
    `priority` TINYINT(1) DEFAULT 0 comment '消息优先级 0: 普通, 1: 高',
    `is_processed` TINYINT(1) DEFAULT 0 comment '是否已处理 0: 否, 1: 是',
    INDEX `idx_status` (`status`),             -- 索引：按状态查询
    INDEX `idx_correlation_id` (`correlation_id`) -- 索引：按关联ID查询
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
