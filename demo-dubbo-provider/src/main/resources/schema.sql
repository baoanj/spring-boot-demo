DROP TABLE IF EXISTS `books`;
CREATE TABLE `books` (
	`book_id` varchar(50) PRIMARY KEY,
	`book_name` varchar(30) NOT NULL,
	`book_page` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;