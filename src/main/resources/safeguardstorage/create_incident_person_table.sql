create table Person_Incident(
	person_id int not null,
	incident_id int not null,
	foreign key (person_id) references person(id),
	foreign key (incident_id) references incident(id)
)