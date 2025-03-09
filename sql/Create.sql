DROP TABLE IF EXISTS subscriber CASCADE;
DROP TABLE IF EXISTS post CASCADE;
DROP TABLE IF EXISTS thread CASCADE;
DROP TABLE IF EXISTS usr CASCADE;
DROP TABLE IF EXISTS likes CASCADE;

CREATE TABLE usr (
                             id SERIAL PRIMARY KEY,
                             name VARCHAR(50) NOT NULL UNIQUE ,
                             pwd VARCHAR(255) NOT NULL
);

CREATE TABLE thread (
                               id SERIAL PRIMARY KEY,
                               title VARCHAR(100) NOT NULL,
                               admin_id INT NOT NULL,
                               FOREIGN KEY (admin_id) REFERENCES usr(id) ON DELETE CASCADE
);

CREATE TABLE post (
                         id SERIAL PRIMARY KEY,
                         contenu TEXT NOT NULL,
                         usr_id INT NOT NULL,
                         thread_id INT NOT NULL,
                         date_posted TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (usr_id) REFERENCES usr(id) ON DELETE CASCADE,
                         FOREIGN KEY (thread_id) REFERENCES thread(id) ON DELETE CASCADE
);

CREATE TABLE subscriber (
                            usr_id INT NOT NULL,
                            thread_id INT NOT NULL,
                            PRIMARY KEY (usr_id, thread_id),
                            FOREIGN KEY (usr_id) REFERENCES usr(id) ON DELETE CASCADE,
                            FOREIGN KEY (thread_id) REFERENCES thread(id) ON DELETE CASCADE
);

CREATE TABLE likes (
                    usr_id INT NOT NULL,
                    post_id INT NOT NULL,
                    PRIMARY KEY (usr_id, post_id),
                    FOREIGN KEY (usr_id) REFERENCES usr(id) ON DELETE CASCADE,
                    FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE
);