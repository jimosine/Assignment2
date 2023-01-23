/*Ensure we dont try to re-create the table */
DROP TABLE IF EXISTS superhero_power;

CREATE TABLE superhero_power (
	superhero_id int REFERENCES superhero,
	power_id int REFERENCES power,
	PRIMARY KEY (superhero_id, power_id)
	);