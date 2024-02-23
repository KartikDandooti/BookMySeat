CREATE TABLE IF NOT EXISTS booking_mapping (
  id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  booking_date date NOT NULL,
  booking_id int NOT NULL,
  additional_desktop bit(1) NOT NULL DEFAULT b'0',
  tea_coffee bit(1) NOT NULL DEFAULT b'0',
  lunch bit(1) NOT NULL DEFAULT b'0',
  parking bit(1) NOT NULL DEFAULT b'0',
  parking_type ENUM('TWO_WHEELER', 'FOUR_WHEELER') DEFAULT NULL,
  tea_coffee_type enum('TEA','COFFEE') DEFAULT NULL,
  modified_date datetime DEFAULT NULL,
  marked_status bit(1) NOT NULL DEFAULT b'0',
  FOREIGN KEY (booking_id) REFERENCES booking (booking_id)
);