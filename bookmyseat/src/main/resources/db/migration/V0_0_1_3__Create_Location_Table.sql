DROP TABLE IF EXISTS location;
CREATE TABLE location (
  location_id int NOT NULL AUTO_INCREMENT,
  location_name varchar(30) NOT NULL,
  PRIMARY KEY (location_id),
  UNIQUE KEY (location_name)
);
