DROP table address;
CREATE TABLE address (
  id INT AUTO_INCREMENT,
  address varchar(42) not null,
  PRIMARY KEY (id),
  CONSTRAINT address_uk1 UNIQUE (address)
);

DROP table address_eth;
CREATE TABLE address_eth (
  id INT AUTO_INCREMENT,
  address_id INT UNIQUE,
  PRIMARY KEY (id),
  FOREIGN KEY (address_id) REFERENCES address(id)
);

DROP table log_status;
CREATE TABLE log_status (
  id INT AUTO_INCREMENT,
  status_desc varchar(200) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

DROP table log_eth;
CREATE TABLE log_eth (
  id INT AUTO_INCREMENT,
  status_id INT not null,
  message varchar(500),
  PRIMARY KEY (id)
);