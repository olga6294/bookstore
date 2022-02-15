SET @bookAuthorId = 0;

CREATE TABLE Author (
    id INT IDENTITY PRIMARY KEY,
    firstname VARCHAR(255),
    lastname VARCHAR(255)
);

CREATE TABLE Book (
    id INT IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    authorId INT
);

INSERT INTO Author(firstname, lastname) VALUES ('Stephen', 'King');
SELECT @bookAuthorId := SCOPE_IDENTITY();

INSERT INTO Book(title, authorId) VALUES ('Knightriders', @bookAuthorId),
                                         ('Creepshow', @bookAuthorId),
                                         ('Frasier', @bookAuthorId),
                                         ('Rose Red', @bookAuthorId),
                                         ('The Diary of Ellen Rimbauer', @bookAuthorId),
                                         ('Gotham Cafe', @bookAuthorId);

INSERT INTO Author(firstname, lastname) VALUES ('Khaled', 'Hosseini');
SELECT @bookAuthorId := SCOPE_IDENTITY();

INSERT INTO Book(title, authorId) VALUES ('The Kite Runner', @bookAuthorId),
                                         ('A Thousand Splendid Suns', @bookAuthorId),
                                         ('And the Mountains Echoed', @bookAuthorId),
                                         ('Sea Prayer', @bookAuthorId);

INSERT INTO Author(firstname, lastname) VALUES ('George Raymond Richard', 'Martin');
SELECT @bookAuthorId := SCOPE_IDENTITY();

INSERT INTO Book(title, authorId) VALUES ('The Hero', @bookAuthorId),
                                         ('Override', @bookAuthorId),
                                         ('A Song for Lya', @bookAuthorId),
                                         ('Sandkings', @bookAuthorId),
                                         ('In the Lost Lands', @bookAuthorId),
                                         ('Heroes for Hope', @bookAuthorId),
                                         ('The Skin Trade', @bookAuthorId),
                                         ('A Clash of Kings', @bookAuthorId),
                                         ('Quartet', @bookAuthorId);

INSERT INTO Author(firstname, lastname) VALUES ('J. K.', 'Rowling');
SELECT @bookAuthorId := SCOPE_IDENTITY();

INSERT INTO Book(title, authorId) VALUES ('Harry Potter and the Philosopher’s Stone', @bookAuthorId),
                                         ('Harry Potter and the Chamber of Secrets', @bookAuthorId),
                                         ('Harry Potter and the Prisoner of Azkaban', @bookAuthorId);

INSERT INTO Author(firstname, lastname) VALUES ('William', 'Shakespeare');
SELECT @bookAuthorId := SCOPE_IDENTITY();

INSERT INTO Book(title, authorId) VALUES ('A Midsummer Night’s Dream', @bookAuthorId),
                                         ('Much Ado about Nothing', @bookAuthorId);

INSERT INTO Author(firstname, lastname) VALUES ('Garri', 'Kasparow');
SELECT @bookAuthorId := SCOPE_IDENTITY();

INSERT INTO Book(title, authorId) VALUES ('Deep Thinking: Where Machine Intelligence Ends and Human Creativity Begins', @bookAuthorId),
                                         ('Fighting Chess: My Games And Career', @bookAuthorId);

INSERT INTO Author(firstname, lastname) VALUES ('Albert', 'Camus');
SELECT @bookAuthorId := SCOPE_IDENTITY();

INSERT INTO Book(title, authorId) VALUES ('Le Mythe de Sisyphe', @bookAuthorId),
                                         ('Lettres à un ami allemand', @bookAuthorId),
                                         ('L’Homme révolté', @bookAuthorId);
