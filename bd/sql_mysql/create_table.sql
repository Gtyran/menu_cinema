DROP TABLE IF EXISTS commandes;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS plats;
DROP TABLE IF EXISTS clients;

CREATE TABLE clients (
	id varchar(100) NOT NULL,
	nom varchar(100) NOT NULL,
	prenom varchar(100) NOT NULL,
	photo varchar(256) NULL,
	email varchar(256) NULL,
	tel varchar(100) NULL,
	adresse varchar(100) NULL,
	points INT(5) NULL,
	CONSTRAINT clients_PK PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8
COLLATE=utf8_general_ci;


CREATE TABLE commandes (
	id int(11) NOT NULL AUTO_INCREMENT,
	id_client varchar(100) NOT NULL,
	`date` timestamp NOT NULL,
	prix DOUBLE NULL,
	adresse_livraison varchar(100) NULL,
	CONSTRAINT commandes_PK PRIMARY KEY (id),
	CONSTRAINT commandes_clients_FK FOREIGN KEY (id_client) REFERENCES clients(id) ON DELETE CASCADE
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8
COLLATE=utf8_general_ci;

CREATE TABLE plats (
	id int(11) NOT NULL AUTO_INCREMENT,
	id_commande int(11) NOT NULL,
	id_menu int(11) NOT NULL,
	CONSTRAINT menus_PK PRIMARY KEY (id),
	CONSTRAINT menus_commandes_FK FOREIGN KEY (id_commande) REFERENCES commandes(id) ON DELETE CASCADE
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8
COLLATE=utf8_general_ci;


CREATE TABLE films (
	id int(11) NOT NULL AUTO_INCREMENT,
	id_commande int(11) NOT NULL,
	id_film int(11) NOT NULL,
	CONSTRAINT films_PK PRIMARY KEY (id),
	CONSTRAINT films_commandes_FK FOREIGN KEY (id_commande) REFERENCES commandes(id) ON DELETE CASCADE
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8
COLLATE=utf8_general_ci;	


DROP PROCEDURE IF EXISTS suggestion_films;

DELIMITER | -- On change le délimiteur
CREATE PROCEDURE suggestion_films(IN id_film_in INT)      
    -- toujours pas de paramètres, toujours des parenthèses
BEGIN
    SELECT id_menu
	from plats
	join commandes on (commandes.id = plats.id_commande)
	join films on (films.id_commande = commandes.id)
	where id_film = id_film_in
	GROUP BY id_menu
	having count(*) >= 2
	LIMIT 10;
END|

DROP PROCEDURE IF EXISTS suggestion_plats;

DELIMITER | -- On change le délimiteur
CREATE PROCEDURE suggestion_plats (IN id_menu_in INT)      
    -- toujours pas de paramètres, toujours des parenthèses
BEGIN
    SELECT id_film
	from films
	join commandes on (commandes.id = films.id_commande)
	join plats on (plats.id_commande = commandes.id)
	where id_menu = id_menu_in
	GROUP BY id_menu
	having count(*) >= 2
	LIMIT 10;
END|
