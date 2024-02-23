DROP TABLE IF EXISTS restrain;
CREATE TABLE restrain (
  floor_id int NOT NULL,
  restrain_id int NOT NULL AUTO_INCREMENT,
  restrain_status bit(1) NOT NULL,
  PRIMARY KEY (restrain_id),
  UNIQUE KEY (floor_id),
  CONSTRAINT FOREIGN KEY (floor_id) REFERENCES floor (floor_id)
);