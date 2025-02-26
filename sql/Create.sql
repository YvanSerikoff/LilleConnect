DROP TABLE IF EXISTS subscriber;
DROP TABLE IF EXISTS post;
DROP TABLE IF EXISTS thread;
DROP TABLE IF EXISTS usr;

CREATE TABLE usr (
                             id SERIAL PRIMARY KEY,
                             name VARCHAR(50) NOT NULL,
                             pwd VARCHAR(255) NOT NULL,
                             email VARCHAR(100) NOT NULL UNIQUE
);

-- Table Fil de Discussion
CREATE TABLE thread (
                               id SERIAL PRIMARY KEY,
                               title VARCHAR(100) NOT NULL,
                               admin_id INT NOT NULL,
                               FOREIGN KEY (admin_id) REFERENCES usr(id) ON DELETE CASCADE
);

-- Table Message
CREATE TABLE post (
                         id SERIAL PRIMARY KEY,
                         contenu TEXT NOT NULL,
                         usr_id INT NOT NULL,
                         thread_id INT NOT NULL,
                         date_posted TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (usr_id) REFERENCES usr(id) ON DELETE CASCADE,
                         FOREIGN KEY (thread_id) REFERENCES thread(id) ON DELETE CASCADE
);

-- Table Abonnement (relation entre Utilisateur et Fil de Discussion)
CREATE TABLE subscriber (
                            usr_id INT NOT NULL,
                            thread_id INT NOT NULL,
                            PRIMARY KEY (usr_id, thread_id),
                            FOREIGN KEY (usr_id) REFERENCES usr(id) ON DELETE CASCADE,
                            FOREIGN KEY (thread_id) REFERENCES thread(id) ON DELETE CASCADE
);



INSERT INTO usr (id, name, email, pwd) VALUES (1, 'admin', 'test@gmail.Com', 'admin');
INSERT INTO usr (id, name, email, pwd) VALUES (2, 'user', 'test2@gmail.com', 'user');

INSERT INTO thread (id, title, admin_id) VALUES (1, 'test', 1);
INSERT INTO thread (id, title, admin_id) VALUES (2, 'test2', 1);

INSERT INTO post (id, contenu, usr_id, thread_id) VALUES (1, 'admin test', 1, 1);
INSERT INTO post (id, contenu, usr_id, thread_id) VALUES (2, 'user test', 2, 1);
INSERT INTO post (id, contenu, usr_id, thread_id) VALUES (3, 'admin test1', 1, 2);
INSERT INTO post (id, contenu, usr_id, thread_id) VALUES (4, 'user test2', 2, 1);

INSERT INTO subscriber (usr_id, thread_id) VALUES (1, 1);
INSERT INTO subscriber (usr_id, thread_id) VALUES (2, 1);
INSERT INTO subscriber (usr_id, thread_id) VALUES (1, 2);
