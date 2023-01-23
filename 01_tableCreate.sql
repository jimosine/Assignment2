CREATE TABLE Superhero (
Id SERIAL PRIMARY KEY,
Name varchar(200),
Alias varchar(200),
Origin varchar(200)
);

CREATE TABLE Assistant (
Id SERIAL PRIMARY KEY,
Name varchar(200)
);

CREATE TABLE Power (
Id SERIAL PRIMARY KEY,
Name varchar(200),
Description varchar(200)
);