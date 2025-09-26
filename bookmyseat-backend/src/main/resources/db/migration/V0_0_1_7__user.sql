CREATE TABLE IF NOT EXISTS `user` (
  `employee_id` int UNIQUE NOT NULL,
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `phone_number` bigint NOT NULL,
  `project_id` int NOT NULL,
  `role_id` int NOT NULL,
  `registered_status` ENUM ('APPROVED', 'REJECTED', 'PENDING') DEFAULT 'PENDING' NOT NULL,
  `created_date` datetime NOT NULL,
  `modified_date` datetime DEFAULT null,
  `first_name` varchar(20) NOT NULL,
  `last_name` varchar(20) DEFAULT null,
  `email` varchar(50) UNIQUE NOT NULL,
  `password` varchar(255) NOT NULL,
  FOREIGN KEY (`project_id`) REFERENCES `project` (`id`),
  FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  
  CHECK (`first_name` IS NOT NULL AND LENGTH(`first_name`) > 0),
  CHECK (`last_name` IS NOT NULL AND LENGTH(`last_name`) > 0),
  CHECK (`email` IS NOT NULL AND LENGTH(`email`) > 0),
  CHECK (`password` IS NOT NULL AND LENGTH(`password`) > 0)
);