CREATE TABLE IF NOT EXISTS `holiday` (
  `holiday_date` date NOT NULL,
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `holiday_name` varchar(30) NOT NULL
);