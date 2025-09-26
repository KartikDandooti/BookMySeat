CREATE TABLE IF NOT EXISTS `shift_details` (
  `end_time` time NOT NULL,
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `start_time` time NOT NULL,
  `created_date` datetime NOT NULL,
  `modified_date` datetime DEFAULT null,
  `shift_name` varchar(20) NOT NULL,
  
  CHECK (`shift_name` IS NOT NULL AND LENGTH(`shift_name`) > 0)
);