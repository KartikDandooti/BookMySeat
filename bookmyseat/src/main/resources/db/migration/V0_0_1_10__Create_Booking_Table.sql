DROP TABLE IF EXISTS booking;
CREATE TABLE booking (
  booking_id int NOT NULL AUTO_INCREMENT,
  end_date date NOT NULL,
  seat_id int NOT NULL,
  shift_id int NOT NULL,
  start_date date NOT NULL,
  user_id int NOT NULL,
  booking_type enum('DAILY','WEEKLY','MONTHLY','CUSTOM_DATE') NOT NULL,
  booking_status bit(1),
  PRIMARY KEY (booking_id),
  UNIQUE KEY (seat_id),
  CONSTRAINT FOREIGN KEY (seat_id) REFERENCES seat (seat_id),
  CONSTRAINT FOREIGN KEY (user_id) REFERENCES user (user_id),
  CONSTRAINT FOREIGN KEY (shift_id) REFERENCES shift (shift_id)
);