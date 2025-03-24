-- public.mig_data_seq definition

-- DROP SEQUENCE mig_data_seq;

CREATE SEQUENCE mig_data_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;


-- public.mig_supplier_spare_parts_seq definition

-- DROP SEQUENCE mig_supplier_spare_parts_seq;

CREATE SEQUENCE mig_supplier_spare_parts_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;


-- public.spare_part_seq definition

-- DROP SEQUENCE spare_part_seq;

CREATE SEQUENCE spare_part_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;


-- public.spare_part_usage_seq definition

-- DROP SEQUENCE spare_part_usage_seq;

CREATE SEQUENCE spare_part_usage_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;


-- public.vehicle_seq definition

-- DROP SEQUENCE vehicle_seq;

CREATE SEQUENCE vehicle_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;