create table Person(
	id serial not null primary key,
	first_name text not null,
	last_name text not null,
	contact text not null,
	age int not null
)