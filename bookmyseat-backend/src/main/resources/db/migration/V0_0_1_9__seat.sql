CREATE TABLE IF NOT EXISTS `seat` (
  `floor_id` int NOT NULL,
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `restriction_id` int DEFAULT null,
  `seat_number` int NOT NULL,
  FOREIGN KEY (`floor_id`) REFERENCES `floor` (`id`),
  FOREIGN KEY (`restriction_id`) REFERENCES `restricted_seat` (`id`),
  
  CHECK (`floor_id` > 0),
  CHECK (`seat_number` > 0)
);