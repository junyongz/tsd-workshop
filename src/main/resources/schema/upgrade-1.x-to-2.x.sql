-- migration to 2.0 data model

-- 1. create workshop_service, for not completed one, use minimum of creation_date as start date

create table workshop_service as
select (row_number() over ()+5000) id,  vehicle_id, vehicle_no, start_date, completion_date, start_date as creation_date, 0 as mileage_km
from (
select count(*) as cnt, (select id from vehicle where plate_no = md.vehicle_no ) vehicle_id,
md.vehicle_no, min(md.creation_date) start_date, null as completion_date
from mig_data md
left outer join spare_part_usages spu
 on md."index"  = spu.service_id
where md.completion_date is null
group by vehicle_id, md.vehicle_no
union all
select count(*) as cnt, (select id from vehicle where plate_no = md.vehicle_no ) vehicle_id,
md.vehicle_no, md.creation_date as start_date, md.completion_date
from mig_data md
left outer join spare_part_usages spu
 on md."index"  = spu.service_id
where md.completion_date is not null
group by vehicle_id, md.vehicle_no, md.creation_date, md.completion_date
) b

--drop sequence workshop_service_seq cascade;
create sequence workshop_service_seq owned by workshop_service.id;
SELECT setval('workshop_service_seq', (select max(id) from workshop_service)) + 1;
ALTER TABLE workshop_service ALTER COLUMN id SET DEFAULT nextval('workshop_service_seq')

-- 2. add service_id into mig_data

alter table mig_data add column service_id int8;

-- 3. patch service_id into mig_data, based on creation_date, vehicle_no

update mig_data md set service_id = (select id from workshop_service where md.vehicle_no  = vehicle_no
and start_date = md.creation_date)

-- patch those without service_id by using largest start date

update mig_data md set service_id = a.id from (
select id, vehicle_no from workshop_service where (start_date, vehicle_no) in (
select max(start_date), vehicle_no from workshop_service group by vehicle_no)) a
where md.vehicle_no = a.vehicle_no
and service_id is null;

-- 4. add mig_data_index into spare_part_usages, use back the same value as "index"

alter table spare_part_usages add column mig_data_index int8;
update spare_part_usages set mig_data_index = service_id where service_id is not null;

alter table spare_part_usages add column sold_price numeric;
update spare_part_usages set sold_price = (select unit_price from mig_supplier_spare_parts mssp  where id = order_id);

-- 4. patch service_id in spare_part_usages to mig_data.service_id based on mig_data.index

update spare_part_usages spu set service_id = (select service_id from mig_data where index = spu.mig_data_index);

-- 5. patch service_id in mig_data to null for index found in spare_part_usages.mig_data_index

update mig_data set service_id = null where index in (select mig_data_index from spare_part_usages);

-- EXTRA MANUAL PATCH
-- 6. how to patch if to tag to particular service.
-- 6.1 change service_id in mig_data or spare_part_usages
-- 6.2 delete orphan workshop_service (without mig_data or spare_part_usages) -- generic sql

create table deleted_workshop_service as (select * from workshop_service where id = -1);
alter table deleted_workshop_service add column deletion_date date;
alter table deleted_workshop_service add column spare_part_usages json;

alter table workshop_service add column transaction_types text[];