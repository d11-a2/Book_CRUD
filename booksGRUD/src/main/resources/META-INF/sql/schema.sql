USE test;

DROP TABLE IF EXISTS book;

CREATE TABLE book (
       id INT NOT NULL AUTO_INCREMENT
     , title VARCHAR(100) NOT NULL
     , description VARCHAR(255)
     , author VARCHAR(100) NOT NULL
     , isbn VARCHAR(20) NOT NULL
     , printyear INT NOT NULL
     , readalready BOOL NOT NULL
     , PRIMARY KEY (ID)
)
DEFAULT CHARACTER SET = utf8;
