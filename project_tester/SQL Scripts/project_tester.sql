/*
	Dieses Skript legt die komplette project_tester Datenbank an. 
    Das geschieht in zwei Schritten: 
    1. Die Tabellen werden erzeugt.
    2. Tabellen Constraints werden erzeugt. Dabei werden die Tabellen
    �ber Schl�sselbeziehungen miteinander verbunden. 
*/
drop database project_tester;
create database project_tester; 
use project_tester;

-- Entity Tabellen erzeugen
CREATE TABLE ACCOUNT (
  ID INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
  BIRTHYEAR INT,
  SEX VARCHAR(1),
  USERNAME VARCHAR(50),
  CITY VARCHAR(50),
  PASSWORD VARCHAR(45),
  EMAIL VARCHAR(45)
);

CREATE TABLE PROJECT (
  ID INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
  NAME VARCHAR(100),
  LOCATION VARCHAR(100),
  DESCRIPTION VARCHAR(500),
  SPECIALFEATURES VARCHAR(500),
  MOTIVATION VARCHAR(500),
  EXPIRATIONDATE DATETIME,
  ACCOUNT_ID INT
);

CREATE TABLE UPLOAD (
	ID INT PRIMARY KEY AUTO_INCREMENT NOT NULL, 
	ACCOUNT_ID INT, 
	FILENAME VARCHAR(100)
);

CREATE TABLE HASHTAG (
	ID INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	TEXT VARCHAR(100),
	UNIQUE(TEXT)
);

CREATE TABLE CATEGORY (
	ID INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	TEXT VARCHAR(100),
	UNIQUE(TEXT)
);

CREATE TABLE FEEDBACK (
 	ID INT NOT NULL,
 	CREATIONDATE datetime,
 	TEXT varchar(255),
 	accountEntity_id INT,
 	projectEntity_id INT
);

CREATE TABLE ANSWER (
 	ID INT NOT NULL,
 	CREATIONDATE datetime,
 	TEXT varchar(255),
 	accountEntity_id INT,
 	feedbackEntity_ID INT
);

CREATE TABLE PROJECT_HASHTAG (
	ID INT,
	PROJECTS_ID INT,
	HASHTAGS_ID INT 
);

CREATE TABLE PROJECT_CATEGORY (
	ID INT,
	PROJECT_ID INT,
	CATEGORY_ID INT 
);

CREATE TABLE PROJECT_QUESTION (
	ID INT,
	PROJECT_ID INT,
	QUESTION_ID INT 
);

-- Constraints festlegen
-- Primary Keys festlegen
--alter table ACCOUNT add primary key (ID);
--alter table PROJECT add primary key (ID);
--alter table CATEGORY add primary key (ID);
--alter table HASHTAG add primary key (ID);
--alter table QUESTION add primary key (ID);
--alter table LINK add primary key (ID);
--alter table PROJECT_LINK add primary key (ID);
--alter table PROJECT_HASHTAG add primary key (ID);
--alter table PROJECT_CATEGORY add primary key (ID);
--alter table PROJECT_QUESTION add primary key (ID);

-- Foreign Keys festlegen
--alter table PROJECT add foreign key (ACCOUNT_ID) references ACCOUNT(ID);
--alter table PROJECT_LINK add foreign key (PROJECT_ID) references PROJECT(ID);
--alter table PROJECT_LINK add foreign key (LINK_ID) references LINK(ID);
--alter table PROJECT_HASHTAG add foreign key (PROJECT_ID) references PROJECT(ID);
--alter table PROJECT_HASHTAG add foreign key (HASHTAG_ID) references HASHTAG(ID);
--alter table PROJECT_CATEGORY add foreign key (PROJECT_ID) references PROJECT(ID);
--alter table PROJECT_CATEGORY add foreign key (CATEGORY_ID) references CATEGORY(ID);
--alter table PROJECT_QUESTION add foreign key (PROJECT_ID) references PROJECT(ID);
--alter table PROJECT_QUESTION add foreign key (QUESTION_ID) references QUESTION(ID);













