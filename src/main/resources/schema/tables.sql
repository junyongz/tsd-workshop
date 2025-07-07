-- company definition

-- Drop table

-- DROP TABLE company;

CREATE TABLE company (
	id numeric NULL,
	company_name varchar(100) NULL,
	internal bool NULL
);


-- deleted_mig_data definition

-- Drop table

-- DROP TABLE deleted_mig_data;

CREATE TABLE deleted_mig_data (
	"index" int8 NULL,
	sheet_name text NULL,
	vehicle_no text NULL,
	creation_date date NULL,
	item_description text NULL,
	part_name text NULL,
	quantity numeric NULL,
	unit text NULL,
	unit_price numeric NULL,
	total_price text NULL,
	calculated_total_price numeric NULL,
	migrated_ind bool NULL,
	completion_date date NULL,
	supplier_id numeric NULL,
	order_id numeric NULL,
	deletion_date date NULL
);


-- deleted_mig_supplier_spare_parts definition

-- Drop table

-- DROP TABLE deleted_mig_supplier_spare_parts;

CREATE TABLE deleted_mig_supplier_spare_parts (
	id int8 NULL,
	delivery_order_no text NULL,
	computed_date text NULL,
	invoice_date date NULL,
	item_code text NULL,
	part_name text NULL,
	particular text NULL,
	quantity numeric NULL,
	unit text NULL,
	unit_price numeric NULL,
	notes text NULL,
	supplier_id int8 NULL,
	sheet_name text NULL,
	status varchar(10) NULL,
	spare_part_id int8 NULL,
	deletion_date date NULL
);


-- deleted_workshop_service definition

-- Drop table

-- DROP TABLE deleted_workshop_service;

CREATE TABLE deleted_workshop_service (
	id int8 NULL,
	vehicle_id int8 NULL,
	vehicle_no text NULL,
	start_date date NULL,
	completion_date date NULL,
	creation_date date NULL,
	mileage_km int4 NULL,
	transaction_types _text NULL,
	notes text NULL,
	spare_parts_margin numeric NULL,
	workmanship_task json NULL,
	spare_part_usages json NULL,
	deletion_date date NULL
);


-- mig_data definition

-- Drop table

-- DROP TABLE mig_data;

CREATE TABLE mig_data (
	"index" int8 DEFAULT nextval('mig_data_seq'::regclass) NULL,
	sheet_name text NULL,
	vehicle_no text NULL,
	creation_date date NULL,
	item_description text NULL,
	part_name text NULL,
	quantity numeric NULL,
	unit text NULL,
	unit_price numeric NULL,
	total_price numeric NULL,
	calculated_total_price numeric NULL,
	migrated_ind bool NULL,
	completion_date date NULL,
	supplier_id numeric NULL,
	order_id numeric NULL,
	service_id int8 NULL
);
CREATE INDEX idx_mig_service_id ON mig_data USING btree (service_id);


-- mig_spare_parts definition

-- Drop table

-- DROP TABLE mig_spare_parts;

CREATE TABLE mig_spare_parts (
	id int8 DEFAULT nextval('mig_spare_part_seq'::regclass) NULL,
	item_code text NULL,
	part_name text NULL,
	unit text NULL,
	unit_price numeric NULL,
	add_allowed bool NULL,
	supplier_id numeric NULL,
	order_id numeric NULL,
	CONSTRAINT mig_spare_parts_pkey PRIMARY KEY (id)
);


-- mig_supplier_spare_parts definition

-- Drop table

-- DROP TABLE mig_supplier_spare_parts;

CREATE TABLE mig_supplier_spare_parts (
	id int8 DEFAULT nextval('mig_supplier_spare_parts_seq'::regclass) NULL,
	delivery_order_no text NULL,
	computed_date text NULL,
	invoice_date date NULL,
	item_code text NULL,
	part_name text NULL,
	particular text NULL,
	quantity numeric NULL,
	unit text NULL,
	unit_price numeric NULL,
	notes text NULL,
	supplier_id int8 NULL,
	sheet_name text NULL,
	status varchar(10) NULL,
	spare_part_id int8 NULL
);


-- raw_mig_data definition

-- Drop table

-- DROP TABLE raw_mig_data;

CREATE TABLE raw_mig_data (
	"index" int8 NULL,
	sheet_name text NULL,
	vehicle_no text NULL,
	creation_date text NULL,
	item_description text NULL,
	total_price text NULL
);
CREATE INDEX ix_raw_mig_data_index ON raw_mig_data USING btree (index);


-- raw_mig_external_bills definition

-- Drop table

-- DROP TABLE raw_mig_external_bills;

CREATE TABLE raw_mig_external_bills (
	"index" int8 NULL,
	dtlkey int8 NULL,
	dockey int8 NULL,
	seq int8 NULL,
	description text NULL,
	qty float8 NULL,
	unitprice float8 NULL,
	customer text NULL,
	invoicedate text NULL
);
CREATE INDEX ix_raw_mig_external_bills_index ON raw_mig_external_bills USING btree (index);


-- raw_mig_supplier_spare_parts definition

-- Drop table

-- DROP TABLE raw_mig_supplier_spare_parts;

CREATE TABLE raw_mig_supplier_spare_parts (
	"index" int8 NULL,
	invoice_date text NULL,
	delivery_order_no text NULL,
	particular text NULL,
	quantity text NULL,
	unit_price text NULL,
	note1 text NULL,
	note2 text NULL,
	computed_date text NULL,
	supplier_name text NULL,
	sheet_name text NULL
);
CREATE INDEX ix_raw_mig_supplier_spare_parts_index ON raw_mig_supplier_spare_parts USING btree (index);


-- raw_mig_tasks definition

-- Drop table

-- DROP TABLE raw_mig_tasks;

CREATE TABLE raw_mig_tasks (
	"index" int8 NULL,
	"Task" text NULL,
	"Malay" text NULL,
	"Chinese" text NULL,
	"Category" text NULL,
	"TruckPart" text NULL,
	"MinPrice" float8 NULL,
	"MaxPrice" float8 NULL,
	"AveragePrice" float8 NULL,
	"Perc90Price" float8 NULL
);
CREATE INDEX ix_raw_mig_tasks_index ON raw_mig_tasks USING btree (index);


-- raw_tasks_unit_price definition

-- Drop table

-- DROP TABLE raw_tasks_unit_price;

CREATE TABLE raw_tasks_unit_price (
	description text NULL,
	unit_price numeric NULL,
	workshop_tasks _text NULL,
	subsystem text NULL,
	status text NULL
);
CREATE INDEX idx_gin_task_desc ON raw_tasks_unit_price USING gin (to_tsvector('english'::regconfig, description));


-- scheduling_service definition

-- Drop table

-- DROP TABLE scheduling_service;

CREATE TABLE scheduling_service (
	id int8 DEFAULT nextval('scheduling_service_seq'::regclass) NOT NULL,
	scheduled_date date NULL,
	vehicle_id int8 NULL,
	vehicle_no varchar(12) NULL,
	notes text NULL,
	CONSTRAINT scheduling_service_pkey PRIMARY KEY (id)
);


-- spare_part definition

-- Drop table

-- DROP TABLE spare_part;

CREATE TABLE spare_part (
	id int8 DEFAULT nextval('spare_part_seq'::regclass) NOT NULL,
	creation_date date NULL,
	part_no varchar NULL,
	part_name varchar NULL,
	description varchar NULL,
	oems json NULL,
	compatible_trucks json NULL,
	CONSTRAINT spare_part_pkey PRIMARY KEY (id)
);


-- spare_part_media definition

-- Drop table

-- DROP TABLE spare_part_media;

CREATE TABLE spare_part_media (
	id int8 DEFAULT nextval('spare_part_media_seq'::regclass) NOT NULL,
	spare_part_id int8 NULL,
	file_name varchar NULL,
	file_size numeric NULL,
	added_timestamp timestamp NULL,
	media bytea NULL,
	media_type varchar NULL,
	CONSTRAINT spare_part_media_pkey PRIMARY KEY (id)
);


-- spare_part_usages definition

-- Drop table

-- DROP TABLE spare_part_usages;

CREATE TABLE spare_part_usages (
	id int8 DEFAULT nextval('spare_part_usage_seq'::regclass) NULL,
	vehicle_id numeric NULL,
	vehicle_no varchar(12) NULL,
	usage_date date NULL,
	order_id numeric NULL,
	service_id numeric NULL,
	quantity numeric NULL,
	mig_data_index int8 NULL,
	sold_price numeric NULL,
	margin numeric NULL
);
CREATE INDEX idx_service_id ON spare_part_usages USING btree (service_id);


-- supplier definition

-- Drop table

-- DROP TABLE supplier;

CREATE TABLE supplier (
	id int8 NULL,
	supplier_name text NULL
);


-- task_component definition

-- Drop table

-- DROP TABLE task_component;

CREATE TABLE task_component (
	id int8 NOT NULL,
	component_name varchar(100) NOT NULL,
	subsystem varchar(50) NOT NULL,
	description varchar(200) NOT NULL,
	CONSTRAINT task_component_component_name_key UNIQUE (component_name),
	CONSTRAINT task_component_pkey PRIMARY KEY (id)
);


-- vehicle definition

-- Drop table

-- DROP TABLE vehicle;

CREATE TABLE vehicle (
	id int8 DEFAULT nextval('vehicle_seq'::regclass) NULL,
	plate_no varchar(12) NULL,
	trailer_no varchar(12) NULL,
	company_id numeric NULL,
	brand varchar(50) NULL,
	model varchar(100) NULL,
	insurance_expiry_date date NULL,
	road_tax_expiry_date date NULL,
	latest_mileage_km int8 NULL,
	inspection_due_date date NULL,
	status text NULL,
	trailer_inspection_due_date date NULL,
	next_inspection_date date NULL,
	next_trailer_inspection_date date NULL
);


-- vehicle_fleet_info definition

-- Drop table

-- DROP TABLE vehicle_fleet_info;

CREATE TABLE vehicle_fleet_info (
	id int8 DEFAULT nextval('vehicle_fleet_info_seq'::regclass) NOT NULL,
	vehicle_id int8 NULL,
	vehicle_no varchar(12) NULL,
	creation_date timestamp NULL,
	"data" jsonb NULL,
	CONSTRAINT vehicle_fleet_info_pkey PRIMARY KEY (id)
);


-- workmanship_task definition

-- Drop table

-- DROP TABLE workmanship_task;

CREATE TABLE workmanship_task (
	id int8 DEFAULT nextval('workmanship_task_seq'::regclass) NOT NULL,
	recorded_date date NULL,
	service_id int8 NULL,
	task_id int8 NULL,
	foremen _text NULL,
	remarks text NULL,
	quoted_price numeric NULL,
	actual_duration_hours numeric NULL,
	CONSTRAINT workmanship_task_pkey PRIMARY KEY (id)
);


-- workshop_service definition

-- Drop table

-- DROP TABLE workshop_service;

CREATE TABLE workshop_service (
	id int8 DEFAULT nextval('workshop_service_seq'::regclass) NOT NULL,
	vehicle_id int8 NULL,
	vehicle_no text NULL,
	start_date date NULL,
	completion_date date NULL,
	creation_date date NULL,
	mileage_km int4 NULL,
	transaction_types _text NULL,
	notes text NULL,
	spare_parts_margin numeric NULL,
	CONSTRAINT workshop_service_pkey PRIMARY KEY (id)
);


-- workshop_service_media definition

-- Drop table

-- DROP TABLE workshop_service_media;

CREATE TABLE workshop_service_media (
	id int8 DEFAULT nextval('workshop_service_media_seq'::regclass) NULL,
	service_id int8 NULL,
	file_name varchar NULL,
	file_size numeric NULL,
	added_timestamp timestamp NULL,
	media bytea NULL,
	media_type varchar NULL
);
CREATE INDEX idx_media_service_id ON workshop_service_media USING btree (service_id);


-- workshop_task definition

-- Drop table

-- DROP TABLE workshop_task;

CREATE TABLE workshop_task (
	id int8 DEFAULT nextval('workshop_task_seq'::regclass) NOT NULL,
	component_id int4 NOT NULL,
	workmanship_task varchar(100) NOT NULL,
	unit_price numeric(10, 2) NOT NULL,
	complexity varchar(20) NOT NULL,
	labour_hours numeric(5, 2) NOT NULL,
	category varchar(20) NOT NULL,
	description text NOT NULL,
	CONSTRAINT workshop_task_component_id_workmanship_task_key UNIQUE (component_id, workmanship_task),
	CONSTRAINT workshop_task_pkey PRIMARY KEY (id)
);