create table victim_incident (
	victim_id int not null,
	incident_id int not null,
	foreign key (victim_id) references victim(id),
	foreign key (incident_id) references incident(id)
);