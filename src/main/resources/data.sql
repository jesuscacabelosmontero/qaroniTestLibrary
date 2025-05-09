-- ROLES
INSERT INTO ROLES (id, role) VALUES (1, 'MANAGER');
INSERT INTO ROLES (id, role) VALUES (2, 'LIBRARIAN');

-- USERS
INSERT INTO LIBRARY_USERS (id, email, password, name) VALUES (1, 'pedro@example.com', 'lib123', 'Pedro Librarian');
INSERT INTO LIBRARY_USERS (id, email, password, name) VALUES (2, 'juan@example.com', 'admin123', 'Juan Admin');
INSERT INTO LIBRARY_USERS (id, email, password, name) VALUES (3, 'jorge@example.com', 'both123', 'Jorge Both');

-- USER_ROLES
INSERT INTO USER_ROLES (user_id, role_id) VALUES (1, 2);
INSERT INTO USER_ROLES (user_id, role_id) VALUES (2, 1);
INSERT INTO USER_ROLES (user_id, role_id) VALUES (3, 1);
INSERT INTO USER_ROLES (user_id, role_id) VALUES (3, 2);

-- AUTHORS
INSERT INTO AUTHORS (id, name) VALUES (1, 'Gabriel García Márquez');
INSERT INTO AUTHORS (id, name) VALUES (2, 'J.K. Rowling');
INSERT INTO AUTHORS (id, name) VALUES (3, 'Stephen King');
INSERT INTO AUTHORS (id, name) VALUES (4, 'Jorge Both');

-- BOOKS
INSERT INTO BOOKS (id, title) VALUES (1, 'Cien Años de Soledad');
INSERT INTO BOOKS (id, title) VALUES (2, 'Harry Potter y la piedra filosofal');
INSERT INTO BOOKS (id, title) VALUES (3, 'Harry Potter y la cámara secreta');
INSERT INTO BOOKS (id, title) VALUES (4, 'Carrie');
INSERT INTO BOOKS (id, title) VALUES (5, 'Inedita colaboracion entre Rowling y King');


-- BOOK_AUTHORS
INSERT INTO BOOK_AUTHORS (book_id, author_id) VALUES (1, 1);
INSERT INTO BOOK_AUTHORS (book_id, author_id) VALUES (2, 2);
INSERT INTO BOOK_AUTHORS (book_id, author_id) VALUES (3, 2);
INSERT INTO BOOK_AUTHORS (book_id, author_id) VALUES (4, 3);
INSERT INTO BOOK_AUTHORS (book_id, author_id) VALUES (5, 2);
INSERT INTO BOOK_AUTHORS (book_id, author_id) VALUES (5, 3);