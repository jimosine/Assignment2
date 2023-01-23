DROP TABLE IF EXISTS power;
DROP TABLE IF EXISTS assistant;
DROP TABLE IF EXISTS superhero;


CREATE TABLE superhero (
  id serial PRIMARY KEY,
  name varchar(50),
  alias varchar(50),
  origin varchar(50));
  
 CREATE TABLE assistant (
   id serial PRIMARY KEY,
   name varchar(50));
   
 CREATE TABLE power (
   id serial PRIMARY KEY,
   name varchar(50),
   description varchar(250));