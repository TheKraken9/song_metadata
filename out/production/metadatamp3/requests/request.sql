create database mp3;

create table mp3info (
    idVibe integer not null default nextval('seq'),
    title varchar(100),
    subtitle varchar(100),
    rating varchar (100),
    comments varchar(200),
    album varchar(100),
    artist varchar(100),
    lenght varchar(100),
    sizes varchar(100),
    primary key(idVibe)
);

create sequence seqhafa
    start with 1
    increment by 1
    NO MAXVALUE
    CACHE 1;

alter sequence seq OWNED BY mp3info.idvibe;

create table modified(
    idModified int not null,
    dateofmodified timestamp
);

