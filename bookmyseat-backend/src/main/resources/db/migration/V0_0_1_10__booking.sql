CREATE TABLE IF NOT EXISTS `booking` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `seat_id` int NOT NULL,
  `user_id` int NOT NULL,
  `booking_range` ENUM ('DAILY', 'MONTHLY', 'WEEKLY') NOT NULL,
  `booking_status` bit(1) NOT NULL DEFAULT b'1',
  `verification_code` int NOT NULL,
  `created_date` datetime NOT NULL,
  `modified_date` datetime DEFAULT NULL,
  FOREIGN KEY (`seat_id`) REFERENCES `seat` (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);