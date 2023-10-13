CREATE TABLE `eozf3fdt6zaw4jbg`.`members`
(
    `id` INT NOT NULL AUTO_INCREMENT,
    `login_id` VARCHAR(16) NOT NULL,
    `login_password` VARCHAR(20) NOT NULL,
    `email` VARCHAR(45) NOT NULL,
    `company_name` VARCHAR(20) NOT NULL,
    `phone_number` VARCHAR(15) NOT NULL,
    `term_of_use` TINYINT NOT NULL,
    `email_verified` TINYINT NOT NULL,
    `auth_code` VARCHAR(36) NOT NULL,
    `created_date_time` DATETIME NOT NULL,
    `updated_date_time` DATETIME NOT NULL,
    PRIMARY KEY (`id`)
);