-- mig_data_seq definition

-- DROP SEQUENCE mig_data_seq;

CREATE SEQUENCE mig_data_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;


-- mig_supplier_spare_parts_seq definition

-- DROP SEQUENCE mig_supplier_spare_parts_seq;

CREATE SEQUENCE mig_supplier_spare_parts_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;


-- spare_part_seq definition

-- DROP SEQUENCE spare_part_seq;

CREATE SEQUENCE spare_part_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;


-- spare_part_usage_seq definition

-- DROP SEQUENCE spare_part_usage_seq;

CREATE SEQUENCE spare_part_usage_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;


-- vehicle_fleet_info_seq definition

-- DROP SEQUENCE vehicle_fleet_info_seq;

CREATE SEQUENCE vehicle_fleet_info_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;


-- vehicle_seq definition

-- DROP SEQUENCE vehicle_seq;

CREATE SEQUENCE vehicle_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;


-- workmanship_task_seq definition

-- DROP SEQUENCE workmanship_task_seq;

CREATE SEQUENCE workmanship_task_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;


-- workshop_service_media_seq definition

-- DROP SEQUENCE workshop_service_media_seq;

CREATE SEQUENCE workshop_service_media_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;


-- workshop_service_seq definition

-- DROP SEQUENCE workshop_service_seq;

CREATE SEQUENCE workshop_service_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;


-- workshop_task_seq definition

-- DROP SEQUENCE workshop_task_seq;

CREATE SEQUENCE workshop_task_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;

CREATE SEQUENCE scheduling_service_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;