DROP TABLE IF EXISTS floor;
CREATE TABLE floor (
  floor_id int NOT NULL AUTO_INCREMENT,
  location_id int NOT NULL,
  floor_name varchar(30) NOT NULL,
  PRIMARY KEY (floor_id),
  UNIQUE KEY (floor_name),
  CONSTRAINT FOREIGN KEY (location_id) REFERENCES location (location_id)
);