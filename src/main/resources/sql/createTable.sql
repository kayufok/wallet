DROP table address_ethereum;
DROP table address;
DROP table address_temporary;
DROP table log_ethereum;
DROP table log_status;

CREATE TABLE address (
  id INT AUTO_INCREMENT,
  address varchar(42) not null,
  PRIMARY KEY (id),
  CONSTRAINT address_uk1 UNIQUE (address)
);

CREATE TABLE address_temporary (
  id INT AUTO_INCREMENT,
  address varchar(42) not null,
  PRIMARY KEY (id)
);

CREATE TABLE address_ethereum (
  id INT AUTO_INCREMENT,
  address_id INT UNIQUE,
  PRIMARY KEY (id),
  FOREIGN KEY (address_id) REFERENCES address(id)
);

CREATE TABLE log_ethereum (
  id INT AUTO_INCREMENT,
  status_id INT not null,
  message varchar(500),
  PRIMARY KEY (id)
);

CREATE TABLE log_status (
  id INT AUTO_INCREMENT,
  status_desc varchar(200) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);