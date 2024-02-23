DROP TABLE IF EXISTS user;
CREATE TABLE user (
  created_by int NOT NULL,
  modified_by int NOT NULL,
  modified_date date NOT NULL,
  project_id int DEFAULT NULL,
  registered_date date NOT NULL,
  restrain_id int DEFAULT NULL,
  role_id int NOT NULL,
  user_id int NOT NULL,
  phone_number bigint NOT NULL,
  first_name varchar(20) NOT NULL,
  last_name varchar(20) NOT NULL,
  email_id varchar(50) NOT NULL,
  password varchar(80) NOT NULL,
  approval_status enum('APPROVED','REJECTED','PENDING') NOT NULL,
  PRIMARY KEY (user_id),
  UNIQUE KEY (email_id),
  CONSTRAINT FOREIGN KEY (project_id) REFERENCES project (project_id),
  CONSTRAINT FOREIGN KEY (restrain_id) REFERENCES restrain (restrain_id),
  CONSTRAINT FOREIGN KEY (role_id) REFERENCES role (role_id)
);
