DROP TABLE IF EXISTS project;
CREATE TABLE project (
  created_by int NOT NULL,
  created_date date NOT NULL,
  modified_by int NOT NULL,
  modified_date date NOT NULL,
  project_id int NOT NULL AUTO_INCREMENT,
  project_name varchar(30) NOT NULL,
  PRIMARY KEY (project_id),
  UNIQUE KEY (project_name)
);
