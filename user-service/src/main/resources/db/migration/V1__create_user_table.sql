CREATE TABLE users (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    `name` varchar(100) NOT NULL,
    `surname` varchar(100) ,
    `email` varchar(255)  NOT NULL,
    `address` TEXT ,
    `alerting` TINYINT(1) NOT NULL DEFAULT 0 ,
    `energy_alerting_threshold` DOUBLE NOT NULL DEFAULT 0,
    `created_at` TIMESTAMP NOT NULL ,
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0,
    `modified_at` TIMESTAMP DEFAULT NULL,
    UNIQUE KEY uk_user_email(`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;