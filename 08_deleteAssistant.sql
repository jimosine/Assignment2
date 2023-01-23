ALTER TABLE assistant
ADD CONSTRAINT constraint_name UNIQUE (name);

DELETE FROM assistant 
WHERE name = 'Rhaegal';