CREATE TABLE task_component (
    id int8 PRIMARY KEY,
    component_name VARCHAR(100) NOT NULL,
    subsystem VARCHAR(50) NOT NULL,
    description VARCHAR(200) not null,
    UNIQUE (component_name)
);

create table workmanship_task (
	id int8 primary key,
	recorded_date date,
	service_id int8,
	task_id int8,
	foremen text[],
	remarks text,
	quoted_price numeric,
	actual_duration_hours numeric
);
create sequence workmanship_task_seq owned by workmanship_task.id;
ALTER TABLE workmanship_task ALTER COLUMN id SET DEFAULT nextval('workmanship_task_seq');

alter table deleted_workshop_service add column workmanship_task json;
create table temp_deleted_workshop_service as
select id, vehicle_id, vehicle_no, start_date, completion_date, creation_date, mileage_km, transaction_types, null::text as notes, null::json as workmanship_task, spare_part_usages, deletion_date
from deleted_workshop_service;
drop table deleted_workshop_service;
create table deleted_workshop_service as (select * from temp_deleted_workshop_service);
drop table temp_deleted_workshop_service;

CREATE TABLE raw_tasks_unit_price (
	description text NULL,
	unit_price numeric NULL,
	workshop_tasks _text NULL,
	subsystem text NULL,
	status text NULL
);
CREATE INDEX idx_gin_task_desc ON public.raw_tasks_unit_price USING gin (to_tsvector('english'::regconfig, description));

