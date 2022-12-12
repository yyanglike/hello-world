DROP TABLE IF EXISTS YUN_RECORD;
CREATE TABLE `YUN_RECORD` (`id` BIGINT AUTO_INCREMENT PRIMARY KEY,`url` VARCHAR(512) NOT NULL,`key` VARCHAR(255) NOT NULL,`value` VARCHAR(4096) NOT NULL,`date_up` DATE,`date_in` DATE);
-- create index IDXNAME on `YUN_RECORD` (`url`,`key`);

