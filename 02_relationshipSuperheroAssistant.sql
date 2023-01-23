ALTER TABLE assistant 
	ADD superhero_id INT
	ADD FOREIGN KEY(superhero_id) REFERENCES superhero(id);

