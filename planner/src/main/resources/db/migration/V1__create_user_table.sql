DROP TABLE IF EXISTS user;
CREATE TABLE user(
                     id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                     first_name VARCHAR(45) NOT NULL,
                     last_name VARCHAR(45) NOT NULL,
                     email VARCHAR(45) UNIQUE NOT NULL,
                     phone_number VARCHAR(15) NOT NULL,
                     password VARCHAR(255) NOT NULL,
                     role ENUM('WORKER', 'ADMIN') NOT NULL
);