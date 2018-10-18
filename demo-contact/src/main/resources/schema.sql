DROP TABLE IF EXISTS `contacts`;
CREATE TABLE `contacts` (
	`contact_id` int AUTO_INCREMENT PRIMARY KEY,
	`full_name` varchar(30) NOT NULL,
	`phone_number` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
	`contact_id` int PRIMARY KEY,
	`province` varchar(20) NOT NULL,
	`city` varchar(20) NOT NULL,
	`detail` varchar(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `xyz_user`;
CREATE TABLE `xyz_user` (
	`user_id` varchar(50) PRIMARY KEY,
	`username` varchar(20) UNIQUE NOT NULL,
	`password` varchar(255) NOT NULL,
	`md5_salt` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `xyz_role`;
CREATE TABLE `xyz_role` (
	`role_name` varchar(20) PRIMARY KEY,
	`role_desc` varchar(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `xyz_auth`;
CREATE TABLE `xyz_auth` (
	`auth_name` varchar(20) PRIMARY KEY,
	`auth_desc` varchar(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `xyz_user_role`;
CREATE TABLE `xyz_user_role` (
    `user_id` varchar(50),
	`role_name` varchar(20),
    PRIMARY KEY (`user_id`, `role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `xyz_role_auth`;
CREATE TABLE xyz_role_auth (
	`role_name` varchar(20),
	`auth_name` varchar(20),
    PRIMARY KEY (`role_name`, `auth_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `xyz_role` (`role_name`, `role_desc`) VALUES ('vip', 'vip user');
INSERT INTO `xyz_role` (`role_name`, `role_desc`) VALUES ('user', 'ordinary user');

INSERT INTO `xyz_auth` (`auth_name`, `auth_desc`) VALUES ('contact:create', 'auth to create contact');
INSERT INTO `xyz_auth` (`auth_name`, `auth_desc`) VALUES ('contact:query', 'auth to query contacts');
INSERT INTO `xyz_auth` (`auth_name`, `auth_desc`) VALUES ('contact:delete', 'auth to query contacts');

INSERT INTO `xyz_role_auth` (`role_name`, `auth_name`) VALUES ('vip', 'contact:create');
INSERT INTO `xyz_role_auth` (`role_name`, `auth_name`) VALUES ('vip', 'contact:delete');
INSERT INTO `xyz_role_auth` (`role_name`, `auth_name`) VALUES ('vip', 'contact:query');
INSERT INTO `xyz_role_auth` (`role_name`, `auth_name`) VALUES ('user', 'contact:query');