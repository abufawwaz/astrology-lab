create table project_statistics_value (
  event_id INT UNSIGNED NOT NULL REFERENCES text (id),
  project_id INT UNSIGNED NOT NULL REFERENCES project (id),
  record_value DOUBLE NOT NULL
) ENGINE=InnoDB;

insert into text values (3000026, NULL, NULL, 'Astronomy', 'Астрономия');
insert into text values (3000027, NULL, NULL, 'Sun Spots', 'Слънчеви петна');

insert into project values (3000027, 3000026, 'white', now(), NULL, 0);

insert into views values (51, 'astrolab.project.statistics.DisplayStatisticsRecords');

insert into actions values (4000025, 4000016, NULL, 3000027, NULL, NULL, 51, NULL);

