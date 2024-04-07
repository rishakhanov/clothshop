insert into image(name, type, data)
values ('draft', 'draft', null);

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

insert into vendor(name)
values ('vendor 1');

insert into vendor(name)
values ('vendor 2');

insert into vendor(name)
values ('vendor 3');

insert into product(category_id, vendor_id, image_id, name, price, quantity)
values (1, 1, 1, 'Product 1', 10.25, 100);

insert into product(category_id, vendor_id, image_id, name, price, quantity)
values (2, 2, 1, 'Product 2', 20.17, 200);

insert into product(category_id, vendor_id, image_id, name, price, quantity)
values (3, 3, 1, 'Product 3', 30.55, 300);