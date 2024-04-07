insert into person(username, firstname, lastname, email, password, phone)
values ('admin', 'adminname', 'adminlastname', 'admin@gmail.com', '$2a$12$9TIAeu6y.B3m6Xpi0P2FduNTadejEdhCO0lRvTQJHRMT1nmOX8fqu', '9-99-999');

insert into person(username, firstname, lastname, email, password, phone)
values ('user1', 'firstname1', 'lastname1', 'user1@gmail.com', '$2a$12$xmoRnuszAiqSGq8AY44QnezaAyFF2FVOioqGq4m/f2qwQw4/tV6ou', '1-11-111');

insert into person(username, firstname, lastname, email, password, phone)
values ('user2', 'firstname2', 'lastname2', 'user2@gmail.com', '$2a$12$vgqGjXQ6CF7qT1Ws3CkxZ.r0Rrg5sDh.2Oua4pJTQCLv3AWwW2xv2', '2-22-222');

insert into person(username, firstname, lastname, email, password, phone)
values ('user3', 'firstname3', 'lastname3', 'user3@gmail.com', '$2a$12$RmQ.0TAaH0G6jow8AAdcNuzI5gHv35Ij2ky2DpvSKt2/8PUTT5qoe', '3-33-333');

insert into "orders"(person_id, created_at, ship_date, status)
values (2, '2023-01-29','2023-01-31', 'P');

insert into "orders"(person_id, created_at, ship_date, status)
values (2, '2023-05-21','2023-05-22', 'CE');

insert into "orders"(person_id, created_at, ship_date, status)
values (2, '2023-02-16','2023-02-17', 'P');

insert into "orders"(person_id, created_at, ship_date, status)
values (2, '2023-03-15', NULL, 'P');

insert into "orders"(person_id, created_at, ship_date, status)
values (3, '2023-02-03','2023-02-04', 'CE');

insert into "orders"(person_id, created_at, ship_date, status)
values (3, '2023-02-16','2023-02-18', 'P');

insert into "orders"(person_id, created_at, ship_date, status)
values (3, '2023-03-15', NULL, 'CD');

insert into "orders"(person_id, created_at, ship_date, status)
values (4, '2023-04-02','2023-04-03', 'CE');

insert into "orders"(person_id, created_at, ship_date, status)
values (4, '2023-05-14','2023-05-17', 'P');
