
CREATE TABLE users(
id BIGSERIAL NOT NULL,
username CHARACTER VARYING(255),
CONSTRAINT pk PRIMARY KEY (id)
)


insert into users(username) values('jay');