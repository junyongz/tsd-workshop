-- to run after copy data into tables
-- select 'select setval(''' || sequencename || ''', (select max(id) from ' || LEFT(sequencename, LENGTH(sequencename) - 4) || '));' from pg_sequences where sequenceowner  = 'tsd';

select setval('mig_data_seq', (select max(index) from mig_data));
select setval('mig_spare_part_seq', (select max(id) from mig_spare_parts));
select setval('mig_supplier_spare_parts_seq', (select max(id) from mig_supplier_spare_parts));
select setval('spare_part_usage_seq', (select max(id) from spare_part_usages));
select setval('spare_part_seq', (select max(id) from spare_part));
select setval('vehicle_fleet_info_seq', (select max(id) from vehicle_fleet_info));
select setval('vehicle_seq', (select max(id) from vehicle));
select setval('workmanship_task_seq', (select max(id) from workmanship_task));
select setval('workshop_service_media_seq', (select max(id) from workshop_service_media));
select setval('workshop_service_seq', (select max(id) from workshop_service));
select setval('workshop_task_seq', (select max(id) from workshop_task));
select setval('scheduling_service_seq', (select max(id) from scheduling_service));
