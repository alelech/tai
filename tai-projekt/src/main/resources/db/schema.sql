CREATE TABLE users(
id BIGINT IDENTITY,
username VARCHAR(30),
password VARCHAR(100),
password_salt VARCHAR(100),
access_token VARCHAR(100)
);