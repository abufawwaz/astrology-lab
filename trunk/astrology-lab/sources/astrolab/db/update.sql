
insert into text values (4000037, NULL, NULL, 'Data Table', 'Data Table');
insert into text values (4000038, NULL, NULL, 'Data Chart', 'Data Chart');
insert into text values (4000039, NULL, NULL, 'Data', 'Data');
insert into text values (4000041, NULL, NULL, 'Track Chart', 'Track Chart');

insert into action_group values (4000039, 4000020);

insert into views values (57, 'astrolab.project.DisplayRecordsTable');
insert into views values (58, 'astrolab.project.DisplayRecordsChart');
insert into views values (59, 'astrolab.project.DisplayTrackChart');

insert into actions values (4000037, 4000039, NULL, NULL, NULL, NULL, 57, NULL);
insert into actions values (4000038, 4000039, NULL, NULL, NULL, NULL, 58, NULL);
insert into actions values (4000041, 4000039, NULL, NULL, NULL, NULL, 59, NULL);

create table project_sunspots (
  time DATETIME,
  sunspots INT,

  INDEX (time)
) ENGINE=InnoDB;

insert into project_sunspots values ('2007-01-01 00:00:00', 10);
insert into project_sunspots values ('2007-01-02 00:00:00', 20);
insert into project_sunspots values ('2007-01-03 00:00:00', 30);
insert into project_sunspots values ('2007-01-04 00:00:00', 40);
insert into project_sunspots values ('2007-01-05 00:00:00', 50);
insert into project_sunspots values ('2007-01-06 00:00:00', 60);
insert into project_sunspots values ('2007-01-07 00:00:00', 70);
insert into project_sunspots values ('2007-01-08 00:00:00', 60);
insert into project_sunspots values ('2007-01-09 00:00:00', 50);
insert into project_sunspots values ('2007-01-10 00:00:00', 40);
insert into project_sunspots values ('2007-01-11 00:00:00', 30);
insert into project_sunspots values ('2007-01-12 00:00:00', 20);
insert into project_sunspots values ('2007-01-13 00:00:00', 10);

create table formula (
  formulae_id INT UNSIGNED NOT NULL REFERENCES text (id),
  project_id INT UNSIGNED NOT NULL REFERENCES text (id),
  owner_id INT UNSIGNED NOT NULL REFERENCES text (id),
  formulae TEXT,
  score DOUBLE
) ENGINE=InnoDB;

create table svg (
  id INT UNSIGNED NOT NULL REFERENCES text (id),
  svg TEXT
) ENGINE=InnoDB;

insert into text values (6000078, NULL, 'Gamma', 'Gamma', 'Гама');

insert into svg values (6000015, "<svg:g style='stroke:gold;stroke-width:20;fill:none'><svg:circle r='90' /><svg:circle r='10' /></svg:g>");
insert into svg values (6000016, "<svg:path d='M-60 -80 A70 70 0 0 1 60 80 A40 70 -25 1 0 -60 -80' style='stroke:yellow;stroke-width:20;fill:none' />");
insert into svg values (6000017, "<svg:g style='stroke:yellow;stroke-width:20;fill:none'><svg:circle r='45' cy='-25' /><svg:path d='M-40 -90 A80 80 0 0 0 40 -90' /><svg:line y1='30' y2='90' /><svg:line x1='-40' y1='60' x2='40' y2='60' /></svg:g>");
insert into svg values (6000018, "<svg:g style='stroke:green;stroke-width:20;fill:none'><svg:circle r='60' cy='-30' /><svg:line y1='30' y2='90' /><svg:line x1='-40' y1='60' x2='40' y2='60' /></svg:g>");
insert into svg values (6000019, "<svg:g style='stroke:red;stroke-width:20;fill:none'><svg:circle r='70' cx='-15' cy='15' /><svg:line x1='40' y1='-40' x2='90' y2='-90' /><svg:path d='M90 -30 L90 -90 L30 -90' /></svg:g>");
insert into svg values (6000020, "<svg:g style='stroke:orange;stroke-width:20;fill:none'><svg:path d='M-90 -90 A70 70 0 0 1 -40 50 L90 50' /><svg:line x1='40' y1='10' x2='40' y2='90' /></svg:g>");
insert into svg values (6000021, "<svg:g style='stroke:darkolivegreen;stroke-width:20;fill:none'><svg:path d='M-20 -90 L-20 -20 A70 70 0 0 1 60 100' /><svg:line x1='-70' y1='-60' x2='20' y2='-60' /></svg:g>");
insert into svg values (6000022, "<svg:g style='stroke:cyan;stroke-width:20;fill:none'><svg:path d='M-90 -90 A50 50 0 1 1 -90 40' /><svg:path d='M90 -90 A50 50 0 1 0 90 40' /><svg:line x1='-40' y1='-30' x2='40' y2='-30' /><svg:line y1='-80' y2='30' /><svg:circle cy='65' r='25' /></svg:g>");
insert into svg values (6000023, "<svg:g style='stroke:teal;stroke-width:20;fill:none'><svg:path d='M-80 -90 A80 110 0 1 0 80 -90' /><svg:line y1='-80' y2='90' /><svg:line x1='-40' y1='60' x2='40' y2='60' /></svg:g>");
insert into svg values (6000024, "<svg:g style='stroke:firebrick;stroke-width:20;fill:none'><svg:circle cy='-60' r='30' /><svg:path d='M-80 -90 A80 110 0 1 0 80 -90' /><svg:line y1='20' y2='90' /><svg:line x1='-40' y1='60' x2='40' y2='60' /></svg:g>");
insert into svg values (6000078, "<svg:path d='M-40 90 L-40 -90 L40 -90' style='stroke:brown;stroke-width:20;fill:none' />");

