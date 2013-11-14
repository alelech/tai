--insert users
insert into users(username, password, access_token) values ('admin', 'admin', 'DFGngJ6fo50AAAAAAAAAASRkY0W8ysvaBjN4hODs95viJfx2_mfwvxyUaRHP6X46');
insert into users(username, password) values ('user', 'user');
--insert roles
insert into user_roles(username,role_name) values ('admin','administrator');
insert into user_roles(username,role_name) values ('admin','user');
insert into user_roles(username,role_name) values ('user','user');
