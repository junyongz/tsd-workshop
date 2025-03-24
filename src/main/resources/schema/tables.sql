-- public.company definition

-- Drop table

-- DROP TABLE company;

CREATE TABLE company (
	id numeric NULL,
	company_name varchar(100) NULL
);


-- public.deleted_mig_data definition

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


-- public.deleted_mig_supplier_spare_parts definition

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
	deletion_date date NULL
);


-- public.mig_data definition

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
	order_id numeric NULL
);


-- public.mig_spare_parts definition

-- Drop table

-- DROP TABLE mig_spare_parts;

CREATE TABLE mig_spare_parts (
	id int8 DEFAULT nextval('spare_part_seq'::regclass) NULL,
	item_code text NULL,
	part_name text NULL,
	unit text NULL,
	unit_price numeric NULL,
	add_allowed bool NULL,
	supplier_id numeric NULL,
	order_id numeric NULL
);


-- public.mig_supplier_spare_parts definition

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
	sheet_name text NULL
);


-- public.raw_mig_data definition

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
CREATE INDEX ix_raw_mig_data_index ON public.raw_mig_data USING btree (index);


-- public.raw_mig_external_bills definition

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
CREATE INDEX ix_raw_mig_external_bills_index ON public.raw_mig_external_bills USING btree (index);


-- public.raw_mig_supplier_spare_parts definition

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
CREATE INDEX ix_raw_mig_supplier_spare_parts_index ON public.raw_mig_supplier_spare_parts USING btree (index);


-- public.raw_mig_tasks definition

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
CREATE INDEX ix_raw_mig_tasks_index ON public.raw_mig_tasks USING btree (index);


-- public.spare_part_usages definition

-- Drop table

-- DROP TABLE spare_part_usages;

CREATE TABLE spare_part_usages (
	id numeric DEFAULT nextval('spare_part_usage_seq'::regclass) NULL,
	vehicle_id numeric NULL,
	vehicle_no varchar(12) NULL,
	usage_date date NULL,
	order_id numeric NULL,
	service_id numeric NULL,
	quantity numeric NULL
);


-- public.supplier definition

-- Drop table

-- DROP TABLE supplier;

CREATE TABLE supplier (
	id int8 NULL,
	supplier_name text NULL
);


-- public.vehicle definition

-- Drop table

-- DROP TABLE vehicle;

CREATE TABLE vehicle (
	id int8 DEFAULT nextval('vehicle_seq'::regclass) NULL,
	plate_no varchar(12) NULL,
	trailer_no varchar(12) NULL,
	company_id numeric NULL,
	brand varchar(50) NULL,
	model varchar(100) NULL
);