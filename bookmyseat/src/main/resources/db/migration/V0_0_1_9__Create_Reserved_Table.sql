DROP TABLE IF EXISTS reserved;
CREATE TABLE reserved (
  reserved_id int NOT NULL AUTO_INCREMENT,
  reserved_status bit(1) NOT NULL,
  seat_id int NOT NULL,
  user_id int NOT NULL,
  PRIMARY KEY (reserved_id),
  UNIQUE KEY (seat_id),
  UNIQUE KEY (user_id),
  CONSTRAINT FOREIGN KEY (seat_id) REFERENCES seat (seat_id),
  CONSTRAINT FOREIGN KEY (user_id) REFERENCES user (user_id)
);
