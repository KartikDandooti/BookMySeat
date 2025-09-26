CREATE TABLE IF NOT EXISTS `floor` (
  `building_id` int NOT NULL,
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `floor_name` varchar(25) NOT NULL,
  FOREIGN KEY (`building_id`) REFERENCES `building` (`id`),
  CHECK (`floor_name` IS NOT NULL AND LENGTH(`floor_name`) > 0)
);