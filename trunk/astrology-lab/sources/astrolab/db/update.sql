alter table project_classmates add column change_password ENUM ('no', 'yes') NOT NULL DEFAULT 'yes';
insert into project_classmates values (1, 'Стефан', '6367c48dd193d56ea7b0baad25b19455e529f5ee', '<b>Име:</b> Стефан Христов Златарев\r\n<b>Сайт:</b>http://www.astrology-lab.net', '<b>Адрес</b>: София\r\n<b>Телефон</b>: +359 885 138 136');
insert into project_classmates values (2, 'Никола', '?', '<b>Име:</b> Никола\r\n<b>Сайт:</b>???', '<b>Адрес</b>: Казанлък');
insert into project_classmates values (3, 'Пламена', '83592796bc17705662dc9a75c8b6da4fd93396', '<b>Име:</b> Пламена\r\n<b>Сайт:</b>???', '<b>Адрес</b>: ???\r\n<b>Телефон</b>: ???');

PASS: didi -> e7f2f616ce8817ad893bbeaad3e942e94c69d323
PASS: sisa -> 21c15bce9e39d65d31fbc7e284f462685685c0

---------------------------------

create table project_webstats (
  subject_id INT UNSIGNED REFERENCES project_archive (event_id),
  time DATETIME,
  perspective_id INT UNSIGNED REFERENCES views_perspective (perspective_id),
  project_id INT UNSIGNED REFERENCES project (name),

  INDEX (subject_id)
) ENGINE=InnoDB;

insert into views values (901, 'astrolab.perspective.classmates.FormClassmatesLogin');
insert into views values (903, 'astrolab.perspective.classmates.DisplayClassmateGeneralInfo');
insert into views values (904, 'astrolab.perspective.classmates.DisplayClassmatePrivateInfo');
insert into views values (905, 'astrolab.perspective.classmates.ModifyClassmateGeneralInfo');
insert into views values (906, 'astrolab.perspective.classmates.ModifyClassmatePrivateInfo');
insert into views values (907, 'astrolab.perspective.classmates.DisplayClassmateImage');

---------------------------------


create table project_blood_type (
  subject_id INT UNSIGNED REFERENCES project_archive (event_id),
  blood_type ENUM ('?', 'O', 'A', 'B', 'AB') NOT NULL,
  rhesus ENUM ('?', '+', '-') NOT NULL,

  INDEX (subject_id)
) ENGINE=InnoDB;

insert into views values (70, 'astrolab.project.bloodtype.FormEditBloodType');
insert into text values (30029, NULL, 'blood_type', 'Blood Type', 'Кръвна група');
insert into project values (30029, 30009, 'white', now(), NULL, 0);
insert into actions values (40018, 40016, NULL, 30029, NULL, NULL, 70, NULL);
insert into actions values (40019, 40016, NULL, 30029, NULL, NULL, 25, NULL);


---------------------------------

update views set request='astrolab.perspective.statistics.DisplayTimeDataChart' where view=58;
update views set request='astrolab.perspective.statistics.DisplayGroupChartConfiguration' where view=52;

insert into views values (72, 'astrolab.perspective.statistics.DisplayTypesDataChart');
insert into views values (73, 'astrolab.perspective.statistics.DisplayZodiacDataChart');
insert into views values (74, 'astrolab.perspective.statistics.DisplayTypesConfiguration');
insert into views values (75, 'astrolab.perspective.statistics.DisplayTimeConfiguration');
insert into views values (76, 'astrolab.perspective.statistics.DisplayZodiacConfiguration');
insert into views values (77, 'astrolab.perspective.statistics.ModifyFormulaeIncrementFactor');

create table perspective_statistics (
  subject_id INT UNSIGNED REFERENCES project_archive (event_id),
  project_id INT UNSIGNED REFERENCES project (name),
  formulae_id INT UNSIGNED NOT NULL REFERENCES formula (formulae_id),

  INDEX (subject_id, project_id)
) ENGINE=InnoDB;

create table perspective_statistics_correction (
  subject_id INT UNSIGNED REFERENCES project_archive (event_id),
  project_id INT UNSIGNED REFERENCES project (name),
  series_id INT UNSIGNED,
  element_offset INT NOT NULL,
  element_level INT NOT NULL,
  element_altitude INT NOT NULL,
  element_length INT NOT NULL,
  formulae_id INT UNSIGNED NOT NULL REFERENCES formula (formulae_id),

  INDEX (subject_id, project_id, series_id)
) ENGINE=InnoDB;

insert into text values (2111, NULL, 'Earth', 'Earth', 'Земя');
insert into svg values (2111, "<svg:g style='stroke:green;stroke-width:20;fill:none'><svg:circle r='90' /><svg:line y1='-90' y2='90' /><svg:line x1='-90' x2='90' /></svg:g>");

insert into views values (78, 'astrolab.perspective.statistics.AJAXDataCorrectionEffect');
insert into views values (79, 'astrolab.perspective.statistics.ModifyDataCorrectionEffect');

insert into text values (3103, NULL, 'x-axis', 'x-axis', 'х-коорд.');
insert into svg values (3103, "<svg:g style='stroke:black;stroke-width:10;fill:none'><svg:line x1='-90' x2='-90' y2='100' /><svg:line x1='-100' y1='90' x2='100' y2='90' /></svg:g>");

----------------------------------------------------

create table project_forex (
  subject_id INT UNSIGNED REFERENCES project_archive (event_id),
  time DATETIME,
  exchange_rate_close DOUBLE,
  min_exchange_rate_close DOUBLE,
  max_exchange_rate_close DOUBLE,
  volume INT,

  Sun DOUBLE,
  Moon DOUBLE,
  Mercury DOUBLE,
  Venus DOUBLE,
  Mars DOUBLE,
  Jupiter DOUBLE,
  Saturn DOUBLE,
  Uranus DOUBLE,
  Neptune DOUBLE,
  Pluto DOUBLE,

  INDEX (subject_id, time),
  INDEX (Sun),
  INDEX (Moon),
  INDEX (Mercury),
  INDEX (Venus),
  INDEX (Mars),
  INDEX (Jupiter),
  INDEX (Saturn),
  INDEX (Uranus),
  INDEX (Neptune),
  INDEX (Pluto)
) ENGINE=InnoDB;

insert into text values (30030, NULL, 'forex', 'Forex', 'Forex');
insert into project values (30030, 30002, 'white', now(), NULL, 0);

