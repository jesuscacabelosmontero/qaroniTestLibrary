-- ROLES
INSERT INTO ROLES (role) VALUES ('MANAGER');
INSERT INTO ROLES (role) VALUES ('LIBRARIAN');

-- USERS
INSERT INTO LIBRARY_USERS (email, password, name) VALUES ('pedro@example.com', 'lib123', 'Pedro Librarian');
INSERT INTO LIBRARY_USERS (email, password, name) VALUES ('juan@example.com', 'admin123', 'Juan Admin');
INSERT INTO LIBRARY_USERS (email, password, name) VALUES ('jorge@example.com', 'both123', 'Jorge Both');

-- USER_ROLES
INSERT INTO USER_ROLES (user_id, role_id) VALUES (1, 2);
INSERT INTO USER_ROLES (user_id, role_id) VALUES (2, 1);
INSERT INTO USER_ROLES (user_id, role_id) VALUES (3, 1);
INSERT INTO USER_ROLES (user_id, role_id) VALUES (3, 2);

-- AUTHORS
INSERT INTO AUTHORS (name) VALUES ('Gabriel García Márquez');
INSERT INTO AUTHORS (name) VALUES ('J.K. Rowling');
INSERT INTO AUTHORS (name) VALUES ('Stephen King');
INSERT INTO AUTHORS (name) VALUES ('Jorge Both');

-- BOOKS
INSERT INTO BOOKS (title) VALUES ('Cien Años de Soledad');
INSERT INTO BOOKS (title) VALUES ('Harry Potter y la piedra filosofal');
INSERT INTO BOOKS (title) VALUES ('Harry Potter y la cámara secreta');
INSERT INTO BOOKS (title) VALUES ('Carrie');
INSERT INTO BOOKS (title) VALUES ('Inedita colaboracion entre Rowling y King');


-- BOOK_AUTHORS
INSERT INTO BOOK_AUTHORS (book_id, author_id) VALUES (1, 1);
INSERT INTO BOOK_AUTHORS (book_id, author_id) VALUES (2, 2);
INSERT INTO BOOK_AUTHORS (book_id, author_id) VALUES (3, 2);
INSERT INTO BOOK_AUTHORS (book_id, author_id) VALUES (4, 3);
INSERT INTO BOOK_AUTHORS (book_id, author_id) VALUES (5, 2);
INSERT INTO BOOK_AUTHORS (book_id, author_id) VALUES (5, 3);