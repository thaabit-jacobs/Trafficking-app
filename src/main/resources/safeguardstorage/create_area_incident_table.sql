create table Area_Incident(
	area_id int not null,
	incident_id int not null,
	foreign key (area_id) references area(id),
	foreign key (incident_id) references incident(id)
)