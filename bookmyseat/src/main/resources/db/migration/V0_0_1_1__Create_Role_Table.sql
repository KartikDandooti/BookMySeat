DROP TABLE IF EXISTS role;
CREATE TABLE role (
  role_id int NOT NULL AUTO_INCREMENT,
  role_name varchar(10) NOT NULL,
  PRIMARY KEY (role_id),
  UNIQUE KEY (role_name)
);
