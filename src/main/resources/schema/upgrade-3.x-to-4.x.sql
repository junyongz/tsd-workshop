create sequence mig_spare_part_seq;
select setval('mig_spare_part_seq', (select max(id) from mig_spare_parts)) + 1;
ALTER TABLE mig_spare_parts ALTER COLUMN id SET DEFAULT nextval('mig_spare_part_seq');

select setval('spare_part_seq', 1);
create table spare_part (
	id int8 primary key DEFAULT nextval('spare_part_seq'::regclass) NOT NULL,
	creation_date date,
	part_no varchar,
	part_name varchar,
	description varchar,
	oems json,
	compatible_trucks json
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

create table service_summary as (
select (select company_name from company where id = veh.company_id) company_name,
            sum(main.part_costs) part_costs, sum(tasks_costs) task_costs,
            start_date, count(*),
			count(*) filter (where 'REPAIR' = any(transaction_types)) as repair_count,
			count(*) filter (where 'SERVICE' = any(transaction_types)) as service_count,
			count(*) filter (where 'INSPECTION' = any(transaction_types)) as inspection_count,
			count(*) filter (where 'TYRE' = any(transaction_types)) as tyre_count,
			count(*) filter (where completion_date is not null) as pending_count,
			count(*) filter (where completion_date is null) as completion_count,
            round(avg(duration)) average_completion_days
              from (select *, (completion_date - start_date) duration,
            (select sum(spu.quantity * spu.sold_price) from spare_part_usages spu
              where spu.service_id  = ws.id) part_costs,
            (select sum(wt.quoted_price ) from workmanship_task wt
              where wt.service_id  = ws.id) tasks_costs
              from workshop_service ws) main
             inner join vehicle veh on main.vehicle_id  = veh.id
             group by start_date, veh.company_id
             order by start_date);