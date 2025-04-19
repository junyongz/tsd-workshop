-- migration to 2.0 data model
--XX 1. patch vehicle_id in mig_data and spare_part_usages

-- 2. add service_id into mig_data

alter table mig_data add column service_id int8;

-- 3. patch service_id into mig_data, based on creation_date, vehicle_no

update mig_data md set service_id = a.service_id from (
select service_id, vehicle_no, creation_date, completion_date from (
select count(*), (row_number() over ()+5000) service_id, (select id from vehicle where plate_no = md.vehicle_no ) vehicle_id,  md.vehicle_no,
md.creation_date, md.completion_date
from mig_data md
left outer join spare_part_usages spu
 on md."index"  = spu.service_id
group by vehicle_id, md.vehicle_no, md.creation_date, md.completion_date) b ) a
where md.vehicle_no = a.vehicle_no
and md.creation_date = a.creation_date
and (md.completion_date = a.completion_date or md.completion_date is null);

-- 4. add mig_data_index into spare_part_usages, use back the same value as "index"

alter table spare_part_usages add column mig_data_index int8;
update spare_part_usages set mig_data_index = service_id where service_id is not null;

alter table spare_part_usages add column sold_price numeric;
update spare_part_usages set sold_price = (select unit_price from mig_supplier_spare_parts where id = order_id);

-- 4. patch service_id in spare_part_usages to mig_data.service_id based on mig_data.index

update spare_part_usages spu set service_id = (select service_id from mig_data where index = spu.mig_data_index);

-- 5. patch service_id in mig_data to null for index found in spare_part_usages.mig_data_index

update mig_data set service_id = null where index in (select mig_data_index from spare_part_usages);

-- 6. create workshop_service based on creation_date, vehicle_no

create table workshop_service as
select (row_number() over ()+5000) as id, (select id from vehicle where plate_no = md.vehicle_no ) vehicle_id,  md.vehicle_no,
md.creation_date as start_date, md.completion_date as completion_date, md.creation_date as creation_date, 0 as mileage_km
from mig_data md
left outer join spare_part_usages spu
 on md."index"  = spu.service_id
group by vehicle_id, md.vehicle_no, md.creation_date, md.completion_date;

-- EXTRA MANUAL PATCH
-- 7. how to patch if to tag to particular service.
-- 7.1 change service_id in mig_data or spare_part_usages
-- 7.2 delete orphan workshop_service (without mig_data or spare_part_usages) -- generic sql