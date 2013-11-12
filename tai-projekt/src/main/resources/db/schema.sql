CREATE TABLE users(
id BIGINT IDENTITY,
username CHAR(30),
password CHAR(30),
password_salt CHAR(30),
access_token CHAR(30)
);