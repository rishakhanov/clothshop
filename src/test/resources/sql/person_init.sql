insert into person(username, firstname, lastname, email, password, phone)
values ('admin', 'adminname', 'adminlastname', 'admin@gmail.com', '$2a$12$9TIAeu6y.B3m6Xpi0P2FduNTadejEdhCO0lRvTQJHRMT1nmOX8fqu', '9-99-999');

insert into person(username, firstname, lastname, email, password, phone)
values ('user1', 'firstname1', 'lastname1', 'user1@gmail.com', '$2a$12$xmoRnuszAiqSGq8AY44QnezaAyFF2FVOioqGq4m/f2qwQw4/tV6ou', '1-11-111');

insert into person(username, firstname, lastname, email, password, phone)
values ('user2', 'firstname2', 'lastname2', 'user2@gmail.com', '$2a$12$vgqGjXQ6CF7qT1Ws3CkxZ.r0Rrg5sDh.2Oua4pJTQCLv3AWwW2xv2', '2-22-222');

insert into person(username, firstname, lastname, email, password, phone)
values ('user3', 'firstname3', 'lastname3', 'user3@gmail.com', '$2a$12$RmQ.0TAaH0G6jow8AAdcNuzI5gHv35Ij2ky2DpvSKt2/8PUTT5qoe', '3-33-333');

insert into roles(name) VALUES ('ROLE_ADMIN');
insert into roles(name) VALUES ('ROLE_USER');

insert into person_roles(person_id, roles_id) VALUES (1, 1);
insert into person_roles(person_id, roles_id) VALUES (2, 2);
insert into person_roles(person_id, roles_id) VALUES (3, 2);
insert into person_roles(person_id, roles_id) VALUES (4, 2);