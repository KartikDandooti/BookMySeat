CREATE TABLE IF NOT EXISTS `password_token` (
  `user_id` int UNIQUE NOT NULL,
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `token` varchar(50) NOT NULL UNIQUE,
  `expiration_date` datetime NOT NULL,
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);