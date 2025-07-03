create sequence mig_spare_part_seq;
select setval('mig_spare_part_seq', (select max(id) from mig_spare_parts)) + 1;
ALTER TABLE mig_spare_parts ALTER COLUMN id SET DEFAULT nextval('mig_spare_part_seq');

select setval('spare_part_seq', 1) + 1;
create table spare_part (
	id int8 primary key DEFAULT nextval('spare_part_seq'::regclass) NOT NULL,
	creation_date date,
	part_no varchar,
	part_name varchar,
	description varchar,
	oems json,
	compatible_trucks json,
	supplier_ids json
)

create sequence spare_part_media_seq;
create table spare_part_media (
	id int8 primary key DEFAULT nextval('spare_part_media_seq'::regclass) NOT NULL,
	spare_part_id int8,
	file_name varchar,
	file_size numeric,
	added_timestamp timestamp,
	media bytea,
	media_type varchar,
	constraint spare_part_media_id_fk foreign key (spare_part_id) references spare_part(id)
);

alter table mig_supplier_spare_parts add column spare_part_id int8;

create table temp_deleted_mig_supplier_spare_parts as
select id, delivery_order_no, computed_date, invoice_date, item_code, part_name, particular,
	quantity, unit, unit_price, notes, supplier_id, sheet_name, status, null::int8 as spare_part_id, deletion_date
from deleted_mig_supplier_spare_parts;
drop table deleted_mig_supplier_spare_parts;
create table deleted_mig_supplier_spare_parts as (select * from temp_deleted_mig_supplier_spare_parts);
drop table temp_deleted_mig_supplier_spare_parts;