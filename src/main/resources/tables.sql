CREATE TABLE IF NOT EXISTS `t_car_stock`(
    `id` BIGINT(20) AUTO_INCREMENT COMMENT '',
    `car_model` VARCHAR(100) NOT NULL COMMENT 'car model',
    `stock` INTEGER(10) NOT NULL DEFAULT 0 COMMENT 'car stock',
    `status` TINYINT(1) DEFAULT 1 COMMENT 'status',
    `create_time` BIGINT(20) COMMENT 'create time',
    `update_time` BIGINT(20) COMMENT 'update time',
    PRIMARY KEY ( `id` ),
    UNIQUE KEY `UK_CAR_MODEL` (`car_model`)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT 'car stock table';

insert into t_car_stock (car_model, stock) values('Toyota Camry', 2);
insert into t_car_stock (car_model, stock) values('BMW 650', 2);

CREATE TABLE IF NOT EXISTS `t_booking_order`(
    `id` BIGINT(20) AUTO_INCREMENT COMMENT '',
    `user_id` VARCHAR(100) NOT NULL COMMENT 'user id',
    `status` TINYINT(1) DEFAULT 1 COMMENT 'status 1 success 2 failed 3 cancel 4 delete',
    `create_time` BIGINT(20)  COMMENT 'create time',
    `update_time` BIGINT(20)  COMMENT 'update time',
    PRIMARY KEY ( `id` ),
    INDEX IDX_USER_ID(`user_id`)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT 'car reverse order table';

CREATE TABLE IF NOT EXISTS `t_order_detail`(
    `id` BIGINT(20) AUTO_INCREMENT COMMENT '',
    `order_id` BIGINT(20) NOT NULL COMMENT 'reverse order id',
    `car_model` VARCHAR(100) NOT NULL COMMENT 'car model',
    `booking_num` INTEGER(10) NOT NULL DEFAULT 0 COMMENT 'booking num',
    `start_time` BIGINT(20) COMMENT 'booking start time',
    `end_time` BIGINT(20)  COMMENT 'booking end time',
    `status` TINYINT(1) DEFAULT 1 COMMENT 'status 1 success 2 cancel 3 delete',
    `create_time` BIGINT(20)  COMMENT 'create time',
    `update_time` BIGINT(20)  COMMENT 'update time',
    PRIMARY KEY ( `id` ),
    INDEX IDX_ORDER_ID(`order_id`)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT 'car reverse order detail table';

CREATE TABLE IF NOT EXISTS `t_order_log`(
    `id` BIGINT(20) AUTO_INCREMENT COMMENT '',
    `order_id` BIGINT(20) NOT NULL COMMENT 'booking order id',
    `car_model` VARCHAR(100) NOT NULL COMMENT 'car model',
    `booking_num` INTEGER(10) NOT NULL DEFAULT 0 COMMENT 'booking num',
    `start_time` BIGINT(20)  COMMENT 'reverse start time',
    `end_time` BIGINT(20)  COMMENT 'reverse end time',
    `version` INTEGER(20) DEFAULT 0 COMMENT 'the order version',
    `status` TINYINT(1) DEFAULT 1 COMMENT 'status 1 create 2 update 3 cancel 4 delete',
    `create_time` BIGINT(20)  COMMENT 'create time',
    `update_time` BIGINT(20)  COMMENT 'update time',
    PRIMARY KEY ( `id` ),
    INDEX IDX_ORDER_ID(`order_id`)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT 'car booking order operate version table';
