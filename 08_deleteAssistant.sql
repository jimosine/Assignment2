ALTER TABLE assistant
ADD CONSTRAINT name UNIQUE (name);

DELETE FROM assistant 
WHERE name = 'Rhaegal';