CREATE TABLE IF NOT EXISTS `restricted_seat` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `project_id` int DEFAULT null,
  `user_id` int UNIQUE DEFAULT null,
  `created_date` datetime NOT NULL,
  `modified_date` datetime DEFAULT null,
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  FOREIGN KEY (`project_id`) REFERENCES `project` (`id`)
);