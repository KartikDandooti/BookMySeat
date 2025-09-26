CREATE TABLE IF NOT EXISTS `user_shifts` (
  `user_id` int NOT NULL,
  `shift_id` int NOT NULL,
  `created_date` datetime NOT NULL,
  PRIMARY KEY (`user_id`, `shift_id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  FOREIGN KEY (`shift_id`) REFERENCES `shift_details` (`id`)
);