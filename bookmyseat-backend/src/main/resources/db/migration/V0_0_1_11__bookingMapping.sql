CREATE TABLE IF NOT EXISTS `booking_mapping` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `attendance` bit(1) NOT NULL DEFAULT b'0',
  `booked_date` date NOT NULL,
  `booking_id` int NOT NULL,
  `additional_desktop` bit(1) NOT NULL DEFAULT b'0',
  `beverage` bit(1) NOT NULL DEFAULT b'0',
  `lunch` bit(1) NOT NULL DEFAULT b'0',
  `parking` bit(1) NOT NULL DEFAULT b'0',
  `shift_id` int NOT NULL,
  `vehicle_type` ENUM ('WHEELER_2', 'WHEELER_4') DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `attendance_time` TIME DEFAULT NULL,
  FOREIGN KEY (`booking_id`) REFERENCES `booking` (`id`),
  FOREIGN KEY (`shift_id`) REFERENCES `shift_details` (`id`)
);