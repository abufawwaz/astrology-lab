insert into project_sunspots values ('1977-01-01 00:00:00', 10);
insert into project_sunspots values ('1977-01-02 00:00:00', 20);
insert into project_sunspots values ('1977-01-03 00:00:00', 30);
insert into project_sunspots values ('1977-01-04 00:00:00', 40);
insert into project_sunspots values ('1977-01-05 00:00:00', 50);
insert into project_sunspots values ('1977-01-06 00:00:00', 60);
insert into project_sunspots values ('1977-01-07 00:00:00', 70);
insert into project_sunspots values ('1977-01-08 00:00:00', 60);
insert into project_sunspots values ('1977-01-09 00:00:00', 50);
insert into project_sunspots values ('1977-01-10 00:00:00', 40);
insert into project_sunspots values ('1977-01-11 00:00:00', 30);
insert into project_sunspots values ('1977-01-12 00:00:00', 20);
insert into project_sunspots values ('1977-01-13 00:00:00', 10);
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

alter table project_classmates add column change_password ENUM ('no', 'yes') NOT NULL DEFAULT 'yes';
insert into project_classmates values (1, 'Стефан', '6367c48dd193d56ea7b0baad25b19455e529f5ee', '<b>Име:</b> Стефан Христов Златарев\r\n<b>Сайт:</b>http://www.astrology-lab.net', '<b>Адрес</b>: София\r\n<b>Телефон</b>: +359 885 138 136');
insert into project_classmates values (2, 'Никола', '?', '<b>Име:</b> Никола\r\n<b>Сайт:</b>???', '<b>Адрес</b>: Казанлък');
insert into project_classmates values (3, 'Пламена', '83592796bc17705662dc9a75c8b6da4fd93396', '<b>Име:</b> Пламена\r\n<b>Сайт:</b>???', '<b>Адрес</b>: ???\r\n<b>Телефон</b>: ???');

PASS: didi -> e7f2f616ce8817ad893bbeaad3e942e94c69d323
PASS: sisa -> 21c15bce9e39d65d31fbc7e284f462685685c0