--drop table pessoa if exists;

create table if not exists pessoa (
	id bigint, 
	name varchar(255), 
	lastname varchar(255), 
	cpf varchar(14), 
	weight integer, 
	approved integer, 
	age integer, 
	curriculum varchar(4000)
);