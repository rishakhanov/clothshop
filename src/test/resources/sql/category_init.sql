
insert into discount(name, value, start_date, end_date, valid)
values('discount 0%', 0.0, '2000-01-01', '2000-01-02', FALSE);

insert into discount(name, value, start_date, end_date, valid)
values('discount 10%', 0.1, '2024-01-29', '2025-01-29', TRUE);

insert into discount(name, value, start_date, end_date, valid)
values('discount 20%', 0.2, '2024-01-29', '2025-01-29', TRUE);

insert into discount(name, value, start_date, end_date, valid)
values('discount 30%', 0.3, '2024-01-29', '2025-01-29', TRUE);

insert into category(discount_id, name)
values (1, 'category 1');

insert into category(discount_id, name)
values (2, 'category 2');

insert into category(discount_id, name)
values (3, 'category 3');