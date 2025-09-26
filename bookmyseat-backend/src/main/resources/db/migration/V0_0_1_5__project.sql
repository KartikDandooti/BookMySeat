CREATE TABLE IF NOT EXISTS `project` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `start_date` date NOT NULL,
  `created_date` datetime NOT NULL,
  `modified_date` datetime DEFAULT null,
  `project_name` varchar(50) NOT NULL,
  CHECK (`project_name` IS NOT NULL AND LENGTH(`project_name`) > 0)
);