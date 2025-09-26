CREATE TABLE IF NOT EXISTS `role` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `role_name` varchar(20) UNIQUE NOT NULL,
  CHECK (LENGTH(`role_name`) <= 20)
);