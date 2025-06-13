-- select 'select setval(''' || sequencename || ''', (select max(id) from ' || LEFT(sequencename, LENGTH(sequencename) - 4) || ')) + 1;' from pg_sequences where sequenceowner  = 'tsd';

select setval('mig_data_seq', (select max(index) from mig_data)) + 1;
select setval('mig_supplier_spare_parts_seq', (select max(id) from mig_supplier_spare_parts)) + 1;
select setval('spare_part_usage_seq', (select max(id) from spare_part_usages)) + 1;
select setval('vehicle_fleet_info_seq', (select max(id) from vehicle_fleet_info)) + 1;
select setval('vehicle_seq', (select max(id) from vehicle)) + 1;
select setval('workmanship_task_seq', (select max(id) from workmanship_task)) + 1;
select setval('workshop_service_media_seq', (select max(id) from workshop_service_media)) + 1;
select setval('workshop_service_seq', (select max(id) from workshop_service)) + 1;
select setval('workshop_task_seq', (select max(id) from workshop_task)) + 1;