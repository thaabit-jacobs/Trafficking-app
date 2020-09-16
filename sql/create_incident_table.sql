create table Incident(
	id serial not null primary key,
	incident_time  timestamp not null,
	descrip text not null  
)