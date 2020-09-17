create table persons(
	id serial not null primary key,
	first_name text not null,
	last_name text not null,
	age int not null,
	gender text not null,
	street text not null,
	date_incident text not null
)