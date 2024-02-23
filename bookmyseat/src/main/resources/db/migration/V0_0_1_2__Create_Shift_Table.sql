DROP TABLE IF EXISTS shift;
CREATE TABLE shift (
  created_by int NOT NULL,
  created_date date NOT NULL,
  end_time time(6) NOT NULL,
  modified_by int NOT NULL,
  modified_date date NOT NULL,
  shift_id int NOT NULL AUTO_INCREMENT,
  start_time time(6) NOT NULL,
  shift_name varchar(20) NOT NULL,
  PRIMARY KEY (shift_id),
  UNIQUE KEY (shift_name)
);