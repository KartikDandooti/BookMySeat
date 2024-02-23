DROP TABLE IF EXISTS seat;
CREATE TABLE seat (
  floor_id int NOT NULL,
  seat_id int NOT NULL AUTO_INCREMENT,
  seat_number int NOT NULL,
  PRIMARY KEY (seat_id),
  CONSTRAINT FOREIGN KEY (floor_id) REFERENCES floor (floor_id)
);
