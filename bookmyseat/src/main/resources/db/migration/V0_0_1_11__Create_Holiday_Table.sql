CREATE TABLE IF NOT EXISTS holiday (
  holiday_date date NOT NULL,
  holiday_id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  holiday_name varchar(30) NOT NULL,
  holiday_month varchar(30) NOT NULL
);