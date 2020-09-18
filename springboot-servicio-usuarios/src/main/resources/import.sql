INSERT INTO usuarios (username, password, enabled, nombre, apellido, email) VALUES ('andres', '$2a$10$IH1fZupXgOhHjr6WqcUlu.h43o8ANu9novU3Kjx7Uuxc3MrPpHQhy', true, 'Andres', 'Guzman', 'professor@bolsadeideas.com');
INSERT INTO usuarios (username, password, enabled, nombre, apellido, email) VALUES ('admin', '$2a$10$KlgCMwAgpKH4NGPw2b1DauoBAIK0F2mlcr6sws.T3t/5.OY4jUFYS', true, 'John', 'Doe', 'john.doe@bolsadeideas.com');

INSERT INTO roles (nombre) VALUES ('ROLE_USER');
INSERT INTO roles (nombre) VALUES ('ROLE_ADMIN');

INSERT INTO usuarios_roles (usuario_id, role_id) VALUES (1 , 1);
INSERT INTO usuarios_roles (usuario_id, role_id) VALUES (2 , 2);
INSERT INTO usuarios_roles (usuario_id, role_id) VALUES (2 , 1);