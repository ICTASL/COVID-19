CREATE DATABASE IF NOT EXISTS covid19_db;

use covid19_db;

--insert create statements below as needed

CREATE TABLE `hpb_notification` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NULL,
  `subtitle` VARCHAR(100) NULL,
  `source` VARCHAR(45) NULL,
  `message_en` VARCHAR(500) NULL,
  `message_si` VARCHAR(500) NULL,
  `message_ta` VARCHAR(500) NULL,
  `created` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`));

CREATE TABLE `epid_case` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `case_number` VARCHAR(100) NULL,
  `message_en` VARCHAR(500) NULL,
  `message_si` VARCHAR(500) NULL,
  `message_ta` VARCHAR(500) NULL,
  `created` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`));

CREATE TABLE `epid_location` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` DATETIME NULL,
  `from` DATETIME NULL,
  `to` DATETIME NULL,
  `location` VARCHAR(100) NULL,
  `case_id` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_epid_location_1_idx` (`case_id` ASC),
  CONSTRAINT `fk_epid_location_1`
    FOREIGN KEY (`case_id`)
    REFERENCES `epid_case` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `notification_message_type` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `description` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));
