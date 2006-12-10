create table project_statistics_value (
  event_id INT UNSIGNED NOT NULL REFERENCES text (id),
  project_id INT UNSIGNED NOT NULL REFERENCES project (id),
  record_value DOUBLE NOT NULL
) ENGINE=InnoDB;

insert into text values (3000026, NULL, NULL, 'Astronomy', 'Астрономия');
insert into text values (3000027, NULL, NULL, 'Sun Spots', 'Слънчеви петна');

insert into project values (3000027, 3000026, 'white', now(), NULL, 0);

insert into views values (51, 'astrolab.project.statistics.ComponentChartStatistics');

insert into actions values (4000025, 4000016, NULL, 3000027, NULL, NULL, 51, NULL);


insert into text values (1000001, NULL, 'type', NULL, NULL);
insert into text values (1000002, NULL, 'planet', 'planet', 'планета');
insert into text values (1000003, NULL, 'house', 'house', 'дом');
insert into text values (1000004, NULL, 'formulae_element', 'formulae element', 'елемент за формула');

create table types (
  element_id INT UNSIGNED NOT NULL REFERENCES text (id),
  type_id INT UNSIGNED NOT NULL REFERENCES text (id)
) ENGINE=InnoDB;

insert into types values (6000015, 1000002);
insert into types values (6000015, 1000004);
insert into types values (6000016, 1000002);
insert into types values (6000016, 1000004);
insert into types values (6000017, 1000002);
insert into types values (6000017, 1000004);
insert into types values (6000018, 1000002);
insert into types values (6000018, 1000004);
insert into types values (6000019, 1000002);
insert into types values (6000019, 1000004);
insert into types values (6000020, 1000002);
insert into types values (6000020, 1000004);
insert into types values (6000021, 1000002);
insert into types values (6000021, 1000004);
insert into types values (6000022, 1000002);
insert into types values (6000022, 1000004);
insert into types values (6000023, 1000002);
insert into types values (6000023, 1000004);
insert into types values (6000024, 1000002);
insert into types values (6000024, 1000004);

update text set descrid = 'Sun' where id = 6000015;
update text set descrid = 'Moon' where id = 6000016;
update text set descrid = 'Mercury' where id = 6000017;
update text set descrid = 'Venus' where id = 6000018;
update text set descrid = 'Mars' where id = 6000019;
update text set descrid = 'Jupiter' where id = 6000020;
update text set descrid = 'Saturn' where id = 6000021;
update text set descrid = 'Uranus' where id = 6000022;
update text set descrid = 'Neptune' where id = 6000023;
update text set descrid = 'Pluto' where id = 6000024;

create table formula_description (
  formulae_id INT UNSIGNED NOT NULL REFERENCES text (id),
  project_id INT UNSIGNED NOT NULL REFERENCES text (id),
  owner_id INT UNSIGNED NOT NULL REFERENCES text (id),
  score DOUBLE
) ENGINE=InnoDB;

create table formula_elements (
  formulae_id INT UNSIGNED NOT NULL REFERENCES formula_description (id),
  element_coefficient DOUBLE NOT NULL,
  element_id INT UNSIGNED NOT NULL REFERENCES text (id)
) ENGINE=InnoDB;

insert into text values (4000032, NULL, NULL, 'Display formula', 'Покажи формули');
insert into text values (4000033, NULL, NULL, 'Edit formulae', 'Редактирай формула');

insert into views values (52, 'astrolab.formula.display.DisplayProjectFormula');
insert into views values (53, 'astrolab.formula.display.ModifyFormulae');
insert into views values (54, 'astrolab.formula.display.FormEditFormulae');

insert into actions values (4000004, NULL, NULL, NULL, 54, 53, 52, NULL);
insert into actions values (4000032, 4000016, NULL, NULL, NULL, NULL, 52, NULL);
insert into actions values (4000033, 4000016, NULL, NULL, NULL, NULL, 54, NULL);

insert into text values (10002020, NULL, 'user.selection.x1', 'selection.x1', 'selection.x1');
insert into text values (10002021, NULL, 'user.selection.x2', 'selection.x2', 'selection.x2');
insert into text values (10002022, NULL, 'user.selection.y2', 'selection.y2', 'selection.y2');

insert into text values (4000034, NULL, NULL, 'Display formulae chart', 'Display formulae chart');
insert into views values (55, 'astrolab.formula.display.ComponentChartFormulae');
insert into actions values (4000034, 4000016, NULL, NULL, NULL, NULL, 55, NULL);
