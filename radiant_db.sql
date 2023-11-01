drop database if exists radiant_db;
create database radiant_db;
use radiant_db;

CREATE TABLE users(
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE music (
    music_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    artist_name VARCHAR(255) NOT NULL,
    length_sec INT NOT NULL
);

CREATE TABLE user_music (
    user_id INT,
    music_id INT,
    status ENUM('INCOMEPLETE', 'IN-PROGRESS', 'COMPLETE'),
    primary key(music_id, user_id),
    foreign key (music_id) references music(music_id),
    foreign key (user_id) references users(user_id)
);

-- Music
insert into music(title, artist_name, length_sec) values('One', 'Swedish House Mafia', 167);
insert into music(title, artist_name, length_sec) values('Secrets', 'Tiesto & KSHMR ft. Vassy', 220);
insert into music(title, artist_name, length_sec) values('Bad', 'David Guetta & Showtek', 171);
insert into music(title, artist_name, length_sec) values('Alive', 'Krewella', 206);
insert into music(title, artist_name, length_sec) values('Gecko', 'Oliver Heldens', 283);
insert into music(title, artist_name, length_sec) values('Promises', 'Nero', 269);
insert into music(title, artist_name, length_sec) values('Riverside', 'Sidney Samson', 208);
insert into music(title, artist_name, length_sec) values('#SELFIE', 'Chainsmokers', 224);
insert into music(title, artist_name, length_sec) values('Five Hours', 'Deorro', 330);
insert into music(title, artist_name, length_sec) values('Runaway', 'Galantis', 227);

-- Users
insert into users(username, password) values('Cris', 'EDM');
insert into users(username, password) values('Jason', 'EDM');
insert into users(username, password) values('Bryan', 'Hiphop');
insert into users(username, password) values('Darshan', 'EDM');

-- User_Music
insert into user_music(user_id, music_id, status) values(1, 1, 'INCOMEPLETE');
insert into user_music(user_id, music_id, status) values(1, 2, 'INCOMEPLETE');
insert into user_music(user_id, music_id, status) values(1, 3, 'INCOMEPLETE');
insert into user_music(user_id, music_id, status) values(1, 4, 'INCOMEPLETE');
insert into user_music(user_id, music_id, status) values(2, 5, 'INCOMEPLETE');
insert into user_music(user_id, music_id, status) values(2, 6, 'INCOMEPLETE');
insert into user_music(user_id, music_id, status) values(3, 7, 'INCOMEPLETE');
insert into user_music(user_id, music_id, status) values(3, 8, 'INCOMEPLETE');
insert into user_music(user_id, music_id, status) values(4, 9, 'INCOMEPLETE');
insert into user_music(user_id, music_id, status) values(4, 10, 'INCOMEPLETE');
