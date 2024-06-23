merge into mpa (mpa_id, name)
    values (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');

merge into genres (genre_id, name)
    values (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');

INSERT INTO USERS (NAME, EMAIL, LOGIN, BIRTHDAY)
    VALUES ('Mr. Kristi Senger', 'Katlynn17@yahoo.com', 'karm0iglcY', '1977-01-07'),
           ('Eileen Mohr', 'Sierra28@hotmail.com', 'bFiscFj1jl', '1989-07-21'),
           ('Belinda Morissette', 'Buck.Rosenbaum@gmail.com', 'HhWslNOalH', '1966-07-26');
--        ('Angelo Connelly', 'Diamond.Renner77@hotmail.com', '41IVPHD4Jw', '1971-01-06'),
--        ('Ismael Cassin I', 'Demarcus.Johnston79@gmail.com', 'KfdrBuEJSN', '2005-11-23'),
--        ('Darnell Terry', 'Moriah_Cummings@gmail.com', 'QmMK4wsMov', '1964-12-03');

INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)
    VALUES ('TJjtJuGS8dUeAzm', 'HZOE3ct3plkt3m4ip6dN4EMzqop93SdO5QdJD16uzdhDIgUaQl', '1962-01-31', 156, 3),
           ('8B8qgTBRtGKBdJN', 'wrVuIIL79f228O2tecGsMdMVbltg1xKpz5qLz86LVHIOv9xJq1', '1962-03-06', 65, 4),
           ('uUk6L30WM5jNBHc', 'cB22DWO2euD7py3KEnxpmqcBOh2sJZAOkHJP1pxgPlvbnuxEVW', '1997-03-04', 118, 1);
--        ('Fj4X6FXhpdSI9vr', 'OxuSzl2soynSCqlcDIWTWBtg84lQXEv45w8Fpp2PwO6vybtNJ5', '1997-08-23', 104, 2),
--        ('G9Bm4qk7CXJdi1G', '7U5R4z7hMDwIedpjT7PvYLT3GQU0bD2zNMVaseYqw7uzZmk2J8', '2004-09-08', 156, 2),
--        ('fHW3acwMUAeOcoG', 'uF2ItDav0ypDF3lgDn7Hcd8mhMZrv64L27mODwdiUVv2Zazut2', '1983-07-10', 115, 1);

INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID)
    VALUES (1, 2),
           (1, 1),
           (2, 3),
           (3, 4);

INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID)
    VALUES (1, 2),
           (1, 3),
           (2, 1),
           (3, 2);


INSERT INTO LIKES (FILM_ID, USER_ID)
    VALUES (2, 2),
           (2, 1),
           (1, 1);


INSERT INTO DIRECTORS (DIRECTOR_ID, NAME) VALUES ( 1, 'Quentin Tarantino' ), (2, 'Tim Burton'), (3, 'Steven Spielberg');