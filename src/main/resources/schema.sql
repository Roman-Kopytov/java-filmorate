create table if not exists "rating"
(
    "rating_id" INTEGER                not null
    primary key,
    "name"      CHARACTER VARYING(255) not null
    );
create table if not exists "genres"
(
    "genre_id" INTEGER                not null
    primary key,
    "name"     CHARACTER VARYING(100) not null
    );

create table if not exists "films"
(
    "film_id"     INTEGER                not null
    primary key,
    "name"        CHARACTER VARYING(255) not null,
    "description" CHARACTER VARYING(200) not null,
    "releaseDate" DATE,
    "duration"    INTEGER                not null,
    "genre_id"    INTEGER                not null,
    "rating_id"   INTEGER                not null,
    constraint "films_rating_id_foreign"
    foreign key ("rating_id") references "rating"
    );

create table if not exists "film_genre"
(
    "film_id"  INTEGER not null,
    "genre_id" INTEGER not null,
    primary key ("film_id", "genre_id" ),
    constraint "film_genre_film_id_foreign"
    foreign key ("film_id") references "films",
    constraint "film_genre_genre_id_foreign"
    foreign key ("genre_id") references "genres"
    );


create table if not exists "users"
(
    "user_id"  INTEGER                not null
    primary key,
    "email"    CHARACTER VARYING(255) not null,
    "login"    CHARACTER VARYING(255) not null,
    "name"     CHARACTER VARYING(255),
    "birthday" DATE
    );


create table if not exists "friends"
(
    "user_id"   INTEGER not null,
    "friend_id" INTEGER not null,
    primary key ("friend_id", "user_id"),
    constraint "friends_friend_id_foreign"
    foreign key ("friend_id") references "users",
    constraint "friends_user_id_foreign"
    foreign key ("user_id") references "users"
    );

create table if not exists "likes"
(
    "film_id" INTEGER not null,
    "user_id" INTEGER not null,
    primary key ("film_id", "user_id"),
    constraint "likes_film_id_foreign"
    foreign key ("film_id") references "films",
    constraint "likes_user_id_foreign"
    foreign key ("user_id") references "users"
    );