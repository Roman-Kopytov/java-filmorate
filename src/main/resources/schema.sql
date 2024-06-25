--drop all objects;

create table if not exists MPA (
    MPA_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME   CHARACTER VARYING(20) NOT NULL
);

create table if not exists GENRES (
    GENRE_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME CHARACTER VARYING(20) NOT NULL
);

create table if not exists DIRECTORS (
     DIRECTOR_ID BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
     NAME VARCHAR NOT NULL
);

create table if not exists FILMS (
    FILM_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME CHARACTER VARYING(255) NOT NULL,
    DESCRIPTION CHARACTER VARYING(200) NOT NULL,
    RELEASE_DATE DATE,
    DURATION BIGINT NOT NULL,
    MPA_ID INTEGER NOT NULL REFERENCES MPA (MPA_ID)
);

create table if not exists USERS (
    USER_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    EMAIL CHARACTER VARYING(255) NOT NULL UNIQUE,
    LOGIN CHARACTER VARYING(100) NOT NULL UNIQUE,
    NAME CHARACTER VARYING(255),
    BIRTHDAY DATE
);

create table if not exists FILMS_GENRES (
    FILM_ID BIGINT REFERENCES FILMS (FILM_ID) ON delete CASCADE,
    GENRE_ID BIGINT REFERENCES GENRES (GENRE_ID) ON delete CASCADE,
    primary key (FILM_ID, GENRE_ID)
);

create table if not exists FILM_DIRECTORS (
    FILM_ID BIGINT REFERENCES FILMS (FILM_ID) ON delete CASCADE,
    DIRECTOR_ID BIGINT REFERENCES DIRECTORS (DIRECTOR_ID) ON delete CASCADE,
    primary key (FILM_ID, DIRECTOR_ID)
);


create table if not exists FRIENDSHIP (
    USER_ID   BIGINT NOT NULL REFERENCES USERS (USER_ID) ON delete CASCADE,
    FRIEND_ID BIGINT NOT NULL REFERENCES USERS (USER_ID) ON delete CASCADE,
    constraint FRIENDSHIP_PK
        primary key (USER_ID, FRIEND_ID)
);

create table if not exists LIKES (
    FILM_ID BIGINT  NOT NULL REFERENCES FILMS (FILM_ID) ON delete CASCADE,
    USER_ID BIGINT NOT NULL REFERENCES USERS (USER_ID) ON delete CASCADE
);

create TABLE if not exists REVIEWS (
	REVIEW_ID BIGINT NOT NULL AUTO_INCREMENT,
	CONTENT CHARACTER VARYING(255) NOT NULL,
	ISPOSITIVE BOOLEAN NOT NULL,
	USER_ID BIGINT NOT NULL,
	FILM_ID BIGINT NOT NULL,
	CONSTRAINT REVIEWS_PK PRIMARY KEY (REVIEW_ID),
	CONSTRAINT REVIEWS_FILMS_FK FOREIGN KEY (FILM_ID) REFERENCES FILMS(FILM_ID) ON delete CASCADE,
	CONSTRAINT REVIEWS_USERS_FK FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID) ON delete CASCADE,
    CONSTRAINT REVIEWS_UNIQUE UNIQUE (USER_ID,FILM_ID)
);

create TABLE if not exists REVIEWS_LIKES (
	USER_ID BIGINT NOT NULL,
	REVIEW_ID BIGINT NOT NULL,
	USEFUL INTEGER NOT NULL,
	CONSTRAINT REVIEWS_LIKE_PK PRIMARY KEY (REVIEW_ID,USER_ID),
	CONSTRAINT REVIEWS_LIKE_REVIEWS_FK FOREIGN KEY (REVIEW_ID) REFERENCES REVIEWS(REVIEW_ID) ON delete CASCADE,
	CONSTRAINT REVIEWS_LIKE_USERS_FK FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID) ON delete CASCADE,
	CONSTRAINT REVIEWS_LIKES_UNIQUE UNIQUE (USER_ID,REVIEW_ID)
);




create table if not exists FEED (
    EVENT_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    USER_ID BIGINT NOT NULL REFERENCES USERS (USER_ID) ON delete CASCADE,
    ENTITY_ID BIGINT NOT NULL,
    TIMESTAMP TIMESTAMP NOT NULL DEFAULT(CURRENT_TIMESTAMP),
    OPERATION VARCHAR NOT NULL,
    EVENT_TYPE VARCHAR
);