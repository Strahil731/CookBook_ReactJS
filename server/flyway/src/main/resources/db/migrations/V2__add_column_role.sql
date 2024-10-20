CREATE TYPE roles AS ENUM ('USER','ADMIN');

ALTER TABLE users
ADD role roles;